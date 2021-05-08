package sprexor;

import sprexor.v1.CommandFactory;
import sprexor.v1.CommandProvider;
import sprexor.v1.Sprexor;
import sprexor.v2.SManager;
import sprexor.v2.SprexorException;
import sprexor.v2.components.SCommand;
import sprexor.v2.components.SFrame;

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
		} catch (sprexor.v1.SprexorException e) {
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
		} catch (sprexor.v1.SprexorException e) {
			e.printStackTrace();
			return 1;
		}
	}
	
	static public int instantRun(SFrame[] sf, String s) {
		SManager ins = new SManager();
		for(SFrame k : sf) {
			ins.use(k.getClass());
		}
		ins.setup();
		try {
			return ins.exec(s);
		} catch (SprexorException e) {
			e.printStackTrace();
			return 1;
		}
	}
	
	static public int instantRun(SCommand[] sf, String s) {
		SManager ins = new SManager();
		SFrame k = new SFrame() {
			
			@Override
			public String PackageName() {
				return "instant-package";
			}

			@Override
			public SCommand[] references() {
				return sf;
			}
			
		};
		ins.use(k.getClass());
		ins.setup();
		try {
			return ins.exec(s);
		} catch (SprexorException e) {
			e.printStackTrace();
			return 1;
		}
	}
}
