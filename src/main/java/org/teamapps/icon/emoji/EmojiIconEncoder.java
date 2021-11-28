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

import org.teamapps.icons.IconEncoderContext;
import org.teamapps.icons.spi.IconEncoder;

public class EmojiIconEncoder implements IconEncoder<EmojiIcon> {
    /**
     * {@inheritDoc}
     */
    @Override
    public String encodeIcon(EmojiIcon icon, IconEncoderContext context) {
        String encodedString;
        EmojiIconStyle style = icon.getStyle();
//        if (style == EmojiIconStyle.DARK) {
//            encodedString = "DARK~" + icon.getIconId();
//        } else {
//            encodedString = icon.getIconId();
//        }
        encodedString = style.getStyleId() + "~" + icon.getIconId();
        return encodedString;
    }
}
