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

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class PlayerAttributable {
    private final UUID player;
    private final Map<AtomAttribute, Double> defaults;
    private final Map<AtomAttribute, PlayerAttributeInstance> attributes;

    public PlayerAttributable(UUID player, Map<AtomAttribute, Double> defaultsOverride) {
        this.player = player;
        this.defaults = defaultsOverride;
        this.attributes = new HashMap<>();
    }

    public Optional<PlayerAttributeInstance> getInstance(AtomAttribute attribute) {
        if(!attributes.containsKey(attribute)) {
            if(defaults.containsKey(attribute)) {
                attributes.put(attribute, new PlayerAttributeInstance(player, attribute, defaults.get(attribute)));
            } else {
                return Optional.empty();
            }
        }

        return Optional.of(attributes.get(attribute));
    }

    public void overrideDefault(AtomAttribute attribute, double defaultValue) {
        defaults.put(attribute, defaultValue);
    }
}
