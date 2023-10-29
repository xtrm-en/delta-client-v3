package me.xtrm.xeon.loader.classloading;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLConnection;
import java.security.CodeSigner;
import java.security.CodeSource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Predicate;
import java.util.jar.Attributes;
import java.util.jar.Attributes.Name;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.Manifest;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.ClassNode;

import me.xtrm.xeon.loader.api.classloading.IXeonClassLoader;
import me.xtrm.xeon.loader.api.transform.ITransformer;
import me.xtrm.xeon.loader.utils.ReflectedClass;
import me.xtrm.xeon.loader.utils.classData.ClassDataProvider;
import net.minecraft.launchwrapper.IClassNameTransformer;
import net.minecraft.launchwrapper.IClassTransformer;
import net.minecraft.launchwrapper.LaunchClassLoader;
import net.minecraft.launchwrapper.LogWrapper;

/**
 * Xeon's ClassLoader
 * 
 * Classes shouldn't be loaded by LaunchWrapper at all cost.
 * If we cannot load them, load them via the System ClassLoader.
 * 
 * @author xTrM_
 */
public class XeonClassLoader extends URLClassLoader implements IXeonClassLoader {

	private final Logger logger;
	public final List<ITransformer> deltaTransformers = new ArrayList<>();
	
	public static final int BUFFER_SIZE = 1 << 12;
	
	private List<URL> sources;
    private ClassLoader parent;

    private List<IClassTransformer> transformers = new ArrayList<IClassTransformer>(2);
    public Map<String, Class<?>> cachedClasses = new ConcurrentHashMap<String, Class<?>>();
    private Set<String> invalidClasses = new HashSet<String>(1000);

    private Set<String> classLoaderExceptions = new HashSet<String>();
    private Set<String> transformerExceptions = new HashSet<String>();
    private Map<Package, Manifest> packageManifests = new ConcurrentHashMap<Package, Manifest>();
    private Map<String, byte[]> resourceCache = new ConcurrentHashMap<String, byte[]>(1000);
    private Set<String> negativeResourceCache = Collections.newSetFromMap(new ConcurrentHashMap<String, Boolean>());

    private IClassNameTransformer renameTransformer;
    
    private static final Manifest EMPTY = new Manifest();

    private final ThreadLocal<byte[]> loadBuffer = new ThreadLocal<byte[]>();

    private static final String[] RESERVED_NAMES = {"CON", "PRN", "AUX", "NUL", "COM1", "COM2", "COM3", "COM4", "COM5", "COM6", "COM7", "COM8", "COM9", "LPT1", "LPT2", "LPT3", "LPT4", "LPT5", "LPT6", "LPT7", "LPT8", "LPT9"};
	
	public XeonClassLoader(LaunchClassLoader lcl) {
		super(lcl.getSources().toArray(new URL[lcl.getSources().size()]), null);
		
		this.logger = LogManager.getLogger("XCL");
		
		setup(lcl);
		
		this.transformers.add(new IClassTransformer() {
			@Override
			public byte[] transform(String name, String transformedName, byte[] basicClass) {
				boolean sc = transformedName.contains("$");
				Predicate<ITransformer> filter = t -> t.isTarget(transformedName, sc);
				
				if(!deltaTransformers.stream().anyMatch(filter)) return basicClass;
    			
    			ClassNode cn = new ClassNode();
    			ClassReader cr = new ClassReader(basicClass);
    			cr.accept(cn, ClassReader.EXPAND_FRAMES);
    			
    			deltaTransformers.stream().filter(filter).forEach(t -> t.transform(cn, transformedName));
    			
    			ClassWriter cw = ClassDataProvider.INSTANCE.newClassWriter();
    			cn.accept(cw);
    			return cw.toByteArray();	
			}
		});
	}
	
