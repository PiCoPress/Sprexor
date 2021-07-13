package sprexor.v2.lib;

/**
 * Sprexor > V3 > lib > VersionManager (2021)
 * @author PICOPress
 * Github link : https://github.com/PiCoPress
 *
 * VersionManager source code can be used by any users, and this sign should be included arccording to MIT license.
 */
public class ByteStack {
	private int size = 1024;
	private int cursor = 0;
	private byte[] head;
	private byte[][] elements;
	
	public ByteStack(int k) { elements = new byte[1024][k]; }
	public void push(byte[] t) {
		head = t;
		elements[cursor ++ ] = t;
	}
	public Object top() {
		return head;
	}
	public void pop() {
		head = elements[-- cursor];
	}
	public boolean empty() {
		return cursor == 0;
	}
	public int getSize() {
		return size;
	}
	public int current() {
		return cursor;
	}
	public byte[][] getBytes() {
		return elements;
	}
}