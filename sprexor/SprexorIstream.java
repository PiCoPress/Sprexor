package sprexor;

public abstract class SprexorIstream {
	public char delimiter = '\n';
	protected StringBuffer sb;
	public SprexorIstream() {
		sb = new StringBuffer();
	}
	public abstract void prompt();
	public abstract void prompt(String msg);
	public abstract void prompt(String msg, int Color);
	public String flush() {
		String tmp = sb.toString();
		sb.setLength(0);
		return tmp;
	}
	public void setDelimiter(char ch) {
		delimiter = ch;
	}
	public abstract String getln();
	public abstract String getln(String msg);
	public abstract String getln(String msg, int Color);
	
	public void buffering(String str) {
		sb.append(str);
	}
	public void buffering(char str) {
		sb.append(str);
	}
}
