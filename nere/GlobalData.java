package nere;

import java.util.HashMap;

class GlobalData {
	HashMap<String, Object> obj = null;
	GlobalData(){
		obj = new HashMap<>();
	}
	
	void putData(String key, Object value) {
		obj.put(key, value);
	}
	
	Object getData(String key) {
		return obj.get(key);
	}
	
	void removeData(String key) {
		obj.remove(key);
	}
	
	boolean existData(String key) {
		return obj.containsKey(key);
	}
}
