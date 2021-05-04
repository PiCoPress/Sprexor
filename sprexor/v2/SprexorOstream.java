package sprexor.v2;

import java.util.ArrayList;

interface i { abstract void lambda(ArrayList<String> buf); }
public abstract class SprexorOstream{
	public IOCenter.TYPE type = IOCenter.STDOUT;
	private boolean building = false;
	private i A;
	protected ArrayList<String> buffer;
	public SprexorOstream(i lam) {
		buffer = new ArrayList<String>();
		A = lam;
	}
	public void setType(IOCenter.TYPE ty) {
		type = ty;
	}
	public abstract void print(String msg);
	public abstract void print(String msg, int Color);
	public abstract void printf(String msg, Object...obj);
	public abstract void println(String msg);
	public void println() {
		print("\n");
	}
	public abstract void println(String msg, int Color);
	public SprexorOstream add(String msg) {
		if(building) buffer.add(msg);
		return this;
	}
	public SprexorOstream build() {
		if(!building) building = true;
		return this;
	}
	public void send() {
		if(building) {
			building = false;
			A.lambda(buffer);
			buffer.clear();
		}
	}
	public abstract void clear();
	protected void print(String str, IOCenter.TYPE ty) {
		
	}
}
