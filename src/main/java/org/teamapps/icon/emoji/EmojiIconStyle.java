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

public class EmojiIconStyle {

    public static final EmojiIconStyle NOTO = new EmojiIconStyle("NOTO", "noto");
    public static final EmojiIconStyle TWEMOJI = new EmojiIconStyle("TWEMOJI", "twemoji");
    public static final EmojiIconStyle OPENMOJI_COLOR = new EmojiIconStyle("OPENMOJI_COLOR", "openmoji-svg-color");
    public static final EmojiIconStyle OPENMOJI_BLACK = new EmojiIconStyle("OPENMOJI_BLACK", "openmoji-svg-black");
    // public static final EmojiIconStyle BW = new EmojiIconStyle("BW", "svg_bw");

    private final String styleId;
    private final String folder;

    public EmojiIconStyle(String styleId, String folder) {
        this.styleId = styleId;
        this.folder = folder;
    }

    public String getFolder() {
        return folder;
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
}
