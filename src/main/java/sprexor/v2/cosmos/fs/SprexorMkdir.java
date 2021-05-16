package sprexor.v2.cosmos.fs;

import java.io.File;
import sprexor.v2.IOCenter;
import sprexor.v2.SManager;
import sprexor.v2.components.SCommand;
import sprexor.v2.components.SParameter;
import sprexor.v2.components.annotations.CommandInfo;

@CommandInfo(name = "mkdir")
public class SprexorMkdir implements SCommand {
	
	@Override
	public int main(IOCenter io, SParameter args, SManager Environment) {
		String ks = FS.currentDirectory(Environment);
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
}
