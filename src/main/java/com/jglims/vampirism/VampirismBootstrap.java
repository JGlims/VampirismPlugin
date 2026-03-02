package com.jglims.vampirism;

import io.papermc.paper.plugin.bootstrap.BootstrapContext;
import io.papermc.paper.plugin.bootstrap.PluginBootstrap;
import io.papermc.paper.registry.RegistryKey;
import io.papermc.paper.registry.TypedKey;
import io.papermc.paper.registry.data.EnchantmentRegistryEntry;
import io.papermc.paper.registry.event.RegistryEvents;
import io.papermc.paper.registry.keys.tags.ItemTypeTagKeys;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.inventory.EquipmentSlotGroup;

@SuppressWarnings("UnstableApiUsage")
public class VampirismBootstrap implements PluginBootstrap {

    public static final TypedKey<org.bukkit.enchantments.Enchantment> VAMPIRISM_KEY =
            TypedKey.create(RegistryKey.ENCHANTMENT, Key.key("jglims", "vampirism"));

    @Override
    public void bootstrap(BootstrapContext context) {
        context.getLifecycleManager().registerEventHandler(
            RegistryEvents.ENCHANTMENT.compose().newHandler(event -> {
                event.registry().register(
                    VAMPIRISM_KEY,
                    b -> b.description(Component.text("Vampirism", NamedTextColor.RED))
                        .supportedItems(event.getOrCreateTag(ItemTypeTagKeys.SWORDS))
                        .anvilCost(4)
                        .maxLevel(4)
                        .weight(2)
                        .minimumCost(EnchantmentRegistryEntry.EnchantmentCost.of(15, 10))
                        .maximumCost(EnchantmentRegistryEntry.EnchantmentCost.of(65, 10))
                        .activeSlots(EquipmentSlotGroup.MAINHAND)
                );
            })
        );
    }
}