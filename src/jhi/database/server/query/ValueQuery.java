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

package jhi.database.server.query;

import java.util.*;

import jhi.database.server.*;
import jhi.database.shared.exception.*;

/**
 * A {@link ValueQuery} can either be used to get a single value from the database or to execute a query (that is, a query without a result). This
 * class supports "fluent" code style.
 *
 * @author Sebastian Raubach
 */
public class ValueQuery extends BaseQuery<ValueQuery>
{
	/**
	 * Creates a new {@link ValueQuery} with the given query and the column to extract
	 *
	 * @param query The sql query
	 * @throws DatabaseException Thrown if the communication with the database fails
	 */
	public ValueQuery(String query) throws DatabaseException
	{
		super(query);
	}

	/**
	 * Executes the current query on the database. The returned result contains the generated ids (if any). Use {@link ValueQuery#run(String)} to run
	 * the query and get the result.
	 *
	 * @return The generated ids (if any)
	 * @throws DatabaseException Thrown if the communication with the database fails.
	 */
	public List<Long> execute() throws DatabaseException
	{
		List<Long> ids = stmt.execute();

		database.close();

		return ids;
	}

	/**
	 * Runs the current query on the database. The result can be requested by calling any of the <code>getDatatype</code> methods.
	 *
	 * @param column The column to extract
	 * @return this
	 * @throws DatabaseException Thrown if the communication with the database fails.
	 */
	public ExecutedValueQuery run(String column) throws DatabaseException
	{
		return new ExecutedValueQuery(column, database, stmt.query());
	}

	/**
	 * {@link ExecutedValueQuery} is a {@link ValueQuery} that has been executed (duh). It can only be used to retrieve information.
	 *
	 * @author Sebastian Raubach
	 */
	public static class ExecutedValueQuery
	{
		private String         column;
		private Database       database;
		private DatabaseResult rs;

		private ExecutedValueQuery(String column, Database database, DatabaseResult rs)
		{
			this.column = column;
			this.database = database;
			this.rs = rs;
		}

		/**
		 * Requests the String result value from the query
		 *
		 * @return The String result
		 * @throws DatabaseException Thrown if {@link ValueQuery#run(String)} wasn't called before calling this method or if there is no result
		 */
		public String getString() throws DatabaseException
		{
			checkResultSet();
			if (rs.next())
			{
				String value = rs.getString(column);
				database.close();
				return value;
			}
			else
			{
				database.close();
				return null;
			}
		}

		/**
		 * Requests the String result values from the query
		 *
		 * @return The String results
		 * @throws DatabaseException Thrown if {@link ValueQuery#run(String)} wasn't called before calling this method or if there is no result
		 */
		public List<String> getStrings() throws DatabaseException
		{
			checkResultSet();
			List<String> result = null;
			while (rs.next())
			{
				if (result == null)
					result = new ArrayList<>();

				result.add(rs.getString(column));
			}

			database.close();
			return result;
		}

		/**
		 * Requests the String result value from the query
		 *
		 * @param fallback This value will be returned if there is no result
		 * @return The String result
		 * @throws DatabaseException Thrown if {@link ValueQuery#run(String)} wasn't called before calling this method
		 */
		public String getString(String fallback) throws DatabaseException
		{
			checkResultSet();
			String result = rs.next() ? rs.getString(column) : fallback;
			database.close();
			return result;
		}

		/**
		 * Requests the int result value from the query
		 *
		 * @return The int result
		 * @throws DatabaseException Thrown if {@link ValueQuery#run(String)} wasn't called before calling this method or if there is no result
		 */
		public Integer getInt() throws DatabaseException
		{
			checkResultSet();
			if (rs.next())
			{
				int value = rs.getInt(column);
				database.close();
				return value;
			}
			else
			{
				database.close();
				return null;
			}
		}

		/**
		 * Requests the int result values from the query
		 *
		 * @return The int results
		 * @throws DatabaseException Thrown if {@link ValueQuery#run(String)} wasn't called before calling this method or if there is no result
		 */
		public List<Integer> getInts() throws DatabaseException
		{
			checkResultSet();
			List<Integer> result = null;
			while (rs.next())
			{
				if (result == null)
					result = new ArrayList<>();

				result.add(rs.getInt(column));
			}

			database.close();
			return result;
		}

		/**
		 * Requests the long result value from the query
		 *
		 * @return The long result
		 * @throws DatabaseException Thrown if {@link ValueQuery#run(String)} wasn't called before calling this method or if there is no result
		 */
		public Long getLong() throws DatabaseException
		{
			checkResultSet();
			if (rs.next())
			{
				long value = rs.getLong(column);
				database.close();
				return value;
			}
			else
			{
				database.close();
				return null;
			}
		}

		/**
		 * Requests the long result value from the query
		 *
		 * @param fallback This value will be returned if there is no result
		 * @return The long result
		 * @throws DatabaseException Thrown if {@link ValueQuery#run(String)} wasn't called before calling this method
		 */
		public Long getLong(long fallback) throws DatabaseException
		{
			checkResultSet();
			long result = rs.next() ? rs.getLong(column) : fallback;
			database.close();
			return result;

		}

		/**
		 * Requests the long result values from the query
		 *
		 * @return The long results
		 * @throws DatabaseException Thrown if {@link ValueQuery#run(String)} wasn't called before calling this method or if there is no result
		 */
		public List<Long> getLongs() throws DatabaseException
		{
			checkResultSet();
			List<Long> result = null;
			while (rs.next())
			{
				if (result == null)
					result = new ArrayList<>();

				result.add(rs.getLong(column));
			}

			database.close();
			return result;
		}

