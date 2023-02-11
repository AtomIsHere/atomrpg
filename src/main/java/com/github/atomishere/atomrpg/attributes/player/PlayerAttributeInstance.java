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

package com.github.atomishere.atomrpg.attributes.player;

import com.github.atomishere.atomrpg.attributes.BaseModifier;
import com.github.atomishere.atomrpg.utils.ItemUtils;
import com.github.atomishere.atomrpg.utils.MathUtils;
import com.github.atomishere.atomrpg.attributes.AtomAttribute;
import com.github.atomishere.atomrpg.attributes.item.ItemModifier;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class PlayerAttributeInstance {
    private final UUID playerUid;

    private final AtomAttribute attribute;

    private final List<BaseModifier> baseModifiers = new ArrayList<>();
    private double baseValue;

    public PlayerAttributeInstance(UUID player, AtomAttribute attribute, double baseValue) {
        this.playerUid = player;
        this.attribute = attribute;
        this.baseValue = MathUtils.clamp(baseValue, attribute.getMin(), attribute.getMax());
    }

    public AtomAttribute getAttribute() {
        return attribute;
    }

    public double getBaseValue() {
        return baseValue;
    }

    public void setBaseValue(double baseValue) {
        this.baseValue = MathUtils.clamp(baseValue, attribute.getMin(), attribute.getMax());
    }

    @SuppressWarnings("DuplicatedCode")
    public double getValueWithBaseModifiers() {
        double value = 0.0D;
        double scalar = 1.0D;

        for(BaseModifier modifier : baseModifiers) {
            switch(modifier.operation()) {
                case ADDITION -> value += modifier.value();
                case SUBTRACTION -> value -= modifier.value();
                case MULTIPLICATION -> scalar *= modifier.value();
                case DIVISION -> scalar /= modifier.value();
            }
        }

        return MathUtils.clamp(value * scalar, attribute.getMin(), attribute.getMax());
    }

    public double getValueWithAllModifiers() {
        Player player = Bukkit.getServer().getPlayer(playerUid);
        if(player == null) {
            return getValueWithBaseModifiers();
        }

        double baseValue = getValueWithBaseModifiers();

        for(EquipmentSlot slot : EquipmentSlot.values()) {
            baseValue += getItemAttributeValue(player.getInventory().getItem(slot), slot);
        }

        return baseValue;
    }

    private double getItemAttributeValue(ItemStack item, EquipmentSlot slot) {
        return ItemUtils.getItemAttribute(item)
                .map(ia -> getValueFromItemModifiers(
                        ia.getModifiers(attribute).orElse(Collections.EMPTY_LIST),
                        slot
                ))
                .orElse(0.0D);
    }

    @SuppressWarnings("DuplicatedCode")
    private double getValueFromItemModifiers(Collection<ItemModifier> modifiers, EquipmentSlot slot) {
        double value = 0.0D;
        double scalar = 1.0D;

        for (ItemModifier modifier : modifiers) {
            if (attribute == modifier.attribute() && modifier.slot() == slot) {
                switch (modifier.operation()) {
                    case ADDITION -> value += modifier.value();
                    case SUBTRACTION -> value -= modifier.value();
                    case MULTIPLICATION -> scalar *= modifier.value();
                    case DIVISION -> scalar /= modifier.value();
                }
            }
        }

        return value * scalar;
    }
}
