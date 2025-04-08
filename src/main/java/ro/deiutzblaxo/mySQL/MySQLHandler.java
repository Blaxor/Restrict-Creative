package ro.deiutzblaxo.mySQL;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import ro.deiutzblaxo.RestrictCreative.Main;
public class MySQLHandler {


	public static boolean basedataOnline = true;
	public String host, database, username, password, table, url;
	public int port;
	private Connection con;
	
	private Main plugin;
	private MySQLGetSet MySQLGetSet;

	public MySQLHandler(Main main) {
		plugin = main;
		setupConnection();
		setMySQLGetSet(new MySQLGetSet(this));
	}

	public void setupConnection() {


		username = plugin.getConfig().getString("username");
		host = plugin.getConfig().getString("host");
		port = plugin.getConfig().getInt("port");
		database = plugin.getConfig().getString("database");
		password = plugin.getConfig().getString("password");
		table = "RestrictCreative";
		url = "jdbc:mysql://" + host + ":" + port + "/";
		// urlLocal = "jdbc:sqlite:" + plugin.getDataFolder() + "/test.db";

		mysqlopenConnection();

		// setMySQLGetSet(new MySQLGetSet(this));
	}
	private void mysqlopenConnection() {
		try {

			synchronized (plugin) {
				// createDataBase();
				Class.forName("com.mysql.jdbc.Driver");
				con =
						DriverManager.getConnection("jdbc:mysql://" + this.host + ":" + this.port + "/" + this.database,
								this.username, this.password);

				// CREATE TABLE
				DatabaseMetaData dbm = con.getMetaData();
				ResultSet rs = dbm.getTables(null, null, table, null);
				if (rs.next()) {
					Bukkit.getLogger().log(Level.INFO, ChatColor.GREEN + "Table " + table + " exists!");
				} else {
					Bukkit.getLogger().log(Level.INFO, ChatColor.GREEN + "Table " + table + " created.");
					createTable1();
				}
				Bukkit.getLogger().log(Level.INFO, ChatColor.GREEN + "MySQL database have been connected!");
			}



		} catch (SQLException e) {
			if (e.getSQLState().equalsIgnoreCase("42000")) {// 42000 no base found
				createDataBase();
				mysqlopenConnection();
				return;
			}
			if (e.getSQLState().equalsIgnoreCase("08S01")) {// no connection fond 08S01
				basedataOnline = false;
				url = "jdbc:sqlite:" + plugin.getDataFolder().getPath() + "/database.db";
				createDatabaseOffline();
				createNewTableOffline();
				try {
					con = DriverManager.getConnection(url);
				} catch (SQLException e1) {

					e1.printStackTrace();
				}
				return;
			}
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();

		}

	}

	public void createDatabaseOffline() {

		try (Connection conn = DriverManager.getConnection(url)) {
			if (conn != null) {
				@SuppressWarnings("unused")
				DatabaseMetaData meta = conn.getMetaData();
				Bukkit.getLogger().log(Level.INFO, "Using offline database.");

			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void createNewTableOffline() {
		// 1 = uuid , 2 = name , 3 = Cop Points (Double) 4= robber points (Double) ,5=
		// Games(int) , 6=lose(int)
		String sql = "CREATE TABLE " + table + "(LOCATION TEXT(1000))";

		try (Connection conn = DriverManager.getConnection(url); Statement stmt = conn.createStatement()) {

			stmt.execute(sql);

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}


	private void createDataBase() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			con = DriverManager.getConnection(url, username, password);
			Statement statment2 = con.createStatement();
			String sql = "CREATE DATABASE " + database;
			statment2.executeUpdate(sql);
			Bukkit.getLogger().log(Level.INFO, ChatColor.GREEN + "DataBase " + "'" + database + "' created!");
		} catch (ClassNotFoundException e1) {

			e1.printStackTrace();
		} catch (SQLException e) {
			if (e.getErrorCode() == 1007/* error code for database not found */) {
				Bukkit.getLogger().log(Level.INFO, ChatColor.GREEN + "DataBase" + " '" + database + "' aleardy exist!");
			} else {
				e.printStackTrace();
			}
		}

	}

	private void createTable1() {
		String myTableName = "CREATE TABLE " + table + "(LOCATION TEXT(1000))";
		try {
			Class.forName("com.mysql.jdbc.Driver");
			con = DriverManager.getConnection("jdbc:mysql://" + this.host + ":" + this.port + "/" + this.database,
					this.username, this.password);
			Statement statement = con.createStatement();
			statement.executeUpdate(myTableName);
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
	}


	public Connection getConnection() {
		if (plugin.getConfigManager().getConfig().getBoolean("Debug")) {
			if (con == null) {
				Bukkit.getLogger().log(Level.INFO, "The connection with the database don`t exist!");
			} else {
				Bukkit.getLogger().log(Level.INFO, "The connection with the database exist!");
			}
		}
		return con;
	}

	public MySQLGetSet getMySQLGetSet() {
		return MySQLGetSet;
	}

	public void setMySQLGetSet(MySQLGetSet mySQLGetSet) {
		MySQLGetSet = mySQLGetSet;
	}

}


