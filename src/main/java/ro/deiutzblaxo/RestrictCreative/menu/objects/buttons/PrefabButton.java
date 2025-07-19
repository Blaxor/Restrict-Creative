package ro.deiutzblaxo.RestrictCreative.menu.objects.buttons;

import lombok.Data;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import ro.deiutzblaxo.RestrictCreative.menu.objects.Button;
import ro.deiutzblaxo.RestrictCreative.menu.objects.Menu;

import java.util.List;
import java.util.stream.Collectors;

@Data
public class PrefabButton implements Button {
    private Material material;
    private ItemStack itemStack;
    private String name;
    private List<String> lore;
    private final Action action;
    private final ButtonObject buttonObject;
    private final Menu parent;
    private boolean glow = false;

    public PrefabButton(Material material, String name, List<String> lore, Action action, ButtonObject buttonObject, Menu parent) {
        this.material = material;
        this.name = ChatColor.translateAlternateColorCodes('&', name);
        this.lore = lore.stream().map(s -> ChatColor.translateAlternateColorCodes('&', s)).toList();
        this.action = action;
        this.buttonObject = buttonObject;
        this.parent = parent;
    }

    public PrefabButton(ItemStack item, String name, List<String> lore, Action action, ButtonObject buttonObject, Menu parent) {
        this.itemStack = item;
        this.name = name;
        this.lore = lore;
        this.action = action;
        this.buttonObject = buttonObject;
        this.parent = parent;
    }


    @Override
    public Material getMaterial() {
        return material;
    }

    @Override
    public ItemStack getItemDefault() {
        return itemStack;
    }

    @Override
    public void setMaterial(Material material) {
        this.material = material;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public List<String> getLore() {
        return lore;
    }

    @Override
    public void setLore(List<String> lore) {
        this.lore = lore;
    }

    @Override
    public Menu getParent() {
        return parent;
    }


}
