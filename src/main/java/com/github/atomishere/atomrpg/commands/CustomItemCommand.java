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
import com.github.atomishere.atomrpg.item.CustomItem;
import com.github.atomishere.atomrpg.item.ItemHandler;
import com.google.inject.Inject;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

@CommandAlias("customitem|atomitem")
@Description("Manage custom commands for AtomRPG")
@CommandClass
public class CustomItemCommand extends BaseCommand {
    @Inject
    private ItemHandler itemHandler;

    @Subcommand("give")
    @Description("Get a custom item")
    @CommandCompletion("@customitem")
    public void give(Player player, CustomItem item) {
        ItemStack stack = itemHandler.createItem(item);

        player.getInventory().addItem(stack);
        player.sendMessage(
                Component.text()
                .append(
                        Component.text(item.displayName())
                        .color(item.rarity().getDisplayColor())
                        .decorate(TextDecoration.BOLD)
                )
                .append(Component.text(" has been given")
                        .color(NamedTextColor.GREEN)
                )
        );
    }
}
