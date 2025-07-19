package ro.deiutzblaxo.RestrictCreative.menu;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import ro.deiutzblaxo.RestrictCreative.Main;

import ro.deiutzblaxo.RestrictCreative.config.enums.GeneralConfigurationEnum;
import ro.deiutzblaxo.RestrictCreative.config.enums.MessageEnum;

import ro.deiutzblaxo.RestrictCreative.menu.events.PlayerOpenMenuEvent;
import ro.deiutzblaxo.RestrictCreative.menu.objects.Menu;
import ro.deiutzblaxo.RestrictCreative.menu.objects.buttons.Action;
import ro.deiutzblaxo.RestrictCreative.menu.objects.buttons.ButtonObject;
import ro.deiutzblaxo.RestrictCreative.menu.objects.buttons.PrefabButton;
import ro.deiutzblaxo.RestrictCreative.menu.objects.menus.PrefabMenu;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Getter
public class MenuManager {
    private final HashMap<String, Menu> menus = new HashMap<>();
    private final Main plugin;

    public MenuManager(Main plugin) {
        this.plugin = plugin;

        init();
    }

    private void init() {
        addMenu(new PrefabMenu("admin", "restrictcreative.admin", plugin.getConfigManager().getStringValue(MessageEnum.TITLE_MENU_ADMIN), 9));

    }

    public Menu getMenu(String id) {
        if (menus.containsKey(id)) {
            return menus.get(id);
        }
        throw new RuntimeException("Menu " + id + " not found!");
    }

    public void openMenu(Menu menu, Player player) {
        Bukkit.getPluginManager().callEvent(new PlayerOpenMenuEvent(menu, player));
    }

    public void addMenu(Menu menu) {
        menus.put(menu.getID(), menu);
    }

    public Menu getAdminMenu() {

        Menu menu = getMenu("admin");
        menu.getButtons().clear();
        menu.setSize(GeneralConfigurationEnum.values().length);
        int pos = 0;

        for (GeneralConfigurationEnum config : GeneralConfigurationEnum.values()) {
            PrefabButton button = new PrefabButton(config.getMenuMaterial(), "&f&l" + config.getPath(),
                    List.of(" ", "&a&lValue is: ", "", "&e" + plugin.getConfigManager().getValueAsString(config)), Action.CHANGE,
                    new ButtonObject(config), menu);

            menu.addButton(pos, button);
            pos++;
        }

        menu.addButton(menu.getButton(menu.getSize() - 1) != null ?
                        menu.getSize() + 8 : menu.getSize() - 1,
                new PrefabButton(Material.BARRIER, plugin.getConfigManager().getStringValue(MessageEnum.BACK_BUTTON_MENU_ADMIN), new ArrayList<>(),
                        Action.CLOSE, null, menu));

        return menu;
    }


    private String cc(String string) {
        return ChatColor.translateAlternateColorCodes('&', string);
    }

}
