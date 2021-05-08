package sprexor.v2;

import sprexor.v2.components.SParameter;

/**
 * Standard Internal Output Center
 * @since 0.1.2
 */
public class IOCenter {
	public static enum TYPE {
		ERR,
		CMT, 
		STDOUT,
		NO_VALUE,
		UNKNOWN,
		WARN,
	}
	
	public static final TYPE ERR = TYPE.ERR;
	public static final TYPE CMT = TYPE.CMT;
	public static final TYPE STDOUT = TYPE.STDOUT;
	public static final TYPE WARN = TYPE.WARN;
	public static final TYPE NO_VALUE = TYPE.NO_VALUE;
	public static final TYPE UNKNOWN = TYPE.UNKNOWN;
	public static final TYPE err = TYPE.ERR;
	public static final TYPE cmt = TYPE.CMT;
	public static final TYPE stdout = TYPE.STDOUT;
	public static final TYPE warn = TYPE.WARN;
	public static final TYPE no_value = TYPE.NO_VALUE;
	public static final TYPE unknown = TYPE.UNKNOWN;
	//
	protected SParameter component = null;
	public Object label;
	public SprexorOstream out;
	public SprexorIstream in;
	//
	protected TYPE status = null;
	public IOCenter(SprexorOstream o, SprexorIstream i) {
		out = o;
		in = i;
	}
	/**
	 * return arguments.
	 * @return Component
	 */
	public SParameter getComponent() {
		return component;
	}
	protected void reset() {
		component = null;
		out.type = stdout;
	}
}