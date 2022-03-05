package org.teamapps.icon.emoji;

import junit.framework.TestCase;
import org.junit.Test;

public class EmojiIconStyleTest extends TestCase {

    @Test
    public void testGetById() {
        assertEquals(EmojiIconStyle.getById("NOTO"), EmojiIconStyle.NOTO);
        assertEquals(EmojiIconStyle.getById("TWEMOJI"), EmojiIconStyle.TWEMOJI);

    }
}
