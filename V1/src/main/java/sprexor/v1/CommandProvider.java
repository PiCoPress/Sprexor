package sprexor.v1;

public interface CommandProvider {
		
	public default Object error(Exception err) {
		return null;
	}
	public default Object emptyArgs() {
		return "empty argument";
	};
	
	//public Object apply(String[] args);
	//public Object apply(String[] args, boolean[] isWrapped);
	public Object code(String[] args, boolean[] isWrapped, GlobalData scope);
}
