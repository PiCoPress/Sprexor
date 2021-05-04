package sprexor.v0;

public interface CommandProvider {
		
	public default Object error(Exception e) {
		return null;
	}
	public default Object emptyArgs() {
		return "argument is empty.";
	};
	
	//public Object apply(String[] args);
	//public Object apply(String[] args, boolean[] isWrapped);
	public Object code(String[] args, boolean[] isWrapped, GlobalData scope);
}
