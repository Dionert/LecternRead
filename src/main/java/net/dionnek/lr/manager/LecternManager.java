package net.dionnek.lr.manager;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Lectern;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class LecternManager {

    /**
     * Returns the written book placed on the given lectern block,
     * or {@code null} if the block is not a lectern or holds no written book.
     *
     * @param block the block to retrieve the book from.
     * @return the written book {@link ItemStack}, or {@code null} if absent.
     */
    public ItemStack getBook(Block block) {
        if (block == null || block.getType() != Material.LECTERN) return null;

        Lectern lectern = (Lectern) block.getState();
        ItemStack item  = lectern.getInventory().getItem(0);

        return (item != null && item.getType() == Material.WRITTEN_BOOK) ? item : null;
    }

    /**
     * Returns the lectern block the player is looking at within 5 blocks,
     * or {@code null} if the player is not looking at a lectern.
     *
     * @param player the player whose line of sight to check.
     * @return the targeted lectern {@link Block}, or {@code null} if not found.
     */
    public Block getTargetLectern(Player player) {
        Block block = player.getTargetBlockExact(5);
        return (block != null && block.getType() == Material.LECTERN) ? block : null;
    }
}

