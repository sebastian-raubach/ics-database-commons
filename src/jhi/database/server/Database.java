/*
 *  Copyright 2018 Information and Computational Sciences,
 *  The James Hutton Institute.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package jhi.database.server;

import java.sql.*;

import jhi.database.shared.exception.*;

/**
 * {@link Database} is a basic implementation of a database connection.
 *
 * @author Sebastian Raubach
 */
public final class Database implements AutoCloseable
{
	private static String dbPath;
	private static String username;
	private static String password;
	private static DatabaseType dbType = DatabaseType.MYSQL;
	private Connection connection;

	private Database()
	{
	}

	public static void init(String dbPath, String username, String password, DatabaseType dbType)
	{
		init(dbPath, username, password);
		Database.dbType = dbType;
	}

	public static void init(String dbPath, String username, String password)
	{
		Database.dbPath = dbPath;
		Database.username = username;
		Database.password = password;
	}

	/**
	 * Connects to the given MySQL database with specified credentials
	 *
	 * @return The {@link Database} instance
	 * @throws DatabaseException Thrown if any kind of {@link Exception} is thrown while trying to connect. The {@link DatabaseException} will contain
	 *                           the message of the original {@link Exception}.
	 */
	public static Database connect() throws DatabaseException
	{
		return connect(dbType, dbPath, username, password);
	}

	/**
	 * Connects to the given MySQL database with specified credentials
	 *
	 * @return The {@link Database} instance
	 * @throws DatabaseException Thrown if any kind of {@link Exception} is thrown while trying to connect. The {@link DatabaseException} will contain
	 *                           the message of the original {@link Exception}.
	 */
	public static Database connect(DatabaseType dbType, String dbPath, String username, String password) throws DatabaseException
	{
		if (dbPath == null || username == null || password == null || "".equals(dbPath) || "".equals(username))
			throw new DatabaseException("Please call Database.init() before connecting to the database");

		Database database = new Database();

        /* Connect to the database */
		try
		{
			Class.forName(dbType.classForName).newInstance();
			database.connection = DriverManager.getConnection(dbType.connectionString + dbPath, username, password);
		}
		catch (InstantiationException | IllegalAccessException | ClassNotFoundException e)
		{
			throw new DatabaseException("Unable to instantiate MySQL connection: " + e.getLocalizedMessage());
		}
		catch (SQLException e)
		{
			throw new DatabaseException(e);
		}

		return database;
	}

	public static void testConnect(String dbPath, String username, String password) throws DatabaseException
	{
		String origDbPath = Database.dbPath;
		String origUsername = Database.username;
		String origPassword = Database.password;

		Database.dbPath = dbPath;
		Database.username = username;
		Database.password = password;

		try
		{
			Database database = Database.connect();
			database.close();
		}
		catch (DatabaseException e)
		{
			Database.dbPath = origDbPath;
			Database.username = origUsername;
			Database.password = origPassword;

			throw e;
		}
	}

	/**
	 * Creates and returns a prepared DatabaseStatement
	 *
	 * @param sql The String representation of the sql query
	 * @return The Prepared Statement
	 * @throws DatabaseException Thrown if any kind of {@link Exception} is thrown while trying to connect. The {@link DatabaseException} will contain
	 *                           the message of the original {@link Exception}.
	 */
	public final DatabaseStatement prepareStatement(String sql) throws DatabaseException
	{
		try
		{
			PreparedStatement stmt = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
			// stmt.setQueryTimeout(120);
			return new DatabaseStatement(stmt, this);
		}
		catch (SQLException e)
		{
			throw new DatabaseException(e);
		}
	}

	/**
	 * Closes the connection to the database
	 */
	@Override
	public final void close()
	{
		try
		{
			if (connection != null)
				connection.close();
			connection = null;
		}
		catch (SQLException e)
		{
			/* Do nothing here! */
		}
	}

	/**
	 * Checks if the {@link Database} is closed
	 *
	 * @return <code>true</code> if the {@link Database} has already been closed.
	 */
	public final boolean isClosed()
	{
		try
		{
			return connection == null || connection.isClosed();
		}
		catch (SQLException e)
		{
			return true;
		}
	}

	/**
	 * The set of database types supported by this library
	 *
	 * @author Sebastian Raubach
	 */
	public enum DatabaseType
	{
		MYSQL("com.mysql.jdbc.Driver", "jdbc:mysql://"),
		POSTGRES("org.postgresql.Driver", "jdbc:postgresql://"),
		SQLSERVER("com.microsoft.sqlserver.jdbc.SQLServerDriver", "jdbc:sqlserver://");

		private String classForName;
		private String connectionString;

		DatabaseType(String classForName, String connectionString)
		{
			this.classForName = classForName;
			this.connectionString = connectionString;
		}

		public String getClassForName()
		{
			return classForName;
		}

		public String getConnectionString()
		{
			return connectionString;
		}
	}
}
