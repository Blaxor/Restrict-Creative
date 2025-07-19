package ro.deiutzblaxo.RestrictCreative.menu.objects.menus;

import lombok.Data;
import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import ro.deiutzblaxo.RestrictCreative.menu.objects.Button;
import ro.deiutzblaxo.RestrictCreative.menu.objects.Menu;


import java.util.HashMap;

@Data
public class PrefabMenu implements Menu {

    private HashMap<Integer, Button> buttons = new HashMap<>();
    private final String id;
    private final String permission;
    private String name;
    private int size = 0;

    public PrefabMenu(String id, String permission, String name, int size) {
        this.id = id;
        this.permission = permission;
        this.name = name;
        setSize(size);

    }

    @Override
    public HashMap<Integer, Button> getButtons() {
        return buttons;
    }

    @Override
    public String getID() {
        return id;
    }

    @Override
    public String getPermission() {
        return permission;
    }

    @Override
    public void setSize(int size) {
        if (size % 9 == 0) {
            this.size = size;
            return;
        }
        this.size = (((size / 9) + 1) * 9);
    }

    @Override
    public Button addButton(int slot, Button button) {
        if (size < slot)
            setSize(slot);
        return Menu.super.addButton(slot, button);
    }

    @Override
    public Inventory getInterface() {
        InventoryHolderCustom invhold = new InventoryHolderCustom("menu");
        Inventory inv = Bukkit.createInventory(invhold, size, name);
        invhold.setInventory(inv);
        getButtons().forEach((integer, button) -> inv.setItem(integer, button.getItem()));
        return inv;
    }
}
