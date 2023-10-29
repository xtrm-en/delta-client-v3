package me.xtrm.delta.client.management.file.impl;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import me.xtrm.delta.client.api.DeltaAPI;
import me.xtrm.delta.client.api.file.SavedFile;
import me.xtrm.delta.client.api.module.IModule;
import me.xtrm.delta.client.api.setting.ISetting;

public class Modules extends SavedFile {

	public Modules(Gson gson, File file) {
		super(gson, file);
	}

	@Override
	public void loadFile() throws IOException {
		for(IModule module : DeltaAPI.getClient().getModuleManager().getModules()) {
			if(module.getName().contains("TestModule")) continue;
			
			if(getFile(module).exists()) {
				FileReader fr = new FileReader(getFile(module));
				JsonObject jsonObject = getGson().fromJson(fr, JsonObject.class);
	            if (jsonObject == null) {
	                fr.close();
	                continue;
	            }
	            
	            if(jsonObject.has("state")) {
	            	String name = module.getName();
	            	if(!name.equalsIgnoreCase("Spammer") && !name.equalsIgnoreCase("Freecam")) {
	            		if(jsonObject.get("state").getAsBoolean()) {
		            		if(!module.isEnabled()) {
			            		module.toggle();
		            		}
		            	}
	            	}
	            }
	            
	            if(jsonObject.has("keybind")) {
	            	module.setKey(jsonObject.get("keybind").getAsInt());
	            }
	            
	            List<ISetting> currentSettings = DeltaAPI.getClient().getSettingManager().getSettingsForModule(module);
            	if(jsonObject.has("settings")) {
            		JsonArray jsonArray = (JsonArray) jsonObject.get("settings");
            		
                    try {
                    	jsonArray.forEach(jsonElement -> currentSettings.stream().filter(setting -> jsonElement.getAsJsonObject().has(setting.getDisplayName())).forEach(setting -> {
                            if (setting.isCheck()) {
                                setting.setCheckValue(jsonElement.getAsJsonObject().get(setting.getDisplayName()).getAsBoolean());
                            } else if (setting.isSlider()) {
                                setting.setSliderValue(jsonElement.getAsJsonObject().get(setting.getDisplayName()).getAsDouble());
                            } else if (setting.isCombo()){
                                setting.setComboValue(jsonElement.getAsJsonObject().get(setting.getDisplayName()).getAsString());
                            }
                        }));
                    } catch(Exception ignored) {}
            	}
	            
	            fr.close();
			}
		}
	}

	@Override
	public void saveFile() throws IOException {
		for(IModule module : DeltaAPI.getClient().getModuleManager().getModules()) {
			makeModuleFile(module);
			
			FileWriter fw = new FileWriter(getFile(module));
			JsonObject jsonObject = new JsonObject(); // the file
			
			jsonObject.addProperty("name", module.getName());
			jsonObject.addProperty("state", module.isEnabled());
			jsonObject.addProperty("keybind", module.getKey());
			
			List<ISetting> currentSettings = DeltaAPI.getClient().getSettingManager().getSettingsForModule(module);
			
			int setCount = 0;
			if(currentSettings != null) 
				for(ISetting s : currentSettings)
					if(s.isCheck() || s.isSlider() || s.isCombo())
						setCount++;
			
			
			if(setCount > 0) {
				JsonArray setArray = new JsonArray();
				JsonObject setArrayObject = new JsonObject();
				for(ISetting s : currentSettings) {
					if(s.isCheck()) {
						setArrayObject.addProperty(s.getDisplayName(), s.getCheckValue());
					}else if(s.isSlider()) {
						setArrayObject.addProperty(s.getDisplayName(), s.getSliderValue());
					}else if(s.isCombo()) {
						setArrayObject.addProperty(s.getDisplayName(), s.getComboValue());
					}
				}
				
				setArray.add(setArrayObject);
				jsonObject.add("settings", setArray);
			}
			
			fw.write(getGson().toJson(jsonObject));
			fw.close();
		}
	}
	
	private void makeModuleFile(IModule module) throws IOException {
        if (!getFile(module).exists())
            getFile(module).createNewFile();
    }
	
	private File getFile(IModule module) {
        return new File(getFile(), module.getName() + ".json");
    }

}
