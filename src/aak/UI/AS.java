/*
 * AllSumarizer v2
 * This file is part of AllSummarizer project; an implementation of the method
 * described in this paper:
 * http://dx.doi.org/10.1117/12.2004001
 * 
 * Copyright (C) 2013  Abdelkrime Aries <kariminfo0@gmail.com>
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package aak.UI;

import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;

import aak.UI.GUI.GUI;

public class AS {

	public static void main(String[] args) {
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
		//GUI.
	}

}

/*
My name is Karim, and I study informatics at ESI, which is at Algiers, to obtain Magister degree.
My research in ESI is about ATS, it is the intersection between IR and NLP.
In This research, the main idea is to find relevant sentences using IR technics.
The statistical features are the power of IR to find relevance.
AI technics are used, such as learning algorithms to create models for each topic in the input text.
	   
 */
