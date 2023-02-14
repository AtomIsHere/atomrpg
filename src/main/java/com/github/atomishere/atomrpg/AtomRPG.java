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

package com.github.atomishere.atomrpg;

import co.aikar.commands.CommandCompletions;
import co.aikar.commands.CommandContexts;
import co.aikar.commands.InvalidCommandArgument;
import co.aikar.commands.PaperCommandManager;
import com.github.atomishere.atomrpg.attributes.player.PlayerAttributeListener;
import com.github.atomishere.atomrpg.attributes.player.PlayerAttributeManager;
import com.github.atomishere.atomrpg.commands.CommandRegistrar;
import com.github.atomishere.atomrpg.item.CustomItem;
import com.github.atomishere.atomrpg.item.ItemHandler;
import com.google.inject.Inject;
import com.google.inject.Injector;
import io.github.classgraph.ClassGraph;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class AtomRPG extends JavaPlugin {
    private Injector injector;

    @Inject
    private CommandRegistrar commandRegistrar;

    @Inject
    private ItemHandler itemHandler;
    @Inject
    private PlayerAttributeManager attributeManager;

    @Inject
    private PlayerAttributeListener attributeListener;

    @Override
    public void onLoad() {
        ClassGraph.CIRCUMVENT_ENCAPSULATION = ClassGraph.CircumventEncapsulationMethod.NARCISSUS;

        PluginKeys.init(this);

        if(!getDataFolder().exists()) {
            getDataFolder().mkdir();
        }
    }

    @Override
    public void onEnable() {
        PaperCommandManager commandManager = new PaperCommandManager(this);
        commandManager.enableUnstableAPI("brigadier");
        commandManager.enableUnstableAPI("help");

        registerCommandCompletions(commandManager.getCommandCompletions());
        registerCommandContexts(commandManager.getCommandContexts());

        AtomRPGModule module = new AtomRPGModule(this, commandManager);
        injector = module.createInjector();
        injector.injectMembers(this);

        itemHandler.loadItems();
        attributeManager.enable();

        commandRegistrar.registerCommands();

        Bukkit.getServer().getPluginManager().registerEvents(attributeListener, this);
    }

    @Override
    public void onDisable() {
        attributeManager.disable();

        injector = null;
    }

    private void registerCommandCompletions(CommandCompletions<?> completions) {
        completions.registerCompletion("customitem", c -> itemHandler
                .getRegisteredItems()
                .keySet());
    }

    private void registerCommandContexts(CommandContexts<?> contexts) {
        contexts.registerContext(CustomItem.class, c -> {
            String id = c.popFirstArg();
            return itemHandler.getRegisteredItem(id)
                    .orElseThrow(() -> new InvalidCommandArgument("Item not found: " + id));
        });
    }
}
