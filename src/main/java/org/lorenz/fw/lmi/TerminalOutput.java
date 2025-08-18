package org.lorenz.fw.lmi;

import java.io.BufferedOutputStream;
import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.PrintStream;

public class TerminalOutput
{
	private static final PrintStream output;

	static
	{
		FileOutputStream fdOut = new FileOutputStream(FileDescriptor.out);
		output = new PrintStream(new BufferedOutputStream(fdOut, 128), true);
	}

	public static void print(String message)
	{
		output.print(message);
	}

	public static void println(String message)
	{
		output.println(message);
	}
}
