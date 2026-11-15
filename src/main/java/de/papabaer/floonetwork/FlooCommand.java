package de.papabaer.floonetwork;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public final class FlooCommand implements CommandExecutor, TabCompleter {
    private final FlooNetworkPlugin plugin;

    public FlooCommand(FlooNetworkPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("§cThis command can only be used by a player.");
            return true;
        }

        if (args.length == 0) {
            sendHelp(player);
            return true;
        }

        String subCommand = args[0].toLowerCase(Locale.ROOT);

        switch (subCommand) {
            case "create" -> {
                if (args.length < 2) {
                    player.sendMessage("§cUsage: /floo create <name>");
                    return true;
                }
                String name = args[1];
                Location target = player.getTargetBlockExact(6) == null ? player.getLocation() : player.getTargetBlockExact(6).getLocation().clone().add(0.5, 0.0, 0.5);
                BlockType type = BlockType.from(target.getBlock().getType());
                if (!type.isPortal()) {
                    player.sendMessage("§cYou need to look at a fire, campfire, or soul campfire to create a Floo fireplace.");
                    return true;
                }
                plugin.getManager().addFireplace(name, target);
                player.sendMessage("§aFloo fireplace §f" + name + " §acreated.");
            }
            case "remove" -> {
                if (args.length < 2) {
                    player.sendMessage("§cUsage: /floo remove <name>");
                    return true;
                }
                if (plugin.getManager().removeFireplace(args[1])) {
                    player.sendMessage("§aRemoved Floo fireplace §f" + args[1] + "§a.");
                } else {
                    player.sendMessage("§cNo fireplace named §f" + args[1] + " §cwas found.");
                }
            }
            case "tp" -> {
                if (args.length < 2) {
                    player.sendMessage("§cUsage: /floo tp <name>");
                    return true;
                }
                plugin.getManager().teleportTo(player, args[1]);
            }
            case "list" -> {
                List<String> names = plugin.getManager().getNames();
                if (names.isEmpty()) {
                    player.sendMessage("§cNo fireplaces have been registered yet.");
                    return true;
                }
                player.sendMessage("§6Registered Floo fireplaces: §f" + String.join(", ", names));
            }
            case "powder" -> {
                player.getInventory().addItem(FlooItems.createFlooPowder(1));
                player.sendMessage("§aYou received 1 Floo Powder.");
            }
            case "help" -> sendHelp(player);
            default -> sendHelp(player);
        }
        return true;
    }

    private void sendHelp(Player player) {
        player.sendMessage("§6Floo Network commands:");
        player.sendMessage("§7/floo create <name> - create a fireplace at the fire you are targeting");
        player.sendMessage("§7/floo remove <name> - remove a fireplace");
        player.sendMessage("§7/floo tp <name> - teleport to an existing fireplace");
        player.sendMessage("§7/floo list - list all registered fireplaces");
        player.sendMessage("§7/floo powder - give yourself Floo Powder");
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        if (args.length <= 1) {
            return Arrays.asList("create", "remove", "tp", "list", "powder", "help");
        }
        if (args[0].equalsIgnoreCase("tp") || args[0].equalsIgnoreCase("remove")) {
            return new ArrayList<>(plugin.getManager().getNames());
        }
        return Collections.emptyList();
    }

    private enum BlockType {
        FIRE,
        CAMPFIRE,
        SOUL_FIRE,
        NONE;

        static BlockType from(org.bukkit.Material material) {
            if (material == org.bukkit.Material.FIRE || material == org.bukkit.Material.SOUL_FIRE) {
                return FIRE;
            }
            if (material == org.bukkit.Material.CAMPFIRE || material == org.bukkit.Material.SOUL_CAMPFIRE) {
                return CAMPFIRE;
            }
            return NONE;
        }

        boolean isPortal() {
            return this != NONE;
        }
    }
}
