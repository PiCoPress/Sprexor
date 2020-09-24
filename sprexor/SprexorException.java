package sprexor;

public class SprexorException extends Exception {
	/**
	 * There is some of error types.
	 * @since 0.2.10
	 */
	private static final long serialVersionUID = -7913584819257271928L;
	protected enum ERROR_TYPE{
		VARIABLE_ERR,
		EXPRSS_ERR,
		ACTIVATION_FAILED
	}
	
	public static final ERROR_TYPE VARIABLE_ERR = ERROR_TYPE.VARIABLE_ERR;
	public static final ERROR_TYPE EXPRSS_ERR = ERROR_TYPE.EXPRSS_ERR;
	public static final ERROR_TYPE ACTIVATION_FAILED = ERROR_TYPE.ACTIVATION_FAILED;
	
	private static String knock(ERROR_TYPE k, String s) {
		switch(k) {
		case VARIABLE_ERR :
			return ("cannot find a variable : " + s);
			
		case EXPRSS_ERR :
			return s;
		
		case ACTIVATION_FAILED :
			return "current state is not activated. ";
		}
		return "";
	}
	
	public SprexorException(ERROR_TYPE e, String s){
		super(knock(e, s));
	}
}
