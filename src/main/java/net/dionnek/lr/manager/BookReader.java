package net.dionnek.lr.manager;

import net.dionnek.lr.utils.LecternConfig;
import net.dionnek.lr.utils.Messages;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;

import java.util.List;

public class BookReader {
    private final LecternConfig config;

    public BookReader(LecternConfig config) {
        this.config = config;
    }

    /**
     * Sends all pages of the given book to the player in chat,
     * preceded by a header showing the title, author and total page count.
     *
     * @param player the player to send the book content to.
     * @param book   the written book to read.
     */
    public void readAll(Player player, ItemStack book) {
        BookMeta meta = (BookMeta) book.getItemMeta();
        if (meta == null || meta.getPageCount() == 0) {
            Messages.send(player, config.getMessage("no-pages", "&e[LR] &fThis book has no pages."));
            return;
        }

        String title  = meta.hasTitle()  ? meta.getTitle()  : "Unknown";
        String author = meta.hasAuthor() ? meta.getAuthor() : "Unknown";
        int    total  = meta.getPageCount();

        String header = config.getMessage("book-header", "&e[LR] &7Reading &f\"{title}\" &7by &f{author} &7({total} pages)")
                .replace("{title}",  title)
                .replace("{author}", author)
                .replace("{total}",  String.valueOf(total));

        Messages.send(player, header);

        List<String> pages = meta.getPages();
        for (int i = 0; i < pages.size(); i++) {
            sendPageDivider(player, i + 1, total);
            sendPageLines(player, pages.get(i));
        }
    }

    /**
     * Sends a single page of the given book to the player in chat.
     * Sends an error message if the page number is out of range.
     *
     * @param player     the player to send the page content to.
     * @param book       the written book to read from.
     * @param pageNumber the 1-based page number to display.
     */
    public void readPage(Player player, ItemStack book, int pageNumber) {
        BookMeta meta = (BookMeta) book.getItemMeta();
        if (meta == null || meta.getPageCount() == 0) {
            Messages.send(player, config.getMessage("no-pages", "&e[LR] &fThis book has no pages."));
            return;
        }

        int total = meta.getPageCount();

        if (pageNumber < 1 || pageNumber > total) {
            String msg = config.getMessage("invalid-page", "&e[LR] &fPage {page} does not exist. This book has {total} page(s).")
                    .replace("{page}",  String.valueOf(pageNumber))
                    .replace("{total}", String.valueOf(total));
            Messages.send(player, msg);
            return;
        }

        String title  = meta.hasTitle() ? meta.getTitle() : "Unknown";
        String header = config.getMessage("page-header", "&e[LR] &7\"{title}\" &f— Page {page}/{total}")
                .replace("{title}", title)
                .replace("{page}",  String.valueOf(pageNumber))
                .replace("{total}", String.valueOf(total));

        Messages.send(player, header);
        sendPageLines(player, meta.getPage(pageNumber));
    }

    /**
     * Sends a page divider line to the player, showing the current and total page numbers.
     *
     * @param player      the player to send the divider to.
     * @param pageNumber  the current 1-based page number.
     * @param totalPages  the total number of pages in the book.
     */
    private void sendPageDivider(Player player, int pageNumber, int totalPages) {
        String divider = config.getMessage("page-divider", "&8--- &ePage {page}/{total} &8---")
                .replace("{page}",  String.valueOf(pageNumber))
                .replace("{total}", String.valueOf(totalPages));

        Messages.send(player, divider);
    }

    /**
     * Sends each line of a page's content to the player in chat.
     *
     * @param player  the player to send the lines to.
     * @param content the raw page content, with {@code \n} as line separator.
     */
    private void sendPageLines(Player player, String content) {
        for (String line : content.split("\n")) {
            Messages.send(player, "&f" + line);
        }
    }
}

