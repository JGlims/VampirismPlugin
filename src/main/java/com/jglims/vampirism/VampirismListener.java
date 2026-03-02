package com.jglims.vampirism;

import io.papermc.paper.registry.RegistryAccess;
import io.papermc.paper.registry.RegistryKey;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.inventory.AnvilInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;

@SuppressWarnings("UnstableApiUsage")
public class VampirismListener implements Listener {

    private Enchantment getVampirism() {
        return RegistryAccess.registryAccess()
                .getRegistry(RegistryKey.ENCHANTMENT)
                .getOrThrow(VampirismBootstrap.VAMPIRISM_KEY);
    }

    @EventHandler
    public void onEntityDamage(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Player player)) return;
        if (!(event.getEntity() instanceof LivingEntity)) return;
        if (event.isCancelled()) return;

        ItemStack weapon = player.getInventory().getItemInMainHand();
        if (weapon.getType() == Material.AIR) return;

        Enchantment vampirism = getVampirism();
        int level = weapon.getEnchantmentLevel(vampirism);

        if (level <= 0) return;

        double healAmount = level * 1.0;

        double maxHealth = player.getAttribute(org.bukkit.attribute.Attribute.MAX_HEALTH).getValue();
        double newHealth = Math.min(player.getHealth() + healAmount, maxHealth);
        player.setHealth(newHealth);
    }

    @EventHandler
    public void onPrepareAnvil(PrepareAnvilEvent event) {
        AnvilInventory inv = event.getInventory();
        ItemStack slot0 = inv.getItem(0);
        ItemStack slot1 = inv.getItem(1);

        if (slot0 == null || slot1 == null) return;

        ItemStack bookSlot = null;

        // CHANGED: Material.NETHERITE_SCRAP instead of Material.NETHERITE_INGOT
        if (slot0.getType() == Material.ENCHANTED_BOOK && slot1.getType() == Material.NETHERITE_SCRAP) {
            bookSlot = slot0;
        } else if (slot1.getType() == Material.ENCHANTED_BOOK && slot0.getType() == Material.NETHERITE_SCRAP) {
            bookSlot = slot1;
        } else {
            return;
        }

        EnchantmentStorageMeta bookMeta = (EnchantmentStorageMeta) bookSlot.getItemMeta();
        if (bookMeta == null) return;

        int sharpnessLevel = bookMeta.getStoredEnchantLevel(Enchantment.SHARPNESS);
        if (sharpnessLevel <= 0) return;

        int vampirismLevel = Math.min(sharpnessLevel, 4);

        Enchantment vampirism = getVampirism();
        ItemStack result = new ItemStack(Material.ENCHANTED_BOOK);
        EnchantmentStorageMeta resultMeta = (EnchantmentStorageMeta) result.getItemMeta();
        resultMeta.addStoredEnchant(vampirism, vampirismLevel, true);
        resultMeta.displayName(Component.text("Vampirism " + toRoman(vampirismLevel), NamedTextColor.RED));
        result.setItemMeta(resultMeta);

        event.setResult(result);
        inv.setRepairCost(5 + (vampirismLevel * 3));
    }

    private String toRoman(int num) {
        return switch (num) {
            case 1 -> "I";
            case 2 -> "II";
            case 3 -> "III";
            case 4 -> "IV";
            default -> String.valueOf(num);
        };
    }
}
