package ro.deiutzblaxo.RestrictCreative.menu.objects;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.HashMap;

public interface Menu {

    HashMap<Integer, Button> getButtons();

    String getID();

    String getPermission();

    default boolean hasPermission(Player player) {
        return player.hasPermission(getPermission());
    }

    default Button getButton(int slot) {
        return getButtons().get(slot);
    }

    default Button addButton(int slot, Button button) {
        return getButtons().put(slot, button);
    }

    default Button removeButton(int slot) {
        return getButtons().remove(slot);
    }

    Inventory getInterface();


    int getSize();

    void setSize(int size);
}
