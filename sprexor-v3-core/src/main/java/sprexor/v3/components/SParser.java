package sprexor.v3.components;

import sprexor.v3.SprexorException;

public interface SParser {
	public String id(String str);
	
	public String[] processing(String str) throws SprexorException;
}
