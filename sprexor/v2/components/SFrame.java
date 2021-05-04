package sprexor.v2.components;

public interface SFrame {
	public default String getDescription() {
		return "no description";
	};
	public abstract String PackageName();

	/**
	 * It should be overridden in mainly class.
	 * <br> And, mainly class can be imported by importSprex.
	 * <br> If class is only one, You may leave this method alone.
	 * @return SCommand arrays.
	 */
	public abstract SCommand[] references();
}
