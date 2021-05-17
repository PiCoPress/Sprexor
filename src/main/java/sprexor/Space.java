package sprexor;

import sprexor.v1.CommandFactory;
import sprexor.v1.CommandProvider;
import sprexor.v1.Sprexor;
import sprexor.v2.SManager;
import sprexor.v2.components.Importable;

public class Space {
	static public String Version = "1.0.0";
	static public String Codename = "Discovery";
	
	static public int instantRun(CommandFactory[] bp, String s) {
		Sprexor sp = new Sprexor();
		for(CommandFactory cf : bp) {
			sp.importSprex(cf);
		}
		try {
			sp.activate();
			return sp.exec(s);
		} catch (SprexorException e) {
			e.printStackTrace();
			return 1;
		}
	}
	
	static public int instantRun(CommandProvider[] bp, String s) {
		Sprexor sp = new Sprexor();
		int i = 0;
		for(CommandProvider cf : bp) {
			sp.register(i + "", cf, "");
			i ++;
		}
		try {
			sp.activate();
			return sp.exec(s);
		} catch (SprexorException e) {
			e.printStackTrace();
			return 1;
		}
	}
	
	static public int instantRun(String s, Class<? extends Importable>...sf) {
		SManager ins = new SManager();
		for(Class<? extends Importable> k : sf) {
			ins.use(k);
		}
		ins.setup();
		try {
			return ins.exec(s);
		} catch (SprexorException e) {
			e.printStackTrace();
			return 1;
		}
	}
}
