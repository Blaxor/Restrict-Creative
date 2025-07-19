package ro.deiutzblaxo.RestrictCreative.config.enums;

import lombok.Getter;

import java.util.List;

@Getter
public enum MessageEnum {

    LORE_MESSAGE("LoreMessage", "&aItem created in creative by %name%"),
    DISABLED_ITEMS("DisabledItems", "&4This is a disabled item!"),
    ERROR_DROP_ITEM("ErrorDropItem", "&4You can`t drop items created in creative mode!"),
    ERROR_INVENTORY_PUT("ErrorInventoryPut", "&4You can`t transfer items created in creative mode in a inventory!"),
    STONECUTTER_GRINDSTONE_RESTRICT("StoneCutter_GrindStone_Restrict", "&4This was made in creative , you can`t access it ."),
    WORLDEDIT_NOT_FIND("WorldEdit_NotFind", "&eYou need to have WE to work."),
    WORLDEDIT_AREA_NOT_SELECTED("WorldEdit_AreaNotSelected", "&4You need to select an a area to work"),
    NO_PERMISSION("noPermission", "&4You need permission to use this command"),
    WRONG_ARGUMENTS("Wrong_Arguments", "Please use : /bulk add|remove"),
    BULK_MARKING_START("Bulk_Marking_Start", "&eThe marking has start , please don`t break/place any block in the selection"),
    BULK_MARK_FINISH("Bulk_Marking_Finish", "&aThe marking has finished , %blocks% blocks changed"),
    TITLE_MENU_ADMIN("menu.title.admin", "Restrict Creative Admin Console"),
    BACK_BUTTON_MENU_ADMIN("menu.button.back", "&4&lBack!");

    private final String path;
    private final Object defaultValue;

    MessageEnum(String path, Object defaultValue) {
        this.path = path;
        this.defaultValue = defaultValue;
    }

}
