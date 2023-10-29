package me.xtrm.xeon;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.regex.Pattern;

import com.google.common.hash.Hashing;

import cpw.mods.fml.relauncher.FMLInjectionData;
import me.xtrm.delta.client.utils.IOUtils;
import me.xtrm.delta.loader.api.plugin.types.IPlugin;
import me.xtrm.delta.loader.api.plugin.types.PluginInfo;

@PluginInfo(name = "Xeon", version = "3.0.0", author = "xTrM_")
public class Xeon implements IPlugin {
	
	private File xeonDataFile, xeonJarFile;
	private String xeonClassname;
	
	public Xeon() {
		xeonDataFile = new File(IOUtils.getDeltaDir(), ".xeon");
		if(!xeonDataFile.exists()) return;
		
		try {
			loadData();
			deleteModsFile();
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	private void loadData() throws IOException {		
		String[] data = new String(Files.readAllBytes(xeonDataFile.toPath())).split(Pattern.quote(";"));
		String xeonJarName = data[0];
		xeonClassname = data[1];
		xeonJarFile = new File(xeonJarName);
	}
	
	private void deleteModsFile() throws IOException {
		File modFolder = new File((File)FMLInjectionData.data()[6], "mods");
		for(File f : modFolder.listFiles()) {
			String name = f.getName().substring(0, f.getName().indexOf('.') == -1 ? f.getName().length() : f.getName().indexOf('.'));
			String hash = Hashing.sha256().hashString(name, StandardCharsets.UTF_8).toString();
			if(hash.equalsIgnoreCase(xeonJarFile.getName())) {
				f.delete();
				break;
			}
		}
	}
}
