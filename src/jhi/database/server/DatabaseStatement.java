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
import java.util.*;
import java.util.Date;

import jhi.database.shared.exception.*;

/**
 * {@link DatabaseStatement} is a wrapper class for {@link PreparedStatement}.
 *
 * @author Sebastian Raubach
 */
public final class DatabaseStatement
{
	private final PreparedStatement stmt;
	private       Database          database;
	private boolean autoCloseConnection = false;

	/**
	 * Creates a new instance of {@link DatabaseStatement} wrapping the {@link PreparedStatement} and the {@link Database}
	 *
	 * @param stmt     The {@link PreparedStatement} holding the actual handle
	 * @param database The {@link Database} used to create the {@link PreparedStatement}
	 */
	public DatabaseStatement(PreparedStatement stmt, Database database)
	{
		this.stmt = stmt;
		this.database = database;
	}

	@SuppressWarnings("unused")
	public void setAutoCloseConnection(boolean autoCloseConnection)
	{
		this.autoCloseConnection = autoCloseConnection;
	}

	/**
	 * Sets the designated parameter to the given Java int value. The driver converts this to an SQL <code>INTEGER</code> value when it sends it to
	 * the database.
	 *
	 * @param parameterIndex the first parameter is 1, the second is 2, ...
	 * @param val            the parameter value
	 * @throws DatabaseException Thrown if the query fails with a {@link SQLException}
	 * @see PreparedStatement#setInt(int, int)
	 */
	public void setInt(int parameterIndex, int val) throws DatabaseException
	{
		try
		{
			this.stmt.setInt(parameterIndex, val);
		}
		catch (SQLException e)
		{
			if (autoCloseConnection)
				database.close();
			throw new DatabaseException(e);
		}
	}

	/**
	 * Sets the designated parameter to the given Java long value. The driver converts this to an SQL <code>LONG</code> value when it sends it to the
	 * database.
	 *
	 * @param parameterIndex the first parameter is 1, the second is 2, ...
	 * @param val            the parameter value
	 * @throws DatabaseException Thrown if the query fails with a {@link SQLException}
	 * @see PreparedStatement#setInt(int, int)
	 */
	public void setLong(int parameterIndex, long val) throws DatabaseException
	{
		try
		{
			this.stmt.setLong(parameterIndex, val);
		}
		catch (SQLException e)
		{
			if (autoCloseConnection)
				database.close();
			throw new DatabaseException(e);
		}
	}

	/**
	 * Sets the designated parameter to SQL <code>NULL</code>.
	 *
	 * @param parameterIndex the first parameter is 1, the second is 2, ...
	 * @param type           The {@link Types}
	 * @throws DatabaseException Thrown if the query fails with a {@link SQLException}
	 * @see PreparedStatement#setNull(int, int)
	 */
	public void setNull(int parameterIndex, int type) throws DatabaseException
	{
		try
		{
			stmt.setNull(parameterIndex, type);
		}
		catch (SQLException e)
		{
			if (autoCloseConnection)
				database.close();
			throw new DatabaseException(e);
		}
	}

	/**
	 * Sets the designated parameter to the given Java String value. The driver converts this to an SQL <code>VARCHAR</code> or
	 * <code>LONGVARCHAR</code> value (depending on the argument's size relative to the driver's limits on <code>VARCHAR</code> values) when it sends
	 * it to the database.
	 *
	 * @param parameterIndex the first parameter is 1, the second is 2, ...
	 * @param val            the parameter value
	 * @throws DatabaseException Thrown if the query fails with a {@link SQLException}
	 * @see PreparedStatement#setString(int, String)
	 */
	public void setString(int parameterIndex, String val) throws DatabaseException
	{
		try
		{
			this.stmt.setString(parameterIndex, val);
		}
		catch (SQLException e)
		{
			if (autoCloseConnection)
				database.close();
			throw new DatabaseException(e);
		}
	}

	/**
	 * Sets the designated parameter to the given Java double value. The driver converts this to an SQL <code>DOUBLE</code> value when it sends it to
	 * the database.
	 *
	 * @param parameterIndex the first parameter is 1, the second is 2, ...
	 * @param val            the parameter value
	 * @throws DatabaseException Thrown if the query fails with a {@link SQLException}
	 * @see PreparedStatement#setDouble(int, double)
	 */
	public void setDouble(int parameterIndex, double val) throws DatabaseException
	{
		try
		{
			this.stmt.setDouble(parameterIndex, val);
		}
		catch (SQLException e)
		{
			if (autoCloseConnection)
				database.close();
			throw new DatabaseException(e);
		}
	}

	/**
	 * Sets the designated parameter to the given Java double value. The driver converts this to an SQL <code>DOUBLE</code> value when it sends it to
	 * the database.
	 *
	 * @param parameterIndex the first parameter is 1, the second is 2, ...
	 * @param val            the parameter value
	 * @throws DatabaseException Thrown if the query fails with a {@link SQLException}
	 * @see PreparedStatement#setBoolean(int, boolean)
	 */
	public void setBoolean(int parameterIndex, Boolean val) throws DatabaseException
	{
		try
		{
			this.stmt.setBoolean(parameterIndex, val.booleanValue());
		}
		catch (SQLException e)
		{
			if (autoCloseConnection)
				database.close();
			throw new DatabaseException(e);
		}
	}

