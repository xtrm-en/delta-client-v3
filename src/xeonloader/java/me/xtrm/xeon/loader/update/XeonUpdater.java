package me.xtrm.xeon.loader.update;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.util.List;
import java.util.Map;

import me.xtrm.xeon.loader.utils.IOUtils;

public class XeonUpdater {

	private IUpdaterCallback callback;
	private File jarFile;
	
	public XeonUpdater(IUpdaterCallback callback) {
		this.callback = callback;
	}

	public void performUpdate() throws Exception {
		File versionsFile = IOUtils.getVersionsDir();
		String latestVer = IOUtils.readUrl(new URL("https://raw.githubusercontent.com/nkosmos/xdelta/master/latest"));
		File latestVersionDir = new File(versionsFile, latestVer);
		jarFile = new File(latestVersionDir, "deltaloader-" + latestVer + ".jar");
		
		if(jarFile.exists()) {
			if(Files.size(jarFile.toPath()) > 0)
				return;
			else
				jarFile.delete(); // corrupted file
		}
		
		callback.start();
		
		if(!latestVersionDir.exists()) {
			latestVersionDir.mkdirs();
		}
		
		URL url = new URL("https://nkosmos.github.io/maven/me/xtrm/delta/deltaloader/" + latestVer + "/deltaloader-" + latestVer + ".jar");
		
	    HttpURLConnection conn2 = (HttpURLConnection)url.openConnection();
	    Map<String, List<String>> header2 = conn2.getHeaderFields();
	    while(IOUtils.isRedirected(header2)) {
	        url = new URL(header2.get("Location").get(0));
	        conn2 = (HttpURLConnection)url.openConnection();
	        header2 = conn2.getHeaderFields();
	    }
	    
		BufferedInputStream bis = new BufferedInputStream(conn2.getInputStream());
		BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(jarFile));
		
		callback.setMax(bis.available());
		
		byte[] data = new byte[1024];
        int x = 0;
        while ((x = bis.read(data, 0, 1024)) >= 0) {
            bos.write(data, 0, x);
            callback.setCurrent(callback.getCurrent() + x);
        }
		
		bos.close();
		bis.close();				
		conn2.disconnect();
		
		callback.finished();
	}
	
	public File getDeltaLoader() {
		return this.jarFile;
	}
	
}
