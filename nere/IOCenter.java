package nere;

import java.util.Vector;

/**
 * Standard Internal Output Center
 * @author PICOPress
 * @since 0.1.2
 */
public class IOCenter {
	private static Nere nere;
	
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
		nere.recentMessage[0] = s;
		nere.recentMessage[1] = t;
	}
	public IOCenter(Nere ne) {
		nere = ne;
	}
	/**
	 * return recent Message.
	 * @return Object[2], index 1 : message<br>index 2 : message TYPE
	 */
	public Object[] getMessage() {
		return nere.recentMessage;
	}
	/**
	 * return all of message.
	 * @return Vector<Object[2]>,  index 1 : message<br>index 2 : message TYPE
	 */
	public Vector<Object[]> getOutput(){
		return nere.MessageLog;
	}
}
