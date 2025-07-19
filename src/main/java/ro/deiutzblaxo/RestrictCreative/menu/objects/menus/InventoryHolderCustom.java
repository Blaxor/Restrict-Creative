package ro.deiutzblaxo.RestrictCreative.menu.objects.menus;

import lombok.Setter;
import org.bukkit.inventory.Inventory;

@Setter
public class InventoryHolderCustom implements InventoryTag {
    private Inventory inventory;
    private String type;

    public InventoryHolderCustom(String type) {
        this.type = type;
    }


    @Override
    public String getType() {
        return type;
    }

    @Override
    public Inventory getInventory() {
        return inventory;
    }
}
