package org.lorenz.fw.utils;

public class LorenzException extends Exception
{

	int errorCode = -1;

	public LorenzException()
	{
	}

	public LorenzException(int errorCode)
	{
		super();
		this.errorCode = errorCode;
	}

	public LorenzException(String message)
	{
		super(message);
	}

	public LorenzException(int errorCode, String message)
	{
		this(message);
		this.errorCode = errorCode;
	}

	public int getErrorCode()
	{
		return this.errorCode;
	}
}
