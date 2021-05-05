package sprexor.v2.cosmos;

import java.io.File;
import sprexor.v2.IOCenter;
import sprexor.v2.SManager;
import sprexor.v2.components.SCommand;
import sprexor.v2.components.SParameter;

public class SprexorMkdir implements SCommand {

	@Override
	public String name() {
		return "mkdir";
	}

	@Override
	public int main(IOCenter io, SManager Environment) {
		SParameter args = io.getComponent();
		String ks = Environment.SystemVar.getData("CURRENT_DIRECTORY").toString();
		File f;
		for(String s : args) {
			f = new File(s.toCharArray()[1] == ':' || s.startsWith("/")? s : ks.concat(File.separator).concat(s));
			try {
		System.out.println(f.getAbsolutePath());
				f.mkdirs();
			} catch(SecurityException se) {
				io.out.printf("%s : Permission denied %n", f.getAbsolutePath());
			}
		}
		return 0;
	}

	@Override
	public String version() {
		return "0.0.0";
	}

}
