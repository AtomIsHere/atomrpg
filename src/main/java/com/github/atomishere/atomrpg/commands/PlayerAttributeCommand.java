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
import co.aikar.commands.annotation.Description;
import co.aikar.commands.annotation.Subcommand;
import com.github.atomishere.atomrpg.attributes.AtomAttribute;
import com.github.atomishere.atomrpg.attributes.player.PlayerAttributable;
import com.github.atomishere.atomrpg.attributes.player.PlayerAttributeManager;
import com.google.inject.Inject;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.Optional;


@CommandAlias("atomattribute|atomattr")
@Description("Gather info and modify (TODO) player attributes")
@CommandClass
public class PlayerAttributeCommand extends BaseCommand {
    @Inject
    private PlayerAttributeManager attributeManager;

    @Subcommand("info")
    @Description("Get attribute info for the player that's executing the command")
    public void info(Player player) {
        player.sendMessage("Attribute Info: ");
        PlayerAttributable attributable = attributeManager.getPlayerAttributes(player);

        Arrays.stream(AtomAttribute.values())
                .map(attributable::getInstance)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .forEach(a ->
                        player.sendMessage(
                                a.getAttribute().getDisplayColor() +
                                a.getAttribute().getDisplayName() +
                                ": " +
                                a.getValueWithAllModifiers()
                        )
                );
    }
}
