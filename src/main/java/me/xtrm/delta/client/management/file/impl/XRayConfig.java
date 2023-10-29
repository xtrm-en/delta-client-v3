package me.xtrm.delta.client.management.file.impl;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import me.xtrm.delta.client.api.file.SavedFile;
import me.xtrm.delta.client.management.module.impl.render.XRay;
import me.xtrm.delta.client.management.module.impl.render.xray.XData;

public class XRayConfig extends SavedFile {

	public XRayConfig(Gson gson, File file) {
		super(gson, file);
	}

	@Override
	public void loadFile() throws IOException {
		if(getFile().exists()) {
			FileReader fr = new FileReader(getFile());
			JsonObject jsonObject = getGson().fromJson(fr, JsonObject.class);
			if(jsonObject == null) {
				fr.close();
				return;
			}
			
			if(jsonObject.has("blacklist")) {
				JsonArray jsonArray = (JsonArray)jsonObject.get("blacklist");
				XData blacc = new XData();
				for(int i = 0; i < jsonArray.size(); i++) {
					JsonObject o = jsonArray.get(i).getAsJsonObject();
					int id = o.get("id").getAsInt();
					int meta = o.get("meta").getAsInt();
					blacc.add(id, meta);
				}
				if(!blacc.list.isEmpty()) {
					XRay.blacklist = blacc;
				}
			}
			
			fr.close();
		}
	}

	@Override
	public void saveFile() throws IOException {
		FileWriter fw = new FileWriter(getFile());
		JsonObject jsonObject = new JsonObject();
		
		JsonArray setArray = new JsonArray();
		
		for(XData xdata : XRay.blacklist.list) {
			JsonObject setArrayObject = new JsonObject();
			setArrayObject.addProperty("id", xdata.id);
			setArrayObject.addProperty("meta", xdata.meta);
			setArray.add(setArrayObject);
		}	
		
		jsonObject.add("blacklist", setArray);
		
		fw.write(getGson().toJson(jsonObject));
		fw.close();
	}

}
