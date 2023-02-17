package com.github.atomishere.atomrpg.entity.adapter;

import com.github.atomishere.atomrpg.attributes.AtomAttribute;
import com.github.atomishere.atomrpg.entity.CustomEntityType;
import com.github.atomishere.atomrpg.entity.attributes.AtomEntityAttributeInstance;
import com.github.atomishere.atomrpg.utils.AdapterUtils;
import io.papermc.paper.adventure.PaperAdventure;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.level.Level;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public abstract class ZombieAdapter<T extends ZombieAdapter<T>> extends Zombie implements AtomEntityAdapter<T> {
    protected final String baseName;
    protected final int skillLevel;

    protected final Map<AtomAttribute, AtomEntityAttributeInstance> attributes = new HashMap<>();

    public ZombieAdapter(EntityType<? extends Zombie> type, Level world, String baseName, int skillLevel) {
        super(type, world);

        this.baseName = baseName;
        this.skillLevel = skillLevel;

        setCustomName(PaperAdventure.asVanilla(AdapterUtils.getDisplayName(this)));
        setCustomNameVisible(true);
    }

    @Override
    public String getBaseName() {
        return baseName;
    }

    @Override
    public int getSkillLevel() {
        return 0;
    }

    @Override
    public Optional<AtomEntityAttributeInstance> getAttribute(AtomAttribute attribute) {
        return Optional.empty();
    }

    @Override
    public void tick() {
        super.tick();
        setCustomName(PaperAdventure.asVanilla(AdapterUtils.getDisplayName(this)));
    }
}
