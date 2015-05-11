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

package dz.aak.as.ui.gui;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;

import dz.aak.as.ui.MonoDoc;


public class GUI extends JFrame{
	private static final long serialVersionUID = 1L;
	//PlainText transformer;
	JTextArea txt=new JTextArea();
	JTextArea txt2=new JTextArea();
	JPanel pan=new JPanel();
	DefaultListModel<String> ls= new DefaultListModel<String>();
	JList<String> myList=new JList<String>(ls);
	private Menu menuBar = null;
	private ToolBar panel;
	public GUI(){ 
		super("Easy Summarization");
		
		menuBar = new Menu(this);
        setJMenuBar(menuBar);
        
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int espaceX=(screenSize.width-800)/2;
		int espaceY=(screenSize.height-600)/2;
		setBounds(espaceX, espaceY, 800, 570); 
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
       
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{123, 621, 0};
		gridBagLayout.rowHeights = new int[]{20, 150, 150, 150, 0};
		gridBagLayout.columnWeights = new double[]{0.0, 1.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.1, 0.3, 0.3, 0.3, Double.MIN_VALUE};
		getContentPane().setLayout(gridBagLayout);
		
		JButton b= new JButton("Summarize");
		MouseListener mouseListener = new MouseAdapter() {
 			public void mouseClicked(MouseEvent e) {
 				faire(txt.getText());
 				
 			}
 		};

		b.addMouseListener(mouseListener);
		GridBagConstraints gbc_b11 = new GridBagConstraints();
		gbc_b11.insets = new Insets(0, 0, 5, 5);
		gbc_b11.gridx = 0;
		gbc_b11.gridy = 0;
		gbc_b11.fill= GridBagConstraints.BOTH;
		getContentPane().add(b, gbc_b11);
 		
 		panel = new ToolBar();
 		GridBagConstraints gbc_panel = new GridBagConstraints();
 		gbc_panel.insets = new Insets(0, 0, 5, 0);
 		gbc_panel.fill = GridBagConstraints.BOTH;
 		gbc_panel.gridx = 1;
 		gbc_panel.gridy = 0;
 		getContentPane().add(panel, gbc_panel);
 		
 		
		
		//txt.setSize(20,200);
 		//txt.setBounds(0, 0, 10, 20);
 		JScrollPane txtscrl = new JScrollPane(txt);
 		//txtscrl.setSize(0, 100);
 		///txtscrl.setPreferredSize(new java.awt.Dimension(120, 100));
 		//txtscrl.setPreferredSize(new java.awt.Dimension(770, 100));
		GridBagConstraints gbc_b21 = new GridBagConstraints();
		gbc_b21.fill = GridBagConstraints.BOTH;
		gbc_b21.insets = new Insets(0, 0, 5, 0);
		gbc_b21.gridwidth = 2;
		gbc_b21.gridx = 0;
		gbc_b21.gridy = 1;
		getContentPane().add(txtscrl, gbc_b21);
		
		//JButton b31 = new JButton("New button");
		JScrollPane lsscrl = new JScrollPane(myList);
		GridBagConstraints gbc_b31 = new GridBagConstraints();
		gbc_b31.fill = GridBagConstraints.BOTH;
		gbc_b31.insets = new Insets(0, 0, 5, 0);
		gbc_b31.gridwidth = 2;
		gbc_b31.gridx = 0;
		gbc_b31.gridy = 2;
		getContentPane().add(lsscrl, gbc_b31);
		
		JScrollPane txt2scrl = new JScrollPane(txt2);
		GridBagConstraints gbc_b41 = new GridBagConstraints();
		gbc_b41.fill = GridBagConstraints.BOTH;
		gbc_b41.gridwidth = 2;
		gbc_b41.gridx = 0;
		gbc_b41.gridy = 3;
		getContentPane().add(txt2scrl, gbc_b41);
		
		setVisible(true);
		
	}
	
	public static void main(String[] args) {
		
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
	
	
	
	public void faire(String adr) {
		ls.clear();
		
		MonoDoc sum = new MonoDoc();
		
		System.out.println(panel.getClusterThreshod());
		
		sum.setThreshold(panel.getClusterThreshod());
		
		sum.Summarize(txt.getText());
		
		String summary = sum.getSummaryPercent(panel.getSummaryRate());
		
		List<String> ordered = sum.getOrdered();
		
		ls.clear();
		for (String sent : ordered)
			ls.addElement(sent);
		
		txt2.setText(summary);
	}

}
