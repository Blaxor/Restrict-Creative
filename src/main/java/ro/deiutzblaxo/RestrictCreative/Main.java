package ro.deiutzblaxo.RestrictCreative;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;
import ro.deiutzblaxo.RestrictCreative.Metrics.Metrics;
import ro.deiutzblaxo.RestrictCreative.commands.BulkCommand;
import ro.deiutzblaxo.RestrictCreative.commands.ImportBaseDatas;
import ro.deiutzblaxo.RestrictCreative.commands.ResctrictCreativeCommand;
import ro.deiutzblaxo.RestrictCreative.config.ConfigManager;
import ro.deiutzblaxo.RestrictCreative.config.enums.GeneralConfigurationEnum;
import ro.deiutzblaxo.RestrictCreative.menu.MenuManager;
import ro.deiutzblaxo.RestrictCreative.menu.listener.InventoryClickListener;
import ro.deiutzblaxo.RestrictCreative.mySQL.DataService;
import ro.deiutzblaxo.RestrictCreative.mySQL.LocationHandler;
import ro.deiutzblaxo.RestrictCreative.mySQL.MySQLManager;
import ro.deiutzblaxo.RestrictCreative.mySQL.MySQLService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.SQLException;

public class Main extends JavaPlugin implements Listener {

    private static Main instance;

    private Mark Mark;
    private ConfigManager ConfigManager;
    private MySQLManager database;
    private LocationHandler dataService;
    @Getter
    private MenuManager menuManager;

    @Override
    public void onEnable() {
        instance = this;
        setConfigManager(new ConfigManager(this));
        loadConfigManager();
        setMark(new Mark(this));
        getConfigManager().LoadConfigs();
        database = new MySQLManager(this);
        dataService = new DataService(new MySQLService(database));
        menuManager = new MenuManager(this);

        registerByVersion(this);

        getServer().getPluginManager().registerEvents(this, this);
        getCommand("import").setExecutor(new ImportBaseDatas(this));
        getCommand("restrictcreative").setExecutor(new ResctrictCreativeCommand(this));
        getCommand("bulk").setExecutor(new BulkCommand(this));


        if (getConfigManager().getBooleanValue(GeneralConfigurationEnum.UpdaterChecker)) {
            updateCheckerConsole(this, ChatColor.translateAlternateColorCodes('&', "&7[&aRestrictCreative&7]"), 66007);
        }

    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        if (getConfigManager().getBooleanValue(GeneralConfigurationEnum.UpdaterChecker)) {
            if (e.getPlayer().isOp()) {

                updateCheckerPlayer(this, e.getPlayer(),
                        ChatColor.translateAlternateColorCodes('&', "&7[&aRestrictCreative&7]"), 66007);
            }
        }
    }


    @Override
    public void onDisable() {

        try {
            database.getConnection().close();
        } catch (SQLException e) {

            e.printStackTrace();
        }
        getConfigManager().saveDataBase();


    }

    public void updateCheckerConsole(Plugin plugin, String prefix, Integer ResourceNumber) {
        PluginDescriptionFile pdffile = plugin.getDescription();
        try {
            HttpURLConnection con = (HttpURLConnection) new URL(
                    "https://api.spigotmc.org/legacy/update.php?resource=" + ResourceNumber).openConnection();
            int timed_out = 1250;
            con.setConnectTimeout(timed_out);
            con.setReadTimeout(timed_out);
            String latestversion = new BufferedReader(new InputStreamReader(con.getInputStream())).readLine();
            if ((latestversion.length() <= 100) && (!pdffile.getVersion().equals(latestversion))) {
                Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&',
                        "&8&m--------------------------------------------------------------------------------------"));
                Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&',
                        prefix + "&8There is a new version available. &9" + latestversion));
                Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', prefix
                        + "&8You can download it at: &9https://www.spigotmc.org/resources/" + ResourceNumber + "/"));
                Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&',
                        "&8&m--------------------------------------------------------------------------------------"));

            }
        } catch (Exception ex) {
            Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&',
                    "&8&m--------------------------------------------------------------------------------------"));
            Bukkit.getConsoleSender().sendMessage(
                    ChatColor.translateAlternateColorCodes('&', prefix + "&cPlease connect to the internet."));
            Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&',
                    "&8&m--------------------------------------------------------------------------------------"));
        }
    }

    public void updateCheckerPlayer(Plugin plugin, Player player, String prefix, Integer ResourceNumber) {

        try {
            PluginDescriptionFile pdffile = plugin.getDescription();
            HttpURLConnection con = (HttpURLConnection) new URL(
                    "https://api.spigotmc.org/legacy/update.php?resource=" + ResourceNumber).openConnection();
            int timed_out = 1250;
            con.setConnectTimeout(timed_out);
            con.setReadTimeout(timed_out);
            String latestversion = new BufferedReader(new InputStreamReader(con.getInputStream())).readLine();
            if ((latestversion.length() <= 100) && (!pdffile.getVersion().equals(latestversion))) {

                if ((player.isOp()) && (!pdffile.getVersion().equals(latestversion))) {
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                            "&8&m-----------------------------------------------------"));
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                            prefix + "&8There is a new version available. &9" + latestversion));
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                            prefix + "&8You can download it at: &9https://www.spigotmc.org/resources/65244/"));
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                            "&8&m-----------------------------------------------------"));
                    player.playSound(player.getLocation(), Sound.BLOCK_CHEST_OPEN, 1.0F, 1.0F);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void loadConfigManager() {

        getConfigManager().SetupFiles();
        getConfigManager().initDefaultsConfig();
        getConfigManager().LoadConfigs();
    }

    private void registerByVersion(Main plugin) {

        getServer().getPluginManager().registerEvents(new InteractionsListener(this), this);

        getServer().getPluginManager().registerEvents(new InventoryClickListener(this), this);

        Metrics metrics = new Metrics(plugin);
        metrics.addCustomChart(new Metrics.SingleLineChart("placed", () -> getDataService().getLocations().size()));

        metrics.addCustomChart(new Metrics.SingleLineChart("cacheSize", () -> getDataService().getCacheLocationsSize()));
    }

    public Mark getMark() {
        return Mark;
    }

    public void setMark(Mark mark) {
        Mark = mark;
    }

    public ConfigManager getConfigManager() {
        return ConfigManager;
    }

    public void setConfigManager(ConfigManager configManager) {
        ConfigManager = configManager;
    }

    public LocationHandler getDataService() {
        return dataService;
    }

    public static Main getPlugin() {
        return instance;
    }
}