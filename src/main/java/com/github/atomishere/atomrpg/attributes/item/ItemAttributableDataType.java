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

import org.bukkit.persistence.PersistentDataAdapterContext;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.util.Arrays;
import java.util.List;

public class ItemAttributableDataType implements PersistentDataType<byte[], ItemAttributable> {
    @Override
    public @NotNull Class<byte[]> getPrimitiveType() {
        return byte[].class;
    }

    @Override
    public @NotNull Class<ItemAttributable> getComplexType() {
        return ItemAttributable.class;
    }

    @Override
    public byte @NotNull [] toPrimitive(@NotNull ItemAttributable complex, @NotNull PersistentDataAdapterContext context) {
        int size = 0;
        for(List<ItemModifier> modifierList : complex.getAttributeMap().values()) {
            size += modifierList.size();
        }

        ItemModifier[] primList = new ItemModifier[size];

        int i = 0;
        for(List<ItemModifier> modifierList : complex.getAttributeMap().values()) {
            for(ItemModifier modifier : modifierList) {
                primList[i] = modifier;
                i++;
            }
        }

        try(ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos)) {
            oos.writeObject(primList);
            return baos.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public @NotNull ItemAttributable fromPrimitive(byte @NotNull [] primitive, @NotNull PersistentDataAdapterContext context) {
        ItemModifier[] primList;
        try(ByteArrayInputStream bis = new ByteArrayInputStream(primitive);
            ObjectInputStream in = new ObjectInputStream(bis)) {
            primList = (ItemModifier[]) in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        ItemAttributable attributable = new ItemAttributable();
        Arrays.stream(primList).forEach(attributable::addModifier);
        return attributable;
    }
}
