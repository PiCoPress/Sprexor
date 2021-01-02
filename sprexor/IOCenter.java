package sprexor;

import java.util.Vector;

/**
 * Standard Internal Output Center
 * @author PICOPress
 * @since 0.1.2
 */
public class IOCenter {
	private static Sprexor sp;
	public static enum TYPE{
		ERR, // red
		CMT, // white or green
		STDOUT, // white
		WARN,
		NO_VALUE,
		UNKNOWN,
	}
	public static final TYPE ERR = TYPE.ERR;
	public static final TYPE CMT = TYPE.CMT;
	public static final TYPE STDOUT = TYPE.STDOUT;
	public static final TYPE WARN = TYPE.WARN;
	public static final TYPE NO_VALUE = TYPE.NO_VALUE;
	public static final TYPE UNKNOWN = TYPE.UNKNOWN;
	//
	public Object label;
	//
	protected String Msg = "";
	protected TYPE status = null;
	@Deprecated
	protected static void log(Object s, TYPE t) {
		sp.recentMessage[0] = s;
		sp.recentMessage[1] = t;
	}
	@Deprecated
	public IOCenter(Sprexor s) {
		sp = s;
	}
	public IOCenter(String msg, TYPE ty) {
		Msg = msg;
		status = ty;
	}
	public IOCenter(String msg) {
		Msg = msg;
		status = STDOUT;
	}
	public IOCenter(String msg, TYPE ty, Object l) {
		Msg = msg;
		status = ty;
		label = l;
	}
	public IOCenter(String msg, Object l) {
		Msg = msg;
		status = STDOUT;
		label = l;
	}
	/**@deprecated
	 * return recent Message.
	 * @return Object[2], index 1 : message<br>index 2 : message TYPE
	 */
	@Deprecated
	public Object[] getMessage() {
		return sp.recentMessage;
	}
	/**@deprecated
	 * return all of message that printed in Sprexor.exec method. This range is smaller than range of getOutput().
	 * @return Vector
	 */
	@Deprecated
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
	/**@deprecated
	 * return all of message.
	 * @return Vector(Object[2]),  index 1 : message<br>index 2 : message TYPE
	 * @since 0.2.1
	 */
	@Deprecated
	public Vector<Object[]> getOutput(){
		return sp.MessageLog;
	}
	
}
