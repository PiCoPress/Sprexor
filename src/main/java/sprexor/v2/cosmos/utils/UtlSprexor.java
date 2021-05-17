package sprexor.v2.cosmos.utils;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;

import sprexor.v2.IOCenter;
import sprexor.v2.SManager;
import sprexor.v2.components.Importable;
import sprexor.v2.components.SCommand;
import sprexor.v2.components.SParameter;
import sprexor.v2.components.annotations.CommandInfo;
import sprexor.v2.lib.Smt;

@CommandInfo(name = "sprexor", version = "0.0.1")
public class UtlSprexor implements SCommand {
	public static String Version = "0.0.1";
	static String help = Smt.SMT_FORM("Sprexor Package Manager : ".concat(Version)
			.concat("[nn]Usage : sprexor [option] args...[nnt]")
			.concat("local-install[t]").concat("install sprexor package with JAR file from local drive [nnnt]")
			.concat("-v [nt]")
			.concat("--version[t]").concat("Display sprexor package manager version [nnnt]")
			.concat("-h [nt]")
			.concat("--help[tt]").concat("Display sprexor package manager help message"));
	@Override
	public int main(IOCenter io, SParameter args, SManager Environment) {
		switch(args.getAllOption()[0]) {
		
		case "-v" :
		case "--version" :
			io.out.printf("sprexor package manager : ").println(Version);
			return 0;
		
		case "-h" :
		case "--help" :
			io.out.println(help);
			return 0;
		}
		if(args.isEmpty()) io.out.println(help);
		else switch(args.getValidElement(0)) {
		case "local-install" :
			String cu = sprexor.v2.cosmos.fs.FS.currentDirectory(Environment);
			String k = cu.endsWith(File.separator)? cu : cu.concat(File.separator);
			String path;
			File file;
			
			Class<?> cla;
			Class<? extends Importable> a;
			
			for(String arg : args.getValidParameters(1)) {
				path = arg.startsWith("/") || arg.toCharArray()[1] == ':'? arg : k + arg;
				file = new File(path);
				if(!file.exists()) {
					io.err.printf("file not found : %s%n", path);
					continue;
				}
				URLClassLoader dm;
				try {
					dm = new URLClassLoader(new URL[] { file.toURI().toURL() });
					cla = dm.loadClass("spm.Main");
					a = cla.getClass().cast(cla);
					Environment.useR(a);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			return 0;
			
		default : 
			io.out.println(help);
		}
		return 0;
	}
}
