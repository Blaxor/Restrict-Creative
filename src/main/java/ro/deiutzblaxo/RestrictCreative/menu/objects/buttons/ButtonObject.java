package ro.deiutzblaxo.RestrictCreative.menu.objects.buttons;

import lombok.Data;
import org.bukkit.inventory.ItemStack;
import ro.deiutzblaxo.RestrictCreative.config.enums.GeneralConfigurationEnum;
import ro.deiutzblaxo.RestrictCreative.menu.objects.Menu;


@Data
public class ButtonObject {

    private GeneralConfigurationEnum generalConfigurationEnum;


    public ButtonObject(GeneralConfigurationEnum generalConfigurationEnum) {
        this.generalConfigurationEnum = generalConfigurationEnum;
    }


}
