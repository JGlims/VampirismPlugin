package com.jglims.vampirism;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class VampirismPlugin extends JavaPlugin {

    @Override
    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(new VampirismListener(), this);
        getLogger().info("Vampirism plugin enabled! Enchantment registered.");
    }

    @Override
    public void onDisable() {
        getLogger().info("Vampirism plugin disabled.");
    }
}