package ro.deiutzblaxo.RestrictCreative.config;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import ro.deiutzblaxo.RestrictCreative.Main;
import ro.deiutzblaxo.RestrictCreative.config.enums.GeneralConfigurationEnum;
import ro.deiutzblaxo.RestrictCreative.config.enums.MessageEnum;

public class ConfigManager {

    private Main plugin;
    private File dataFolder;
    private File messageFile;
    private FileConfiguration message;
    private File configFile;
    private FileConfiguration config;
    private File databaseFile;
    private FileConfiguration database;

    public ConfigManager(Main main) {
        plugin = main;
    }

    public void SetupFiles() {
        dataFolder = plugin.getDataFolder();
        if (!dataFolder.exists()) {
            dataFolder.mkdirs();
        }
        messageFile = new File(dataFolder, "/message.yml");
        if (!messageFile.exists()) {
            try {
                messageFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        configFile = new File(dataFolder, "/config.yml");
        if (!configFile.exists()) {
            try {
                configFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        databaseFile = new File(dataFolder, "/DataBaseYML/Locations.yml");
        if (databaseFile.exists()) {
            database = YamlConfiguration.loadConfiguration(databaseFile);
        }
        LoadConfigs();

    }

    public void initDefaultsConfig() {

        config = YamlConfiguration.loadConfiguration(configFile);
        for (GeneralConfigurationEnum value : GeneralConfigurationEnum.values()) {
            config.addDefault(value.getPath(), value.getDefaultValue());
        }
        config.options().copyDefaults(true);
        config.options().parseComments(true);

        message = YamlConfiguration.loadConfiguration(messageFile);
        for (MessageEnum value : MessageEnum.values()) {
            message.addDefault(value.getPath(), value.getDefaultValue());
        }
        message.options().copyDefaults(true);
        message.options().parseComments(true);

        try {
            message.save(messageFile);
            config.save(configFile);
        } catch (IOException e) {

            e.printStackTrace();
        }
    }

    public ArrayList<Material> getBannedItems() {
        ArrayList<String> rawMaterial = new ArrayList<String>(config.getStringList("Disabled-Items"));
        ArrayList<Material> Materials = new ArrayList<Material>();
        for (String str : rawMaterial) {
            Materials.add(Material.getMaterial(str.toUpperCase()));
        }
        return Materials;
    }

    public void saveDataBase() {

        if (database == null)
            return;

        try {
            database.save(databaseFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void LoadConfigs() {
        message = YamlConfiguration.loadConfiguration(messageFile);
        config = YamlConfiguration.loadConfiguration(configFile);
    }

    public void setString(FileConfiguration configuration, String path, String value) {
        configuration.set(path, value);
    }

    public void setConfigBoolean(GeneralConfigurationEnum generalConfigurationEnum, boolean value) {
        config.set(generalConfigurationEnum.getPath(), value);
        try {
            config.save(configFile);
        } catch (IOException e) {
            //ignore
        }
    }

    public void setConfigInteger(GeneralConfigurationEnum generalConfigurationEnum, Integer value) {
        config.set(generalConfigurationEnum.getPath(), value);
        try {
            config.save(configFile);
        } catch (IOException e) {
            //ignore
        }
    }

    public void setConfigString(GeneralConfigurationEnum generalConfigurationEnum, String value) {
        config.set(generalConfigurationEnum.getPath(), value);
        try {
            config.save(configFile);
        } catch (IOException e) {
            //ignore
        }
    }

    public Boolean getBooleanValue(GeneralConfigurationEnum generalConfigurationEnum) {
        return config.getBoolean(generalConfigurationEnum.getPath());
    }

    public String getStringValue(GeneralConfigurationEnum generalConfigurationEnum) {
        return config.getString(generalConfigurationEnum.getPath());
    }

    public List<String> getListStringValue(GeneralConfigurationEnum generalConfigurationEnum) {
        return config.getStringList(generalConfigurationEnum.getPath());
    }

    public String getStringValue(MessageEnum generalConfigurationEnum) {
        return message.getString(generalConfigurationEnum.getPath());
    }

    public String getValueAsString(GeneralConfigurationEnum generalConfigurationEnum) {

        return switch (generalConfigurationEnum.getTypeOfData()) {
            case String -> config.getString(generalConfigurationEnum.getPath());
            case Boolean -> String.valueOf(config.getBoolean(generalConfigurationEnum.getPath()));
            case Integer -> String.valueOf(config.getInt(generalConfigurationEnum.getPath()));
            case ListString -> String.valueOf(config.getStringList(generalConfigurationEnum.getPath()));
            case null, default -> throw new RuntimeException("Type not found/handled");
        };
    }
}