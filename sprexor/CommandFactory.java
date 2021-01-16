package sprexor;

public interface CommandFactory {
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
	public abstract int code(IOCenter io, Sprexor SprexorInstance);
}
