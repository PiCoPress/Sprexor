package sprexor.v3;

import java.util.HashMap;

/*
 * It is a value that cannot modify or remove if last char is '_'.
 * 
 */

public final class GlobalData {
	private HashMap<String, Object> obj = null;
	private boolean flag = false;
	
	public GlobalData(){
		obj = new HashMap<>();
	}
	
	protected HashMap<String, Object> getter() {
		return obj;
	}
	
	public boolean putData(String key, Object value) {
		if(existKey(key))return false;
		obj.put(key, value);
		if(key.toCharArray()[key.length() - 1] == '_')flag = true;
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
		if(!existKey(key) || key.toCharArray()[key.length() -1] == '_')return false;
		obj.replace(key, data);
		return true;
	}
	
	public boolean reset() {
		if(flag)return false;
		obj.clear();
		return true;
	}
	
	public boolean forceReset() {
		obj.clear();
		return true;
	}
}
