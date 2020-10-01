package sprexor;


public interface CommandProvider {
	/**
	 * This function will be called when occur error in the method : code. redefinable
	 * @param e : Execption e
	 * @return Object (any type which not array.)
	 */
	public default Object error(Exception e) {
		return null;
	}
	
	/**
	 * this function will be called when argument is null. redefinable
	 * @return Object (any type which not array.)
	 */
	public default Object emptyArgs() {
		return "argument is empty.";
	};
	
	/**
	 * for import other class(import class)
	 * @return String
	 */
	public default String getCommandName() {
		return "";
	}
	/**
	 * for import other class(import class)
	 * @return String
	 */
	public default String help() {
		return "";
	}
	/**
	 * @param msg : input
	 * @return print message.
	 * @since 0.2.5
	 */
	public default Object EntryMode(String msg) {
		return null;
	}
	public default CommandProvider[] referenceClass() {
		return null;
	}
	
	//public Object apply(String[] args);
	//public Object apply(String[] args, boolean[] isWrapped);
	/**
	 * Operate with java source overrided method.
	 * @param args : arguments
	 * @param isWrapped : (boolean) isWrapped[i] == whether args[i] is wrapped by " or '.
	 * @param scope : GlobalData
	 * @return Object (any type which not array.)
	 */
	public Object code(String[] args, boolean[] isWrapped, GlobalData scope);
}
