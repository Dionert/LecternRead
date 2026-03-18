package net.dionnek.lr.listener;

import net.dionnek.lr.LecternRead;
import net.dionnek.lr.manager.BookReader;
import net.dionnek.lr.utils.Messages;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class LecternListener implements Listener {

    private final LecternRead plugin;
    private final BookReader bookReader;

    /**
     * Creates a new LecternListener backed by the given plugin instance.
     *
     * @param plugin the plugin instance used for manager and config access.
     */
    public LecternListener(LecternRead plugin) {
        this.plugin     = plugin;
        this.bookReader = new BookReader(plugin.getPluginConfig());
    }

    /**
     * Listens for left-clicks on lecterns and reads the full book to the player in chat.
     * Only fires when the {@code click-to-read} option is enabled in config.yml
     * and the player has the {@code lecternbook.read} permission.
     *
     * @param event the player interact event.
     */
    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onLecternLeftClick(PlayerInteractEvent event) {
        if (event.getAction() != Action.LEFT_CLICK_BLOCK) return;

        Block block = event.getClickedBlock();
        if (block == null) return;

        Block lectern = plugin.getLecternManager().getTargetLectern(event.getPlayer());
        if (!block.equals(lectern)) return;

        if (!plugin.getPluginConfig().isClickToReadEnabled()) return;

        Player player = event.getPlayer();
        if (!player.hasPermission("lecternread.read")) return;

        ItemStack book = plugin.getLecternManager().getBook(block);
        if (book == null) {
            if (plugin.getPluginConfig().shouldNotifyEmptyLectern()) {
                Messages.send(player, plugin.getPluginConfig().getMessage("no-book", "&e[LR] &fThere is no book on this lectern."));
            }
            return;
        }

        bookReader.readAll(player, book);
    }
}