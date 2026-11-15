package de.papabaer.floonetwork;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import java.util.List;
import java.util.Locale;

public final class FlooListener implements Listener {
    private final FlooNetworkPlugin plugin;

    public FlooListener(FlooNetworkPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.getHand() != EquipmentSlot.HAND) {
            return;
        }
        ItemStack item = event.getItem();
        if (item == null || !FlooItems.isFlooPowder(item)) {
            return;
        }

        Block clicked = event.getClickedBlock();
        if (clicked == null) {
            return;
        }

        Material material = clicked.getType();
        boolean isFlooBlock = material == Material.FIRE
                || material == Material.SOUL_FIRE
                || material == Material.CAMPFIRE
                || material == Material.SOUL_CAMPFIRE;

        if (!isFlooBlock) {
            return;
        }

        Player player = event.getPlayer();
        event.setCancelled(true);

        if (player.isSneaking()) {
            String suggestedName = "fireplace_" + (plugin.getManager().size() + 1);
            plugin.getManager().addFireplace(suggestedName, clicked.getLocation().clone().add(0.5, 0.0, 0.5));
            player.sendMessage("§aCreated a new Floo fireplace named §f" + suggestedName + "§a at your selected fire.");
            return;
        }

        List<String> names = plugin.getManager().getNames();
        if (names.isEmpty()) {
            player.sendMessage("§cThere are no registered fireplaces yet. Sneak + right-click a fire to create one.");
            return;
        }

        String destinationName = names.get((int) (Math.random() * names.size()));
        plugin.getManager().teleportTo(player, destinationName);
        if (item.getAmount() > 1) {
            item.setAmount(item.getAmount() - 1);
        } else {
            player.getInventory().setItemInMainHand(null);
        }
    }
}
