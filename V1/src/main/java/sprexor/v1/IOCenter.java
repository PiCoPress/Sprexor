package sprexor.v1;

import java.util.Vector;

public class IOCenter {
	private static Core nere;
	
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
	public IOCenter(Core ne) {
		nere = ne;
	}
	public Object[] getMessage() {
		return nere.recentMessage;
	}
	
	public Vector<Object[]> getOutput(){
		return nere.MessageLog;
	}
}
