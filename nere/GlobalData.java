package nere;

import java.util.HashMap;

/*
 * if last char of keyName is '_', read-only data that cannot modify or remove.
 * 
 */

public class GlobalData {
	private HashMap<String, Object> obj = null;
	GlobalData(){
		obj = new HashMap<>();
	}
	
	public boolean putData(String key, Object value) {
		if(existKey(key))return false;
		obj.put(key, value);
		return true;
	}
	
	public Object getData(String key) {
		return obj.get(key);
	}
	
	public boolean removeData(String key) {
		if(key.toCharArray()[key.length() - 1] == '_')return false;
		obj.remove(key);
		return true;
	}
	
	public boolean existKey(String key) {
		return obj.containsKey(key);
	}
	
	public boolean existData(Object data) {
		return obj.containsValue(data);
	}
	
	public boolean modifyData(String key, Object data) {
		if(existKey(key) || key.toCharArray()[key.length() -1] == '_')return false;
		obj.replace(key, data);
		return true;
	}
}
