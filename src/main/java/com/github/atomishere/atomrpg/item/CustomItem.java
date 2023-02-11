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

import com.github.atomishere.atomrpg.attributes.AtomAttribute;
import com.google.gson.annotations.SerializedName;
import org.bukkit.Material;
import org.bukkit.inventory.EquipmentSlot;

public record CustomItem(@SerializedName("id") String id, @SerializedName("display_name") String displayName, @SerializedName("rarity") Rarity rarity, @SerializedName("base") Material baseItem, @SerializedName("attributes") AttributeContainer[] attributes, @SerializedName("health_boost") double healthBoost, @SerializedName("speed_boost") double speedBoost, @SerializedName("slot") EquipmentSlot slot) {
    public record AttributeContainer(@SerializedName("attribute") AtomAttribute attribute, @SerializedName("value") double value) {}
}

