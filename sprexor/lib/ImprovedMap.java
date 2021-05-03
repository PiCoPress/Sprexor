package sprexor.lib;

public class ImprovedMap <K, V> {
	Object[][] Keys;
	Object[] Values;
	int volume = 16;
	int size = 0;
	
	public ImprovedMap() {
		Keys = new Object[16][2];
		Values = new Object[16];
	}
	
	public boolean put(K k, V v) {
		if(searchByKeys(k) != -1) return false;
		if(size >= volume) {
			volume *= 2;
			expandArray(volume);
		}
		int m = findValid();
		Keys[m][0] = k;
		Keys[m][1] = true;
		Values[m] = v;
		size ++;
		return true;
	}
	
	public boolean put(K k, V v, boolean mutable) {
		if(searchByKeys(k) != -1) return false;
		if(size >= volume) {
			volume *= 2;
			expandArray(volume);
		}
		int m = findValid();
		Keys[m][0] = k;
		Keys[m][1] = mutable;
		Values[m] = v;
		size ++;
		return true;
	}
	
	public boolean delete(K k) {
		int se = searchByKeys(k);
		if(((boolean)Keys[se][1] == false) || se == -1) return false;
		Keys[se][0] = null;
		Values[se] = null;
		return true;
	}
	
	public boolean set(K k, V v) {
		int se = searchByKeys(k);
		if(((boolean)Keys[se][1] == false) || se == -1) return false;
		Values[se] = v;
		return true;
	}
	
	public V get(K k) {
		int ind = searchByKeys(k);
		if(ind == -1) return null;
		return (V)Values[ind];
	}
	
	
	private int findValid() {
		int i = 0;
		for(Object[] obj : Keys) {
			if(obj[0] == null) return i;
			i ++;
		}
		return -1;
	}
	private int searchByKeys(K key) {
		int i = 0;
		Object tmp = (Object)key;
		while(i < volume) {
			if(Keys[i][0] == tmp) return i;
			i ++;
		}
		return -1;
	}
	private void expandArray(int s) {
		Object[][] kk = new Object[s][2];
		Object[] vv = new Object[s];
		int i = 0, c = 0;;
		for(Object[] obj : Keys) {
			if(obj == null) {
				c ++;
				continue;
			}
			kk[i] = obj;
			vv[i] = vv[c];
			c ++;
		}
		Keys = kk;
		Values = vv;
	}
}
