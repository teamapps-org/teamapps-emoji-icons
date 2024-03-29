/*-
 * ========================LICENSE_START=================================
 * TeamApps Emoji Icon Library
 * ---
 * Copyright (C) 2021 - 2022 TeamApps.org
 * ---
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * =========================LICENSE_END==================================
 */
package org.teamapps.icon.emoji;

import org.apache.commons.lang3.StringUtils;
import org.teamapps.common.format.Color;
import org.teamapps.icon.material.MaterialIcon;
import org.teamapps.server.jetty.embedded.TeamAppsJettyEmbeddedServer;
import org.teamapps.ux.component.Component;
import org.teamapps.ux.component.field.*;
import org.teamapps.ux.component.field.combobox.ComboBox;
import org.teamapps.ux.component.flexcontainer.VerticalLayout;
import org.teamapps.ux.component.form.ResponsiveForm;
import org.teamapps.ux.component.form.ResponsiveFormLayout;
import org.teamapps.ux.component.format.VerticalElementAlignment;
import org.teamapps.ux.component.infiniteitemview.InfiniteItemView2;
import org.teamapps.ux.component.infiniteitemview.ListInfiniteItemViewModel;
import org.teamapps.ux.component.notification.Notification;
import org.teamapps.ux.component.notification.NotificationPosition;
import org.teamapps.ux.component.panel.Panel;
import org.teamapps.ux.component.rootpanel.RootPanel;
import org.teamapps.ux.component.template.BaseTemplate;
import org.teamapps.ux.component.template.BaseTemplateRecord;
import org.teamapps.ux.session.SessionContext;
import org.teamapps.webcontroller.WebController;

import java.util.stream.Collectors;

public class EmojiIconBrowser {

    private SessionContext sessionContext;
    private EmojiIconStyle iconStyle = EmojiIconStyle.NOTO;
    private final ListInfiniteItemViewModel<EmojiIcon> iconViewModel = new ListInfiniteItemViewModel<>(EmojiIcon.getIcons());
    private boolean showSkinToneVariants = false;
    private Panel iconViewComponent;
    private InfiniteItemView2<EmojiIcon> iconView;
    private TextField unicodeField;

    public EmojiIconBrowser(SessionContext sessionContext) {
        this.sessionContext = sessionContext;
    }

    public Component getUI() {
        Panel panel = new Panel();
        Component iconFinder = createIconFinder();
        panel.setContent(iconFinder);
        panel.setTitle("Icon Viewer");
        panel.setIcon(EmojiIcon.GRINNING_FACE); // MaterialIcon.STARS
        return panel;
    }