	@SuppressWarnings("serial")
	@Override
	public void setup(LaunchClassLoader initializer) {
		try {
			ReflectedClass rc = ReflectedClass.of(initializer);
			
			// set some base vars in here
			this.parent = rc.get0("parent");
			this.renameTransformer = rc.get0("renameTransformer");
			
			// the lists (we copy the same instance)
			this.sources = rc.get0("sources");
			this.transformers = rc.get0("transformers");
			this.invalidClasses = rc.get0("invalidClasses");
			this.classLoaderExceptions = rc.get0("classLoaderExceptions");
			this.transformerExceptions = rc.get0("transformerExceptions");
			this.packageManifests = rc.get0("packageManifests");
			this.resourceCache = rc.get0("resourceCache");
			this.negativeResourceCache = rc.get0("negativeResourceCache");

			// OK so
			// we first need to copy the classes, then create our own array, then set the cachedClasses array instance in LCL to a Reflective one
			
			Map<String, Class<?>> classes = rc.get0("cachedClasses");
			this.cachedClasses = new ConcurrentHashMap<>(classes);
			
			rc.set0("cachedClasses", 
					new ConcurrentHashMap<String, Class<?>>(classes){
						public boolean containsKey(Object key) {
							if(key instanceof String) {
								if(XeonClassLoader.this.cachedClasses.containsKey((String)key)) {
									logger.info("[DEBUG] Loading class: " + key + " from subload");
									super.put((String) key, XeonClassLoader.this.cachedClasses.get((String)key));
								}
							}
							return super.containsKey(key);
						};
					}
			);
		} catch(ReflectiveOperationException e) {
			e.printStackTrace();
			System.exit(-1); // throws an ExitTrappedException, good enough
		}
	}
	
	/**
	 * We still need to use the same procedure as LCL for transformers to apply and packages to be correctly defined
	 * ... so just copy paste everything in
	 * 
	 * oh also implement komodo
	 */
	@Override
	protected Class<?> findClass(String name) throws ClassNotFoundException {		
		if (invalidClasses.contains(name)) {
            throw new ClassNotFoundException(name);
        }

        for (final String exception : classLoaderExceptions) {
            if (name.startsWith(exception)) {
                return parent.loadClass(name);
            }
        }

        if (cachedClasses.containsKey(name)) {
            return cachedClasses.get(name);
        }
        
        this.logger.info("Loading {}", name);

        try {
        	long start = System.currentTimeMillis();
            Class<?> komodoClass = loadKomodoClass(name);
            if(komodoClass != null) {
            	logger.info("Loaded komodo class: {} (Took {}ms)", name, System.currentTimeMillis() - start);
            	return komodoClass;
            }
        } catch(Exception e) {
        	logger.error("Error while loading class as Komodo class: {}", name);
        	e.printStackTrace();
        }
        
        for (final String exception : transformerExceptions) {
            if (name.startsWith(exception)) {
                try {
                    final Class<?> clazz = super.findClass(name);
                    cachedClasses.put(name, clazz);
                    return clazz;
                } catch (ClassNotFoundException e) {
                    invalidClasses.add(name);
                    throw e;
                }
            }
        }

        try {
            final String transformedName = transformName(name);
            if (cachedClasses.containsKey(transformedName)) {
                return cachedClasses.get(transformedName);
            }

            final String untransformedName = untransformName(name);

            final int lastDot = untransformedName.lastIndexOf('.');
            final String packageName = lastDot == -1 ? "" : untransformedName.substring(0, lastDot);
            final String fileName = untransformedName.replace('.', '/').concat(".class");
            URLConnection urlConnection = findCodeSourceConnectionFor(fileName);

            CodeSigner[] signers = null;

            if (lastDot > -1 && !untransformedName.startsWith("net.minecraft.")) {
                if (urlConnection instanceof JarURLConnection) {
                    final JarURLConnection jarURLConnection = (JarURLConnection) urlConnection;
                    final JarFile jarFile = jarURLConnection.getJarFile();

                    if (jarFile != null && jarFile.getManifest() != null) {
                        final Manifest manifest = jarFile.getManifest();
                        final JarEntry entry = jarFile.getJarEntry(fileName);

                        Package pkg = getPackage(packageName);
                        getClassBytes(untransformedName);
                        signers = entry.getCodeSigners();
                        if (pkg == null) {
                            pkg = definePackage(packageName, manifest, jarURLConnection.getJarFileURL());
                            packageManifests.put(pkg, manifest);
                        } else {
                            if (pkg.isSealed() && !pkg.isSealed(jarURLConnection.getJarFileURL())) {
                                LogWrapper.severe("The jar file %s is trying to seal already secured path %s", jarFile.getName(), packageName);
                            } else if (isSealed(packageName, manifest)) {
                                LogWrapper.severe("The jar file %s has a security seal for path %s, but that path is defined and not secure", jarFile.getName(), packageName);
                            }
                        }
                    }
                } else {
                    Package pkg = getPackage(packageName);
                    if (pkg == null) {
                        pkg = definePackage(packageName, null, null, null, null, null, null, null);
                        packageManifests.put(pkg, EMPTY);
                    } else if (pkg.isSealed()) {
                        LogWrapper.severe("The URL %s is defining elements for sealed path %s", urlConnection.getURL(), packageName);
                    }
                }
            }

            final byte[] transformedClass = runTransformers(untransformedName, transformedName, getClassBytes(untransformedName));
            final CodeSource codeSource = urlConnection == null ? null : new CodeSource(urlConnection.getURL(), signers);
            final Class<?> clazz = defineClass(transformedName, transformedClass, 0, transformedClass.length, codeSource);
            cachedClasses.put(transformedName, clazz);
            return clazz;
        } catch (Throwable e) {
            invalidClasses.add(name);
            throw new ClassNotFoundException(name, e);
        }
	}

