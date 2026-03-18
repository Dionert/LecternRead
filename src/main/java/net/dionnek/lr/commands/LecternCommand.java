package net.dionnek.lr.commands;

import net.dionnek.lr.LecternRead;
import net.dionnek.lr.manager.BookReader;
import net.dionnek.lr.utils.Messages;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;

public class LecternCommand implements CommandExecutor {
    private final LecternRead plugin;
    private final BookReader bookReader;


    public LecternCommand(LecternRead plugin) {
        this.plugin     = plugin;
        this.bookReader = new BookReader(plugin.getPluginConfig());
    }

    /**
     * Handles the {@code /lr} command and its sub-commands:
     * {@code help}, {@code read [page]}, {@code info} and {@code reload}.
     */
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Only players can use this command.");
            return true;
        }

        Player player = (Player) sender;

        if (args.length == 0 || args[0].equalsIgnoreCase("help")) {
            sendHelp(player);
            return true;
        }

        switch (args[0].toLowerCase()) {
            case "reload" -> handleReload(player);
            case "read"   -> handleRead(player, args);
            case "info"   -> handleInfo(player);
            default       -> sendHelp(player);
        }

        return true;
    }

    /**
     * Handles {@code /lr reload}. Reloads config.yml from disk.
     * Requires the {@code lecternread.reload} permission.
     *
     * @param player the player who issued the command.
     */
    private void handleReload(Player player) {
        if (!player.hasPermission("lecternread.reload")) {
            Messages.send(player, plugin.getPluginConfig().getMessage("no-permission", "&e[LR] &cYou don't have permission to do this."));
            return;
        }

        plugin.getPluginConfig().reload();
        Messages.send(player, plugin.getPluginConfig().getMessage("reloaded", "&e[LR] &aConfiguration reloaded."));
    }

    /**
     * Handles {@code /lr read} and {@code /lr read <page>}.
     * Without a page number, all pages are sent to chat.
     * Requires the {@code lecternread.read} permission.
     */
    private void handleRead(Player player, String[] args) {
        if (!player.hasPermission("lecternread.read")) {
            Messages.send(player, plugin.getPluginConfig().getMessage("no-permission", "&e[LR] &cYou don't have permission to do this."));
            return;
        }

        ItemStack book = getBookFromLecternInSight(player);
        if (book == null) return;

        if (args.length == 1) {
            bookReader.readAll(player, book);
            return;
        }

        try {
            int page = Integer.parseInt(args[1]);
            bookReader.readPage(player, book, page);
        } catch (NumberFormatException e) {
            String msg = plugin.getPluginConfig().getMessage("invalid-number", "&e[LR] &f'{input}' is not a valid page number.")
                    .replace("{input}", args[1]);
            Messages.send(player, msg);
        }
    }

    /**
     * Handles {@code /lr info}. Shows the book's title, author and page count.
     * Requires the {@code lecternbook.read} permission.
     *
     * @param player the player who issued the command.
     */
    private void handleInfo(Player player) {
        if (!player.hasPermission("lecternread.read")) {
            Messages.send(player, plugin.getPluginConfig().getMessage("no-permission", "&e[LR] &cYou don't have permission to do this."));
            return;
        }

        ItemStack book = getBookFromLecternInSight(player);
        if (book == null) return;

        BookMeta meta   = (BookMeta) book.getItemMeta();
        String   title  = (meta != null && meta.hasTitle())  ? meta.getTitle()  : "Unknown";
        String   author = (meta != null && meta.hasAuthor()) ? meta.getAuthor() : "Unknown";
        int      pages  = (meta != null) ? meta.getPageCount() : 0;

        Messages.send(player, "&e[LR] &7Title: &f"  + title);
        Messages.send(player, "&e[LR] &7Author: &f" + author);
        Messages.send(player, "&e[LR] &7Pages: &f"  + pages);
    }

    /**
     * @param player the player whose line of sight to check.
     * @return the written book {@link ItemStack}, or {@code null} on failure.
     */
    private ItemStack getBookFromLecternInSight(Player player) {
        Block block = plugin.getLecternManager().getTargetLectern(player);
        if (block == null) {
            Messages.send(player, plugin.getPluginConfig().getMessage("not-looking-at-lectern", "&e[LR] &fYou are not looking at a lectern."));
            return null;
        }

        ItemStack book = plugin.getLecternManager().getBook(block);
        if (book == null) {
            Messages.send(player, plugin.getPluginConfig().getMessage("no-book", "&e[LR] &fThere is no book on this lectern."));
            return null;
        }

        return book;
    }

    /**
     * Sends the help overview to the player listing all available sub-commands.
     *
     * @param player the player to send the help message to.
     */
    private void sendHelp(Player player) {
        Messages.send(player, "&e[LR] &7Commands:");
        Messages.send(player, "&e  /lr read &f— Read all pages of the book on the lectern you are looking at.");
        Messages.send(player, "&e  /lr read <page> &f— Read a specific page.");
        Messages.send(player, "&e  /lr info &f— Show the title, author and page count.");
        Messages.send(player, "&e  /lr reload &f— Reload the configuration.");
    }
}

