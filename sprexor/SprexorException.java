package sprexor;

public class SprexorException extends Exception {
	public ERROR_TYPE status;
	/**
	 * There is some of error types.
	 * @since 0.2.10
	 */
	private static final long serialVersionUID = -7913584819257271928L;
	protected enum ERROR_TYPE{
		VARIABLE_ERR,
		EXPRSS_ERR,
		ACTIVATION_FAILED,
		CMD_NOTFOUND
	}
	protected static String repeat(String s, int count) {
		String sum = "";
		for(int i = 0; i < count; i++) {
			sum += s;
		}
		return sum;
	}
	public static final ERROR_TYPE VARIABLE_ERR = ERROR_TYPE.VARIABLE_ERR;
	public static final ERROR_TYPE EXPRSS_ERR = ERROR_TYPE.EXPRSS_ERR;
	public static final ERROR_TYPE ACTIVATION_FAILED = ERROR_TYPE.ACTIVATION_FAILED;
	public static final ERROR_TYPE CMD_NOTFOUND = ERROR_TYPE.CMD_NOTFOUND;
	
	private String knock(ERROR_TYPE k, String s) {
		status = k;
		switch(k) {
		case VARIABLE_ERR :
			return ("cannot find a variable : " + s);
			
		case EXPRSS_ERR :
			return s;
		
		case ACTIVATION_FAILED :
			if(!s.isEmpty()) return s;
			return "current status was not activated. ";
		
		case CMD_NOTFOUND :
			return s + " : command not found.";
		}
		return "";
	}
	
	public SprexorException(ERROR_TYPE e, String s){
		new Exception(knock(e, s));
	}
}
