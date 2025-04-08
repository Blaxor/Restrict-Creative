package ro.deiutzblaxo.RestrictCreative;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class ConfigManager {

	private Main plugin;
	private File DataFolder;
	private File MessageFile;
	private FileConfiguration Message;
	private File configFile;
	private FileConfiguration config;
	private File databaseFile;
	private FileConfiguration database;

	public ConfigManager(Main main) {
		plugin = main;
	}

	public void SetupFiles() {
		DataFolder = plugin.getDataFolder();
		if (!DataFolder.exists()) {
			DataFolder.mkdirs();
		}
		MessageFile = new File(DataFolder, "/Message.yml");
		if (!MessageFile.exists()) {
			try {
				MessageFile.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
		configFile = new File(DataFolder, "/config.yml");
		if (!configFile.exists()) {
			try {
				configFile.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		databaseFile = new File(DataFolder, "/DataBaseYML/Locations.yml");
		if (databaseFile.exists()) {
			database = YamlConfiguration.loadConfiguration(databaseFile);
		}
		LoadConfigs();

	}

	public void SaveMessages() {
		Message = YamlConfiguration.loadConfiguration(MessageFile);
		config = YamlConfiguration.loadConfiguration(configFile);

		config.addDefault("RestrictInventoryPut", true);
		config.addDefault("UpdaterChecker", true);
		config.addDefault("Debug", false);
		config.addDefault("PvP", true);
		config.addDefault("PvE", false);
		config.addDefault("BlockPlace", false);
		config.addDefault("BlockPistonPush", true);
		config.addDefault("DisableBlockSpreadEvent", true);
		config.addDefault("DisableBlockGrowEvent", true);
		config.addDefault("DisableStructureGrowEvent", true);
		ArrayList<String> itemsDisabledDefault = new ArrayList<String>();
		itemsDisabledDefault.add("lava_bucket");
		itemsDisabledDefault.add("bedrock");
		config.addDefault("Disabled-Items", itemsDisabledDefault);
		config.addDefault("username", "root");
		config.addDefault("host", "localhost");
		config.addDefault("port", 3306);
		config.addDefault("database", "restrictcreative");
		config.addDefault("password", "password");

		config.options().copyDefaults(true);
		config.options().copyHeader(true);
		setString(Message, "LoreMessage", "&aItem created in creative by %name%");
		setString(Message, "DisabledItems" , "&4This is a disabled item!");
		setString(Message, "ErrorDropItem", "&4You can`t drop items created in creative mode!");
		setString(Message, "ErrorInventoryPut", "&4You can`t transfer items created in creative mode in a inventory!");
		// TODO NEW
		setString(Message, "StoneCutter_GrindStone_Restrict", "&4This was made in creative , you can`t access it .");
		setString(Message, "WorldEdit_NotFind", "&eYou need to have WE to work.");
		setString(Message, "WorldEdit_AreaNotSelected", "&4You need to select an a area to work");
		setString(Message, "noPermission", "&4You need permission to use this command");
		setString(Message, "Wrong_Arguments", "Please use : /bulk add|remove");
		setString(Message, "Bulk_Marking_Start",
				"&eThe marking has start , please don`t break/place any block in the selection");
		setString(Message, "Bulk_Marking_Finish",
				"The marking has finished , %blocks% blocks changed");

		try {
			Message.save(MessageFile);
			config.save(configFile);
		} catch (IOException e) {

			e.printStackTrace();
		}
	}
	public ArrayList<Material> getBannedItems(){
		ArrayList<String> rawMaterial = new ArrayList<String>(this.getConfig().getStringList("Disabled-Items"));
		ArrayList<Material> Materials = new ArrayList<Material>();
		for(String str: rawMaterial) {
			Materials.add(Material.getMaterial(str.toUpperCase()));
		}
		return Materials;
	}

	public void saveDataBase() {

		try {
			database.save(databaseFile);
		} catch (IOException e) {

			e.printStackTrace();
		}
	}
	public void LoadConfigs() {
		Message = YamlConfiguration.loadConfiguration(MessageFile);
		config = YamlConfiguration.loadConfiguration(configFile);
	}

	public FileConfiguration getMessage() {
		return Message;
	}

	public File getMessageFile() {
		return MessageFile;
	}

	public FileConfiguration getConfig() {
		return config;
	}

	public File getConfigFile() {
		return configFile;
	}

	public void setString(FileConfiguration configuration, String path, String value) {
		if (!configuration.contains(path)) {
			configuration.set(path, value);
		}
	}

	public FileConfiguration getDatabase() {
		return database;
	}

}