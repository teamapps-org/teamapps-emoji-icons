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

        for (EmojiIcon antuIcon : EmojiIcon.getIcons()) {
            String svg = "";
            try {
                IconResource iconResource = emojiIconLoader.loadIcon(antuIcon, 22, null);
                svg = new String(iconResource.getBytes());
            } catch (Exception e) {
                System.out.println("ERROR Loading Icon: " + antuIcon.getIconId() + ", error: " + e.getMessage());
            }
            assertNotNull(svg);
//            assertNotEquals("", svg);
            if (!svg.contains("<svg")) {
                System.out.println(antuIcon.getIconId() + "\n" + svg);
            }
            assertTrue("resource contains svg start tag " + antuIcon.getIconId() + "\n" + svg, svg.contains("<svg"));
            assertTrue("resource contains svg end tag " + antuIcon.getIconId() + "\n" + svg, svg.contains("</svg>"));
        }
    }
}
