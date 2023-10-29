package me.xtrm.delta.client.management.file.impl;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import me.xtrm.delta.client.api.DeltaAPI;
import me.xtrm.delta.client.api.file.SavedFile;

public class Friends extends SavedFile {

	public Friends(Gson gson, File file) {
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
			
			if(jsonObject.has("friends")) {
				JsonArray jsonArray = (JsonArray)jsonObject.get("friends");
				for(int i = 0; i < jsonArray.size(); i++) {
					JsonObject o = jsonArray.get(i).getAsJsonObject();
					String name = o.get("name").getAsString();
					DeltaAPI.getClient().getFriendManager().addFriend(name);
				}
			}
			
			fr.close();
		}
	}

	@Override
	public void saveFile() throws IOException {
		FileWriter fw = new FileWriter(getFile());
		JsonObject jsonObject = new JsonObject();
		
		JsonArray friendsArray = new JsonArray();
		
		for(String s : DeltaAPI.getClient().getFriendManager().getFriends()) {
			JsonObject friendsArrayObject = new JsonObject();
			friendsArrayObject.addProperty("name", s);
			friendsArray.add(friendsArrayObject);
		}
		
		jsonObject.add("friends", friendsArray);
		
		fw.write(getGson().toJson(jsonObject));
		fw.close();
	}

}
