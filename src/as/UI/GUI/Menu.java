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

package aak.UI.GUI;

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
