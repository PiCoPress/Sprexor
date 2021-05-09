package sprexor.v0.loadNere;

import java.util.HashMap;

import sprexor.v0.CommandProvider;
import sprexor.v0.GlobalData;

public class BasicPackages {
	public static class ImportTemplate{
		public HashMap<String, CommandProvider> cmds;
		public HashMap<String, Object> variables;
		public String cmdLists;
		public HashMap<String, String> help;
		public ImportTemplate(HashMap<String, CommandProvider> cmds, HashMap<String, Object> variables, String cmdLists, HashMap<String, String> help) {
			this.cmds = cmds;
			this.variables = variables;
			this.cmdLists = cmdLists;
			this.help = help;
		}
	}
	public static ImportTemplate get() {
		HashMap<String, CommandProvider> tmp = new HashMap<String, CommandProvider>();
		tmp.put("example", new CommandProvider(){
			@Override
			public Object code(String[] args, boolean[] isWrapped, GlobalData scope) {
				return args[0] + args[1] + args[2];
			}
			
			@Override
			public Object error(Exception e) {
				return "error";
			}
			
			@Override
			public Object emptyArgs() {
				return "argument empty.";
			}
		});
		HashMap<String, String> h = ((new HashMap<String, String>()));
		h.put("example", "example help.");
		ImportTemplate im = new ImportTemplate(tmp, new HashMap<String, Object>(), "example\n", h);
		return im;
	}
}
