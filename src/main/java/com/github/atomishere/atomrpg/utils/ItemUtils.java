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

package com.github.atomishere.atomrpg.utils;

import com.github.atomishere.atomrpg.PluginKeys;
import com.github.atomishere.atomrpg.attributes.item.ItemAttributable;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;

import java.util.Optional;

public final class ItemUtils {
    private ItemUtils() {
        throw new AssertionError();
    }

    public static Optional<ItemAttributable> getItemAttribute(ItemStack item) {
        PersistentDataContainer container = item.getItemMeta().getPersistentDataContainer();

        if(!container.has(PluginKeys.getAtomAttributeKey(), ItemAttributable.DATA_TYPE)) {
            return Optional.empty();
        }

        return Optional.of(container.get(PluginKeys.getAtomAttributeKey(), ItemAttributable.DATA_TYPE));
    }


}
