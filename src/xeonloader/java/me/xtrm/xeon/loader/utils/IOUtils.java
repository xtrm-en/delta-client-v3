package me.xtrm.xeon.loader.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.List;
import java.util.Map;

public class IOUtils {
	
	/** Returns the AppData folder */
	public static File getAppdata() {
		String userHome = System.getProperty("user.home", ".");
		File workingDirectory;
		switch (getPlatform()) {
			case "WINDOWS":
				String applicationData = System.getenv("APPDATA");
				String folder = applicationData != null ? applicationData : userHome;
				workingDirectory = new File(folder);
				break;
			case "MACOS":
				workingDirectory = new File(userHome, "Library/Application Support");
				break;
			default:
				workingDirectory = new File(userHome);
				break;
		}
		return workingDirectory;
	}

	/** Returns the OS's name */
	public static String getPlatform() {
		String s = System.getProperty("os.name").toLowerCase();
		return s.contains("win") ? "WINDOWS"
				: (s.contains("mac") ? "MACOS"
						: (s.contains("solaris") ? "SOLARIS"
								: (s.contains("sunos") ? "SOLARIS"
										: (s.contains("linux") ? "LINUX"
												: (s.contains("unix") ? "LINUX" : "UNKNOWN")))));
	}
	
	/** Gets the Delta Client directory */
	public static File getDeltaDir() {
		File oldDir = new File(getAppdata(), "Delta");
		File newDir = new File(getAppdata(), "Delta Client");
		if(oldDir.exists() && oldDir.isDirectory()) {
			oldDir.renameTo(newDir);
		}
		if(!newDir.exists()) newDir.mkdirs();
		return newDir;
	}
	
	/** Gets the versions cache folder */
	public static File getVersionsDir() {
		return new File(getDeltaDir(), "cache" + File.separator + "versions");
	}
	
	/** @return Is the current connection redirected? */
	public static boolean isRedirected(Map<String, List<String>> header) {
		for(String hv : header.get(null)) {
			if(hv.contains("301") || hv.contains("302")) return true;
	    }
	    return false;
	}
	
	/** Reads a URL's content */
	public static String readUrl(URL url) throws IOException {
        url.openConnection();
        BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));
        String line;
        StringBuilder sb = new StringBuilder();
        while ((line = br.readLine()) != null) {
            sb.append(line + "\n");
        }
        String str = sb.toString();
        str = str.substring(0, str.lastIndexOf("\n")); // issou
		return str;
	}

}
