package me.xtrm.delta.loader.library;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.common.collect.Lists;

import me.xtrm.delta.loader.api.LoaderProvider;
import me.xtrm.delta.loader.api.library.ILibrary;
import me.xtrm.delta.loader.api.library.ILibraryManager;
import me.xtrm.delta.loader.api.library.Library;
import me.xtrm.delta.loader.utils.IOUtils;
import me.xtrm.xeon.loader.api.XeonProvider;

public class LibraryManager implements ILibraryManager {

	private List<URL> repositories;
	private List<ILibrary> libraries;
	
	private Logger logger;
	
	@Override
	public void init() {
		this.repositories = Lists.newArrayList();
		this.libraries = Lists.newArrayList();
		
		this.logger = LogManager.getLogger("LibraryManager");
		
		this.logger.info("Searching for repositories...");
		LoaderProvider.getLoaderData().getRepositories().forEach(r -> {
			try {
				repositories.add(new URL(r));
			} catch (MalformedURLException e) {
				e.printStackTrace();
			}
		});

		this.logger.info("Searching for libraries...");
		LoaderProvider.getLoaderData().getLibraries().forEach(l -> {
			this.libraries.add(new Library(l));
		});
		
		if(Boolean.getBoolean(System.getProperty("delta.devenv", "false")))
			this.libraries.add(new Library("me.xtrm.delta:delta-client:b4"));
		
		this.logger.info("Populated with " + this.libraries.size() + " libraries on " + this.repositories.size() + " repositories!");
		
		this.libraries.forEach(this::loadLibrary);
	}
	
	private Method addurlMethod;
	
	@Override
	public void loadLibrary(ILibrary lib) {
		this.logger.info("Loading " + lib.getGroup() + "." + lib.getName() + " version " + lib.getVersion());
		
		File jarFile = new File(IOUtils.getLibrariesDir(), lib.getFilePath().replace('/', File.separatorChar));
		if(!jarFile.exists()) {
			downloadLibrary(lib, jarFile);
		}
		
		URL url = null;
		try {
			url = jarFile.toURI().toURL();
		} catch (MalformedURLException e) {
			this.logger.error("Couldn't get library {} jarfile url... wierd", lib.getLibraryDeclaration());
			throw new RuntimeException(e);
		}
		XeonProvider.getXeonLoader().getXCL().addURL(url);	
	}
	
	private void downloadLibrary(ILibrary lib, File jarFile) {
		this.logger.info("Downloading library...");
		
		URL fileUrl = repositories.stream().filter(url -> {
			try {
				return existsRemotely(new URL(url, lib.getFilePath()));
			} catch (MalformedURLException e) {
				e.printStackTrace();
			}
			return false;
		}).findFirst().orElse(null);
		
		if(fileUrl == null) {
			this.logger.error("Couldn't find library " + lib.getLibraryDeclaration() + " in any repositories!");
			throw new RuntimeException();
		}
		
		URL url = fileUrl;
		
		try {
			HttpURLConnection conn = (HttpURLConnection)url.openConnection();
		    
			// find the correct connection
			Map<String, List<String>> header = conn.getHeaderFields(); 
		    while(isRedirected(header)) {
		        url = new URL(header.get("Location").get(0));
		        conn = (HttpURLConnection)url.openConnection();
		        header = conn.getHeaderFields();
		    }
			
		    InputStream in = conn.getInputStream();
			Files.copy(in, jarFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
			conn.disconnect();
		} catch(IOException e) {
			this.logger.error("Couldn't download library " + lib.getLibraryDeclaration());
			throw new RuntimeException(e);
		}
	}
	
	@Override
	public List<ILibrary> getLibraries() {
		return this.libraries;
	}
	
	private boolean existsRemotely(URL url) {
		try {
			HttpURLConnection conn = (HttpURLConnection)url.openConnection();
		    conn.setRequestMethod("HEAD");
		    Map<String, List<String>> header = conn.getHeaderFields();
		    while(isRedirected(header)) {
		        url = new URL(header.get("Location").get(0));
		        conn = (HttpURLConnection)url.openConnection();
		        conn.setRequestMethod("HEAD");
		        header = conn.getHeaderFields();
		    } 
		    System.out.println(conn + ", " + conn.getResponseCode());
		    return (conn.getResponseCode() == HttpURLConnection.HTTP_OK);
		} catch(IOException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	private boolean isRedirected(Map<String, List<String>> header) {
		for(String hv : header.keySet()) {
			List<String> h = header.get(hv);
			if(h.contains("301") || h.contains("302")) return true;
	    }
	    return false;
	}

}
