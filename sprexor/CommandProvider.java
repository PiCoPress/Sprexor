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
	 * this function will be called when argument empty.
	 * @return IOCenter
	 */
	public default IOCenter emptyArgs() {
		return new IOCenter("no data");
	};
	/**
	 * If entered in EntryMode, it will be invoked.
	 * @param msg
	 * @return string
	 * @since 0.2.5
	 */
	public default String EntryMode(String msg) {
		return null;
	}
	/**
	 * Almost execution proceeds in here.
	 * @param args : The Component Class that processed parameter.
	 * @return IOCenter
	 */
	public abstract IOCenter code(Component args);
}