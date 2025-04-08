package ro.deiutzblaxo.RestrictCreative;

import java.util.ArrayList;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;



public class Mark {
	protected Main plugin;

	public Mark(Main main) {
		plugin = main;
	}
	public static ArrayList<String> P = new ArrayList<String>();



	public void setMark(Location l) {

		plugin.getDatabase().getMySQLGetSet().createLocation(LocationConvert(l));
		if (plugin.getConfigManager().getConfig().getBoolean("Debug")) {
			Bukkit.getLogger().log(
					Level.INFO,
					"The block from cords " + LocationConvert(l) + " have been MARKED and add to MySQL");
		}

	}

	public boolean isMarked(Location l) {


		if (plugin.getDatabase().getMySQLGetSet().locationExists(LocationConvert(l))) {
			if (plugin.getConfigManager().getConfig().getBoolean("Debug")) {

				Bukkit.getLogger().log(Level.INFO, "The block from cords " + LocationConvert(
						l)
				+ " have been CHECKED in MySQL and its return true");



			}
			return true;
		} else if (P.contains(l.getBlockX() + " " + l.getBlockY() + " " + l.getBlockZ())) {

			if (plugin.getConfigManager().getConfig().getBoolean("Debug")) {

				Bukkit.getLogger().log(Level.INFO, "The block from cords " + LocationConvert(l)
				+ " have been CHECKED in YAML and its return true");

			}
			return true;

		}else{
			if (plugin.getConfigManager().getConfig().getBoolean("Debug")) {
				Bukkit.getLogger().log(Level.INFO, "The block from cords " + LocationConvert(
						l)
				+ " have been CHECKED in YAML or MySQL and its return false");
			}
			return false;
		}

	}


	public void removeMark(Location l) {

		if (isMarked(l)) {
			plugin.getDatabase().getMySQLGetSet().removeLocation(LocationConvert(l));
			P.remove(l.getBlockX() + " " + l.getBlockY() + " " + l.getBlockZ());
			if (plugin.getConfigManager().getConfig().getBoolean("Debug")) {
				Bukkit.getLogger().log(Level.INFO,
						"The block from cords " + LocationConvert(l) + " have been REMOVED from MySQL");
			}
		}
	}



	public String LocationConvert(Location l) {

		String location = l.getBlockX() + " " + l.getBlockY() + " " + l.getBlockZ() + " " + l.getWorld().getName();
		return location;
	}

	public ItemStack setCreativeItem(String player, ItemStack item) {
		String BypassRenamePermission = "restrictcreative.bypass.rename";
		if (!plugin.getServer().getPlayer(player).hasPermission(BypassRenamePermission)) {
			if (item != null && item.getType() != Material.AIR) {

				if (!isCreativeItem(item)) {
					ItemMeta meta = item.getItemMeta();
					ArrayList<String> lore = new ArrayList<String>();
					if (meta.hasLore())
						lore = (ArrayList<String>) meta.getLore();
					plugin.getConfigManager().LoadConfigs();
					lore.add(0, ChatColor.translateAlternateColorCodes('&',
							plugin.getConfigManager().getMessage().getString("LoreMessage").replaceAll("%name%",
									player)));
					meta.setLore(lore);
					item.setItemMeta(meta);
				}

			}

		}
		return item;
	}

	// verifica daca itemul a fost creat in creative
	public boolean isCreativeItem(ItemStack item) {
		if (item != null && item.hasItemMeta()) {
			ItemMeta meta = item.getItemMeta();
			if (meta.hasLore()) {
				for (String s : meta.getLore()) {
					plugin.getConfigManager().LoadConfigs();
					if (s.startsWith(ChatColor.translateAlternateColorCodes('&',
							plugin.getConfigManager().getMessage().getString("LoreMessage").replace("%name%", ""))))
						return true;
				}
			}
		}
		return false;
	}


}
