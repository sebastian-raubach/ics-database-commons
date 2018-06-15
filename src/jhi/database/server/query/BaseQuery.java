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

import java.io.*;
import java.sql.*;
import java.util.*;
import java.util.Date;

import jhi.database.server.*;
import jhi.database.shared.exception.*;

/**
 * {@link BaseQuery} is an abstract class containing all the base methods to build a query. <p/> <b>IMPORTANT</b>: When subclassing this class, make
 * sure to do it in this fashion: <p/> <code> class YourClass extends GerminateQuery&lt;YourClass&gt; </code> <p/> Otherwise you WILL end up with a
 * {@link ClassCastException}. <p/> Thanks to <a href="http://stackoverflow.com/a/14105328/1740724">this</a> answer on StackOverflow for the idea.
 *
 * @param <T> The type of the extending subclass
 * @author Sebastian Raubach
 */
public abstract class BaseQuery<T extends BaseQuery<?>>
{
	protected String query;
	protected DatabaseStatement stmt;
	protected int i = 1;
	protected Database database;

	/**
	 * Creates a new {@link BaseQuery} with the given query. Does NOT check the session id for validity.
	 *
	 * @param query The sql query
	 * @throws DatabaseException Thrown if the communication with the database fails
	 */
	protected BaseQuery(String query) throws DatabaseException
	{
		this(null, query);
	}

	protected BaseQuery(Database database, String query) throws DatabaseException
	{
		this.database = database;
		this.query = query;
		init(query);
	}

	/**
	 * Initializes the {@link Database}
	 *
	 * @param query The sql query
	 * @throws DatabaseException Thrown if the communication with the database fails
	 */
	private void init(String query) throws DatabaseException
	{
		if(database == null)
			database = Database.connect();
		this.stmt = database.prepareStatement(query);
	}

	/**
	 * Replaces the next placeholder with the given value
	 *
	 * @param value The int value to set
	 * @return this
	 * @throws DatabaseException Thrown if the query fails with a {@link SQLException}
	 */
	@SuppressWarnings("unchecked")
	public T setInt(int value) throws DatabaseException
	{
		stmt.setInt(i++, value);
		return (T) this;
	}

	/**
	 * Replaces the next placeholders with the given values
	 *
	 * @param values The int values to set
	 * @return this
	 * @throws DatabaseException Thrown if the query fails with a {@link SQLException}
	 */
	@SuppressWarnings("unchecked")
	public T setInts(Collection<Integer> values) throws DatabaseException
	{
		for (Integer value : values)
			stmt.setInt(i++, value);
		return (T) this;
	}

	/**
	 * Replaces the next placeholders with the given values
	 *
	 * @param values The int values to set
	 * @return this
	 * @throws DatabaseException Thrown if the query fails with a {@link SQLException}
	 */
	@SuppressWarnings("unchecked")
	public T setInts(Integer[] values) throws DatabaseException
	{
		for (Integer value : values)
			stmt.setInt(i++, value);
		return (T) this;
	}

	/**
	 * Replaces the next placeholder with the given value
	 *
	 * @param value The date value to set
	 * @return this
	 * @throws DatabaseException Thrown if the query fails with a {@link SQLException}
	 */
	@SuppressWarnings("unchecked")
	public T setTimestamp(Date value) throws DatabaseException
	{
		stmt.setTimestamp(i++, value);
		return (T) this;
	}

	/**
	 * Replaces the next placeholders with the given values
	 *
	 * @param values The int values to set
	 * @return this
	 * @throws DatabaseException Thrown if the query fails with a {@link SQLException}
	 */
	@SuppressWarnings("unchecked")
	public T setTimestamps(Collection<Date> values) throws DatabaseException
	{
		for (Date value : values)
			stmt.setTimestamp(i++, value);
		return (T) this;
	}

	/**
	 * Replaces the next placeholder with the given value
	 *
	 * @param value The date value to set
	 * @return this
	 * @throws DatabaseException Thrown if the query fails with a {@link SQLException}
	 */
	public T setDate(Date value) throws DatabaseException
	{
		stmt.setDate(i++, value);
		return (T) this;
	}

	/**
	 * Replaces the next placeholders with the given values
	 *
	 * @param values The int values to set
	 * @return this
	 * @throws DatabaseException Thrown if the query fails with a {@link SQLException}
	 */
	@SuppressWarnings("unchecked")
	public T setTimestamps(Date[] values) throws DatabaseException
	{
		for (Date value : values)
			stmt.setTimestamp(i++, value);
		return (T) this;
	}

	/**
	 * Replaces the next placeholder with the given value
	 *
	 * @param value The long value to set
	 * @return this
	 * @throws DatabaseException Thrown if the query fails with a {@link SQLException}
	 */
	@SuppressWarnings("unchecked")
	public T setLong(long value) throws DatabaseException
	{
		stmt.setLong(i++, value);
		return (T) this;
	}

	/**
	 * Replaces the next placeholders with the given values
	 *
	 * @param values The int values to set
	 * @return this
	 * @throws DatabaseException Thrown if the query fails with a {@link SQLException}
	 */
	@SuppressWarnings("unchecked")
	public T setLongs(Collection<Long> values) throws DatabaseException
	{
		for (Long value : values)
			stmt.setLong(i++, value);
		return (T) this;
	}

	/**
	 * Replaces the next placeholders with the given values
	 *
	 * @param values The int values to set
	 * @return this
	 * @throws DatabaseException Thrown if the query fails with a {@link SQLException}
	 */
	@SuppressWarnings("unchecked")
	public T setLongs(Long[] values) throws DatabaseException
	{
		for (Long value : values)
			stmt.setLong(i++, value);
		return (T) this;
	}

