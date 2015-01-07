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

package as.UI.GUI;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

public class ToolBar extends JPanel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JSpinner summaryRate;
	private JSpinner clusterThreshold;

	public ToolBar(){
		super();
		GridBagLayout gbl = new GridBagLayout();
 		gbl.columnWidths = new int[]{265, 267, 340, 0};
 		gbl.rowHeights = new int[]{0, 0, 0};
 		gbl.columnWeights = new double[]{1.0, 1.0, 0.0, Double.MIN_VALUE};
 		gbl.rowWeights = new double[]{0.0, 0.0, Double.MIN_VALUE};
 		this.setLayout(gbl);
 		
 		//----------------- Line = 0 -----------------
 		// Column = 0
 		JLabel lblThrshold = new JLabel("Clustering Threshold %");
 		GridBagConstraints gbc_x0y0 = new GridBagConstraints();
 		gbc_x0y0.insets = new Insets(0, 0, 5, 5);
 		gbc_x0y0.gridx = 0;
 		gbc_x0y0.gridy = 0;
 		this.add(lblThrshold, gbc_x0y0);
 		
 		// Column = 1
 		JLabel lblSummUnit = new JLabel("Summarization unit");
 		GridBagConstraints gbc_x1y0 = new GridBagConstraints();
 		gbc_x1y0.insets = new Insets(0, 0, 5, 5);
 		gbc_x1y0.gridx = 1;
 		gbc_x1y0.gridy = 0;
 		this.add(lblSummUnit, gbc_x1y0);
 		
 		// Column = 2
 		JLabel lblSummRate = new JLabel("Summarization ratio %");
 		GridBagConstraints gbc_x2y0 = new GridBagConstraints();
 		gbc_x2y0.insets = new Insets(0, 0, 5, 0);
 		gbc_x2y0.gridx = 2;
 		gbc_x2y0.gridy = 0;
 		this.add(lblSummRate, gbc_x2y0);
 		 
 		//----------------- Line = 1 -----------------
 		// Column = 0
 		clusterThreshold = new JSpinner(new SpinnerNumberModel(50, 0, 100, 5));
 		GridBagConstraints gbc_spinner = new GridBagConstraints();
 		gbc_spinner.insets = new Insets(0, 0, 0, 5);
 		gbc_spinner.gridx = 0;
 		gbc_spinner.gridy = 1;
 		this.add(clusterThreshold, gbc_spinner);
 		
 		// Column = 1
 		JComboBox<String> comboBox = new JComboBox<String>();
 		comboBox.setModel(new DefaultComboBoxModel<String>(new String[] {"Characters", "Words", "Sentences"}));
 		GridBagConstraints gbc_x1y1 = new GridBagConstraints();
 		gbc_x1y1.insets = new Insets(0, 0, 0, 5);
 		gbc_x1y1.fill = GridBagConstraints.HORIZONTAL;
 		gbc_x1y1.gridx = 1;
 		gbc_x1y1.gridy = 1;
 		this.add(comboBox, gbc_x1y1);
 		
 		// Column = 2
 		summaryRate = new JSpinner(new SpinnerNumberModel(20, 1, 50, 1));
 		GridBagConstraints gbc_x2y1 = new GridBagConstraints();
 		gbc_x2y1.gridx = 2;
 		gbc_x2y1.gridy = 1;
 		this.add(summaryRate, gbc_x2y1);
	}
	
	public int getSummaryRate(){
		return (Integer) summaryRate.getValue();
	}
	
	public double getClusterThreshod(){
		return (double)(Integer) clusterThreshold.getValue()/100;
	}
}