    protected Component createIconFinder() {
        iconViewComponent = createIconViewer();

        VerticalLayout verticalLayout = new VerticalLayout();
        // New Component: ResponsiveForm
        ResponsiveForm responsiveForm = new ResponsiveForm<>(100, 200, 0);
        verticalLayout.addComponent(responsiveForm);

        ResponsiveFormLayout layout = responsiveForm.addResponsiveFormLayout(400);

        // Icon Search
        layout.addSection(EmojiIcon.GLASSES, "Filter Icons"); // MaterialIcon.FILTER
        TextField searchField = new TextField();
        layout.addLabelAndField(EmojiIcon.MAGNIFYING_GLASS_TILTED_LEFT, "Icon Name", searchField); // MaterialIcon.SEARCH
        searchField.setEmptyText("Search...");
        searchField.onTextInput.addListener(s -> {
            iconViewModel.setRecords(EmojiIcon.getIcons().stream()
                    .filter(icon -> s == null || StringUtils.containsIgnoreCase(icon.getIconId(), s))
                    .filter(icon -> showSkinToneVariants || !(icon.getIconId().contains("__") && icon.getIconId().contains("_SKIN_TONE")))
                    .collect(Collectors.toList()));
            updateViewerCount();
        });
        searchField.onTextInput.fire(); // initial filtering (hide skintone variants)

        CheckBox showSkinTonesField = new CheckBox();
        showSkinTonesField.setValue(showSkinToneVariants);
        layout.addLabelAndField(EmojiIcon.COUPLE_WITH_HEART__WOMAN_MAN_LIGHT_SKIN_TONE_MEDIUMDARK_SKIN_TONE, "Show Skin Tone variants", showSkinTonesField);
        showSkinTonesField.onValueChanged.addListener(value -> {
            showSkinToneVariants = value;
            searchField.onTextInput.fire(searchField.getValue()); // update records
        });

        unicodeField = new TextField();
        layout.addLabelAndField(EmojiIcon.SLIGHTLY_SMILING_FACE, "Unicode", unicodeField);
        unicodeField.setEmptyText("👋");
        unicodeField.setValue("");
        unicodeField.onTextInput.addListener(s -> {
            iconViewModel.setRecords(EmojiIcon.getIcons().stream()
                    .filter(icon -> StringUtils.isEmpty(s) || StringUtils.containsIgnoreCase(s, icon.getUnicode()))
                    .collect(Collectors.toList()));
            updateViewerCount();
        });

        // Style Selector
        ComboBox<EmojiIconStyle> styleSelector = ComboBox.createForList(EmojiIconStyle.getStyles());
        styleSelector.setTemplate(BaseTemplate.LIST_ITEM_MEDIUM_ICON_SINGLE_LINE);
        styleSelector.setPropertyExtractor((style, propertyName) -> {
            switch (propertyName) {
                case BaseTemplate.PROPERTY_ICON:
                    return EmojiIcon.PAINTBRUSH; // MaterialIcon.BRUSH;
                case BaseTemplate.PROPERTY_CAPTION:
                    return style.getStyleId();
            }
            return null;
        });
        styleSelector.setRecordToStringFunction(EmojiIconStyle::getStyleId);
        styleSelector.setValue(EmojiIconStyle.NOTO);
        styleSelector.setShowClearButton(false);
        styleSelector.onValueChanged.addListener(style -> {
            iconStyle = style;
            iconViewModel.onAllDataChanged.fire();
        });
        layout.addLabelAndField(EmojiIcon.PAINTBRUSH, "Icon Style", styleSelector); // MaterialIcon.BRUSH

        // Icon Size
        NumberField sizeField = new NumberField(0);
        layout.addLabelAndField(EmojiIcon.LEFTRIGHT_ARROW, "Icon Size", sizeField);
        sizeField.setValue(48);
        sizeField.setMinValue(10);
        sizeField.setMaxValue(300);
        sizeField.setSliderMode(NumberFieldSliderMode.VISIBLE);
        sizeField.onValueChanged.addListener(value -> {
            iconView.setItemTemplate(BaseTemplate.createTreeSingleLineNodeTemplate(value.intValue(), VerticalElementAlignment.CENTER, value.intValue() + 50));
            iconView.setItemHeight(value.intValue() + 10);
        });

        verticalLayout.addComponentFillRemaining(iconViewComponent);
        updateViewerCount();
        return verticalLayout;
    }

    private void updateViewerCount() {
        iconViewComponent.setTitle("Icons (" + iconViewModel.getCount() + ")");
    }

    public Panel createIconViewer() {
        iconView = new InfiniteItemView2<>();
        iconView.setItemTemplate(BaseTemplate.LIST_ITEM_LARGE_ICON_SINGLE_LINE);
        iconView.setItemHeight(50);
        iconView.setItemWidth(300);
        iconView.setItemPropertyExtractor((emojiIcon, propertyName) -> {
            switch (propertyName) {
                case BaseTemplate.PROPERTY_ICON:
                    return emojiIcon.withStyle(iconStyle);
                case BaseTemplate.PROPERTY_CAPTION:
                    return emojiIcon.getIconId();
                default:
                    return null;
            }
        });
        iconView.setModel(iconViewModel);
        Panel panel = new Panel(null, "Icons");
        panel.setContent(iconView);
        panel.setBodyBackgroundColor(Color.WHITE.withAlpha(0.96f));
        iconView.onItemClicked.addListener(iconItemClickedEventData -> {

            // Custom Notification with VERY LARGE ICON
            TemplateField<BaseTemplateRecord<Void>> templateField = new TemplateField<>(BaseTemplate.LIST_ITEM_EXTRA_VERY_LARGE_ICON_TWO_LINES);
            EmojiIcon icon = iconItemClickedEventData.getRecord();
            templateField.setValue(new BaseTemplateRecord<>(icon.withStyle(iconStyle), "EmojiIcon." + icon.getIconId(), "EmojiIcon.forUnicode(\"" + icon.getUnicode() + "\")"));
            Notification iconNotification = new Notification();
            iconNotification.setContent(templateField);
            iconNotification.setShowProgressBar(false);
            iconNotification.setDisplayTimeInMillis(10000);
            sessionContext.showNotification(iconNotification, NotificationPosition.TOP_RIGHT);
            if (!unicodeField.getValue().contains(icon.getUnicode())) {
                unicodeField.setValue(unicodeField.getValue() + icon.getUnicode());
            }
        });
        return panel;
    }

    // main method to launch the IconBrowser standalone
    public static void main(String[] args) throws Exception {
        WebController controller = sessionContext -> {
            RootPanel rootPanel = new RootPanel();
            sessionContext.addRootPanel(null, rootPanel);
            Component emojiIconBrowser = new EmojiIconBrowser(sessionContext).getUI();
            rootPanel.setContent(emojiIconBrowser);
        };
        new TeamAppsJettyEmbeddedServer(controller, 8082).start();
    }

}
