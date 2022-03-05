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

import org.teamapps.icons.IconLoaderContext;
import org.teamapps.icons.IconResource;
import org.teamapps.icons.IconType;
import org.teamapps.icons.spi.IconLoader;

import java.io.IOException;
import java.io.InputStream;
import java.text.MessageFormat;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class EmojiIconLoader implements IconLoader<EmojiIcon> {
    /**
     * {@inheritDoc}
     */
    @Override
    public IconResource loadIcon(EmojiIcon icon, int size, IconLoaderContext context) {
        EmojiIconStyle style = icon.getStyle();

        String resourcePath = "/org/teamapps/icon/emoji/" + style.getIconPath(icon);
        byte[] svgBytes = getSVG(resourcePath, icon);

        if (svgBytes == null) {
            // try without -fe0f suffix
            resourcePath = resourcePath.replaceAll("[_-](fe0f|FE0F).svg", ".svg");
            svgBytes = getSVG(resourcePath, icon);
        }
        if (svgBytes == null) {
            // remove all -fe0f
            resourcePath = resourcePath.replaceAll("[_-](fe0f|FE0F)", "");
            svgBytes = getSVG(resourcePath, icon);
        }
        if (svgBytes == null) {
            System.out.println(MessageFormat.format("Could not load icon {0} style: {1} from path: {2}", icon.getIconId(), icon.getStyle().getStyleId(), resourcePath));
            return null;
        }
        return new IconResource(svgBytes, IconType.SVG);
    }

    private byte[] getSVG(String resourcePath, EmojiIcon icon) {

        byte[] svgBytes = null;
        try (InputStream inputStream = getClass().getResourceAsStream(resourcePath)) {
            if (inputStream != null) {
                svgBytes = inputStream.readAllBytes();
            }
        } catch (IOException e) {
            System.out.println(MessageFormat.format("Could not load icon '{0}' style: '{1}' from path '{2}. error: '{3}", icon.getIconId(), icon.getStyle().getStyleId(), resourcePath, e.getMessage()));
        }
        return svgBytes;

    }

}
