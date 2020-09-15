package nere;

@SuppressWarnings("unused")
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
	 * for import other class(importNere class)
	 * @return String
	 * @see nere.Nere.importNere
	 */
	public default String getCommandName() {
		return "";
	}
	/**
	 * for import other class(importNere class)
	 * @return String
	 * @see nere.Nere.importNere
	 */
	public default String help() {
		return "";
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
