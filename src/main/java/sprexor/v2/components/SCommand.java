package sprexor.v2.components;

import sprexor.v2.IOCenter;
import sprexor.v2.SManager;

/**
 * It is for make a command used in Sprexor.
 */
public interface SCommand {
	/**
	 * Operate with java source overrided method.
	 * @param io : to use IO
	 * @param Environment
	 * @return int exit code
	 */
	public abstract int main(IOCenter io, SParameter args, SManager Environment);
	
	public default String[] strarray(String...a) {
		return a;
	}
	
	public default int indexOf(String[] arr, String e) {
		int i = 0;
		for(String s : arr) {
			if(s.contentEquals(e)) return i;
			i ++;
		}
		return -1;
	}
}