	/**
	 * Replaces the next placeholder with the given value
	 *
	 * @param value The String value to set
	 * @return this
	 * @throws DatabaseException Thrown if the query fails with a {@link SQLException}
	 */
	@SuppressWarnings("unchecked")
	public T setString(String value) throws DatabaseException
	{
		stmt.setString(i++, value);
		return (T) this;
	}

	/**
	 * Replaces the next placeholders with the given values
	 *
	 * @param values The String values to set
	 * @return this
	 * @throws DatabaseException Thrown if the query fails with a {@link SQLException}
	 */
	@SuppressWarnings("unchecked")
	public T setStrings(Collection<String> values) throws DatabaseException
	{
		for (String value : values)
			stmt.setString(i++, value);
		return (T) this;
	}

	/**
	 * Replaces the next placeholders with the given values
	 *
	 * @param values The String values to set
	 * @return this
	 * @throws DatabaseException Thrown if the query fails with a {@link SQLException}
	 */
	@SuppressWarnings("unchecked")
	public T setStrings(String[] values) throws DatabaseException
	{
		for (String value : values)
			stmt.setString(i++, value);
		return (T) this;
	}

	/**
	 * Replaces the next placeholder with the given value
	 *
	 * @param value The double value to set
	 * @return this
	 * @throws DatabaseException Thrown if the query fails with a {@link SQLException}
	 */
	@SuppressWarnings("unchecked")
	public T setDouble(double value) throws DatabaseException
	{
		stmt.setDouble(i++, value);
		return (T) this;
	}

	/**
	 * Replaces the next placeholders with the given values
	 *
	 * @param values The Double values to set
	 * @return this
	 * @throws DatabaseException Thrown if the query fails with a {@link SQLException}
	 */
	@SuppressWarnings("unchecked")
	public T setDoubles(Collection<Double> values) throws DatabaseException
	{
		for (Double value : values)
			stmt.setDouble(i++, value);
		return (T) this;
	}

	/**
	 * Replaces the next placeholders with the given values
	 *
	 * @param values The Double values to set
	 * @return this
	 * @throws DatabaseException Thrown if the query fails with a {@link SQLException}
	 */
	@SuppressWarnings("unchecked")
	public T setDoubles(Double[] values) throws DatabaseException
	{
		for (Double value : values)
			stmt.setDouble(i++, value);
		return (T) this;
	}

	/**
	 * Replaces the next placeholder with the given value
	 *
	 * @param value The boolean value to set
	 * @return this
	 * @throws DatabaseException Thrown if the query fails with a {@link SQLException}
	 */
	@SuppressWarnings("unchecked")
	public T setBoolean(boolean value) throws DatabaseException
	{
		stmt.setBoolean(i++, value);
		return (T) this;
	}

	/**
	 * Replaces the next placeholders with the given values
	 *
	 * @param values The Boolean values to set
	 * @return this
	 * @throws DatabaseException Thrown if the query fails with a {@link SQLException}
	 */
	@SuppressWarnings("unchecked")
	public T setBooleans(Collection<Boolean> values) throws DatabaseException
	{
		for (Boolean value : values)
			stmt.setBoolean(i++, value);
		return (T) this;
	}

	/**
	 * Replaces the next placeholders with the given values
	 *
	 * @param values The Boolean values to set
	 * @return this
	 * @throws DatabaseException Thrown if the query fails with a {@link SQLException}
	 */
	@SuppressWarnings("unchecked")
	public T setBooleans(Boolean[] values) throws DatabaseException
	{
		for (Boolean value : values)
			stmt.setBoolean(i++, value);
		return (T) this;
	}

	/**
	 * Sets the next placeholder to null of the given {@link Types} instance.
	 *
	 * @param type The {@link Types} instance
	 * @return this
	 * @throws DatabaseException Thrown if the query fails with a {@link SQLException}
	 */
	@SuppressWarnings("unchecked")
	public T setNull(int type) throws DatabaseException
	{
		stmt.setNull(i++, type);
		return (T) this;
	}

	/**
	 * Sets the next placeholders to null of the given {@link Types} instances.
	 *
	 * @param types The {@link Types} instances
	 * @return this
	 * @throws DatabaseException Thrown if the query fails with a {@link SQLException}
	 */
	@SuppressWarnings("unchecked")
	public T setNull(Collection<Integer> types) throws DatabaseException
	{
		for (Integer type : types)
			stmt.setNull(i++, type);
		return (T) this;
	}

	/**
	 * Sets the next placeholders to null of the given {@link Types} instances.
	 *
	 * @param types The {@link Types} instances
	 * @return this
	 * @throws DatabaseException Thrown if the query fails with a {@link SQLException}
	 */
	@SuppressWarnings("unchecked")
	public T setNull(Integer[] types) throws DatabaseException
	{
		for (Integer type : types)
			stmt.setNull(i++, type);
		return (T) this;
	}

	@SuppressWarnings("unchecked")
	public T printTo(PrintStream out)
	{
		out.println(getStringRepresentation());
		return (T) this;
	}

	public void closeDatabase()
	{
		database.close();
	}

	/**
	 * Returns the {@link String} representation of the {@link DatabaseStatement}. Depending on the database, this will either return the actual built
	 * query or just some other string representation. <p/> MySQL returns the actual query.
	 *
	 * @return The string representation of the contained {@link DatabaseStatement}
	 */
	public String getStringRepresentation()
	{
		return stmt.getStringRepresentation();
	}
}
