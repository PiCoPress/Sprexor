package sprexor;


public interface CommandProvider {
	/**
	 * This function will be called when occur error in the method - code. 
	 * @param e : Execption e
	 * @return String
	 */
	public default String error(Exception e) {
		return "Error has been occured (1).";
	}
	/**
	 * this function will be called when argument is null. redefinable
	 * @return IOCenter
	 */
	public default IOCenter emptyArgs() {
		return new IOCenter("Argument emptied.");
	};
	/**
	 * If entered in EntryMode, this method will be invoked.
	 * @param msg
	 * @return string
	 * @since 0.2.5
	 */
	public default String EntryMode(String msg) {
		return null;
	}
	//public Object apply(String[] args);
	//public Object apply(String[] args, boolean[] isWrapped);
	/**
	 * It is main method for operate.
	 * @param args : The Component Class that processed parameter.
	 * @return IOCenter
	 */
	public abstract IOCenter code(Component args);
}
