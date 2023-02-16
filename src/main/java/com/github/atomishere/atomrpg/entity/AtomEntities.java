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

import com.google.common.collect.ImmutableMap;
import io.github.toolfactory.narcissus.Narcissus;
import net.minecraft.core.Holder;
import net.minecraft.core.MappedRegistry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.DefaultAttributes;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.Optional;

import static com.github.atomishere.atomrpg.utils.ReflectionUtils.DEOBF_ENV;

public class AtomEntities {
    private static boolean registered = false;

    public static final CustomEntityType<TestEntity> TEST_ENTITY = new CustomEntityType<>("test_entity", TestEntity.class, EntityType.ZOMBIE, TestEntity::new);

    private static final ImmutableMap<String, CustomEntityType<?>> entityMap;

    static {
        ImmutableMap.Builder<String, CustomEntityType<?>> builder = ImmutableMap.builder();

        for(Field field : AtomEntities.class.getDeclaredFields()) {
            if(Modifier.isStatic(field.getModifiers())
                    && Modifier.isPublic(field.getModifiers())
                    && field.getType().equals(CustomEntityType.class)) {
                Object obj;
                try {
                    obj = field.get(null);
                } catch (IllegalAccessException e) {
                    continue;
                }

                if(!(obj instanceof CustomEntityType<?>)) {
                    continue;
                }

                CustomEntityType<? extends LivingEntity> entityType = (CustomEntityType<? extends LivingEntity>) obj;
                builder.put(field.getName(), entityType);
            }
        }

        entityMap = builder.build();
    }

    private AtomEntities() {
        throw new AssertionError();
    }

    public static void register() {
        if(registered) {
            throw new IllegalStateException("Custom entities are already registered");
        }

        try {
            Field frozenField = MappedRegistry.class.getDeclaredField(DEOBF_ENV ? "frozen" : "l");
            Field intrusiveHolderField = MappedRegistry.class.getDeclaredField(DEOBF_ENV ? "unregisteredIntrusiveHolders" : "m");
            intrusiveHolderField.setAccessible(true);
            frozenField.setAccessible(true);

            intrusiveHolderField.set(BuiltInRegistries.ENTITY_TYPE, new IdentityHashMap<EntityType<?>, Holder.Reference<EntityType<?>>>());
            frozenField.set(BuiltInRegistries.ENTITY_TYPE, false);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
            return;
        }

        entityMap.values().forEach(CustomEntityType::register);
        BuiltInRegistries.ENTITY_TYPE.freeze();

        try {
            Field defaultAttributesField = DefaultAttributes.class.getDeclaredField(DEOBF_ENV ? "SUPPLIERS" : "b");

            Field modifiersField = Narcissus.findField(Field.class, "modifiers");

            defaultAttributesField.setAccessible(true);

            Narcissus.setIntField(defaultAttributesField, modifiersField, defaultAttributesField.getModifiers() & ~Modifier.FINAL);

            ImmutableMap.Builder<EntityType<? extends LivingEntity>, AttributeSupplier> attributeBuilder = ImmutableMap.builder();
            Map<EntityType<? extends LivingEntity>, AttributeSupplier> originalMap = (Map<EntityType<? extends LivingEntity>, AttributeSupplier>) defaultAttributesField.get(null);
            attributeBuilder.putAll(originalMap);

            for(CustomEntityType<?> entityType : entityMap.values()) {
                Class<? extends LivingEntity> entityClass = entityType.getEntityClass();

                Method attributeMethod;
                try {
                    attributeMethod = entityClass.getDeclaredMethod("createAttributes");
                } catch (NoSuchMethodException e) {
                    continue;
                }

                AttributeSupplier.Builder builder = (AttributeSupplier.Builder) attributeMethod.invoke(null);
                attributeBuilder.put(entityType.getEntityType(), builder.build());
            }

            defaultAttributesField.set(null, attributeBuilder.build());
        } catch (NoSuchFieldException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }

        registered = true;
    }


    public static void unregister() {
        if(!registered) {
            throw new IllegalStateException("Custom entities aren't registered");
        }

        entityMap.values().forEach(CustomEntityType::unregister);
        registered = false;
    }

    public static Optional<CustomEntityType<? extends LivingEntity>> get(String key) {
        if(!entityMap.containsKey(key)) {
            return Optional.empty();
        }

        return Optional.of(entityMap.get(key));
    }

    public static ImmutableMap<String, CustomEntityType<?>> getEntityMap() {
        return entityMap;
    }
}
