/*
 * This file is part of AllSummarizer project
 * 
 * Copyright 2015 Abdelkrime Aries <kariminfo0@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package kariminf.as.tools;

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
 * A class for loading jar files
 * 
 * @author Abdelkrime Aries
 */
public class JarLoader {

	private URLClassLoader classLoader = null;
	private String extName;
	private String extVersion;

	/**
	 * Creates a new JarLoader
	 * 
	 * @param location the location where we can find the Jar Files (must end with "/")
	 * @param extensionName to compare it with "Extension-Name" in 
	 * META-INF/MANIFEST.MF file of the Jar
	 * @param extensionVersion  to compare it with "Specification-Version" in
	 *            META-INF/MANIFEST.MF file of the Jar
	 */
	public JarLoader(String location, String extensionName, String extensionVersion){

		extName = extensionName;
		extVersion = extensionVersion;

		// extName and extVersion must be affected before loading the Jar files
		classLoader = loadJars(location);

	}
	

	/**
	 * Gets an instance of the class that implements {@link Info} in the Jar,
	 * given a language
	 * 
	 * @param lang the language code (ISO 639-1); eg. "ar", "cs", "en", "ja"
	 * @param infoClass the class that extends {@link Info}; 
	 * eg. {@link PreProcessInfo}
	 * @return an instance of "infoClass" specific for the language needed
	 */
	public <T extends Info> T getInfoService(String lang, Class<T> infoClass) {

		if (classLoader == null){
			//System.out.println("No class loader");
			return null;
		}		

		ServiceLoader<T> sl = ServiceLoader.load(infoClass, classLoader);
		
		Iterator<T> it = sl.iterator();
		T info = null;
		
		while (it.hasNext()) {
			info = it.next();
			if (lang.equals(info.getISO639_1()))
				return info;
		}

		return null;

	}

	/**
	 * Gets an instance of the class that implements a service (or a class cls)
	 * 
	 * @param info the info file where we want to get the service
	 * @param cls the class representing the service we looking for
	 * @return an instance of the class that implements a service cls
	 */
	public <T> T getLangService(Info info, Class<T> cls) {

		if (classLoader == null)
			return null;
		if (info == null)
			return null;

		String packageName = info.getClass().getName();
		packageName = packageName.substring(0, packageName.lastIndexOf("."));

		{
			String className = packageName + "." + info.getPrefix()
					+ cls.getSimpleName();

			T service = getServiceByName(className, cls);

			if (service != null)
				return service;
		}

		return FindService(packageName, cls);

	}

	
	/**
	 * Gets an instance of the class that implements a service (or a class cls) 
	 * by its name
	 * 
	 * @param className the class name
	 * @param cls the class which we are looking for its service
	 * @return an instance of the class cls
	 */
	private <T> T getServiceByName(String className, Class<T> cls) {

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

	
	/**
	 * Searches for an instance of the class that implements a service (or a class cls).
	 * This is used when we fail to get the instance using the name
	 * 
	 * @param packageName the name of the jar file
	 * @param cls the class which we are looking for its service
	 * @return an instance of the class cls
	 */
	private <T> T FindService(String packageName, Class<T> cls) {

		ServiceLoader<T> sl = ServiceLoader.load(cls, classLoader);
		Iterator<T> it = sl.iterator();

		it = sl.iterator();
		while (it.hasNext()) {
			T service;
			try {
				service = it.next();
			} catch (ServiceConfigurationError e) {
				System.out.println("ServiceConfigurationError: " + cls.getName());
				return null;
			}

			// System.out.println(service.getClass().getName());

			if (service.getClass().getName().contains(packageName))
				return service;

		}

		return null;

	}

	
	/**
	 * Loads all jar files that have the wanted extension's name and version.
	 * 
	 * @param location the folder where we search for jars
	 * @return a class loader
	 */
	private URLClassLoader loadJars(String location) {

		File preprocessFolder = new File(location);

		File[] jarFiles = preprocessFolder.listFiles(new JarFileFilter());

		if (jarFiles.length < 1)
			return null;

		URL[] jarURLs = new URL[jarFiles.length];
		
		for (int i = 0; i < jarFiles.length; i++)
			
			try {
				jarURLs[i] = jarFiles[i].toURI().toURL();
			} catch (MalformedURLException e) {
				System.out.println("MalformedURLException");
				return null;
			}

		URLClassLoader ucl = new URLClassLoader(jarURLs);

		return ucl;
	}

	
	/**
	 * This class is used to filter the jars that implements the wanted extension.
	 * 
	 * @author Abdelkrime Aries
	 *
	 */
	private class JarFileFilter implements FilenameFilter {

		@Override
		public boolean accept(File dir, String name) {
			if (!name.toLowerCase().endsWith(".jar"))
				return false;
			try {
				JarFile jf = new JarFile(dir.getPath() + "/" + name);
				Manifest manifest = jf.getManifest();
				Attributes attributes = manifest.getMainAttributes();
				{
					String extensionName = attributes
							.getValue("Extension-Name");
					if (extensionName == null)
						return false;
					// System.out.println(jf.getName() + ": extension= "+
					// extName + ", found="+ extensionName);
					if (!extensionName.equals(extName))
						return false;

				}

				{
					String extensionVersion = attributes
							.getValue("Specification-Version");
					if (extensionVersion == null)
						return false;
					// System.out.println(jf.getName() + ": extension= "+
					// extName + ", found="+ extensionName);
					if (!extensionVersion.equals(extVersion))
						return false;

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
