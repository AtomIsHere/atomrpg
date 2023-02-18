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

package com.github.atomishere.atomrpg.damage.nms;

import com.github.atomishere.atomrpg.attributes.AtomAttribute;
import com.github.atomishere.atomrpg.attributes.player.PlayerAttributable;
import com.github.atomishere.atomrpg.damage.AtomDamageSource;
import com.github.atomishere.atomrpg.entity.adapter.AtomEntityAdapter;
import org.bukkit.Bukkit;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class DamageSourceImpl implements AtomDamageSource {
    private final LivingEntity attacker;
    private final LivingEntity damaged;

    private final Map<AtomAttribute, Double> attackerAttributes;
    private final Map<AtomAttribute, Double> damagedAttributes;

    private DamageSourceImpl(LivingEntity attacker, LivingEntity damaged, Map<AtomAttribute, Double> attackerAttributes, Map<AtomAttribute, Double> damagedAttributes) {
        this.attacker = attacker;
        this.damaged = damaged;

        this.attackerAttributes = attackerAttributes;
        this.damagedAttributes = damagedAttributes;
    }

    @Override
    public LivingEntity attacker() {
        return attacker;
    }

    @Override
    public LivingEntity damaged() {
        return damaged;
    }

    @Override
    public double getAttackerAttribute(AtomAttribute attribute) {
        return attackerAttributes.getOrDefault(attribute, 0.0D);
    }

    @Override
    public double getDamagedAttribute(AtomAttribute attribute) {
        return damagedAttributes.getOrDefault(attribute, 0.0D);
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public static class Builder {
        private LivingEntity attacker;
        private final Map<AtomAttribute, Double> attackerAttributes = new HashMap<>();

        private LivingEntity damaged;
        private final Map<AtomAttribute, Double> damagedAttributes = new HashMap<>();

        private Builder() {}


        public Builder setPlayerAttacker(PlayerAttributable attributable) {
            Player player = Bukkit.getServer().getPlayer(attributable.getPlayer());

            attackerAttributes.clear();
            Arrays.stream(AtomAttribute.values())
                    .map(attributable::getInstance)
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .forEach(a -> attackerAttributes.put(a.getAttribute(), a.getValueWithAllModifiers()));

            this.attacker = player;

            return this;
        }

        public Builder setEntityAttacker(AtomEntityAdapter<? extends LivingEntity> entityAttacker) {
            attackerAttributes.clear();
            Arrays.stream(AtomAttribute.values())
                    .map(entityAttacker::getAttribute)
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .forEach(a -> attackerAttributes.put(a.getAttribute(), a.getValue()));

            this.attacker = entityAttacker.get();

            return this;
        }

        public Builder setPlayerDamaged(PlayerAttributable attributable) {
            Player player = Bukkit.getServer().getPlayer(attributable.getPlayer());

            damagedAttributes.clear();
            Arrays.stream(AtomAttribute.values())
                    .map(attributable::getInstance)
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .forEach(a -> damagedAttributes.put(a.getAttribute(), a.getValueWithAllModifiers()));

            this.damaged = player;

            return this;
        }

        public Builder setCustomEntityDamaged(AtomEntityAdapter<? extends LivingEntity> entityAdapter) {
            damagedAttributes.clear();
            Arrays.stream(AtomAttribute.values())
                    .map(entityAdapter::getAttribute)
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .forEach(a -> damagedAttributes.put(a.getAttribute(), a.getValue()));

            this.damaged = entityAdapter.get();

            return this;
        }

        public Builder setVanillaEntityDamaged(LivingEntity entity) {
            this.damaged = entity;

            return this;
        }

        public AtomDamageSource build() {
            return new DamageSourceImpl(attacker, damaged, attackerAttributes, damagedAttributes);
        }
    }
}
