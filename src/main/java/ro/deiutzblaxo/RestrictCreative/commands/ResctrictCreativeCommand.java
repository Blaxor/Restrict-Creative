package ro.deiutzblaxo.RestrictCreative.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import ro.deiutzblaxo.RestrictCreative.Main;
import ro.deiutzblaxo.RestrictCreative.menu.MenuManager;

public class ResctrictCreativeCommand implements CommandExecutor {
    protected Main plugin;


    public ResctrictCreativeCommand(Main main) {
        plugin = main;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command arg1, String arg2, String[] arg3) {
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
                "&7[&aRestrictCreative&7]&r Plugin by JDeiutz, Version: " + plugin.getDescription().getVersion()));
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7[&aRestrictCreative&7]&r Link to spigot: https://www.spigotmc.org/resources/66007/ "));


        if (sender instanceof Player && (sender.hasPermission("restrictcreative.admin") || sender.isOp())) {

            plugin.getMenuManager().openMenu(plugin.getMenuManager().getAdminMenu(), ((Player) sender).getPlayer());

        }

        return false;
    }

}
