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

import com.github.atomishere.atomrpg.entity.adapter.ZombieAdapter;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.level.Level;

public class TestEntity extends ZombieAdapter<TestEntity> {
    public TestEntity(EntityType<? extends Zombie> type, Level world) {
        super(type, world, "Test Entity", 1);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Zombie.createAttributes();
    }

    @Override
    public CustomEntityType<TestEntity> getCustomType() {
        return AtomEntities.TEST_ENTITY;
    }

    @Override
    public TestEntity get() {
        return this;
    }
}
