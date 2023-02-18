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

package com.github.atomishere.atomrpg.damage;

import com.github.atomishere.atomrpg.attributes.AtomAttribute;
import com.google.common.collect.ImmutableList;
import com.google.inject.Singleton;

import java.util.ArrayList;
import java.util.List;

@Singleton
public class DamageHandler {
    private final List<DamageModifier> modifiers = new ArrayList<>();
    private final ImmutableList<DamageModifier> baseModifiers = ImmutableList
            .<DamageModifier>builder()
            .add((ds, cd) -> ((5.0D + ds.getAttackerAttribute(AtomAttribute.DAMAGE)) * (1.0D + (ds.getAttackerAttribute(AtomAttribute.STRENGTH) / 100.0D))))
            .add((ds, cd) -> {
                double critChance = ds.getAttackerAttribute(AtomAttribute.CRIT_CHANCE) / 100.0D;

                if(Math.random() <= critChance) {
                    return cd * (1 + (ds.getAttackerAttribute(AtomAttribute.CRIT_DAMAGE)/100.0D));
                }

                return cd;
            })
            .add((ds, cd) -> {
                double def = ds.getDamagedAttribute(AtomAttribute.DEFENSE);

                return cd * (1 - (def/(def + 100)));
            })
            .build();

    public void registerModifier(DamageModifier modifier) {
        modifiers.add(modifier);
    }

    public List<DamageModifier> getModifiers() {
        return modifiers;
    }

    public void clear() {
        modifiers.clear();
    }
}
