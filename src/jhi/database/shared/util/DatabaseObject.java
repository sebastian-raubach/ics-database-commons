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

package jhi.database.shared.util;

import java.io.*;
import java.util.*;

/**
 * @author Sebastian Raubach
 */
public abstract class DatabaseObject implements Serializable
{
	public static final String COUNT      = "count";
	public static final String CREATED_ON = "created_on";
	public static final String UPDATED_ON = "updated_on";

	protected Long id = -1L;
	protected Map<String, String> extra;

	public DatabaseObject()
	{
	}

	public DatabaseObject(Long id)
	{
		this.id = id;
	}

	public static boolean hasId(DatabaseObject object)
	{
		return object != null && object.getId() != null && object.getId() != -1L;
	}

	public final Long getId()
	{
		return id;
	}

	public final DatabaseObject setId(Long id)
	{
		this.id = id;
		return this;
	}

	public String getExtra(String key)
	{
		if (extra != null)
			return extra.get(key);
		else
			return null;
	}

	public void setExtra(String key, String value)
	{
		if (extra == null)
			extra = new HashMap<>();

		extra.put(key, value);
	}

	@Override
	public boolean equals(Object o)
	{
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		DatabaseObject that = (DatabaseObject) o;

		return id.equals(that.id);

	}

	@Override
	public int hashCode()
	{
		return id.hashCode();
	}
}
