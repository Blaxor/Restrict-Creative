package ro.deiutzblaxo.RestrictCreative.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import ro.deiutzblaxo.RestrictCreative.Main;

public class ResctrictCreativeCommand implements CommandExecutor {
	protected Main plugin;

	public ResctrictCreativeCommand(Main main) {
		plugin = main;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command arg1, String arg2, String[] arg3) {
		sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
				"&7[&aRestrictCreative&7]&r Plugin by Deiutz, Version: " + plugin.getDescription().getVersion()));
		sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7[&aRestrictCreative&7]&r Link to spigot: https://www.spigotmc.org/resources/66007/ "));
		return false;
	}

}
