package sprexor.v2.components;

import sprexor.v2.SprexorException;

public interface SParser {
	public String id(String str);
	
	public String[] processing(String str) throws SprexorException;
}
