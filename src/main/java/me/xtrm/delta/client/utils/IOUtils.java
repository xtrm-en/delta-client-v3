package me.xtrm.delta.client.utils;

import java.io.File;

public class IOUtils {
	
	public static File getDeltaDir() {
		File oldDir = new File(OSHelper.getAppdata(), "Delta");
		File newDir = new File(OSHelper.getAppdata(), "Delta Client");
		if(oldDir.exists() && oldDir.isDirectory()) {
			oldDir.renameTo(newDir);
		}
		return newDir;
	}
	
	public static File getCacheDir() {
		return new File(getDeltaDir(), "cache");
	}
	
	public static File getAssetsDir() {
		return new File(getCacheDir(), "assets");
	}
	
	public static File getLibrariesDir() {
		return new File(getCacheDir(), "libraries");
	}

}
