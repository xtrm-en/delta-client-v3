package me.xtrm.xeon.loader;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.LoggerContext;

import me.xtrm.delta.loader.api.LoaderProvider;
import me.xtrm.xeon.loader.api.IXeonLoader;
import me.xtrm.xeon.loader.api.classloading.IXeonClassLoader;
import me.xtrm.xeon.loader.classloading.XeonClassLoader;
import me.xtrm.xeon.loader.classloading.transform.XeonPatcher;
import me.xtrm.xeon.loader.update.XeonUpdater;
import me.xtrm.xeon.loader.update.swing.XeonUpdaterApp;
import net.minecraft.launchwrapper.Launch;
import net.minecraft.launchwrapper.LaunchClassLoader;

public class XeonLoader implements IXeonLoader {

	private final Logger logger;
	private final boolean devEnv;

	private XeonUpdater xeonUpdater;

	private boolean classWrapping;

	private LaunchClassLoader lcl;
	private XeonClassLoader xcl;

	public XeonLoader() {
		initLog4j();
		
		this.logger = LogManager.getLogger("XeonLoader");
		this.devEnv = Boolean.parseBoolean(System.getProperty("delta.devenv", "false"));
	}
	
	private void initLog4j() {
		LoggerContext context = (LoggerContext) LogManager.getContext(false);
		try {
			context.setConfigLocation(getClass().getResource("/override_log4j2.xml").toURI());
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void init(List<String> launchArgs, File gameDir) {
		gameDir = gameDir == null ? new File(".") : gameDir;

		this.logger.info("Initializing XeonLoader v3.0.0");
		this.logger.info("Game DataDir: " + gameDir.getAbsolutePath());

		if (!devEnv) {
			logger.info("Initializing Updater...");
			this.xeonUpdater = new XeonUpdater(new XeonUpdaterApp());

			try {
				logger.info("Performing update...");
				this.xeonUpdater.performUpdate();
			} catch (Exception e) {
				logger.error("Error while updating client:");
				e.printStackTrace();
			}
		}

		this.logger.info("Setting-up classloading toolchain...");

		this.lcl = Launch.classLoader;
		this.lcl.addClassLoaderExclusion("me.xtrm.delta.client.api.");
		this.lcl.addClassLoaderExclusion("me.xtrm.delta.loader.api.");
		this.lcl.addClassLoaderExclusion("me.xtrm.xeon.loader.api.");

		this.xcl = new XeonClassLoader(this.lcl);
		this.xcl.registerTransformers(new XeonPatcher());
	}

	@Override
	public void postInit(LaunchClassLoader launchClassLoader) {
		if (!devEnv) {
			File deltaLoaderJar = this.xeonUpdater.getDeltaLoader();
			injectDelta(deltaLoaderJar);
		}

		this.setClassWrapping(true);

		LoaderProvider.getLoader().initialize();
		LoaderProvider.getLoader().getTransformers().forEach(this.xcl::registerTransformers);

		this.setClassWrapping(false);
	}

	/** Injects Delta into the current classpath */
	private void injectDelta(File deltaLoaderJar) {
		try {
			this.xcl.addURL(deltaLoaderJar.toURI().toURL());
		} catch (MalformedURLException e) {
			throw new RuntimeException("Couldn't add DeltaLoader to URLClassPath", e);
		}
	}

	@Override
	public IXeonClassLoader getXCL() {
		return xcl;
	}

	@Override
	public LaunchClassLoader getLCL() {
		return lcl;
	}

	@Override
	public boolean isClassWrappingEnabled() {
		return classWrapping;
	}

	@Override
	public void setClassWrapping(boolean state) {
		if (this.classWrapping != state) {
			this.classWrapping = state;

			this.logger.info("{} classwrapping", (state ? "Enabling" : "Disabling"));

			if (state)
				Thread.getAllStackTraces().keySet().forEach(t -> t.setContextClassLoader(this.xcl));
			else
				Thread.getAllStackTraces().keySet().forEach(t -> t.setContextClassLoader(this.lcl));
		}
	}

}
