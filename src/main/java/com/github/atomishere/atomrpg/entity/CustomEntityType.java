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

package com.github.atomishere.atomrpg.entity;

import com.mojang.datafixers.DataFixUtils;
import com.mojang.datafixers.types.Type;

import net.minecraft.SharedConstants;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.datafix.DataFixers;
import net.minecraft.util.datafix.fixes.References;
import net.minecraft.world.entity.*;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_19_R2.CraftWorld;

import java.util.Map;
import java.util.Optional;

public class CustomEntityType<T extends LivingEntity> {
    private final ResourceLocation key;
    private final Class<T> clazz;
    private final EntityType.EntityFactory<T> factory;
    private final EntityType<? super T> parentType;
    private final int networkId;
    private EntityType<T> entityType;
    private boolean registered;

    public CustomEntityType(String name, Class<T> customEntityClass, EntityType<? super T> parentType, EntityType.EntityFactory<T> factory, int networkId) {
        this.key = ResourceLocation.tryParse(name);
        this.clazz = customEntityClass;
        this.parentType = parentType;
        this.factory = factory;
        this.networkId = networkId;
    }

    public CustomEntityType(String name, Class<T> customEntityClass, EntityType<? super T> parentType, EntityType.EntityFactory<T> factory) {
        this(name, customEntityClass, parentType, factory, BuiltInRegistries.ENTITY_TYPE.getId(parentType));
    }

    public Optional<org.bukkit.entity.Entity> spawn(Location loc) {
        Entity entity = entityType.spawn(((CraftWorld) loc.getWorld()).getHandle(), new BlockPos(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ()), MobSpawnType.EVENT);

        return entity == null ? Optional.empty() : Optional.of(entity.getBukkitEntity());
    }

    public void register() {
        if(registered || BuiltInRegistries.ENTITY_TYPE.getOptional(key).isPresent()) {
            throw new IllegalStateException(String.format("Entity %s is already registered", key));
        }

        Map<Object, Type<?>> dataTypes = (Map<Object, Type<?>>) DataFixers.getDataFixer()
                .getSchema(DataFixUtils.makeKey(SharedConstants.getCurrentVersion().getDataVersion().getVersion()))
                .findChoiceType(References.ENTITY_TREE)
                .types();
        dataTypes.put(key.toString(), dataTypes.get(parentType.getDefaultLootTable().toString().replace("entities/", "")));

        EntityType.Builder<T> entBuilder = EntityType.Builder.of(factory, MobCategory.CREATURE);
        entityType = entBuilder.build(key.getPath());
        Registry.registerMapping(BuiltInRegistries.ENTITY_TYPE, networkId, key.getPath(), entityType);
        registered = true;
    }

    public void unregister() {
        if(!registered) {
            throw new IllegalStateException(String.format("Entity %s is not registered", key));
        }

        Map<Object, Type<?>> dataTypes = (Map<Object, Type<?>>) DataFixers.getDataFixer()
                .getSchema(DataFixUtils.makeKey(SharedConstants.getCurrentVersion().getDataVersion().getVersion()))
                .findChoiceType(References.ENTITY_TREE)
                .types();
        dataTypes.remove(key.getPath());

        registered = false;
    }

    public ResourceLocation getKey() {
        return key;
    }

    public Class<T> getEntityClass() {
        return clazz;
    }

    public EntityType.EntityFactory<T> getFactory() {
        return factory;
    }

    public EntityType<? super T> getParentType() {
        return parentType;
    }

    public int getNetworkId() {
        return networkId;
    }

    public EntityType<T> getEntityType() {
        return entityType;
    }

    public boolean isRegistered() {
        return registered;
    }
}
