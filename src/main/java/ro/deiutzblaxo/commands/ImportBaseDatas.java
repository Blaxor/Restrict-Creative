package ro.deiutzblaxo.commands;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;

import ro.deiutzblaxo.RestrictCreative.Main;
import ro.deiutzblaxo.RestrictCreative.mySQL.MySQLHandler;


public class ImportBaseDatas implements CommandExecutor {
	private Main plugin;
	public ImportBaseDatas(Main main) {
		plugin = main;

	}

	@Override
	public boolean onCommand(CommandSender sender, Command arg1, String arg2, String[] args) {

		if (sender instanceof ConsoleCommandSender) {
			if (args.length == 2) {
				if (args[0].equalsIgnoreCase("local") && args[1].equalsIgnoreCase("online")) {
					if (MySQLHandler.basedataOnline) {
						Bukkit.getScheduler().runTaskAsynchronously(plugin, new Runnable() {

							@Override
							public void run() {



								sender.sendMessage(
										"Import from local file to online started!This can take a while, don`t stop your server/online database or place/distroy any others marked blocks.");
								ArrayList<String> locs = null;
								try {
									locs = getLocalLocations();
								} catch (Exception e) {
									if (e.getMessage().equalsIgnoreCase("IBD1")) {
										sender.sendMessage(ChatColor.RED + "Error , The Local DataBase do not exist!");
										return ;
									}
								}
								for (String l : locs) {
									plugin.getDatabase().getMySQLGetSet().createLocation(l);
									if (plugin.getConfigManager().getConfig().getBoolean("Debug")) {
										plugin.getServer().getConsoleSender()
										.sendMessage("Moving the location " + l + " from local to online");

									}
								}
								sender.sendMessage("Import from local file to online complete");
							}
						});
					} else {
						sender.sendMessage(
								ChatColor.RED + "Error , the Online DataBase is not connected , please check the config.");
					}
				} else if (args[0].equalsIgnoreCase("online") && args[1].equalsIgnoreCase("local")) {
					if (MySQLHandler.basedataOnline) {
						Bukkit.getScheduler().runTaskAsynchronously(plugin, new Runnable() {

							@Override
							public void run() {




								sender.sendMessage(
										"Import from local file to online started!This can take a while, don`t stop your server/online database or place/distroy any others marked blocks.");


								String url = "jdbc:sqlite:" + plugin.getDataFolder().getPath() + "/database.db";
								Connection con;
								try {
									String sql = "CREATE TABLE " + plugin.getDatabase().table + "(LOCATION TEXT(1000))";
									con = DriverManager.getConnection(url);
									DatabaseMetaData meta = con.getMetaData();
									Statement stmt = con.createStatement();
									try {
										stmt.execute(sql);
									} catch (Exception e) {

									}
									for (String loc : plugin.getDatabase().getMySQLGetSet().getLocations()) {

										PreparedStatement statement;

										statement = con
												.prepareStatement("SELECT * FROM " + plugin.getDatabase().table
														+ " WHERE LOCATION =(?)");
										statement.setString(1, loc);
										ResultSet results = statement.executeQuery();
										if (!results.next()) {
											PreparedStatement insert = con.prepareStatement(
													"INSERT INTO " + plugin.getDatabase().table + " (LOCATION) VALUES (?)");
											insert.setString(1, loc);
											insert.executeUpdate();
											if (!insert.isClosed()) {
												insert.close();
											}
										}
										if (plugin.getConfigManager().getConfig().getBoolean("Debug")) {
											plugin.getServer().getConsoleSender()
											.sendMessage("The location " + loc + "has move from Online to local");
										}
									}
									con.close();
									sender.sendMessage("Import from online file to local complete");
								} catch (SQLException e) {
									e.printStackTrace();

								}
							}
						});
					} else {
						sender.sendMessage(
								ChatColor.RED + "Error , the Online DataBase is not connected , please check the config.");
					}
				}
			} else {
				sender.sendMessage("Not Enough arguments");
			}
			return false;
		} else {
			sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
					plugin.getConfigManager().getMessage().getString("noPermission")));
			return false;
		}

	}

	private ArrayList<String> getLocalLocations() throws Exception {
		String url = "jdbc:sqlite:" + plugin.getDataFolder().getPath() + "/database.db";
		ArrayList<String> list = new ArrayList<String>();

		String sql = "CREATE TABLE " + "RestrictCreative" + "(LOCATION TEXT(1000))";

		try (Connection conn = DriverManager.getConnection(url); Statement stmt = conn.createStatement()) {
			stmt.execute(sql);

		} catch (SQLException e) {
			if (e.getMessage()
					.contains("[SQLITE_ERROR] SQL error or missing database (table RestrictCreative already exists)")) {
				try {
					Connection con = DriverManager.getConnection(url);
					Statement statement = con.createStatement();
					String sql2 = "SELECT * FROM " + "RestrictCreative";
					ResultSet rs = statement.executeQuery(sql2);

					while (rs.next()) {
						list.add(rs.getString(1));
					}

					statement.close();
					con.close();
				} catch (SQLException e2) {
					// e.printStackTrace();
				}

			} else {
				// The table don`t exist
				throw new Exception("IBD1");
			}
		}
		return list;

	}


	//		private Main plugin = Main.getInstance();
	//		private MysqlMain BaseDataMain = Main.getInstance().getDatabase();
	//		@Override
	//		public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
	//
	//
	//			if(sender instanceof ConsoleCommandSender) {
	//				if(args.length >= 2) {
	//					if(args[0].equalsIgnoreCase("YAML")&& args[1].equalsIgnoreCase("MySQL")) {
	//						sender.sendMessage("Import from YAML file to MySQL started!This can take a while, don`t stop your server/MySQL or place/distroy any others marked blocks.");
	//						BaseDataMain.mysqlSetup();
	//						BaseDataMain.getLocationsSetterGetter().clearLocations();
	//						plugin.Mark.setLocationsConfig();
	//						plugin.Mark.getLocations().clear();
	//						plugin.Mark.LoadLocations();
	//
	//						for(String location :plugin.Mark.getLocations() ) {
	//							BaseDataMain.getLocationsSetterGetter().createLocation(location);
	//						}
	//						plugin.Mark.setLocationsConfig();
	//						if(!BaseDataMain.BaseData()) {
	//							try {
	//								BaseDataMain.getConnection().close();
	//							} catch (SQLException e) {
	//								e.printStackTrace();
	//							}
	//						}
	//						sender.sendMessage("Import from YAML file to MySQL have been completed!");
	//
	//
	//
	//
	//					}else if(args[0].equalsIgnoreCase("MySQL")&&args[1].equalsIgnoreCase("YAML")) {
	//						sender.sendMessage("Import from MySQL file to YAML started!This can take a while, don`t stop your server/MySQL or place/distroy any marked blocks.");
	//						BaseDataMain.mysqlSetup();
	//						plugin.Mark.SetupLocations();
	//						plugin.Mark.getLocations().clear();
	//						plugin.Mark.setLocationsConfig();
	//						for(String location : BaseDataMain.getLocationsSetterGetter().getLocations()) {
	//							if(!plugin.Mark.getLocations().contains(location)) {
	//								plugin.Mark.getLocations().add(location);
	//
	//							}
	//						}
	//						plugin.Mark.setLocationsConfig();
	//						if(!BaseDataMain.BaseData()) {
	//							try {
	//								BaseDataMain.getConnection().close();
	//							} catch (SQLException e) {
	//								e.printStackTrace();
	//							}
	//						}
	//						sender.sendMessage("Import from MySQL file to YAML have been completed!");
	//					}else {
	//						sender.sendMessage("Error ussage, please try /import <from> <to> .");
	//						sender.sendMessage("Available locations : YAML , MySQL");
	//
	//					}
	//				}else {
	//					sender.sendMessage("Error ussage, please try /import <from> <to> .");
	//					sender.sendMessage("Available locations : YAML , MySQL");
	//				}
	//			}else{
	//				sender.sendMessage("This command can be used just by console");
	//			}
	//
	//
	//
	//			return false;

}