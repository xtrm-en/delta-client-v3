package me.xtrm.delta.client.utils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;

import me.xtrm.delta.client.utils.render.RenderUtils;

public class CachedResource {

	private URL url;
	private File file;
	
	public CachedResource(String url) {
		
		try {
			this.url = new URL(url);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		
		this.file = new File(IOUtils.getAssetsDir(), EncryptionHelper.MD5(url.toString()));
		
		if(!file.exists()) {
			file.getParentFile().mkdirs();
			
			LogManager.getLogger("CachedResource").info("Downloading " + url);
			
			try {
				URL tempUrl = this.url;
			    HttpURLConnection conn = (HttpURLConnection)tempUrl.openConnection();
			    Map<String, List<String>> header = conn.getHeaderFields();
			    while(isRedirected(header)) {
			    	tempUrl = new URL(header.get("Location").get(0));
			        conn = (HttpURLConnection)tempUrl.openConnection();
			        header = conn.getHeaderFields();
			    }			
				
				InputStream in = conn.getInputStream();
				Files.copy(in, file.toPath(), StandardCopyOption.REPLACE_EXISTING);
				
				conn.disconnect();
			} catch(IOException e) {
				e.printStackTrace();
			}
		}
		
		if(url.toString().endsWith(".png") || url.toString().endsWith(".jpg")) {
			RenderUtils.loadCachedResource(this);
		}
	}
	
	private boolean isRedirected(Map<String, List<String>> header) {
		for(String hv : header.get(null)) {
			if(hv.contains("301") || hv.contains("302")) return true;
	    }
	    return false;
	}
	
	public URL getURL() {
		return url;
	}
	
	public File getFile() {
		return file;
	}
	
}
