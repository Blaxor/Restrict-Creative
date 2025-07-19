package ro.deiutzblaxo.RestrictCreative.menu.objects;

import de.rapha149.signgui.SignGUI;
import de.rapha149.signgui.exception.SignGUIVersionException;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import ro.deiutzblaxo.RestrictCreative.Main;
import ro.deiutzblaxo.RestrictCreative.config.enums.GeneralConfigurationEnum;
import ro.deiutzblaxo.RestrictCreative.menu.objects.buttons.Action;
import ro.deiutzblaxo.RestrictCreative.menu.objects.buttons.ButtonObject;
import ro.deiutzblaxo.RestrictCreative.nbt.NBTUtil;

import java.util.List;
import java.util.logging.Level;

public interface Button {

    default void onClick(Player player, ClickType clickType) {
        Main plugin = Main.getPlugin();
        Bukkit.getLogger().log(Level.INFO, "MenuManager: " + player.getName() + " clicked on the button " + ChatColor.stripColor(getName()) + " in the menu " + getParent().getID());
        switch (getAction()) {
            case CLOSE -> player.closeInventory();
            case CHANGE -> {
                GeneralConfigurationEnum config = getButtonObject().getGeneralConfigurationEnum();
                switch (config.getTypeOfData()) {
                    case Boolean -> {
                        boolean changedValue = flipBoolean(plugin.getConfigManager().getBooleanValue(config));
                        plugin.getConfigManager().setConfigBoolean(config, changedValue);

                        player.closeInventory();
                        plugin.getMenuManager().openMenu(plugin.getMenuManager().getAdminMenu(), player);

                    }
                    case Integer -> {
                        SignGUI gui = null;
                        try {
                            gui = SignGUI.builder()
                                    .setType(Material.OAK_SIGN)
                                    .setLine(1, "§6^^The new value^^")
                                    .setHandler((player1, result) -> {

                                        String str = result.getLine(0);
                                        Integer value = Integer.valueOf(str);
                                        plugin.getConfigManager().setConfigInteger(config, value);
                                        return null;
                                    })
                                    .build();
                        } catch (SignGUIVersionException e) {
                            throw new RuntimeException(e);
                        }
                        gui.open(player);

                    }
                    case String -> {
                        SignGUI gui = null;
                        try {
                            gui = SignGUI.builder()
                                    .setType(Material.OAK_SIGN)
                                    .setLine(1, "§6^^The new value^^")
                                    .setHandler((player1, result) -> {

                                        String str = result.getLine(0);
                                        plugin.getConfigManager().setConfigString(config, str);
                                        return null;
                                    })
                                    .build();
                        } catch (SignGUIVersionException e) {
                            throw new RuntimeException(e);
                        }
                        gui.open(player);

                    }
                    case ListString -> {
                        player.closeInventory();
                        player.sendMessage("This was not really yet implemented and idk if will be ");
                    }
                }


            }
        }
    }


    private boolean flipBoolean(boolean bool) {
        return !bool;

    }

    Material getMaterial();

    ItemStack getItemDefault();

    void setMaterial(Material material);

    String getName();

    void setName(String name);

    List<String> getLore();

    void setLore(List<String> lore);

    Action getAction();

    ButtonObject getButtonObject();

    Menu getParent();

    boolean isGlow();

    default ItemStack getItem() {
        ItemStack item;
        if (getMaterial() == null)
            item = this.getItemDefault();
        else
            item = new ItemStack(getMaterial());
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(getName());
        meta.setLore(getLore());
        item.setItemMeta(meta);
        if (isGlow()) {
/*
            TODO item = EnchantManager.addGlow(item);
*/
        }

        NBTUtil.setKey(item, "menu", getParent().getID());

        return item;


    }


}
