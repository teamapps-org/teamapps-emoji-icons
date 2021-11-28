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

public class EmojiIconLoader implements IconLoader<EmojiIcon> {
    /**
     * {@inheritDoc}
     */
    @Override
    public IconResource loadIcon(EmojiIcon icon, int size, IconLoaderContext context) {
        return new IconResource(getSVG(icon.getIconPath(), icon.getStyle()), IconType.SVG);
    }

    private byte[] getSVG(String iconPath, EmojiIconStyle style) {

        String folder = style.getFolder();
        String resourcePath = "/org/teamapps/icon/emoji/" + folder + "/" + iconPath;

        if (style.equals(EmojiIconStyle.TWEMOJI)){
            resourcePath = "/org/teamapps/icon/emoji/" + folder + "/" + iconPath.replace('_','-').replaceAll("u", "");
        } else if (style.equals(EmojiIconStyle.OPENMOJI_COLOR) || style.equals(EmojiIconStyle.OPENMOJI_BLACK)){
            resourcePath = "/org/teamapps/icon/emoji/" + folder + "/" + iconPath.replaceAll("svg/", "").replace('_','-').replaceAll("u", "").toUpperCase().replaceAll("SVG", "svg");
        }


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
