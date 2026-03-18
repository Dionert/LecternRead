package net.dionnek.lr;

import net.dionnek.lr.commands.LecternCommand;
import net.dionnek.lr.listener.LecternListener;
import net.dionnek.lr.manager.LecternManager;
import net.dionnek.lr.utils.LecternConfig;
import org.bukkit.plugin.java.JavaPlugin;

public final class LecternRead extends JavaPlugin {

    private LecternManager lecternManager;
    private LecternConfig lecternConfig;

    @Override
    public void onEnable() {
        saveDefaultConfig();

        lecternConfig   = new LecternConfig(this);
        lecternManager = new LecternManager();

        getCommand("lr").setExecutor(new LecternCommand(this));
        getServer().getPluginManager().registerEvents(new LecternListener(this), this);

        System.out.println("LecternRead enabled.");
    }


    @Override
    public void onDisable() {
        System.out.println("LecternRead disabled.");
    }


    public LecternConfig getPluginConfig() {
        return lecternConfig;
    }


    public LecternManager getLecternManager() {
        return lecternManager;
    }
}
