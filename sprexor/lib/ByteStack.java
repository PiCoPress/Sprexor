package sprexor.lib;

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