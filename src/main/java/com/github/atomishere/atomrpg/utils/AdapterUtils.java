package com.github.atomishere.atomrpg.utils;

import com.github.atomishere.atomrpg.entity.adapter.AtomEntityAdapter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;

public final class AdapterUtils {

    public static TextComponent getDisplayName(AtomEntityAdapter<?> adapter) {
        return Component.text()
                .append(Component.text("[Lvl " + adapter.getSkillLevel() + "] ").color(NamedTextColor.GRAY))
                .append(Component.text(adapter.getBaseName()).appendSpace().color(NamedTextColor.WHITE))
                .append(Component.text("[").color(NamedTextColor.GRAY))
                .append(Component.text(Math.round(adapter.get().getHealth())).color(NamedTextColor.RED))
                .append(Component.text("/").color(NamedTextColor.WHITE))
                .append(Component.text(Math.round(adapter.get().getMaxHealth())).color(NamedTextColor.RED))
                .append(Component.text("]").color(NamedTextColor.GRAY))
                .build();
    }

    private AdapterUtils() {
        throw new AssertionError();
    }
}
