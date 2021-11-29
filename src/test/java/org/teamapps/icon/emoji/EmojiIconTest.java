package org.teamapps.icon.emoji;

import org.junit.Test;
import org.teamapps.icons.IconResource;

import static org.junit.Assert.*;
import static org.junit.Assert.assertTrue;

public class EmojiIconTest {
    private EmojiIconLoader emojiIconLoader = new EmojiIconLoader();

    public static void main(String[] args) {
        System.out.println("EmojiIcon Count: " + EmojiIcon.getIcons().size());
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
//    @Test
//    public void loadAllIconsTwemoji() {
//
//        for (EmojiIcon icon : EmojiIcon.getIcons()) {
//            String svg = "";
//            try {
//                EmojiIcon styledIcon = icon.withStyle(EmojiIconStyle.TWEMOJI);
//                IconResource iconResource = emojiIconLoader.loadIcon(styledIcon, 22, null);
//                svg = new String(iconResource.getBytes());
//            } catch (Exception e) {
//                System.out.println("ERROR Loading Icon: " + icon.getIconId() + ", error: " + e.getMessage());
//            }
//            assertNotNull(svg);
////            assertNotEquals("", svg);
//            if (!svg.contains("<svg")) {
//                System.out.println(icon.getIconId() + "\n" + svg);
//            }
//            assertTrue("resource contains svg start tag " + icon.getIconId() + "\n" + svg, svg.contains("<svg"));
//            assertTrue("resource contains svg end tag " + icon.getIconId() + "\n" + svg, svg.contains("</svg>"));
//        }
//    }
}
