package me.xtrm.delta.client.management.file.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import com.google.gson.Gson;

import me.xtrm.delta.client.api.file.SavedFile;

public class Spammer extends SavedFile {

	public Spammer(Gson gson, File file) {
		super(gson, file);
	}

	@Override
	public void loadFile() throws IOException {
		if(getFile().exists()) {
			BufferedReader br = new BufferedReader(new FileReader(getFile())); 
			String line = br.readLine();
			if(line != null) {
				me.xtrm.delta.client.management.module.impl.player.Spammer.str = line;
			}
			br.close();
		}
	}

	@Override
	public void saveFile() throws IOException {
		FileWriter fw = new FileWriter(getFile());
		fw.write(me.xtrm.delta.client.management.module.impl.player.Spammer.str);
		fw.close();
	}

}
