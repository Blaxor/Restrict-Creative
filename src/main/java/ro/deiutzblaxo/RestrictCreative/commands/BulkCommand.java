package ro.deiutzblaxo.RestrictCreative.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import ro.deiutzblaxo.RestrictCreative.Main;
import ro.deiutzblaxo.RestrictCreative.Hookers.WorldEditHoocker;
import ro.deiutzblaxo.RestrictCreative.config.enums.MessageEnum;

public class BulkCommand implements CommandExecutor {

	Main plugin;

	public BulkCommand(Main main) {
		plugin = main;
	}


	@Override
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
									plugin.getConfigManager().getStringValue(MessageEnum.WORLDEDIT_NOT_FIND)));
						}
					}else
						sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
								plugin.getConfigManager().getStringValue(MessageEnum.NO_PERMISSION)));
				} else if (args[0].equalsIgnoreCase("add")) {
					if (player.hasPermission(
							"restrictcreative.bulk.add")
							|| player.hasPermission("restrictcreative.bulk.*")) {
						if (plugin.getServer().getPluginManager().isPluginEnabled("WorldEdit")) {
							WorldEditHoocker.changeMark(plugin, player, true);
						} else {
							sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
									plugin.getConfigManager().getStringValue(MessageEnum.WORLDEDIT_NOT_FIND)));
						}
					}else
						sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
								plugin.getConfigManager().getStringValue(MessageEnum.NO_PERMISSION)));
				} else {
					sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
							plugin.getConfigManager().getStringValue(MessageEnum.WRONG_ARGUMENTS)));
				}
			} else {
				sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
						plugin.getConfigManager().getStringValue(MessageEnum.WRONG_ARGUMENTS)));
			}

		} else {
			sender.sendMessage("Only players can use this command.");
		}
		return false;
	}

}
