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

import org.teamapps.icons.IconLoaderContext;
import org.teamapps.icons.IconResource;
import org.teamapps.icons.IconType;
import org.teamapps.icons.spi.IconLoader;

import java.io.IOException;
import java.io.InputStream;
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
        return new IconResource(getSVG(icon), IconType.SVG);
    }

    private byte[] getSVG(EmojiIcon icon) {
        EmojiIconStyle style = icon.getStyle();

        String resourcePath = "/org/teamapps/icon/emoji/" + style.getIconPath(icon);


        try(InputStream inputStream = getClass().getResourceAsStream(resourcePath)) {
            if (inputStream == null) {
                return null;
            }
            return inputStream.readAllBytes();
            // String svg = IOUtils.toString(inputStream, StandardCharsets.UTF_8);
            // return svg.getBytes(StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
