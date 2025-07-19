package ro.deiutzblaxo.RestrictCreative.config.enums;

import lombok.Getter;
import org.bukkit.Material;

import java.util.List;

@Getter
public enum GeneralConfigurationEnum {

    RestrictInventoryPut("RestrictInventoryPut", true, Material.CHEST, DataEntryType.Boolean),
    UpdaterChecker("UpdaterChecker", true, Material.COMPARATOR, DataEntryType.Boolean),
    Debug("Debug", false, Material.REDSTONE_TORCH, DataEntryType.Boolean),
    PvP("PvP", true, Material.DIAMOND_SWORD, DataEntryType.Boolean),
    PvE("PvE", false, Material.ZOMBIE_HEAD, DataEntryType.Boolean),
    BlockPlace("BlockPlace", false, Material.STONE, DataEntryType.Boolean),
    BlockPistonPush("BlockPistonPush", true, Material.PISTON, DataEntryType.Boolean),
    DisableBlockSpreadEvent("DisableBlockSpreadEvent", true, Material.MYCELIUM, DataEntryType.Boolean),
    DisableBlockGrowEvent("DisableBlockGrowEvent", true, Material.BONE_MEAL, DataEntryType.Boolean),
    DisableStructureGrowEvent("DisableStructureGrowEvent", true, Material.OAK_SAPLING, DataEntryType.Boolean),
    DisabledItems("Disabled-Items", List.of("lava_bucket", "bedrock"), Material.BARRIER, DataEntryType.ListString),
    username("username", "root", Material.NAME_TAG, DataEntryType.String),
    host("host", "localhost", Material.COMPASS, DataEntryType.String),
    port("port", 3306, Material.REPEATER, DataEntryType.Integer),
    database("database", "restrictcreative", Material.BOOKSHELF, DataEntryType.String),
    password("password", "password", Material.TRIPWIRE_HOOK, DataEntryType.String);


    private final String path;
    private final Object defaultValue;
    private final Material menuMaterial;
    private final DataEntryType typeOfData;

    GeneralConfigurationEnum(String path, Object defaultValue, Material menuMaterial, DataEntryType typeOfData) {
        this.path = path;
        this.defaultValue = defaultValue;
        this.menuMaterial = menuMaterial;
        this.typeOfData = typeOfData;
    }

}
