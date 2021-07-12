package sprexor.v3.components;

import sprexor.SprexorException;

public interface SParser {
	public String id(String str);
	
	public String[] processing(String str) throws SprexorException;
}
