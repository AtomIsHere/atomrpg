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
import co.aikar.commands.PaperCommandManager;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import io.github.classgraph.ClassGraph;
import io.github.classgraph.ClassInfo;
import io.github.classgraph.ScanResult;

@Singleton
public class CommandRegistrar {
    @Inject
    private Injector injector;
    @Inject
    private PaperCommandManager commandManager;

    public void registerCommands() {
        try(ScanResult scanResult =
                new ClassGraph()
                        .enableAllInfo()
                        .addClassLoader(getClass().getClassLoader())
                        .acceptPackages(getClass().getPackageName())
                        .scan()
        ) {
            for(ClassInfo commandClassInfo : scanResult.getClassesWithAnnotation(CommandClass.class)) {
                if(commandClassInfo.extendsSuperclass(BaseCommand.class)) {
                    registerCommand(commandClassInfo.loadClass(BaseCommand.class));
                }
            }
        }
    }

    private void registerCommand(Class<? extends BaseCommand> commandClass) {
        commandManager.registerCommand(injector.getInstance(commandClass));
    }
}
