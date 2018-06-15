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

import jhi.database.server.*;
import jhi.database.shared.exception.*;
import jhi.database.shared.util.*;

/**
 * {@link BaseStreamer} is a utility class allowing to "stream" {@link DatabaseResult}s, i.e., this class will return one {@link DatabaseObject} at a
 * time.
 *
 * @author Sebastian Raubach
 */
public abstract class BaseStreamer<T extends DatabaseObject>
{
	private Database       database;
	private DatabaseResult res;

	public BaseStreamer(Database database, DatabaseStatement stmt) throws DatabaseException
	{
		this.database = database;

        /* Run the query */
//		stmt.setFetchSize(Integer.MIN_VALUE);
		res = stmt.query();
	}

	public DatabaseResult getDatabaseResult()
	{
		return res;
	}

	/**
	 * Returns the next T or <code>null</code> if there is no next item
	 *
	 * @return The next T or <code>null</code> if there is no next item
	 * @throws DatabaseException Thrown if either {@link DatabaseResult#next()} or {@link DatabaseResult#getString(String)} fails
	 */
	public T next() throws DatabaseException
	{
		if (!database.isClosed())
		{
			if (res.next())
			{
				return getNext(res);
			}
			else
			{
				/* Else close the database connection */
				database.close();
			}
		}

		return null;
	}

	protected abstract T getNext(DatabaseResult row) throws DatabaseException;

	/**
	 * Closes the database
	 */
	public void close()
	{
		if (!database.isClosed())
			database.close();
	}
}
