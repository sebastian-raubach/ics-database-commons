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

package jhi.database.shared.exception;

import java.io.*;
import java.util.logging.*;

/**
 * {@link DatabaseException} extends {@link Exception} that will be used to wrap all exceptions that occur while interacting with the database, e.g.
 * {@link java.sql.SQLException}
 *
 * @author Sebastian Raubach
 */
public class DatabaseException extends Exception implements Serializable
{
	private static final long serialVersionUID = -7935074533073743071L;

	private transient final static Logger LOGGER = Logger.getLogger(DatabaseException.class.getName());

	private static boolean forwardExceptionContent = true;
	private static boolean printExceptions         = true;

	public DatabaseException()
	{
		if (printExceptions)
			logException();
	}

	public DatabaseException(String message)
	{
		super(forwardExceptionContent ? message : DatabaseException.class.getName());

		if (printExceptions)
			logException();
	}

	public DatabaseException(Exception e)
	{
		super(forwardExceptionContent ? e : null);

		if (printExceptions)
			logException(e);
	}

	public static void setForwardExceptionContent(boolean forwardExceptionContent)
	{
		DatabaseException.forwardExceptionContent = forwardExceptionContent;
	}

	public static void setPrintExceptions(boolean printExceptions)
	{
		DatabaseException.printExceptions = printExceptions;
	}

	private void logException()
	{
		LOGGER.log(Level.SEVERE, this.toString(), this);
	}

	private void logException(Exception e)
	{
		LOGGER.log(Level.SEVERE, e.toString(), e);
	}
}
