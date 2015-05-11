/*
 * This file is part of AllSummarizer project
 * 
 * Copyright 2013-2015 Abdelkrime Aries <kariminfo0@gmail.com>
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

package dz.aak.as.ui;

import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;

import dz.aak.as.ui.gui.GUI;

public class AS {

	public static void main(String[] args) {
		//TODO add other options
		if (args==null || args.length<1)
			startGUI();
	}
	
	public static void startGUI(){
		try {
			for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
				if ("Nimbus".equals(info.getName())) {
					UIManager.setLookAndFeel(info.getClassName());
					System.out.println(info.getClassName());
					break;
				}
			}
		} catch (Exception e) {
		    // If Nimbus is not available, you can set the GUI to another look and feel.
		}
	
		new GUI();
	}

}

/*
My name is Karim, and I study informatics at ESI, which is at Algiers, to obtain Magister degree.
My research in ESI is about ATS, it is the intersection between IR and NLP.
In This research, the main idea is to find relevant sentences using IR technics.
The statistical features are the power of IR to find relevance.
AI technics are used, such as learning algorithms to create models for each topic in the input text.
	   
 */
