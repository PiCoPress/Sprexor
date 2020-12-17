package sprexor;

public interface CommandFactory {
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
	public default IOCenter emptyArgs(GlobalData scope, Sprexor sprex) {
		return new IOCenter("Argument emptied.");
	};
	/**
	 * @param msg : input
	 * @return print message.
	 * @throws SprexorException 
	 * @throws CommandNotFoundException 
	 * @since 0.2.18
	 */
	public default Object EntryMode(String msg) throws CommandNotFoundException, SprexorException {
		return null;
	}
	/**
	 * for import other class(import class)
	 * @return String
	 */
	public abstract String help();
	/**
	 * for import other class(import class)
	 * @return String
	 */
	public abstract String getCommandName();
	public default CommandFactory[] referenceClass() {
		return null;
	}
	/**
	 * Operate with java source overrided method.
	 * @param args : arguments
	 * @param isWrapped : (boolean) isWrapped[i] == whether args[i] is wrapped by " or '.
	 * @param scope : GlobalData
	 * @param sprex is Sprexor Instance which working now.
	 * @return Object (any type which not array.)
	 */
	public abstract IOCenter code(String[] args, boolean[] isWrapped, GlobalData scope, Sprexor sprex);
}
