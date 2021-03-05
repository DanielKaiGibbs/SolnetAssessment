package nz.co.solnet.helper;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.Optional;

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
				sqlCreateTable.append(" due_date date,");
				sqlCreateTable.append(" status varchar(10),");
				sqlCreateTable.append(" creation_date date not null,");
				sqlCreateTable.append(" primary key (id))");
				statement.execute(sqlCreateTable.toString());
				logger.info("Table created.");

				StringBuilder sqlInsertInitialData = new StringBuilder();
				sqlInsertInitialData.append("INSERT INTO tasks (");
				sqlInsertInitialData.append("title, description, status, due_date, creation_date)");
				sqlInsertInitialData.append("VALUES");
				sqlInsertInitialData.append("('Dinner', 'Cook up something yum', 'todo', '1992-04-16', '1992-04-16'),");
				sqlInsertInitialData.append("('Breakfast', 'Cook up something yum', 'complete', '1992-04-16', '1992-04-16')");
				statement.execute(sqlInsertInitialData.toString());

				logger.info("Init data inserted.");
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

	public static ResultSet queryDatabase(String query) {
		try {
			Connection conn = DriverManager.getConnection(DATABASE_URL);
			Statement statement = conn.createStatement();

			if (doesTableExist("tasks", conn)) {
				ResultSet rs = statement.executeQuery(query);
				return rs;
			} else {
				logger.error("Cannot access database");
			}
		} catch (SQLException e) {
			logger.error("Error connecting to db", e);
		}
		return null;
	}

}
