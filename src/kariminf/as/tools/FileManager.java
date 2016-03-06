/*
 * This file is part of AllSummarizer project
 * 
 * Copyright 2014-2015 Abdelkrime Aries <kariminfo0@gmail.com>
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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;

/**
 * This class is used to manage files
 * 
 * @author Abdelkrime Aries
 * 
 */
public class FileManager {

	/**
	 * Saves a string into a specific file path.
	 * 
	 * @param filePath file's path
	 * @param content the content to be saved
	 * @throws IOException when writing the file
	 */
	public static void saveFile(String filePath, String content)
			throws IOException {
		Writer out = new OutputStreamWriter(new FileOutputStream(filePath), "UTF-8");
		
		try {
			out.write(content);
		} finally {
			out.close();
		}
	}

	
	/**
	 * Reads a textual file and returns its content.
	 * 
	 * @param f the textual file to be read.
	 * @return the content of the file
	 */
	public static String readFile(File f) {
		try {
			String contents = "";

			BufferedReader input = new BufferedReader(new FileReader(f));

			for (String line = input.readLine(); line != null; line = input
					.readLine()) {
				contents += " " + line + System.lineSeparator();
			}
			input.close();

			return contents;

		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
			return null;
		}
	}

	
	/**
	 * Creates a folder
	 * 
	 * @param dirName the folder path
	 * @return true if created, false otherwise
	 */
	public static boolean createFolder(String dirName) {

		File dir = new File(dirName);

		if (dir.exists()) {
			// System.out.println("The directory already exists");
			return false;
		}

		try {
			dir.mkdir();
		} catch (SecurityException se) {
			System.out.println("Can't create the directory!!");
			return false;
		}
		
		System.out.println("folder" + dirName + "created");

		return true;
	}

}
