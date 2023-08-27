package fr.koopa.vanish;


import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

    @Override
    public void onEnable() {

        System.out.println("PixeLand Vanish plugin enabled!");
        getCommand("vanish").setExecutor(new VanishClass());
        Bukkit.getPluginManager().registerEvents(new VanishClass(), this);
    }
}