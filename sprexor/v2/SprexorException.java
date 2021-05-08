package sprexor.v2;

import sprexor.SOutputs;

public class SprexorException extends Exception {
	
	/**
	 * There is some of error types.
	 * @since 0.2.10
	 */
	private static final long serialVersionUID = -7913584819257271928L;
	protected enum ERROR_TYPE{
		VARIABLE_ERR,
		EXPRSS_ERR,
		ACTIVATION_FAILED,
		CMD_NOT_FOUND,
		INTERNAL_ERROR,
		NULL
	}
	
	public static final ERROR_TYPE VARIABLE_ERR = ERROR_TYPE.VARIABLE_ERR;
	public static final ERROR_TYPE EXPRSS_ERR = ERROR_TYPE.EXPRSS_ERR;
	public static final ERROR_TYPE ACTIVATION_FAILED = ERROR_TYPE.ACTIVATION_FAILED;
	public static final ERROR_TYPE CMD_NOT_FOUND = ERROR_TYPE.CMD_NOT_FOUND;
	public static final ERROR_TYPE INTERNAL_ERROR = ERROR_TYPE.INTERNAL_ERROR;
	
	protected ERROR_TYPE status = ERROR_TYPE.NULL;
	protected String msg = "";
	
	private String knock(ERROR_TYPE k, String s) {
		return switch(k) {
		case VARIABLE_ERR -> String.format(SOutputs.nov, s);
			
		case EXPRSS_ERR -> s;
		
		case ACTIVATION_FAILED -> SOutputs.noact;
			
		case CMD_NOT_FOUND -> String.format(SOutputs.notf, s);
		
		case INTERNAL_ERROR -> s;
			
		default -> SOutputs.nul;
		};
	}
	@Override
	public String getMessage() {
		return knock(status, msg);
	}
	public SprexorException(ERROR_TYPE e, String s){
		super(s);
		status = e;
		msg = s;
	}
}
