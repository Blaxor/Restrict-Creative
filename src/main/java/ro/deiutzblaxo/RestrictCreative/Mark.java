package ro.deiutzblaxo.RestrictCreative;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import ro.deiutzblaxo.RestrictCreative.config.enums.GeneralConfigurationEnum;
import ro.deiutzblaxo.RestrictCreative.config.enums.MessageEnum;
import ro.deiutzblaxo.RestrictCreative.nbt.NBTUtil;

import java.util.ArrayList;
import java.util.logging.Level;


public class Mark {
    protected Main plugin;

    public Mark(Main main) {
        plugin = main;
    }

    public void setMark(Location l) {

        plugin.getDataService().createLocation(LocationConvert(l));
        if (plugin.getConfigManager().getBooleanValue(GeneralConfigurationEnum.Debug)) {
            Bukkit.getLogger().log(
                    Level.INFO,
                    "The block from cords " + LocationConvert(l) + " have been MARKED and add to MySQL");
        }

    }

    public boolean isMarked(Location l) {
        if (plugin.getDataService().locationExists(LocationConvert(l))) {
            if (plugin.getConfigManager().getBooleanValue(GeneralConfigurationEnum.Debug)) {

                Bukkit.getLogger().log(Level.INFO, "The block from cords " + LocationConvert(
                        l)
                        + " have been CHECKED in MySQL and its return true");


            }
            return true;
        }
        return false;

    }


    public void removeMark(Location l) {

        if (isMarked(l)) {
            plugin.getDataService().removeLocation(LocationConvert(l));
            if (plugin.getConfigManager().getBooleanValue(GeneralConfigurationEnum.Debug)) {
                Bukkit.getLogger().log(Level.INFO,
                        "The block from cords " + LocationConvert(l) + " have been REMOVED from MySQL");
            }
        }
    }


    public String LocationConvert(Location l) {
        String location = l.getBlockX() + " " + l.getBlockY() + " " + l.getBlockZ() + " " + l.getWorld().getName();
        return location;
    }

    public ItemStack setCreativeItem(Player player, ItemStack item) {
        String BypassRenamePermission = "restrictcreative.bypass.rename";
        if (!player.hasPermission(BypassRenamePermission)) {
            if (item != null && item.getType() != Material.AIR) {

                if (!isCreativeItem(item)) {
                    String playerName = player.getName();
                    NBTUtil.setCreativeItem(item, playerName);
                    ItemMeta meta = item.getItemMeta();
                    ArrayList<String> lore = new ArrayList<String>();
                    if (meta.hasLore())
                        lore = (ArrayList<String>) meta.getLore();
                    plugin.getConfigManager().LoadConfigs();
                    lore.add(0, ChatColor.translateAlternateColorCodes('&',
                            plugin.getConfigManager().getStringValue(MessageEnum.LORE_MESSAGE).replaceAll("%name%",
                                    playerName)));
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
            if (meta != null)
                if (meta.hasLore()) {
                    for (String s : meta.getLore()) {
                        plugin.getConfigManager().LoadConfigs();
                        if (s.startsWith(ChatColor.translateAlternateColorCodes('&',
                                plugin.getConfigManager().getStringValue(MessageEnum.LORE_MESSAGE).replace("%name%", ""))))
                            return NBTUtil.isCreativeItem(item);
                    }
                }
        }
        return false;
    }


}
