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

/**
 * @author Sebastian Raubach
 */
public class PaginatedResult<T> implements Serializable
{
	private long count = 0;
	private T result;

	public PaginatedResult()
	{
	}

	public PaginatedResult(long count, T result)
	{
		this.count = count;
		this.result = result;
	}

	public long getCount()
	{
		return count;
	}

	public PaginatedResult setCount(long count)
	{
		this.count = count;
		return this;
	}

	public T getResult()
	{
		return result;
	}

	public PaginatedResult setResult(T result)
	{
		this.result = result;
		return this;
	}
}
