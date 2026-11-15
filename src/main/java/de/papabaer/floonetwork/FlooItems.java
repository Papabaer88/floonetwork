package de.papabaer.floonetwork;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Locale;

public final class FlooItems {
    private FlooItems() {
    }

    public static ItemStack createFlooPowder(int amount) {
        ItemStack item = new ItemStack(Material.GLOWSTONE_DUST, amount);
        ItemMeta meta = item.getItemMeta();
        if (meta == null) {
            return item;
        }
        meta.setDisplayName("§aFloo Powder");
        meta.setLocalizedName("floo_powder");
        meta.setLore(java.util.List.of(
                "§7Use this on a fire or campfire",
                "§7to connect or travel through the Floo Network."
        ));
        item.setItemMeta(meta);
        return item;
    }

    public static boolean isFlooPowder(ItemStack item) {
        if (item == null || item.getType() != Material.GLOWSTONE_DUST) {
            return false;
        }
        ItemMeta meta = item.getItemMeta();
        if (meta == null) {
            return false;
        }
        String display = meta.getDisplayName();
        String localized = meta.getLocalizedName();
        return (display != null && display.toLowerCase(Locale.ROOT).contains("floo powder"))
                || "floo_powder".equals(localized);
    }
}
