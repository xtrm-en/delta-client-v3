package me.xtrm.xeon;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;
import java.util.UUID;
import java.util.jar.JarFile;
import java.util.regex.Pattern;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.common.hash.Hashing;

import cpw.mods.fml.relauncher.CoreModManager;
import cpw.mods.fml.relauncher.FMLInjectionData;
import me.xtrm.delta.client.utils.IOUtils;
import me.xtrm.delta.client.utils.MinecraftEnvironment;
import me.xtrm.delta.client.utils.reflect.Java9Fix;
import me.xtrm.delta.client.utils.reflect.ReflectedClass;
import me.xtrm.delta.loader.api.plugin.types.IPlugin;
import me.xtrm.delta.loader.api.plugin.types.PluginInfo;
import net.minecraft.launchwrapper.ITweaker;
import net.minecraft.launchwrapper.Launch;

@Deprecated
@PluginInfo(name = "Xeon", version = "2.1.5", author = "xTrM_")
public class XeonOld implements IPlugin {
	
	private File xeonDataFile, xeonJarFile, deltaFile;
	private String xeonJarName;
	private String xeonClassname;
	
	public XeonOld() {
		Logger logger = LogManager.getLogger("Xeon");
		
		if(!xeonFound()) {
			logger.info("Xeon wasn't found, aborting...");
			return;
		}
		
		if(xeonDataFile.exists())
			xeonDataFile.delete(); // delete the datafile
		
		if(!xeonNeedsUnloading()) {
			logger.info("Xeon doesn't need unloading, aborting...");
			return;
		}
				
		try {
			deleteModsFile();
//			unload(); 
		} catch(Exception e) {
			logger.error("Couldn't unload Xeon!");
			e.printStackTrace();
		}
	}
	
	private boolean xeonNeedsUnloading() {
		return MinecraftEnvironment.isPaladium();
	}
	
	private File xeonUnshit(File file) {
		String path = file.getAbsolutePath().replace('\\', '/');
		// C:\Users\NOXW7\AppData\Roaming\.paladium\instances\paladium-v6.5\file:\C:\Users\NOXW7\AppData\Local\Temp\4f4a9410ffcdf895c4adb880659e9b5c0dd1f23a30790684340b3eaacb045398
		int i = path.indexOf("file:/");
		if (i != -1) {
			path = path.substring(i + 6);
		}
		// C:\Users\NOXW7\AppData\Local\Temp\4f4a9410ffcdf895c4adb880659e9b5c0dd1f23a30790684340b3eaacb045398
		return new File(path);
	}
	
