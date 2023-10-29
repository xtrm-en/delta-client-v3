package me.xtrm.delta.client.utils;

import java.io.File;

public class OSHelper {
	
	public static File getAppdata() {
		String userHome = System.getProperty("user.home", ".");
		File workingDirectory;
		switch (getPlatform()) {
			case LINUX:
				workingDirectory = new File(userHome);
				break;
			case WINDOWS:
				String applicationData = System.getenv("APPDATA");
				String folder = applicationData != null ? applicationData : userHome;
				workingDirectory = new File(folder);
				break;
			case MACOS:
				workingDirectory = new File(userHome, "Library/Application Support");
				break;
			default:
				workingDirectory = new File(userHome);
		}
		return workingDirectory;
	}

	public static OS getPlatform() {
		String s = System.getProperty("os.name").toLowerCase();
		return s.contains("win") ? OS.WINDOWS
				: (s.contains("mac") ? OS.MACOS
						: (s.contains("solaris") ? OS.SOLARIS
								: (s.contains("sunos") ? OS.SOLARIS
										: (s.contains("linux") ? OS.LINUX
												: (s.contains("unix") ? OS.LINUX : OS.UNKNOWN)))));
	}

	public static enum OS { LINUX, SOLARIS, WINDOWS, MACOS, UNKNOWN; }
	
}
