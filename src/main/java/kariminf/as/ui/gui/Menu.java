/*
 * This file is part of AllSummarizer project
 * 
 * Copyright 2013 Abdelkrime Aries <kariminfo0@gmail.com>
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

package kariminf.as.ui.gui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


import javax.swing.*;

public class Menu extends JMenuBar {

	private static final long serialVersionUID = 1L;
	
	//private static final ImageIcon newIcon = ImageHelper.loadImage("new.png");
    //private static final ImageIcon openIcon = ImageHelper.loadImage("open.png");
    //private static final ImageIcon saveIcon = ImageHelper.loadImage("save.png");
    //private static final ImageIcon filterIcon = ImageHelper.loadImage("filter.png");
    
    //private Component parent = null;
   // private ES es = null;
    //private ButtonGroup plafGroup = null;
    
    public Menu(Component aParent) {
        //parent = aParent;
       // es = (ES) aParent;
        //plafGroup = new ButtonGroup();
        setBorderPainted(true);
        JMenu subMenu = new JMenu("Submenu");
        JMenuItem subMenuItem = new JMenuItem("Submenu one");
        subMenu.add(subMenuItem);
        subMenuItem = new JMenuItem("Submenu two");
        subMenu.add(subMenuItem);
        
        JMenu menu = new JMenu("File");
        menu.setMnemonic('F');
        JMenuItem menuItem;
        /*
        menuItem = new JMenuItem("New"); //JMenuItem menuItem = new JMenuItem("New", newIcon);
        menuItem.setMnemonic('N');
        //menuItem.setAccelerator(KeyStroke.getKeyStroke('N', KeyEvent.CTRL_MASK));
        menu.add(menuItem);
        menuItem = new JMenuItem("Open...");//menuItem = new JMenuItem("Open...", openIcon);
              
        menuItem.setMnemonic('O');
        //menuItem.setAccelerator(KeyStroke.getKeyStroke('O', KeyEvent.CTRL_MASK));
        menu.add(menuItem);
        menuItem = new JMenuItem("Save...");//menuItem = new JMenuItem("Save...", saveIcon);
        menuItem.setMnemonic('S');
        //menuItem.setAccelerator(KeyStroke.getKeyStroke('S', KeyEvent.CTRL_MASK));
        menu.add(menuItem);
        menu.addSeparator();
        menu.add(subMenu);
        menu.addSeparator();*/
        menuItem = new JMenuItem("Exit");
        menuItem.setMnemonic('x');
        menuItem.addActionListener(new ActionListener(){

        	@Override
			public void actionPerformed(ActionEvent arg0) {
				System.exit(0);
        		
			}
        	
        });
        menu.add(menuItem);
      
        add(menu);

    }
    
}
