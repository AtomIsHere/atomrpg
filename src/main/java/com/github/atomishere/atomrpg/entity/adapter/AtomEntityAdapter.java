package com.github.atomishere.atomrpg.entity.adapter;

import com.github.atomishere.atomrpg.entity.CustomEntityType;
import com.github.atomishere.atomrpg.entity.attributes.AtomEntityAttributable;
import net.minecraft.world.entity.LivingEntity;

public interface AtomEntityAdapter<T extends LivingEntity> extends AtomEntityAttributable {
    String getBaseName();
    CustomEntityType<T> getCustomType();
    int getSkillLevel(); // TODO: Used for skill system
    T get();

}
