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

package jhi.database.server.util;

import java.util.*;
import java.util.concurrent.*;

import jhi.database.server.*;
import jhi.database.shared.exception.*;
import jhi.database.shared.util.*;

/**
 * DatabaseObjectCache is a utility class that can be used to retrieve and cache database entries. In more detail, this class will do: <ul> <li>When
 * {@link #get(Long, DatabaseResult, boolean)} is called, check if the DatabaseObject exists in the cache</li> <li>Use the appropriate manager (handled in sub-class) to get
 * the DatabaseObject from the database if it doesn't exist in the cache and then cache it</li> </ul> Calling {@link #clear()} will clear the cache.
 * This is particularly advisable if one wants to prevent long term caching and returning potentially outdated data.
 *
 * @author Sebastian Raubach
 */
public abstract class DatabaseObjectCache<T extends DatabaseObject>
{
	protected final Class clazz;
	private Map<Long, T> CACHE = new ConcurrentHashMap<>();

	public DatabaseObjectCache(Class<T> clazz)
	{
		this.clazz = clazz;
	}

	/**
	 * Returns the DatabaseObject either from the cache or fetches it from the database
	 *
	 * @param id The id of the DatabaseObject to return
	 * @return The DatabaseObject either from the cache or fetches it from the database
	 * @throws DatabaseException Thrown if the communication with the database fails
	 */
	public T get(Long id, DatabaseResult res, boolean fromResult) throws DatabaseException
	{
		/* If we're supposed to query the database, but the id is null, return here */
		if (id == null && !fromResult)
			return null;

		/* Check the cache if we have an id */
		T result = id == null ? null : CACHE.get(id);

		/* If it's not in the cache, get it from the manager */
		if (result == null)
		{
			/* Either get it from the DatabaseResult */
			if (fromResult)
				result = getFromManager(res);
			/* Or get it from the database based on the id */
			else
				result = getFromManager(id);

			/* If there is a result */
			if (result != null)
			{
				/* Check if we have an id, otherwise get it */
				if (id == null)
					id = result.getId();

				/* Remember the mapping */
				CACHE.put(id, result);
			}
		}

		/* Return whatever it is we've got */
		return result;
	}

	/**
	 * Clears the cache
	 */
	public void clear()
	{
		CACHE.clear();
	}

	protected abstract T getFromManager(Long id) throws DatabaseException;

	protected abstract T getFromManager(DatabaseResult res) throws DatabaseException;
}
