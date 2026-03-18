package net.dionnek.lr.utils;

import net.dionnek.lr.LecternRead;

public class LecternConfig {

    private final LecternRead plugin;

    public LecternConfig(LecternRead plugin) {
        this.plugin = plugin;
    }

    /**
     * Reloads the config.yml
     */
    public void reload() {
        plugin.reloadConfig();
    }

    /**
     * Returns whether left-clicking a lectern should read the book to the player in chat.
     */
    public boolean isClickToReadEnabled() {
        return plugin.getConfig().getBoolean("click-to-read", true);
    }

    /**
     * Returns whether an empty lectern should send a "no book" message on click.
     */
    public boolean shouldNotifyEmptyLectern() {
        return plugin.getConfig().getBoolean("notify-empty-lectern", false);
    }

    /**
     * Returns a message from the messages section in config.yml.
     * Falls back to the provided default if the key is absent.
     */
    public String getMessage(String key, String defaultValue) {
        return plugin.getConfig().getString("messages." + key, defaultValue);
    }
}