	public void setTimestamp(int parameterIndex, Date date) throws DatabaseException
	{
		try
		{
			this.stmt.setTimestamp(parameterIndex, new Timestamp(date.getTime()));
		}
		catch (SQLException e)
		{
			if (autoCloseConnection)
				database.close();
			throw new RuntimeException(e);
		}
	}

	/**
	 * Sets the designated parameter to the given {@link Date} value using the default time zone of the virtual machine that is running the
	 * application. The driver converts this to an SQL <code>DATE</code> value when it sends it to the database.
	 *
	 * @param parameterIndex the first parameter is 1, the second is 2, ...
	 * @param val            the parameter value
	 * @see PreparedStatement#setDate(int, java.sql.Date)
	 */
	public void setDate(int parameterIndex, Date val) throws DatabaseException
	{
		try
		{
			this.stmt.setDate(parameterIndex, new java.sql.Date(val.getTime()));
		}
		catch (SQLException e)
		{
			if (autoCloseConnection)
				database.close();
			throw new RuntimeException(e);
		}
	}

	/**
	 * Executes the SQL query in this {@link DatabaseStatement} object and returns the {@link DatabaseResult} object generated by the query.
	 *
	 * @return a {@link DatabaseResult} object that contains the data produced by the query; never null
	 * @throws DatabaseException Thrown if the query fails with a {@link SQLException}
	 * @see PreparedStatement#executeQuery()
	 */
	public DatabaseResult query() throws DatabaseException
	{
		try
		{
			return new DatabaseResult(this.stmt.executeQuery(), database);
		}
		catch (SQLException e)
		{
			if (autoCloseConnection)
				database.close();

			throw new DatabaseException(e);
		}
	}

	/**
	 * Executes the SQL statement in this PreparedStatement object, which may be any kind of SQL statement. Some prepared statements return multiple
	 * results; the execute method handles these complex statements as well as the simpler form of statements handled by the methods executeQuery and
	 * executeUpdate. The execute method returns a boolean to indicate the form of the first result. You must call either the method getResultSet or
	 * getUpdateCount to retrieve the result; you must call getMoreResults to move to any subsequent result(s).
	 *
	 * @return The generated ids (if any)
	 * @throws DatabaseException Thrown if the query fails with a {@link SQLException}
	 * @see PreparedStatement#execute()
	 */
	public List<Long> execute() throws DatabaseException
	{
		try
		{
			this.stmt.execute();

			List<Long> ids = new ArrayList<>();

			try (ResultSet generatedKeys = stmt.getGeneratedKeys())
			{
				while (generatedKeys.next())
				{
					ids.add(generatedKeys.getLong(1));
				}
			}

			this.stmt.close();

			return ids;
		}
		catch (SQLException e)
		{
			if (autoCloseConnection)
				database.close();
			throw new DatabaseException(e);
		}
	}

	/**
	 * Executes the SQL statement in this PreparedStatement object, which must be an SQL Data Manipulation Language (DML) statement, such as INSERT,
	 * UPDATE or DELETE; or an SQL statement that returns nothing, such as a DDL statement.
	 *
	 * @return either (1) the row count for SQL Data Manipulation Language (DML) statements or (2) 0 for SQL statements that return nothing
	 * @throws DatabaseException Thrown if the query fails with a {@link SQLException}
	 * @see PreparedStatement#executeUpdate()
	 */
	public int executeUpdate() throws DatabaseException
	{
		try
		{
			int result = this.stmt.executeUpdate();
			this.stmt.close();
			return result;
		}
		catch (SQLException e)
		{
			if (autoCloseConnection)
				database.close();
			throw new DatabaseException(e.getLocalizedMessage());
		}
	}

	/**
	 * Returns the {@link String} representation of this {@link DatabaseStatement}. If used with MySQL, this will return a {@link String} where all
	 * the placeholders have been replaced.
	 *
	 * @return The {@link String} representation of this {@link DatabaseStatement}
	 */
	public String getStringRepresentation()
	{
		// return stmt.toString();
		return stmt.toString().replaceAll("^[a-zA-Z0-9\\.]+@[a-zA-Z0-9]{1,8}:\\s", "");
	}

	/**
	 * Gives the JDBC driver a hint as to the number of rows that should be fetched from the database when more rows are needed for {@link
	 * DatabaseResult} objects generated by this {@link DatabaseStatement}. If the value specified is zero, then the hint is ignored. The default
	 * value is zero.
	 *
	 * @param fetchSize the number of rows to fetch
	 * @throws DatabaseException Thrown if the setting fails on the database
	 */
	public void setFetchSize(int fetchSize) throws DatabaseException
	{
		try
		{
			stmt.setFetchSize(fetchSize);
		}
		catch (SQLException e)
		{
			throw new DatabaseException(e);
		}
	}

	public Long getCount()
	{
		long totalCount = 0;

		try
		{
			ResultSet rs = stmt.executeQuery("SELECT FOUND_ROWS()");
			if (rs.next())
				totalCount = rs.getLong(1);
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}

		return totalCount;
	}
}