	private boolean xeonFound() {
		xeonDataFile = new File(IOUtils.getDeltaDir(), ".xeon");
		System.out.println("check1");
		if(!xeonDataFile.exists()) return false;
		System.out.println("check1 yeet");
		
		try {
			String[] data = new String(Files.readAllBytes(xeonDataFile.toPath())).split(Pattern.quote(";"));
			xeonJarName = data[0];
			xeonClassname = data[1];
			xeonJarFile = new File(xeonJarName);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		System.out.println("check2");

		xeonJarFile = xeonUnshit(xeonJarFile);
		
		if(!xeonJarFile.getAbsoluteFile().exists()) 
			return false;
		
		System.out.println("check2 yeet");
		
		try {
			URL url = xeonJarFile.toURI().toURL();
			List<URL> urls = Launch.classLoader.getSources();
			for(URL u : urls) {
				if(u.toString().equalsIgnoreCase(url.toString()))
					return true;
			}
			
			System.out.println("not found in sources url");
			
			if(Launch.classLoader.getClassBytes(xeonClassname) != null) {
				return true;
			}
			
			System.out.println("classbytes not found???");
			
			if(Class.forName(xeonClassname, false, Launch.classLoader) != null) {
				return true;
			}
			
			System.out.println("CLASS FORNAME NOT FOUND§§!§!!,l../§,§;!???");
			System.out.println("well fuck");
			
		} catch(Exception e) {
			e.printStackTrace();
		}
		 
		return false;
	}
	
	private void deleteModsFile() throws IOException {
		File modFolder = new File((File)FMLInjectionData.data()[6], "mods");
		for(File f : modFolder.listFiles()) {
			String name = f.getName().substring(0, f.getName().indexOf('.') == -1 ? f.getName().length() : f.getName().indexOf('.'));
			String hash = Hashing.sha256().hashString(name, StandardCharsets.UTF_8).toString();
			if(hash.equalsIgnoreCase(xeonJarFile.getName())) {
				f.delete();
				break;
			}
		}
	}
	
	/** Remove Xeon from vars so it can be GC'd */
	private void unload() throws ReflectiveOperationException, IOException {
		ReflectedClass lcl_rc = ReflectedClass.of(Launch.classLoader);
		
		// Launch#blackboard "TweakClasses" (good)
		List<String> tweakClasses = (List<String>) Launch.blackboard.get("TweakClasses");
		tweakClasses.remove(xeonClassname);
		
		// Launch#blackboard "Tweaks" (good)
		List<ITweaker> tweaks = (List<ITweaker>) Launch.blackboard.get("Tweaks");
		List<ITweaker> okdoskodo = new ArrayList<>();
		Iterator<ITweaker> iter0 = tweaks.iterator();
		while(iter0.hasNext()) {
			ITweaker it = iter0.next();
			if(it.getClass().getName().equalsIgnoreCase(xeonClassname))
				okdoskodo.add(it);
		}
		okdoskodo.forEach(tweaks::remove);
		
		// LaunchClassLoader#sources (good)
		List<URL> sources = (List<URL>) lcl_rc.get0("sources");
		List<URL> remplz = new ArrayList<>();
		Iterator<URL> iter69 = sources.iterator();
		while(iter69.hasNext()) {
			URL url = iter69.next();
			if(url.toString().equalsIgnoreCase(xeonJarFile.toURI().toURL().toString())) {
				remplz.add(url);
			}
		}
		remplz.forEach(sources::remove);
		
		// some LaunchClassLoader fields (good)
		/**
		    private Set<String> invalidClasses = new HashSet<String>(1000);
		    private Set<String> classLoaderExceptions = new HashSet<String>();
		    private Set<String> transformerExceptions = new HashSet<String>();
		    private Set<String> negativeResourceCache = Collections.newSetFromMap(new ConcurrentHashMap<String, Boolean>());
		  
		    //private Map<String, Class<?>> cachedClasses = new ConcurrentHashMap<String, Class<?>>(); // handled somewhere else
		    
		    //private Map<Package, Manifest> packageManifests = new ConcurrentHashMap<Package, Manifest>(); // can be excluded
		    //private Map<String, byte[]> resourceCache = new ConcurrentHashMap<String,byte[]>(1000); // also excluded
		*/
		for(Field f : Launch.classLoader.getClass().getDeclaredFields()) {
			Java9Fix.setAccessible(f);
			Object fieldValue = f.get(Launch.classLoader);
			if(fieldValue == null) continue;
			if(fieldValue instanceof Set<?>) {
				Set<String> set = (Set<String>)fieldValue;
				List<String> remplzme = new ArrayList<>();
				Iterator<String> iterr = set.iterator();
				while(iterr.hasNext()) {
					String str = iterr.next();
					if(str.equalsIgnoreCase(xeonClassname))
						remplzme.add(str);
					if(str.equalsIgnoreCase(xeonClassname.replace('.', '/')))
						remplzme.add(str);
				}
				remplzme.forEach(set::remove);
				f.set(Launch.classLoader, set);
			}
		}
		
		// LaunchClassLoader#cachedClasses (good)
		Map<String, Class<?>> cachedClasses = (Map<String, Class<?>>)lcl_rc.get0("cachedClasses");
		List<String> removeMEEEEEE = new ArrayList<>();
		Set<String> keySet = cachedClasses.keySet();
		for(String str : keySet) {
			if(str.equalsIgnoreCase(xeonClassname))
				removeMEEEEEE.add(str);
			if(str.equalsIgnoreCase(xeonClassname.replace('.', '/')))
				removeMEEEEEE.add(str);
		}
		removeMEEEEEE.forEach(cachedClasses::remove);
		
		// CoreModManager#loadedCoremods (good)
		ReflectedClass cmm_rc = ReflectedClass.forName(CoreModManager.class.getName());
		List<String> loadedCoremods = (List<String>)cmm_rc.get0("loadedCoremods");
		loadedCoremods.remove(xeonJarFile.getName());
		
		// CoreModManager#tweakSorting (good)
		Map<String,Integer> tweakSorting = (Map<String,Integer>)cmm_rc.get0("tweakSorting");
		tweakSorting.remove(xeonClassname);
		
		// URLClassLoader#ucp#urls (good)
		String xeonUrl = xeonJarFile.toURI().toURL().toString();
		
		Stack<URL> urls = (Stack<URL>)lcl_rc.get("ucp").get0("urls");
		Iterator<URL> iter = urls.iterator();
		List<URL> removv = new ArrayList<>();
		while(iter.hasNext()) {
			URL url = iter.next();
			if(url.toString().equalsIgnoreCase(xeonUrl)) {
				removv.add(url);
			}
		}
		removv.forEach(urls::remove);
		
		// URLClassLoader#ucp#path (good)
		ArrayList<URL> path = (ArrayList<URL>)lcl_rc.get("ucp").get0("path");
		List<URL> zokaiojkfa = new ArrayList<>();
		Iterator<URL> iter2 = path.iterator();
		while(iter2.hasNext()) {
			URL url = iter2.next();
			if(url.toString().equalsIgnoreCase(xeonUrl)) {
				zokaiojkfa.add(url);
			}
		}
		zokaiojkfa.forEach(path::remove);
		
		// URLClassLoader#ucp#loaders (good)	
		// sum classes that r useful
		Class<?> jarLoaderClass = Class.forName("sun.misc.URLClassPath$JarLoader");
		
		ArrayList<Object> loaders = (ArrayList<Object>)lcl_rc.get("ucp").get0("loaders");
		Iterator<Object> iter3 = loaders.iterator();
		List<Object> toBeRemoved = new ArrayList();
		while(iter3.hasNext()) {
			Object loader = iter3.next();
			if(jarLoaderClass.isInstance(loader)) {
				// we have ourselves a JarLoader
				// lets see what the fuck it loads
				ReflectedClass rc_jl = ReflectedClass.of(loader);
				URL jarUrl = (URL)rc_jl.get0("csu");
				if(jarUrl.toString().equals(xeonUrl)) { // YOU'RE LOADING XEON?
					// FUCK YOU THEN
					toBeRemoved.add(loader);
					JarFile jar = (JarFile)rc_jl.get0("jar");
					jar.close();
				}
			}
		}
		toBeRemoved.forEach(loaders::remove);
		
		// URLClassLoader#ucp#lmap (good)
		HashMap<String, Object> lmap = (HashMap<String, Object>)lcl_rc.get("ucp").get0("lmap");
		List<String> toBeRemoved2 = new ArrayList();
		for(String s : lmap.keySet()) {
			Object loader = lmap.get(s);
			if(toBeRemoved.contains(loader)) {
				toBeRemoved2.add(s);
			}
		}
		toBeRemoved2.forEach(lmap::remove);
		
		System.out.println("running GC");
		
		// Trigger internal GC
		List<UUID> uuid = new ArrayList();
		for(int i = 0; i < 100; i++) {
			uuid.add(UUID.randomUUID());
		}
		uuid.clear(); uuid = null;
		
		System.gc();
		System.gc(); 
		
		for(int i = 0; i < 10; i++) {
			System.out.println("REMOVING XEON TRY #" + (i + 1));
			if(xeonJarFile.delete()) {
				System.out.println("YESSSSSSSSSSSSSSSSSSSSSSS");
				return;
			}
		}
		
		System.out.println("well... shit");
	}

}