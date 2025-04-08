package ro.deiutzblaxo.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import ro.deiutzblaxo.RestrictCreative.Main;
import ro.deiutzblaxo.RestrictCreative.Hookers.WorldEditHoocker;

public class BulkCommand implements CommandExecutor {

	Main plugin;

	public BulkCommand(Main main) {
		plugin = main;
	}


	@Override // TODO NEW
	public boolean onCommand(CommandSender sender, Command arg1, String arg2, String[] args) {
		if (sender instanceof Player) {


			Player player = (Player) sender;
			if (args.length == 1) {
				if (args[0].equalsIgnoreCase("remove")) {
					if (player.hasPermission("restrictcreative.bulk.remove")
							|| player.hasPermission("restrictcreative.bulk.*")) {
						if (plugin.getServer().getPluginManager().isPluginEnabled("WorldEdit")) {
							WorldEditHoocker.changeMark(plugin, player, false);
						} else {
							sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
									plugin.getConfigManager().getMessage().getString("WorldEdit_NotFind")));
						}
					}else
						sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
								plugin.getConfigManager().getMessage().getString("noPermission")));
				} else if (args[0].equalsIgnoreCase("add")) {
					if (player.hasPermission(
							"restrictcreative.bulk.add")
							|| player.hasPermission("restrictcreative.bulk.*")) {
						if (plugin.getServer().getPluginManager().isPluginEnabled("WorldEdit")) {
							WorldEditHoocker.changeMark(plugin, player, true);
						} else {
							sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
									plugin.getConfigManager().getMessage().getString("WorldEdit_NotFind")));
						}
					}else
						sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
								plugin.getConfigManager().getMessage().getString("noPermission")));
				} else {
					sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
							plugin.getConfigManager().getMessage().getString("Wrong_Arguments")));
				}
			} else {
				sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
						plugin.getConfigManager().getMessage().getString("Wrong_Arguments")));
			}

		} else {
			sender.sendMessage("Only players can use this command.");
		}
		return false;
	}

}
