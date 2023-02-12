/*
 * Copyright (c) 2023 AtomIsHere
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Affero General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Affero General Public License for more details.
 *
 *     You should have received a copy of the GNU Affero General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.github.atomishere.atomrpg.item;

import com.google.gson.Gson;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.JarURLConnection;
import java.nio.charset.StandardCharsets;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;


@Singleton
public class ItemHandler {
    private final Map<String, CustomItem> registeredItems = new HashMap<>();

    @Inject
    private Gson gson;

    public void loadItems() {
        JarURLConnection connection;
        JarFile file;

        try {
            connection = (JarURLConnection) getClass().getResource("/items/").openConnection();
            file = connection.getJarFile();
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        Enumeration<JarEntry> entries = file.entries();
        while(entries.hasMoreElements()) {
            JarEntry entry = entries.nextElement();
            if(entry.getName().startsWith("items/") && entry.getName().endsWith(".json") && !entry.isDirectory()) {
                try(InputStream jarStream =
                        file.getInputStream(entry)) {
                    CustomItem item = gson.fromJson(new InputStreamReader(jarStream, StandardCharsets.UTF_8), CustomItem.class);

                    registeredItems.put(item.id(), item);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public Optional<CustomItem> getRegisteredItem(String id) {
        if(!registeredItems.containsKey(id)) {
            return Optional.empty();
        }

        return Optional.of(registeredItems.get(id));
    }
}
