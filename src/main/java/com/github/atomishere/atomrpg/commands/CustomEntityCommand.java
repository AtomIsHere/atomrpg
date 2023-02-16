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

package com.github.atomishere.atomrpg.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandCompletion;
import co.aikar.commands.annotation.Description;
import co.aikar.commands.annotation.Subcommand;
import com.github.atomishere.atomrpg.entity.CustomEntityType;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.entity.Player;

@CommandAlias("customentity|atomentity")
@Description("Handle and spawn custom entities")
@CommandClass
public class CustomEntityCommand extends BaseCommand {
    @Subcommand("spawn")
    @Description("spawn an entity")
    @CommandCompletion("@atomentity")
    public void spawn(Player player, CustomEntityType<?> customEntity) {
        customEntity.spawn(player.getLocation()).ifPresentOrElse(
                e -> player.sendMessage(Component.text("Entity spawned").color(NamedTextColor.GREEN)),
                () -> player.sendMessage(Component.text("Could not spawn entity").color(NamedTextColor.RED))
        );
    }

}
