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

package com.github.atomishere.atomrpg.attributes;

import org.bukkit.ChatColor;

public enum AtomAttribute {
    DEFENSE("Defense", ChatColor.GREEN, 0.0D, Double.MAX_VALUE),
    STRENGTH("Strength", ChatColor.RED, 0.0D, Double.MAX_VALUE),
    INTELLIGENCE("Intelligence", ChatColor.AQUA, 0.0D, Double.MAX_VALUE),
    CRIT_DAMAGE("Crit Damage", ChatColor.BLUE, 0.0D, Double.MAX_VALUE),
    CRIT_CHANCE("Crit Chance", ChatColor.BLUE, 0.0D, 100.0D);

    private final String displayName;
    private final ChatColor displayColor;

    private final double min;
    private final double max;

    AtomAttribute(String displayName, ChatColor displayColor, double min, double max) {
        this.displayName = displayName;
        this.displayColor = displayColor;
        this.min = min;
        this.max = max;
    }

    public String getDisplayName() {
        return displayName;
    }

    public ChatColor getDisplayColor() {
        return displayColor;
    }

    public double getMin() {
        return min;
    }

    public double getMax() {
        return max;
    }
}
