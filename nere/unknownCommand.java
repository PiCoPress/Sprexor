package nere;

public class unknownCommand extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1216453421057028085L;
	
	
	protected static String repeat(String s, int count) {
		String sum = "";
		for(int i = 0; i < count; i++) {
			sum += s;
		}
		return sum;
	}
	public unknownCommand(String name, String fc){
		super("\n" + fc + "\n" + repeat("^", name.length()) + "\n'" + name + "' command not found.");
	}
}
