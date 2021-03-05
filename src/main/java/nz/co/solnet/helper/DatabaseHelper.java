package nz.co.solnet.helper;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class DatabaseHelper {

	// Hide the default constructor to prevent erroneous initialisation
	private DatabaseHelper() {

	}

	private static final String DATABASE_URL = "jdbc:derby:applicationdb;create=true";
	private static final Logger logger = LogManager.getLogger(DatabaseHelper.class);

	/**
	 * Create a derby database if it doesn't exist and insert seed data.
	 */
	public static void initialiseDB() {

		try (Connection conn = DriverManager.getConnection(DATABASE_URL)) {

			insertInitialData(conn);
		} catch (SQLException e) {
			logger.error("Error in inserting initial data", e);
		}
	}

	/**
	 * Insert sample seed data into the database.
	 * 
	 * @param conn
	 * @throws SQLException
	 */
	private static void insertInitialData(Connection conn) throws SQLException {

		try (Statement statement = conn.createStatement()) {

			if (!doesTableExist("tasks", conn)) {

				StringBuilder sqlCreateTable = new StringBuilder();
				sqlCreateTable.append("CREATE TABLE tasks (id int not null generated always as identity,");
				sqlCreateTable.append(" title varchar(256) not null,");
				sqlCreateTable.append(" description varchar(1024),");
				//sqlCreateTable.append(" due_date date,");
				sqlCreateTable.append(" status varchar(10),");
				//sqlCreateTable.append(" creation_date date not null,");
				sqlCreateTable.append(" primary key (id))");
				statement.execute(sqlCreateTable.toString());
				logger.info("Table created.");
			} else {
				logger.info("Table already exists");
			}
		}
	}

	/**
	 * Checks if the table exists in the database.
	 * 
	 * @param tableName - table name to be checked in the database
	 * @param conn      - database connection to use
	 * @return - boolean to indicate of the table exists or not
	 * @throws SQLException
	 */
	private static boolean doesTableExist(String tableName, Connection conn) throws SQLException {
		DatabaseMetaData meta = conn.getMetaData();
		ResultSet result = meta.getTables(null, null, tableName.toUpperCase(), null);

		return result.next();
	}

	/**
	 * Utility method to drop the table.
	 */
	public static void cleanDatabase() {

		try (Connection conn = DriverManager.getConnection(DATABASE_URL);
				Statement statement = conn.createStatement()) {
			String sql1 = "DROP TABLE tasks";
			statement.execute(sql1);
			logger.info("Table dropped successfully");
		} catch (SQLException e) {
			logger.error("Error in dropping table", e);
		}
	}

	/**
	 * This method does a graceful shutdown for the database.
	 */
	public static void cleanupDB() {

		try {
			DriverManager.getConnection("jdbc:derby:;shutdown=true");
		} catch (SQLException e) {

			if (e.getSQLState().equals("XJ015")) {
				logger.info("Database shutdown successfully");
			} else {
				logger.error("Error in database shutdown", e);
			}
		}
	}

	public static ResultSet getAllTasks() {
		try {
			Connection conn = DriverManager.getConnection(DATABASE_URL);
			Statement statement = conn.createStatement();

			if (doesTableExist("tasks", conn)) {
				String sql1 = "SELECT * FROM tasks";

				ResultSet r = statement.executeQuery(sql1);

				return r;
			}
		} catch (SQLException e) {
			logger.error("Error connecting to db", e);
		}
		return null;
	}

}
