package sprexor.v2.components;

/**
 * SFrame : The concept of Sprexor Application Package
 */
public interface SFrame {
	public default String getDescription() {
		return "no description";
	};
	public abstract String PackageName();

	/**
	 * reference Classes of commands to use
	 * @return SCommand arrays.
	 */
	public abstract SCommand[] references();
}
