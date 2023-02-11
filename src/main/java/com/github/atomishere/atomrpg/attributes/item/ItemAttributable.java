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

package com.github.atomishere.atomrpg.attributes.item;

import com.github.atomishere.atomrpg.attributes.AtomAttribute;

import java.util.*;

public class ItemAttributable {
    public static final ItemAttributableDataType DATA_TYPE = new ItemAttributableDataType();

    private final Map<AtomAttribute, List<ItemModifier>> attributeMap = new HashMap<>();

    public void addModifier(ItemModifier modifier) {
        if(attributeMap.containsKey(modifier.attribute())) {
            attributeMap.put(modifier.attribute(), new ArrayList<>());
        }

        attributeMap.get(modifier.attribute()).add(modifier);
    }

    public Optional<List<ItemModifier>> getModifiers(AtomAttribute attribute) {
        List<ItemModifier> modifiers = attributeMap.get(attribute);
        if(modifiers == null) {
            return Optional.empty();
        }

        return Optional.of(modifiers);
    }

    public Map<AtomAttribute, List<ItemModifier>> getAttributeMap() {
        return attributeMap;
    }
}
