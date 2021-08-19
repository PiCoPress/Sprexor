package sprexor.v1;

public interface CommandProvider {
		
	public default String error(Exception err) {
		return null;
	}
	public default String emptyArgs() {
		return "empty argument";
	};
	public String code(String[] args);
}
