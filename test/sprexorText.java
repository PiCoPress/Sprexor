package test;

import sprexor.v0.*;
import sprexor.v1.*;

import sprexor.v2.*;
import sprexor.v2.cosmos.BasicPackages;
import java.util.Scanner;

public class sprexorText {
	public static void main(String[] args) {
		Scanner s = new Scanner(System.in);
		SManager n = new SManager();
		n.use(BasicPackages.class);
		n.setup();
		try {
			while(true) {
				String ss = s.nextLine();
				if(ss.trim().contentEquals("exit"))break;
				n.run(ss, "USE_VARIABLE;USE_COMMENT;WRAP_NAME;DEBUG");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		s.close();
	}
}