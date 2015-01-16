package aak.as.tools;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Iterator;
import java.util.ServiceConfigurationError;
import java.util.ServiceLoader;
import java.util.jar.Attributes;
import java.util.jar.JarFile;
import java.util.jar.Manifest;



/**
 * A class for loading AllSummerizer plugin Jars
 * @author Abdelkrime Aries
 */
public class JarLoader {

	private URLClassLoader classLoader = null;
	private String extName;
	private String extVersion;

	/**
	 * Create a new JarLoader
	 * @param location the location where we can find the Jar Files (must end with "/")
	 * @param extensionName to compare it with "Extension-Name" in 
	 * META-INF/MANIFEST.MF File of the Jar
	 * @param extensionVersion to compare it with "Specification-Version" in
	 * META-INF/MANIFEST.MF File of the Jar
	 */
	public JarLoader(String location, String extensionName, String extensionVersion) {
		
		extName = extensionName;
		extVersion = extensionVersion;
		
		//extName and extVersion must be affected before loading the Jar files
		classLoader = loadJars(location);
		
	}

	
	/**
	 * Get an instance of the class that implements @Info in the Jar, 
	 * given a language
	 * @param lang the language code (ISO 639-1); eg. "ar", "cs", "en", "ja"
	 * @param infoClass the class that extends @Info; eg. @PreProcessInfo
	 * @return an instance of @PreProcessInfo specific for the language needed
	 */
	public <T extends Info> T getInfoService(String lang, Class<T> infoClass){

		if (classLoader == null) return null;
		
		ServiceLoader<T> sl = ServiceLoader.load(infoClass, classLoader);
		Iterator<T> it = sl.iterator();
		T info = null;
		it = sl.iterator();
		while (it.hasNext()){
			info = it.next();
			if (lang.equals(info.getISO639_1())) return info;
		}

		return null;

	}
	

	/**
	 * Get an instance of the class that implements a service (or a class cls)
	 * @param info the info file where we want to get the service
	 * @param cls the class representing the service we looking for
	 * @return an instance of the class that implements a service cls
	 */
	public <T> T getLangService(Info info, Class<T> cls){

		if (classLoader == null) return null;
		
		String packageName = info.getClass().getName();
		packageName = packageName.substring(0, packageName.lastIndexOf("."));
			
		{
			String className = packageName + "." + info.getPrefix() + cls.getSimpleName();
		
			T service = getServiceByName(className, cls);
			
			if (service != null) return service;
		}
		
		
		return FindService(packageName, cls);

	}
	
	
	/**
	 * 
	 * @param className
	 * @param cls
	 * @return
	 */
	private <T> T getServiceByName (String className, Class<T> cls){

		try {

			Class<?> cls2 = classLoader.loadClass(className);
			T service = (T) cls2.newInstance();
			return service;
		} catch (ClassNotFoundException e) {
			System.out.println("ClassNotFoundException: " + className);
			return null;
		} catch (InstantiationException e) {
			System.out.println("InstantiationException: " + className);
			return null;
		} catch (IllegalAccessException e) {
			System.out.println("IllegalAccessException: " + className);
			return null;
		}

	}
	

	private <T> T FindService(String packageName, Class<T> cls){
		
		ServiceLoader<T> sl = ServiceLoader.load(cls, classLoader);
		Iterator<T> it = sl.iterator();
		
		it = sl.iterator();
		while (it.hasNext()){
			T service;
			try{
				service = it.next();
			} catch (ServiceConfigurationError e){
				System.out.println("ServiceConfigurationError: " + cls.getName());
				return null;
			}
			
			if (service.getClass().getName().contains(packageName)) 
				return service;
		}

		return null;

	}

	private URLClassLoader loadJars(String location){

		File preprocessFolder = new File(location);

		File [] jarFiles  = preprocessFolder.listFiles(new JarFileFilter());

		if (jarFiles.length <1) return null;

		URL [] jarURLs = new URL[jarFiles.length];
		for(int i=0; i< jarFiles.length; i++)
			try {
				jarURLs[i] = jarFiles[i].toURI().toURL();
			} catch (MalformedURLException e) {
				System.out.println("MalformedURLException");
				return null;
			}

		URLClassLoader ucl = new URLClassLoader(jarURLs);

		return ucl;
	}

	private class JarFileFilter implements FilenameFilter {
		
		@Override
		public boolean accept(File dir, String name) {
			if (! name.toLowerCase().endsWith(".jar")) return false;
			try {
				JarFile jf = new JarFile(dir.getPath() + "/" + name);
				Manifest manifest = jf.getManifest();
				Attributes attributes = manifest.getMainAttributes();
				{
					String extensionName = 
							attributes.getValue("Extension-Name");
					if (extensionName == null) return false;
					System.out.println(jf.getName() + ": extension= "+ extName + ", found="+ extensionName);
					if (! extensionName.equals(extName)) return false;
				}

			} catch (IOException e) {
				return false;
			} catch (IllegalArgumentException e) {
				return false;
			}

			return true;
		}

	}

}
