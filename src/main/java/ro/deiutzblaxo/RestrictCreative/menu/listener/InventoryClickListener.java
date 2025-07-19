package ro.deiutzblaxo.RestrictCreative.menu.listener;

import de.tr7zw.changeme.nbtapi.NBTContainer;
import de.tr7zw.changeme.nbtapi.NBTItem;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import ro.deiutzblaxo.RestrictCreative.Main;
import ro.deiutzblaxo.RestrictCreative.menu.objects.Menu;
import ro.deiutzblaxo.RestrictCreative.menu.objects.menus.InventoryTag;
import ro.deiutzblaxo.RestrictCreative.nbt.NBTUtil;

public class InventoryClickListener implements Listener {
    private final Main plugin;

    public InventoryClickListener(Main plugin) {
        this.plugin = plugin;
    }

    @EventHandler(ignoreCancelled = true)
    public void onClick(InventoryClickEvent event) {
        if (event.getCurrentItem() == null)
            return;
        if (event.getCurrentItem().getType() == Material.AIR)
            return;

        Inventory inventory = event.getInventory();
        if (inventory.getHolder() instanceof InventoryTag) {
            if (((InventoryTag) inventory.getHolder()).getType().equalsIgnoreCase("menu")) {
                if (!NBTUtil.isKey(event.getCurrentItem(), "menu")) {
                    event.setCancelled(true);
                    return;
                }
                Menu menu = plugin.getMenuManager().getMenu(NBTUtil.getValue(event.getCurrentItem(), "menu"));
                menu.getButton(event.getSlot()).onClick((Player) event.getWhoClicked(), event.getClick());
                event.setCancelled(true);
            }
        }
    }
}
