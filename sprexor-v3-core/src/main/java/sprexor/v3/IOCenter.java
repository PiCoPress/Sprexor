package sprexor.v3;

import java.io.PrintStream;
import java.util.Scanner;

import sprexor.v3.lib.Utils;

/**
 * Standard Internal Output Center
 */
public class IOCenter {
	public Object label;
	//
	public PrintStream out;
	public PrintStream err;
	public Scanner in;
	//
	public Utils utils;
	//
	public IOCenter(PrintStream o, Scanner i) {
		out = o;
		in = i;
	}
	public IOCenter() { }
}