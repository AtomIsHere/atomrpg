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

import io.papermc.paper.adventure.PaperAdventure;
import net.kyori.adventure.text.Component;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.level.Level;

public class DisplayEntity extends ArmorStand {
    private int timer = 0;

    private int startTime = 0;
    private int timeAlive = 0;

    private boolean started = false;

    public DisplayEntity(EntityType<? extends ArmorStand> type, Level world) {
        super(type, world);
        setMarker(true);
        setNoGravity(true);
        setSmall(true);
        setInvisible(true);
        setCustomNameVisible(true);
        setInvulnerable(true);
        collides = false;

    }

    public static AttributeSupplier.Builder createAttributes() {
        return ArmorStand.createLivingAttributes();
    }

    public void startTimer(int timer) {
        this.startTime = this.timeAlive;
        this.timer = timer;
        started = true;
    }

    public void setDisplayName(Component component) {
        setCustomName(PaperAdventure.asVanilla(component));
    }

    @Override
    public void tick() {
        super.tick();

        if(started && (timeAlive - startTime) >= timer) {
            remove(RemovalReason.DISCARDED);
        }

        timeAlive++;
    }
}
