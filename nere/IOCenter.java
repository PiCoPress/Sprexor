package nere;


public class IOCenter {
	private static Nere nere;
	private static Object[] recentMessage = new Object[2];
	private static enum TYPE{
		ERR,
		CMT,
		NO_VALUE
	}
	public static TYPE ERR = TYPE.ERR;
	public static TYPE CMT = TYPE.CMT;
	public static TYPE NO_VALUE = TYPE.NO_VALUE;
	
	protected static String progressString = "";
	
	protected static void log(Object s, TYPE t) {
		recentMessage[0] = s;
		recentMessage[1] = t;
		if(t == CMT) {
			nere.msg.add(s + "");
		}else {
			nere.err.add(s + "");
		}
	}
	public IOCenter(Nere ne) {
		nere = ne;
	}
	public Object[] getMessage() {
		return recentMessage;
	}
}