	public Class<?> loadKomodoClass(String name) { return null; }

	@Override
	public void registerTransformers(ITransformer... transformers) {
		Collections.addAll(deltaTransformers, transformers);
	}
	
	private byte[] runTransformers(final String name, final String transformedName, byte[] basicClass) {
		byte[] classBuffer = basicClass;
		for (final IClassTransformer transformer : transformers) {
            byte[] newBuffer = transformer.transform(name, transformedName, basicClass);
            if(newBuffer == null) continue;
            classBuffer = newBuffer;
        }
        return classBuffer;
    }
	
	@Override
	public void addURL(URL url) {
		this.sources.add(url);
		super.addURL(url);
	}
	
	private byte[] getOrCreateBuffer() {
        byte[] buffer = loadBuffer.get();
        if (buffer == null) {
            loadBuffer.set(new byte[BUFFER_SIZE]);
            buffer = loadBuffer.get();
        }
        return buffer;
    }
	
	private byte[] readFully(InputStream stream) {
        try {
            byte[] buffer = getOrCreateBuffer();

            int read;
            int totalLength = 0;
            while ((read = stream.read(buffer, totalLength, buffer.length - totalLength)) != -1) {
                totalLength += read;

                // Extend our buffer
                if (totalLength >= buffer.length - 1) {
                    byte[] newBuffer = new byte[buffer.length + BUFFER_SIZE];
                    System.arraycopy(buffer, 0, newBuffer, 0, buffer.length);
                    buffer = newBuffer;
                }
            }

            final byte[] result = new byte[totalLength];
            System.arraycopy(buffer, 0, result, 0, totalLength);
            return result;
        } catch (Throwable t) {
            LogWrapper.log(Level.WARN, t, "Problem loading class");
            return new byte[0];
        }
    }
	
	private String untransformName(final String name) {
        if (renameTransformer != null) {
            return renameTransformer.unmapClassName(name);
        }

        return name;
    }

    private String transformName(final String name) {
        if (renameTransformer != null) {
            return renameTransformer.remapClassName(name);
        }

        return name;
    }

    private boolean isSealed(final String path, final Manifest manifest) {
        Attributes attributes = manifest.getAttributes(path);
        String sealed = null;
        if (attributes != null) {
            sealed = attributes.getValue(Name.SEALED);
        }

        if (sealed == null) {
            attributes = manifest.getMainAttributes();
            if (attributes != null) {
                sealed = attributes.getValue(Name.SEALED);
            }
        }
        return "true".equalsIgnoreCase(sealed);
    }

    private URLConnection findCodeSourceConnectionFor(final String name) {
        final URL resource = findResource(name);
        if (resource != null) {
            try {
                return resource.openConnection();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        return null;
    }
    
    public byte[] getClassBytes(String name) throws IOException {
        if (negativeResourceCache.contains(name)) {
            return null;
        } else if (resourceCache.containsKey(name)) {
            return resourceCache.get(name);
        }
        if (name.indexOf('.') == -1) {
            for (final String reservedName : RESERVED_NAMES) {
                if (name.toUpperCase(Locale.ENGLISH).startsWith(reservedName)) {
                    final byte[] data = getClassBytes("_" + name);
                    if (data != null) {
                        resourceCache.put(name, data);
                        return data;
                    }
                }
            }
        }

        InputStream classStream = null;
        try {
            final String resourcePath = name.replace('.', '/').concat(".class");
            final URL classResource = findResource(resourcePath);

            if (classResource == null) {
                negativeResourceCache.add(name);
                return null;
            }
            classStream = classResource.openStream();

            final byte[] data = readFully(classStream);
            resourceCache.put(name, data);
            return data;
        } finally {
            closeSilently(classStream);
        }
    }
    
    private static void closeSilently(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException ignored) {
            }
        }
    }

}
