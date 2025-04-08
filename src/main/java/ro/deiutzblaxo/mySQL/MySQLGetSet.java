package ro.deiutzblaxo.mySQL;

import java.io.File;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import ro.deiutzblaxo.RestrictCreative.Main;

public class MySQLGetSet {

	private MySQLHandler main;
	public static int nrMoved = 0;

	public MySQLGetSet(MySQLHandler main) {
		this.main = main;
		moveFromYml();

	}

	public boolean locationExists(String l) {
		PreparedStatement statement;
		try {
			statement = main.getConnection().prepareStatement("SELECT * FROM " + main.table + " WHERE LOCATION =(?)");
			statement.setString(1, l);
			ResultSet results = statement.executeQuery();
			if (results.next()) {

				return true;
			}
			if (!statement.isClosed()) {
				statement.close();
			}
		} catch (SQLException e) {

			e.printStackTrace();
		}
		return false;
	}

	public void createLocation(String l) {
		try {
			PreparedStatement statement = main.getConnection()
					.prepareStatement("SELECT * FROM " + main.table + " WHERE LOCATION =(?)");
			statement.setString(1, l);
			ResultSet results = statement.executeQuery();
			results.next();
			if (locationExists(l) != true) {
				PreparedStatement insert = main.getConnection()
						.prepareStatement("INSERT INTO " + main.table + " (LOCATION) VALUES (?)");
				insert.setString(1, l);
				insert.executeUpdate();
				if (!insert.isClosed()) {
					insert.close();
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	public void removeLocation(String l) {
		try {
			PreparedStatement statement = main.getConnection()
					.prepareStatement("SELECT * FROM " + main.table + " WHERE LOCATION =(?)");
			statement.setString(1, l);
			ResultSet results = statement.executeQuery();
			results.next();
			if (locationExists(l) == true) {
				PreparedStatement remove = main.getConnection()
						.prepareStatement("DELETE FROM " + main.table + " WHERE LOCATION =(?)");
				remove.setString(1, l);
				remove.executeUpdate();
				if (!remove.isClosed()) {
					remove.close();
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void clearLocations() {
		PreparedStatement clear;
		try {
			clear = main.getConnection().prepareStatement("TRUNCATE " + main.table);
			clear.executeUpdate();
			if (!clear.isClosed()) {
				clear.close();
			}
		} catch (SQLException e) {

			e.printStackTrace();
		}

	}

	public ArrayList<String> getLocations() {
		ArrayList<String> List = new ArrayList<String>();
		try {
			Statement statement = main.getConnection().createStatement();
			String sql = "SELECT * FROM " + main.table;
			ResultSet rs = statement.executeQuery(sql);

			while (rs.next()) {
				List.add(rs.getString(1));
			}

			statement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return List;

	}

	private void moveFromYml() {
		Main pl = JavaPlugin.getPlugin(Main.class);
		File file = new File(pl.getDataFolder() + "/BaseDataYML/Locations.yml");
		if (!file.exists()) {
			return;
		} else {
			Bukkit.getScheduler().runTaskAsynchronously(pl, new Runnable() {

				@Override
				public void run() {


					System.out.println(
							"Updating the YAML database to a local SQlite database , this may take a while and will work in background. Don`t worry you will not lose any data");
					FileConfiguration locationsconf = YamlConfiguration.loadConfiguration(file);
					List<String> locations = locationsconf.getStringList("Locations");

					for (String loc : locations) {

						nrMoved++;
						if (pl.getConfigManager().getConfig().getBoolean("Debug")) {
							if (nrMoved % 100 == 0) {

								Bukkit.broadcastMessage("Progress : " + nrMoved + "/" + locations.size());
							}
						}
						if (!locationExists(loc)) {

							String[] l = loc.split(" ");
							if (l.length == 4) {
								createLocation(loc);
							} else if (l.length == 3) {
								ArrayList<World> countWorld = new ArrayList<World>();

								for (World world : pl.getServer().getWorlds()) {
									if (pl.getConfigManager().getConfig().getBoolean("Debug")) {

										System.out.println("Checking the world named" + world.getName()
										+ " for the location  " + loc);
									}
									if (!world.getBlockAt((int) Double.parseDouble(l[0]),
											(int) Double.parseDouble(l[1]), (int) Double.parseDouble(l[2])).getType()
											.isAir()) {
										if (pl.getConfigManager().getConfig().getBoolean("Debug")) {
											System.out.println("is not air ");
											System.out.println(world.getBlockAt((int) Double.parseDouble(l[0]),
													(int) Double.parseDouble(l[1]), (int) Double.parseDouble(l[2]))
													.getType().toString());
										}
										countWorld.add(world);

									} else {
										if (pl.getConfigManager().getConfig().getBoolean("Debug")) {
											System.out.println("is air");
										}
									}
								}
								if (countWorld.size() == 1) {
									if (pl.getConfigManager().getConfig().getBoolean("Debug")) {
										System.out.println("Location changed for " + loc);
									}
									createLocation(loc + " " + countWorld.get(0).getName());
									countWorld.clear();

								}
								else if (countWorld.size() == 0) {
									countWorld.clear();
									if (pl.getConfigManager().getConfig().getBoolean("Debug")) {
										System.out.println("Location deleted :" + loc);
									}
								} else {
									createLocation(loc);
									countWorld.clear();
								}

							}
						}
					}

					file.delete();
					System.out.println("The moving complete .");
				}
			});
		}
	}
}