		/**
		 * Requests the int result value from the query
		 *
		 * @param fallback This value will be returned if there is no result
		 * @return The int result
		 * @throws DatabaseException Thrown if {@link ValueQuery#run(String)} wasn't called before calling this method
		 */
		public Integer getInt(int fallback) throws DatabaseException
		{
			checkResultSet();
			int result = rs.next() ? rs.getInt(column) : fallback;
			database.close();
			return result;

		}

		/**
		 * Requests the double result value from the query
		 *
		 * @return The double result
		 * @throws DatabaseException Thrown if {@link ValueQuery#run(String)} wasn't called before calling this method or if there is no result
		 */
		public Double getDouble() throws DatabaseException
		{
			checkResultSet();
			if (rs.next())
			{
				double value = rs.getDouble(column);
				database.close();
				return value;
			}
			else
			{
				database.close();
				return null;
			}
		}

		/**
		 * Requests the double result values from the query
		 *
		 * @return The double results
		 * @throws DatabaseException Thrown if {@link ValueQuery#run(String)} wasn't called before calling this method or if there is no result
		 */
		public List<Double> getDoubles() throws DatabaseException
		{
			checkResultSet();
			List<Double> result = null;
			while (rs.next())
			{
				if (result == null)
					result = new ArrayList<>();

				result.add(rs.getDouble(column));
			}

			database.close();
			return result;
		}

		/**
		 * Requests the double result value from the query
		 *
		 * @param fallback This value will be returned if there is no result
		 * @return The double result
		 * @throws DatabaseException Thrown if {@link ValueQuery#run(String)} wasn't called before calling this method
		 */
		public Double getDouble(double fallback) throws DatabaseException
		{
			checkResultSet();
			double result = rs.next() ? rs.getDouble(column) : fallback;
			database.close();
			return result;
		}

		/**
		 * Requests the double result values from the query
		 *
		 * @return The double results
		 * @throws DatabaseException Thrown if {@link ValueQuery#run(String)} wasn't called before calling this method or if there is no result
		 */
		public List<Float> getFloats() throws DatabaseException
		{
			checkResultSet();
			List<Float> result = null;
			while (rs.next())
			{
				if (result == null)
					result = new ArrayList<>();

				result.add(rs.getFloat(column));
			}

			database.close();
			return result;
		}

		/**
		 * Requests the double result value from the query
		 *
		 * @param fallback This value will be returned if there is no result
		 * @return The double result
		 * @throws DatabaseException Thrown if {@link ValueQuery#run(String)} wasn't called before calling this method
		 */
		public Float getFloat(float fallback) throws DatabaseException
		{
			checkResultSet();
			float result = rs.next() ? rs.getFloat(column) : fallback;
			database.close();
			return result;
		}

		/**
		 * Requests the boolean result value from the query
		 *
		 * @return The boolean result
		 * @throws DatabaseException Thrown if {@link ValueQuery#run(String)} wasn't called before calling this method or if there is no result
		 */
		public Boolean getBoolean() throws DatabaseException
		{
			checkResultSet();
			if (rs.next())
			{
				boolean value = rs.getBoolean(column);
				database.close();
				return value;
			}
			else
			{
				database.close();
				return null;
			}
		}

		/**
		 * Requests the boolean result values from the query
		 *
		 * @return The boolean results
		 * @throws DatabaseException Thrown if {@link ValueQuery#run(String)} wasn't called before calling this method or if there is no result
		 */
		public List<Boolean> getBooleans() throws DatabaseException
		{
			checkResultSet();
			List<Boolean> result = null;
			while (rs.next())
			{
				if (result == null)
					result = new ArrayList<>();

				result.add(rs.getBoolean(column));
			}

			database.close();
			return result;
		}

		/**
		 * Requests the boolean result value from the query
		 *
		 * @param fallback This value will be returned if there is no result
		 * @return The boolean result
		 * @throws DatabaseException Thrown if {@link ValueQuery#run(String)} wasn't called before calling this method
		 */
		public Boolean getBoolean(boolean fallback) throws DatabaseException
		{
			checkResultSet();
			boolean result = rs.next() ? rs.getBoolean(column) : fallback;
			database.close();
			return result;
		}

		/**
		 * Returns an array of all the column names in the {@link java.sql.ResultSet}
		 *
		 * @return An array of all the column names in the {@link java.sql.ResultSet}
		 * @throws DatabaseException if a database access error occurs
		 */
		public List<String> getColumnNames() throws DatabaseException
		{
			return Arrays.asList(rs.getColumnNames());
		}

		/**
		 * Checks if the {@link java.sql.ResultSet} is not <code>null</code>.
		 *
		 * @throws DatabaseException Thrown if the {@link java.sql.ResultSet} is <code>null</code>.
		 */
		private void checkResultSet() throws DatabaseException
		{
			if (rs == null)
			{
				database.close();
				throw new DatabaseException("You need to run the query before requesting result values!");
			}
		}

		/**
		 * Returns true if there is a result (Note that you cannot get the result after this is called. It's simply a method that you call when you
		 * want to know IF there is a result, not if you want to know WHAT the result is.
		 *
		 * @return True if there is a result
		 * @throws DatabaseException Thrown if {@link ValueQuery#run(String)} wasn't called before calling this method
		 */
		public boolean hasNext() throws DatabaseException
		{
			checkResultSet();
			return rs.next();
		}
	}
}
