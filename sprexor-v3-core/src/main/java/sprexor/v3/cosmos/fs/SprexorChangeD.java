package sprexor.v3.cosmos.fs;

import java.io.File;

import sprexor.v3.IOCenter;
import sprexor.v3.SManager;
import sprexor.v3.components.SCommand;
import sprexor.v3.components.SParameter;
import sprexor.v3.components.annotations.CommandInfo;
import sprexor.v3.lib.Utils;

@CommandInfo(name = "cd", version = "0.0.1")
public class SprexorChangeD implements SCommand {

	@Override
	public int main(IOCenter io, SParameter args, SManager Environment) {
		String sum = Utils.join(args.getArrays(), " ") + " ";
		File f = new File(sum.toCharArray()[1] == ':' || sum.startsWith("/")?
				sum.trim() : FS.currentDirectory(Environment).concat(File.separator).concat(sum).trim());
		
		System.out.println(f.getAbsolutePath());
		if(f.isDirectory()) Environment.SystemVar.modifyData("CURRENT_DIRECTORY", f.getAbsoluteFile());
		else {
			io.out.printf("no directory : %s %n", sum);
			return 1;
		}
		return 0;
	}
}
