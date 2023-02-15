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

package com.github.atomishere.atomrpg.item;

import com.github.atomishere.atomrpg.PluginKeys;
import com.github.atomishere.atomrpg.attributes.AtomAttribute;
import com.github.atomishere.atomrpg.attributes.Operation;
import com.github.atomishere.atomrpg.attributes.item.ItemAttributable;
import com.github.atomishere.atomrpg.attributes.item.ItemModifier;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Multimap;
import com.google.common.util.concurrent.AtomicDouble;
import com.google.gson.Gson;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataAdapterContext;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.JarURLConnection;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;


@Singleton
public class ItemHandler {
    private static final ImmutableMap<Attribute, String> DISPLAYABLE_BUKKIT_ATTRIBUTES = new ImmutableMap.Builder<Attribute, String>()
            .put(Attribute.GENERIC_MAX_HEALTH, "Health")
            .put(Attribute.GENERIC_MOVEMENT_SPEED, "Speed")
            .build();
    private final Map<String, CustomItem> registeredItems = new HashMap<>();

    @Inject
    private Gson gson;
    @Inject
    private CustomItemDataType dataType;

    public void loadItems() {
        JarURLConnection connection;
        JarFile file;

        try {
            connection = (JarURLConnection) getClass().getResource("/items/").openConnection();
            file = connection.getJarFile();
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        Enumeration<JarEntry> entries = file.entries();
        while(entries.hasMoreElements()) {
            JarEntry entry = entries.nextElement();
            if(entry.getName().startsWith("items/") && entry.getName().endsWith(".json") && !entry.isDirectory()) {
                try(InputStream jarStream =
                        file.getInputStream(entry)) {
                    CustomItem item = gson.fromJson(new InputStreamReader(jarStream, StandardCharsets.UTF_8), CustomItem.class);

                    registeredItems.put(item.id(), item);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public Optional<CustomItem> getRegisteredItem(String id) {
        if(!registeredItems.containsKey(id)) {
            return Optional.empty();
        }

        return Optional.of(registeredItems.get(id));
    }

    public Map<String, CustomItem> getRegisteredItems() {
        return registeredItems;
    }

    public boolean hasItem(String id) {
        return registeredItems.containsKey(id);
    }

    public ItemStack createItem(CustomItem item) {
        ItemAttributable itemAttributes = new ItemAttributable();
        Arrays.stream(item.attributes())
                .map(a -> new ItemModifier(
                        "base",
                        a.attribute(),
                        item.slot(),
                        Operation.ADDITION,
                        a.value())
                )
                .forEach(itemAttributes::addModifier);

        ItemStack stack = new ItemStack(item.baseItem());
        ItemMeta meta = stack.getItemMeta();

        meta.displayName(Component.text(item.displayName()).color(item.rarity().getDisplayColor()));
        meta.addItemFlags(
                ItemFlag.HIDE_ITEM_SPECIFICS,
                ItemFlag.HIDE_ATTRIBUTES,
                ItemFlag.HIDE_DESTROYS,
                ItemFlag.HIDE_DYE,
                ItemFlag.HIDE_ENCHANTS,
                ItemFlag.HIDE_PLACED_ON,
                ItemFlag.HIDE_UNBREAKABLE
        );
        meta.setUnbreakable(true);

        if(item.healthBoost() != 0.0D) {
            meta.addAttributeModifier(Attribute.GENERIC_MAX_HEALTH, new AttributeModifier("base", item.healthBoost(), AttributeModifier.Operation.ADD_NUMBER));
        }
        if(item.speedBoost() != 0.0D) {
            meta.addAttributeModifier(Attribute.GENERIC_MOVEMENT_SPEED, new AttributeModifier("base", item.speedBoost(), AttributeModifier.Operation.ADD_NUMBER));
        }

        meta.getPersistentDataContainer().set(PluginKeys.getCustomItemKey(), dataType, item);
        meta.getPersistentDataContainer().set(PluginKeys.getAtomAttributeKey(), ItemAttributable.DATA_TYPE, itemAttributes);

        stack.setItemMeta(meta);

        buildLore(stack);

        return stack;
    }

    public void buildLore(ItemStack stack) {
        ItemMeta meta = stack.getItemMeta();
        PersistentDataContainer dataContainer = meta.getPersistentDataContainer();
        if(!dataContainer.has(PluginKeys.getCustomItemKey(), dataType)) {
            return;
        }
        CustomItem item = dataContainer.get(PluginKeys.getCustomItemKey(), dataType);

        List<Component> loreComponentList = new ArrayList<>();

        if(dataContainer.has(PluginKeys.getAtomAttributeKey(), ItemAttributable.DATA_TYPE)) {
            ItemAttributable itemAttributes = dataContainer.get(PluginKeys.getAtomAttributeKey(), ItemAttributable.DATA_TYPE);

            for(Map.Entry<AtomAttribute, List<ItemModifier>> entry : itemAttributes.getAttributeMap().entrySet()) {
                double baseValue = entry.getValue()
                        .stream()
                        .filter(im -> im.name().equals("base"))
                        .map(ItemModifier::value)
                        .findAny()
                        .orElse(0.0D);
                double total = itemAttributes.getValue(entry.getKey());
                loreComponentList.add(createAttributeComponent(entry.getKey().name(), baseValue, total));
            }
        }

        for(Map.Entry<Attribute, String> entry : DISPLAYABLE_BUKKIT_ATTRIBUTES.entrySet()) {
            Multimap<Attribute, AttributeModifier> attributes = meta.getAttributeModifiers(item.slot());

            if(attributes.containsKey(entry.getKey())) {
                double baseValue = attributes.get(entry.getKey())
                        .stream()
                        .filter(am -> am.getName().equals("base") && am.getOperation().equals(AttributeModifier.Operation.ADD_NUMBER))
                        .findAny()
                        .map(AttributeModifier::getAmount)
                        .orElse(0.0D);
                AtomicDouble total = new AtomicDouble();
                attributes.get(entry.getKey())
                        .stream()
                        .filter(am -> am.getOperation().equals(AttributeModifier.Operation.ADD_NUMBER))
                        .forEach(am -> total.addAndGet(am.getAmount()));

                loreComponentList.add(createAttributeComponent(entry.getValue(), baseValue, total.get()));
            }
        }

        loreComponentList.add(Component.text(""));

        loreComponentList.add(Component.text(item.rarity().getDisplayName()).color(item.rarity().getDisplayColor()).decorate(TextDecoration.BOLD));

        meta.lore(loreComponentList);
        stack.setItemMeta(meta);
    }

    private TextComponent createAttributeComponent(String attributeName, double baseValue, double total) {
        TextComponent attributeComponent = Component.text(attributeName + ": ")
                .color(NamedTextColor.WHITE)
                .append(
                        Component.text(Math.round(total))
                                .color(NamedTextColor.BLUE)
                );
        if(Math.round(total) - Math.round(baseValue) != 0) {
            String sign = total > 0 ? "+" : "-";
            attributeComponent.append(Component.text("(" + sign + Math.round(total - baseValue) + ")").color(NamedTextColor.BLUE));
        }

        return attributeComponent;
    }

    public static class CustomItemDataType implements PersistentDataType<String, CustomItem> {
        @Inject
        private ItemHandler handler;

        @Override
        public @NotNull Class<String> getPrimitiveType() {
            return String.class;
        }

        @Override
        public @NotNull Class<CustomItem> getComplexType() {
            return CustomItem.class;
        }

        @Override
        public @NotNull String toPrimitive(@NotNull CustomItem complex, @NotNull PersistentDataAdapterContext context) {
            return complex.id();
        }

        @Override
        public @NotNull CustomItem fromPrimitive(@NotNull String primitive, @NotNull PersistentDataAdapterContext context) {
            return handler.getRegisteredItem(primitive).orElseThrow(() -> new RuntimeException("Item: " + primitive + " does not exist"));
        }
    }
}
