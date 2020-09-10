package nere;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class Lib{
	private static boolean isOnlyEnglish(String a) {
		String[] arr = a.split("");
		for(String str : arr) {
			if(!str.matches("[a-zA-Z]"))return false;
			
		}
		return true;
	}
	public static String Processor(String opt) {
		String res = "";
		try {
			Runtime r = Runtime.getRuntime();
			Process p = r.exec(opt);
			InputStream in = p.getInputStream();
			InputStreamReader isr = new InputStreamReader(in);
			BufferedReader br = new BufferedReader(isr);
			String line = null;
			while ((line = br.readLine()) != null)
			{
				res += line;
			}
			in.close();
		}catch(IOException e) {
			res = e + "";
		}
		return res;
	}
	public static byte AnalOption(String str, boolean[] isWrapped) {
		if(str.startsWith("--") && !isWrapped[1]) {
			if(isOnlyEnglish(str.substring(2)))
				return 2;
		}else if(str.startsWith("-") && !isWrapped[1]) {
			if(isOnlyEnglish(str.substring(1)))
				return 1;
		}
		return 0;
	}
	public static boolean OptionPrs(String compare, String to, byte i)throws Exception {
		if(i == 1) {
			to = to.substring(1);
			if(compare.length() != to.length())return false;
			for(int j = 0; j < to.length(); j ++) {
				if(compare.indexOf(to.split("")[j]) == -1)return false;
			}
			return true;
		}else if(i == 2) {
			to = to.substring(2);
			if(compare == to)return true;
		}else {
			throw new Exception(to + ":" + i + "# is not allowed token.");
		}
		return false;
	}
}
