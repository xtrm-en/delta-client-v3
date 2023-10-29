package me.xtrm.delta.client.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class WebUtils {
	
	public static void checkInternet() {
		if(!hasInternetConnection()) {
			Wrapper.exitGame();
		}
	}
	
	public static boolean hasInternetConnection() {
	    try {
	    	URL url = new URL("https://www.google.com");
	        URLConnection conn = url.openConnection();
	        
	        conn.addRequestProperty("User-Agent", "Mozilla/5.0 AppleWebKit/537.36 (KHTML, like Gecko) Chrome/43.0.2357.124 Safari/537.36"); // try to fix crashes
	        conn.setConnectTimeout(15000);
	        
	        conn.connect();
	        conn.getInputStream().close();
	        return true;
	    } catch (MalformedURLException e) {
	        e.printStackTrace();
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	    return false;
	}
	
	private static String apiDomain;
	public static String getWebsite() {
		if(apiDomain == null) {
			try {
				apiDomain = WebUtils.readUrl(new URL("https://raw.githubusercontent.com/nkosmos/xdelta/master/website"));
			} catch(IOException e) {
				e.printStackTrace();
			}
		}
		return apiDomain;
	}
	
	private static String discordUrl;
	private static boolean retryNext;
	public static String getDiscordUrl() {
		if(discordUrl == null || retryNext) {
			try {
				discordUrl = readUrl(new URL("https://raw.githubusercontent.com/nkosmos/xdelta/master/discord"));
				retryNext = false;
			} catch(Exception e) {
				e.printStackTrace();
				retryNext = true;
			}
		}
		return discordUrl;
	}

	public static String readUrl(URL url) throws IOException {
        url.openConnection();
        BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));
        String line;
        StringBuilder sb = new StringBuilder();
        while ((line = br.readLine()) != null) {
            sb.append(line + "\n");
        }
        String str = sb.toString();
        str = str.substring(0, str.lastIndexOf("\n"));
		return str;
	}
}
