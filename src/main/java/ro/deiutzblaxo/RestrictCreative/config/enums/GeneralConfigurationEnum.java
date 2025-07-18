package ro.deiutzblaxo.RestrictCreative.config.enums;

import lombok.Getter;

import java.util.List;

@Getter
public enum GeneralConfigurationEnum {

        RestrictInventoryPut( "RestrictInventoryPut", true),
		UpdaterChecker( "UpdaterChecker", true),
		Debug( "Debug", false),
		PvP( "PvP", true),
		PvE( "PvE", false),
		BlockPlace( "BlockPlace", false),
		BlockPistonPush( "BlockPistonPush", true),
		DisableBlockSpreadEvent( "DisableBlockSpreadEvent", true),
		DisableBlockGrowEvent( "DisableBlockGrowEvent", true),
		DisableStructureGrowEvent( "DisableStructureGrowEvent", true),
		DisabledItems( "Disabled-Items", List.of("lava_bucket","bedrock")),
		username( "username", "root"),
		host( "host", "localhost"),
		port( "port", 3306),
		database( "database", "restrictcreative"),
		password( "password", "password");

    private final String path;
    private final Object defaultValue;
    GeneralConfigurationEnum(String path, Object defaultValue) {
        this.path=path;
        this.defaultValue=defaultValue;
    }

}
