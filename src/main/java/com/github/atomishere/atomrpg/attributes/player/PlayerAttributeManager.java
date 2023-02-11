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

import com.github.atomishere.atomrpg.attributes.AtomAttribute;
import com.google.inject.Singleton;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Singleton
public class PlayerAttributeManager {
    private final Map<UUID, PlayerAttributable> attributeMap = new HashMap<>();
    private final Map<AtomAttribute, Double> playerDefaults = new HashMap<>();

    public void enable() {
        // TODO: Move this to config
        playerDefaults.put(AtomAttribute.DEFENSE, 0.0D);
        playerDefaults.put(AtomAttribute.STRENGTH, 10.0D);
        playerDefaults.put(AtomAttribute.DAMAGE, 0.0D);
        playerDefaults.put(AtomAttribute.INTELLIGENCE, 100.0D);
        playerDefaults.put(AtomAttribute.CRIT_DAMAGE, 50.0D);
        playerDefaults.put(AtomAttribute.CRIT_CHANCE, 10.0D);
    }

    public void disable() {
        attributeMap.clear();
        playerDefaults.clear();
    }

    public void registerPlayer(Player player) {
        PlayerAttributable attributable = new PlayerAttributable(player.getUniqueId(), new HashMap<>());
        playerDefaults.forEach(attributable::overrideDefault);

        // TODO: Add per player defaults through configs

        attributeMap.put(player.getUniqueId(), attributable);
    }

    public void cleanPlayer(Player player) {
        attributeMap.remove(player.getUniqueId());
    }

    public PlayerAttributable getPlayerAttributes(Player player) {
        if(!attributeMap.containsKey(player.getUniqueId())) {
            registerPlayer(player);
        }

        return attributeMap.get(player.getUniqueId());
    }
}
