package sprexor;

public interface CommandProvider {
	/**
	 * Almost execution proceeds in here.
	 * @param io : The Component Class that processed parameter.
	 * @return IOCenter
	 */
	public abstract int code(IOCenter io);
}