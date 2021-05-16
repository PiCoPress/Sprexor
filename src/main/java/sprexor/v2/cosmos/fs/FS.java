package sprexor.v2.cosmos.fs;

import sprexor.v2.SManager;
import sprexor.v2.components.Importable;
import sprexor.v2.components.annotations.Spackage;

@Spackage(packageName = "fs", ref = {SprexorChangeD.class, SprexorMkdir.class, SprexorRm.class, SprexorLs.class})
public class FS implements Importable {
	
	public static String currentDirectory(SManager s) {
		return s.SystemVar.getData("CURRENT_DIRECTORY").toString();
	}
}
