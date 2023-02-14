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

package com.github.atomishere.atomrpg;

import org.bukkit.NamespacedKey;

public final class PluginKeys {
    private static NamespacedKey ATOM_ATTRIBUTE_KEY;
    private static NamespacedKey CUSTOM_ITEM_KEY;

    static void init(AtomRPG plugin) {
        ATOM_ATTRIBUTE_KEY = new NamespacedKey(plugin, "atom_attributes");
        CUSTOM_ITEM_KEY = new NamespacedKey(plugin, "custom_item");
    }

    public static NamespacedKey getAtomAttributeKey() {
        return ATOM_ATTRIBUTE_KEY;
    }
    public static NamespacedKey getCustomItemKey() {
        return CUSTOM_ITEM_KEY;
    }

    private PluginKeys() {
        throw new AssertionError();
    }
}
