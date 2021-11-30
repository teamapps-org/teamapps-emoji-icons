/*-
 * ========================LICENSE_START=================================
 * TeamApps Emoji Icon Provider
 * ---
 * Copyright (C) 2014 - 2020 TeamApps.org
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

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class EmojiIconStyle {

    public static final EmojiIconStyle NOTO = new EmojiIconStyle("NOTO", emojiIcon -> {
        String iconFilename = "u" + emojiIcon.getCodePointsList().stream()
                .map(String::toLowerCase)
                .filter(s -> ! s.equals("fe0f"))
                .filter(s -> ! s.equals("e007f"))
                .collect(Collectors.joining("_")
        );
        if (emojiIcon.isFlag()){
            return "noto/flags/" + iconFilename + ".svg";
        } else {
            return "noto/svg/" + iconFilename + ".svg";
        }

    });
    public static final EmojiIconStyle TWEMOJI = new EmojiIconStyle("TWEMOJI", emojiIcon -> {
        String iconFilename = emojiIcon.getCodePointsList().stream()
                .map(String::toLowerCase)
                .collect(Collectors.joining("-"));
        iconFilename = iconFilename.replaceAll("^00", ""); // remove leading 00 (in Keycap icons)
        return "twemoji/svg/" + iconFilename + ".svg";
    });
    public static final EmojiIconStyle OPENMOJI_COLOR = new EmojiIconStyle("OPENMOJI_COLOR", emojiIcon -> {
        String iconFilename = emojiIcon.getCodePointsList().stream()
                .map(String::toUpperCase)
                .collect(Collectors.joining("-"));
        if (emojiIcon.getIconId().equals("WHITE_FLAG")){ iconFilename = "1F3F3"; } // waving flag like BLACK_FLAG instead of white rectangle
        return "openmoji-svg-color/" + iconFilename + ".svg";
    });
    public static final EmojiIconStyle OPENMOJI_BLACK = new EmojiIconStyle("OPENMOJI_BLACK", emojiIcon -> {
        String iconFilename = emojiIcon.getCodePointsList().stream()
                .map(String::toUpperCase)
                .collect(Collectors.joining("-"));
        if (emojiIcon.getIconId().equals("WHITE_FLAG")){ iconFilename = "1F3F3"; } // waving flag like BLACK_FLAG instead of white rectangle
        if (emojiIcon.isFlag() || emojiIcon.getIconId().equals("RAINBOW_FLAG") || emojiIcon.getIconId().equals("TRANSGENDER_FLAG")){
            // use colored flags, black flags are just empty rectangles
            return "openmoji-svg-color/" + iconFilename + ".svg";
        } else {
            return "openmoji-svg-black/" + iconFilename + ".svg";
        }

    });
    // public static final EmojiIconStyle BW = new EmojiIconStyle("BW", "svg_bw");

    private final String styleId;
    private final Function<EmojiIcon, String> iconPathProvider;

    public EmojiIconStyle(String styleId, Function<EmojiIcon, String> iconPathProvider) {
        this.styleId = styleId;
        this.iconPathProvider = iconPathProvider;
    }

    public String getStyleId() {
        return styleId;
    }

    public static List<EmojiIconStyle> getStyles(){
        return List.of(
                EmojiIconStyle.NOTO,
                EmojiIconStyle.TWEMOJI,
                EmojiIconStyle.OPENMOJI_COLOR,
                EmojiIconStyle.OPENMOJI_BLACK
        );
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        EmojiIconStyle iconStyle = (EmojiIconStyle) o;

        return styleId.equals(iconStyle.styleId);
    }

    @Override
    public int hashCode() {
        return styleId.hashCode();
    }

    public Function<EmojiIcon, String> getIconPathProvider() {
        return this.iconPathProvider;
    }
    public String getIconPath(EmojiIcon icon){
        return getIconPathProvider().apply(icon);
    }
}
