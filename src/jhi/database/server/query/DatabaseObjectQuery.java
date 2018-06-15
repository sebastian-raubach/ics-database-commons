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
import jhi.database.server.parser.*;
import jhi.database.shared.exception.*;
import jhi.database.shared.util.*;

/**
 * @author Sebastian Raubach
 */
public class DatabaseObjectQuery<T extends DatabaseObject> extends BaseQuery<DatabaseObjectQuery<T>>
{
	private static final String CALC_ROWS = "SELECT SQL_CALC_FOUND_ROWS ";

	private Long previousCount = -1L;

	public DatabaseObjectQuery(String query) throws DatabaseException
	{
		super(query);
	}

	public DatabaseObjectQuery(Database database, String query) throws DatabaseException
	{
		super(database, query);
	}

	public DatabaseObjectQuery<T> setFetchesCount(Long previousCount) throws DatabaseException
	{
		this.previousCount = previousCount;

		if (previousCount == null && !query.contains(CALC_ROWS))
		{
			query = query.replaceFirst("SELECT ", CALC_ROWS);

			this.stmt = database.prepareStatement(query);
		}

		return this;
	}

	/**
	 * Runs the current query on the database. The result can be requested by calling any of the <code>getDatatype</code> methods.
	 *
	 * @return this
	 * @throws DatabaseException Thrown if the communication with the database fails.
	 */
	public ExecutedDatabaseObjectQuery run() throws DatabaseException
	{
		return new ExecutedDatabaseObjectQuery(database, stmt.query(), previousCount);
	}

	public DatabaseObjectStreamer<T> getStreamer(DatabaseObjectParser<T> parser) throws DatabaseException
	{
		return new DatabaseObjectStreamer<>(database, stmt, parser);
	}

	public static class DatabaseObjectStreamer<U extends DatabaseObject> extends BaseStreamer<U>
	{
		private DatabaseObjectParser<U> parser;

		DatabaseObjectStreamer(Database database, DatabaseStatement stmt, DatabaseObjectParser<U> parser) throws DatabaseException
		{
			super(database, stmt);

			this.parser = parser;
		}

		@Override
		protected U getNext(DatabaseResult row) throws DatabaseException
		{
			return parser.parse(row, false); // TODO: Use second parameter?
		}
	}

	public class ExecutedDatabaseObjectQuery
	{
		private Database       database;
		private DatabaseResult rs;
		private Long previousCount = null;

		public ExecutedDatabaseObjectQuery(Database database, DatabaseResult rs, Long previousCount)
		{
			this.database = database;
			this.rs = rs;
			this.previousCount = previousCount;
		}

		public T getObject(DatabaseObjectParser<T> parser) throws DatabaseException
		{
			return getObject(parser, false);
		}

		public T getObject(DatabaseObjectParser<T> parser, boolean foreignsFromResultSet) throws DatabaseException
		{
			checkResultSet();
			if (rs.next())
			{
				T obj = parser.parse(rs, foreignsFromResultSet);
				database.close();
				return obj;
			}
			else
			{
				database.close();
				return null;
			}
		}

		public List<T> getObjects(DatabaseObjectParser<T> parser) throws DatabaseException
		{
			return getObjects(parser, false);
		}

		public List<T> getObjects(DatabaseObjectParser<T> parser, boolean foreignsFromResultSet) throws DatabaseException
		{
			checkResultSet();
			List<T> result = null;

			parser.clearCache();

			while (rs.next())
			{
				if (result == null)
					result = new ArrayList<>();

				T object = parser.parse(rs, foreignsFromResultSet);

				if (object != null)
					result.add(object);
			}

			parser.clearCache();

			database.close();
			return result;
		}

		public PaginatedResult<List<T>> getObjectsPaginated(DatabaseObjectParser<T> parser, boolean foreignsFromResultSet) throws DatabaseException
		{
			checkResultSet();
			List<T> result = null;

			parser.clearCache();

			while (rs.next())
			{
				if (result == null)
					result = new ArrayList<>();

				T object = parser.parse(rs, foreignsFromResultSet);

				if (object != null)
					result.add(object);
			}

			parser.clearCache();

			Long count = previousCount == null ? stmt.getCount() : previousCount;

			database.close();
			return new PaginatedResult<>(count, result);
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
