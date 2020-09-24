package sprexor;

import java.util.Vector;

/**
 * Standard Internal Output Center
 * @author PICOPress
 * @since 0.1.2
 */
public class IOCenter {
	private static Sprexor sp;
	
	protected static enum TYPE{
		ERR, // red
		CMT, // white or green
		STDOUT, // white
		NO_VALUE,
		custom1,
		custom2,
		custom3,
	}
	public static TYPE ERR = TYPE.ERR;
	public static TYPE CMT = TYPE.CMT;
	public static TYPE STDOUT = TYPE.STDOUT;
	public static TYPE NO_VALUE = TYPE.NO_VALUE;
	public static TYPE custom1 = TYPE.custom1;
	public static TYPE custom2 = TYPE.custom2;
	public static TYPE custom3 = TYPE.custom3;
	
	protected static String progressString = "";
	
	protected static void log(Object s, TYPE t) {
		sp.recentMessage[0] = s;
		sp.recentMessage[1] = t;
	}
	public IOCenter(Sprexor s) {
		sp = s;
	}
	/**
	 * return recent Message.
	 * @return Object[2], index 1 : message<br>index 2 : message TYPE
	 */
	public Object[] getMessage() {
		return sp.recentMessage;
	}
	/**
	 * return all of message that printed in Sprexor.exec method. This range is smaller than range of getOutput().
	 * @return Vector
	 */
	public Vector<Object[]> getBlockMessage() {
		return sp.blockMessage;
	}
	/**
	 * exit entry mode
	 * @since 0.2.7
	 */
	public void exitEntry() {
		sp.entryMode = false;
		sp.entryId = "";
		sp.doEntry = false;
	}
	/**
	 * return all of message.
	 * @return Vector(Object[2]),  index 1 : message<br>index 2 : message TYPE
	 * @since 0.2.1
	 */
	public Vector<Object[]> getOutput(){
		return sp.MessageLog;
	}
}
