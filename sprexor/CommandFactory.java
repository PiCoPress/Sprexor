package sprexor;

public interface CommandFactory {
	/**
	 * This function will be called when occur error in the method : code.
	 * @param e : Execption e
	 * @return String
	 */
	public default String error(Exception e) {
		return "Error";
	}
	/**
	 * It will be called when argument is empty.
	 * @return IOCenter (any type which not array.)
	 */
	public default IOCenter emptyArgs(Sprexor SprexorInstance) {
		return new IOCenter("Argument emptied.");
	};
	/**
	 * @param msg : input
	 * @return print message.
	 * @throws SprexorException
	 * @since 0.2.18
	 */
	public default String EntryMode(String msg) throws SprexorException {
		return null;
	}
	/**
	 * It will be used to inform detailed information help.
	 * @return String - message.
	 */
	public abstract String help();
	/**
	 * getCommandName defines command name as returned value.
	 * @return String : The name has only English is recommended.
	 */
	public abstract String getCommandName();
	/**
	 * It should be overridden in mainly class.
	 * <br> And, mainly class can be imported by importSprex.
	 * <br> If class is only one, You may leave this method alone.
	 * @return CommandFactory[] arrays.
	 */
	public default CommandFactory[] referenceClass() {
		return null;
	}
	/**
	 * Operate with java source overrided method.
	 * @param args : The processed parameter.
	 * @param SprexorInstance
	 * @return IOCenter
	 */
	public abstract IOCenter code(Component args, Sprexor SprexorInstance);
}
