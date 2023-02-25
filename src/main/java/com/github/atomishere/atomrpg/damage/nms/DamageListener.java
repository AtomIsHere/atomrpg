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

import com.github.atomishere.atomrpg.attributes.player.PlayerAttributeManager;
import com.github.atomishere.atomrpg.damage.AtomDamageSource;
import com.github.atomishere.atomrpg.damage.DamageHandler;
import com.github.atomishere.atomrpg.damage.DamageModifier;
import com.github.atomishere.atomrpg.entity.AtomEntities;
import com.github.atomishere.atomrpg.entity.DisplayEntity;
import com.github.atomishere.atomrpg.entity.adapter.AtomEntityAdapter;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.kyori.adventure.text.Component;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_19_R2.entity.CraftEntity;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

@Singleton
public class DamageListener implements Listener {
    @Inject
    private DamageHandler handler;
    @Inject
    private PlayerAttributeManager attributeManager;

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent event) {
        if(event.getEntity() instanceof LivingEntity) {
            DamageSourceImpl.Builder damageSourceBuilder = DamageSourceImpl.newBuilder();

            if(event.getDamager() instanceof Player player) {
                damageSourceBuilder.setPlayerAttacker(attributeManager.getPlayerAttributes(player));
            } else if(((CraftEntity) event.getDamager()).getHandle() instanceof AtomEntityAdapter<?> entity) {
                damageSourceBuilder.setEntityAttacker((AtomEntityAdapter<? extends LivingEntity>) entity);
            } else {
                return;
            }

            if(event.getEntity() instanceof Player player) {
                damageSourceBuilder.setPlayerDamaged(attributeManager.getPlayerAttributes(player));
            } else if(((CraftEntity) event.getEntity()).getHandle() instanceof AtomEntityAdapter<?> entity) {
                damageSourceBuilder.setCustomEntityDamaged((AtomEntityAdapter<? extends LivingEntity>) entity);
            } else {
                damageSourceBuilder.setVanillaEntityDamaged((LivingEntity) event.getEntity());
            }

            AtomDamageSource damageSource = damageSourceBuilder.build();

            double damage = 0;
            for(DamageModifier modifier : handler.getBaseModifiers()) {
                damage = modifier.apply(damageSource, damage);
            }
            for(DamageModifier modifier : handler.getModifiers()) {
                damage = modifier.apply(damageSource, damage);
            }

            event.setDamage(EntityDamageEvent.DamageModifier.BASE, damage);
            // Completely override all other forms of damage
            Arrays.stream(EntityDamageEvent.DamageModifier.values())
                    .filter(dm -> !dm.equals(EntityDamageEvent.DamageModifier.BASE) && event.isApplicable(dm))
                    .forEach(dm -> event.setDamage(dm, 0.0D));

            createDamageDisplay(damage, damageSource.damaged(), damageSource.attacker().getLocation().getYaw(), damageSource.attacker().getLocation().getPitch());
        }
    }

    private void createDamageDisplay(double damage, Entity entityDamaged, float yaw, float pitch) {
        double height = entityDamaged.getLocation().y() + entityDamaged.getHeight();

        double maxX = entityDamaged.getLocation().x() + (entityDamaged.getBoundingBox().getWidthX()/2.0D);
        double minX = entityDamaged.getLocation().x() - (entityDamaged.getBoundingBox().getWidthX()/2.0D);

        double maxZ = entityDamaged.getLocation().z() + (entityDamaged.getBoundingBox().getWidthZ()/2.0D);
        double minZ = entityDamaged.getLocation().z() - (entityDamaged.getBoundingBox().getWidthZ()/2.0D);

        Random random = ThreadLocalRandom.current();
        Location displayLoc = new Location(entityDamaged.getWorld(), random.nextDouble(minX, maxX), height, random.nextDouble(minZ, maxZ), yaw, pitch);

        AtomEntities.DISPLAY_ENTITY.spawn(displayLoc).ifPresent(e -> {
            DisplayEntity display = (DisplayEntity) ((CraftEntity) e).getHandle();

            display.setDisplayName(Component.text(Math.round(damage)));
            display.startTimer(40);
        });
    }
}
