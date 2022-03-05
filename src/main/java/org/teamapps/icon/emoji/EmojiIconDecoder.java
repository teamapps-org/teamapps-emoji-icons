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

import org.teamapps.icons.IconDecoderContext;
import org.teamapps.icons.spi.IconDecoder;

public class EmojiIconDecoder implements IconDecoder<EmojiIcon> {
    /**
     * {@inheritDoc}
     */
    @Override
    public EmojiIcon decodeIcon(String encodedIconString, IconDecoderContext context) {
        String[] parts = encodedIconString.split("~");

        if (parts.length == 1) {
            // No Style / Default Style
            String iconID = parts[0];
            return EmojiIcon.forId(iconID); // .withStyle(EmojiIconStyle.LIGHT);
        } else if (parts.length == 2) {
            // Some Style
            String iconID = parts[1];
            String iconStyle = parts[0];

            EmojiIconStyle style = EmojiIconStyle.getById(iconStyle);
            if (style != null){
                return EmojiIcon.forId(iconID).withStyle(style);
            } else {
                System.out.printf("EmojiIconDecoder: iconStyle {1} not implemented%n", iconStyle);
                return null;
            }
//            switch (iconStyle) {
//                case "TWEMOJI":
//                    return EmojiIcon.forId(iconID).withStyle(EmojiIconStyle.TWEMOJI);
//                case "NOTO":
//                    return EmojiIcon.forId(iconID).withStyle(EmojiIconStyle.NOTO);
//                case "NOTO_BLACK":
//                    return EmojiIcon.forId(iconID).withStyle(EmojiIconStyle.NOTO_BLACK);
//                case "OPENMOJI":
//                    return EmojiIcon.forId(iconID).withStyle(EmojiIconStyle.OPENMOJI);
//                case "OPENMOJI_BLACK":
//                    return EmojiIcon.forId(iconID).withStyle(EmojiIconStyle.OPENMOJI_BLACK);
//                case "OPENMOJI_MOD_YELLOW":
//                    return EmojiIcon.forId(iconID).withStyle(EmojiIconStyle.OPENMOJI_MOD_YELLOW);
//                default:
//                    System.out.printf("EmojiIconDecoder: iconStyle {1} not implemented%n", iconStyle);
//                    return null;
//            }

        }
        return null;
    }
}
