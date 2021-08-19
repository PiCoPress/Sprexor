package sprexor.v3.cosmos.fs;

import sprexor.v3.SManager;
import sprexor.v3.components.Importable;
import sprexor.v3.components.annotations.Spackage;

@Spackage(packageName = "fs", ref = {SprexorChangeD.class, SprexorMkdir.class, SprexorRm.class, SprexorLs.class})
public class FS implements Importable {
	
	public static String currentDirectory(SManager s) {
		return s.SystemVar.getData("CURRENT_DIRECTORY").toString();
	}
}
