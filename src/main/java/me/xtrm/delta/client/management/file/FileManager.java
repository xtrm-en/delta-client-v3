package me.xtrm.delta.client.management.file;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import me.xtrm.delta.client.api.file.IFileManager;
import me.xtrm.delta.client.api.file.ISavedFile;
import me.xtrm.delta.client.management.file.impl.Friends;
import me.xtrm.delta.client.management.file.impl.Modules;
import me.xtrm.delta.client.management.file.impl.Spammer;
import me.xtrm.delta.client.management.file.impl.XRayConfig;
import me.xtrm.delta.client.utils.IOUtils;

public class FileManager implements IFileManager {
	
	private final List<ISavedFile> files;
	private final File deltaDir;
	private final Gson gson;
	
	public FileManager() {
		deltaDir = IOUtils.getDeltaDir();
		gson = new GsonBuilder().serializeNulls().setPrettyPrinting().create();
		files = new ArrayList<>();
	}

	@Override
	public void init() {
		initFiles();
		createFiles();
		loadFiles();
	}

	@Override
	public void onShutdown() {
		createFiles();
		saveFiles();
	}

	@Override
	public List<ISavedFile> getFiles() {
		return files;
	}
	
	private void initFiles() {
		files.add(new Modules(gson, new File(deltaDir, "modules")));
		files.add(new XRayConfig(gson, new File(deltaDir, "xrayconfig.json")));
		files.add(new Friends(gson, new File(deltaDir, "friends.json")));
		files.add(new Spammer(gson, new File(deltaDir, "spammermsg.txt")));
	}
	
	private void createFiles() {
		if(!deltaDir.exists())
			deltaDir.mkdir();
		
		for(ISavedFile f : files) {
			try {
				f.createFile();
			} catch(IOException e) {
				e.printStackTrace();
			}
		}
	}

}
