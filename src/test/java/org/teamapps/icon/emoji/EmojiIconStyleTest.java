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

import junit.framework.TestCase;
import org.junit.Test;

public class EmojiIconStyleTest extends TestCase {

    @Test
    public void testGetById() {
        assertEquals(EmojiIconStyle.getById("NOTO"), EmojiIconStyle.NOTO);
        assertEquals(EmojiIconStyle.getById("TWEMOJI"), EmojiIconStyle.TWEMOJI);

    }
}
