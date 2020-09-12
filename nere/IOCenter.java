package nere;

import java.util.Vector;

public class IOCenter {
	private static Nere nere;
	protected static Object[] recentMessage = new Object[2];
	
	protected static enum TYPE{
		ERR,
		CMT,
		STDOUT,
		NO_VALUE
	}
	public static TYPE ERR = TYPE.ERR;
	public static TYPE CMT = TYPE.CMT;
	public static TYPE STDOUT = TYPE.STDOUT;
	public static TYPE NO_VALUE = TYPE.NO_VALUE;
	
	protected static String progressString = "";
	
	protected static void log(Object s, TYPE t) {
		recentMessage[0] = s;
		recentMessage[1] = t;
	}
	public IOCenter(Nere ne) {
		nere = ne;
	}
	public Object[] getMessage() {
		return recentMessage;
	}
	
	public Vector<Object[]> getOutput(){
		return nere.MessageLog;
	}
}
