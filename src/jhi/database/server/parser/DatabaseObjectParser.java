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

package jhi.database.server.parser;

import java.util.*;

import jhi.database.server.*;
import jhi.database.server.util.*;
import jhi.database.shared.exception.*;
import jhi.database.shared.util.*;

/**
 * @author Sebastian Raubach
 */
public abstract class DatabaseObjectParser<T extends DatabaseObject>
{
	private List<DatabaseObjectCache<? extends DatabaseObject>> CACHES = new ArrayList<>();

	public final void clearCache()
	{
		CACHES.forEach(DatabaseObjectCache::clear);
	}

	protected <U extends DatabaseObject> void registerCache(DatabaseObjectCache<U> cache)
	{
		CACHES.add(cache);
	}

	public abstract T parse(DatabaseResult databaseRow, boolean foreignsFromResultSet) throws DatabaseException;
}
