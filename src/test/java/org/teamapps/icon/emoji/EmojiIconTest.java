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

import org.junit.Test;
import org.teamapps.icons.IconResource;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.Assert.*;
import static org.junit.Assert.assertTrue;

public class EmojiIconTest {
    private EmojiIconLoader emojiIconLoader = new EmojiIconLoader();

    public static void main(String[] args) {
        System.out.println("EmojiIcon Count: " + EmojiIcon.getIcons().size());
        EmojiIcon icon = EmojiIcon.WAVING_HAND__LIGHT_SKIN_TONE;

        int count = 0;
        // this.unicode = new String(bytes, "UTF-8");
        int stringLength = icon.getUnicode().length();
        String[] pointCodes = new String[stringLength];
        String[] pointCodesHex = new String[stringLength];

        for (int offset = 0; offset < stringLength; ) {
            final int codePoint = icon.getUnicode().codePointAt(offset);

            pointCodes[count] = String.format("&#%d;", codePoint);
            pointCodesHex[count++] = String.format("%x", codePoint);

            offset += Character.charCount(codePoint);
        }
        System.out.println(Arrays.toString(pointCodesHex));
        // var htmlDec = "".join(pointCodes);
        IntStream codePoints = icon.getUnicode().codePoints();

        // codePoints.forEach(value -> System.out.println(value));
        codePoints.forEach(codePoint -> System.out.println(String.format("%x", codePoint)));

        List<String> codePointsList = icon.getUnicode().codePoints().mapToObj(codePoint -> String.format("%x", codePoint)).collect(Collectors.toUnmodifiableList());
        String filename = codePointsList.stream().map(String::toUpperCase).collect(Collectors.joining("_"));
        System.out.println(filename);
    }

    @Test
    public void forUnicode() {
        assertEquals(EmojiIcon.forUnicode("👋🏻"), EmojiIcon.WAVING_HAND__LIGHT_SKIN_TONE);
    }


    @Test
    public void loadAllIcons() {

        for (EmojiIcon icon : EmojiIcon.getIcons()) {
            String svg = "";
            try {
                IconResource iconResource = emojiIconLoader.loadIcon(icon, 22, null);
                svg = new String(iconResource.getBytes());
            } catch (Exception e) {
                System.out.println("ERROR Loading Icon: " + icon.getIconId() + ", error: " + e.getMessage());
            }
            assertNotNull(svg);
//            assertNotEquals("", svg);
            if (!svg.contains("<svg")) {
                System.out.println(icon.getIconId() + "\n" + svg);
            }
            assertTrue("resource contains svg start tag " + icon.getIconId() + "\n" + svg, svg.contains("<svg"));
            assertTrue("resource contains svg end tag " + icon.getIconId() + "\n" + svg, svg.contains("</svg>"));
        }
    }

    @Test
    public void loadAllIconsNoto() {
        loadAllIconsWithStyle(EmojiIconStyle.NOTO);
    }
    @Test
    public void loadAllIconsNotoBlack() {
        loadAllIconsWithStyle(EmojiIconStyle.NOTO_BLACK);
    }
    @Test
    public void loadAllIconsTwemoji() {
        loadAllIconsWithStyle(EmojiIconStyle.TWEMOJI);
    }
    @Test
    public void loadAllIconsOpenMojiColor() {
        loadAllIconsWithStyle(EmojiIconStyle.OPENMOJI);
    }
    @Test
    public void loadAllIconsOpenMojiBlack() {
        loadAllIconsWithStyle(EmojiIconStyle.OPENMOJI_BLACK);
    }
    @Test
    public void loadAllIconsOpenMojiModYellow() {
        loadAllIconsWithStyle(EmojiIconStyle.OPENMOJI_MOD_YELLOW);
    }

    private void loadAllIconsWithStyle(EmojiIconStyle style){

        for (EmojiIcon icon : EmojiIcon.getIcons()) {
            String svg = "";
            try {
                EmojiIcon styledIcon = icon.withStyle(style);
                IconResource iconResource = emojiIconLoader.loadIcon(styledIcon, 22, null);
                svg = new String(iconResource.getBytes());
            } catch (Exception e) {
                System.out.println("ERROR Loading Icon: " + icon.getIconId() + ", error: " + e.getMessage());
            }
            assertNotNull(svg);
//            assertNotEquals("", svg);
            if (!svg.contains("<svg")) {
                System.out.println(icon.getIconId() + "\n" + svg);
            }
            assertTrue("resource contains svg start tag " + icon.getIconId() + "\n" + svg, svg.contains("<svg"));
            assertTrue("resource contains svg end tag " + icon.getIconId() + "\n" + svg, svg.contains("</svg>"));
        }

    }
}
