/******************************************************************
*
*	MediaGate for CyberLink
*
*	Copyright (C) Satoshi Konno 2004
*
*	File: ContentPane.java
*
*	Revision;
*
*	01/28/04
*		- first revision.
*	
******************************************************************/

package org.cybergarage.mediagate.frame.swing;

import java.io.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class DirectoryAddPane implements ActionListener
{
	////////////////////////////////////////////////
	// Member
	////////////////////////////////////////////////

	private Component parentComp;	
	private JTextField nameText;
	private JTextField dirText;
	private JButton dirButton;
	
	////////////////////////////////////////////////
	// Constuctor
	////////////////////////////////////////////////
	
	public DirectoryAddPane(Component parent)
	{
		parentComp = parent;
	}

	////////////////////////////////////////////////
	// Result
	////////////////////////////////////////////////
	
	public String getName()
	{
		return nameText.getText();
	}

	public String getDirectory()
	{
		return dirText.getText();
	}
	
	////////////////////////////////////////////////
	// showDialog
	////////////////////////////////////////////////
	
	public int showDialog()
	{
		Object paneObj[] = new Object[2];
		
		JPanel mainPane = new JPanel();
		mainPane.setAlignmentX(Component.LEFT_ALIGNMENT);
		mainPane.setLayout(new BoxLayout(mainPane, BoxLayout.Y_AXIS));
		
		JPanel namePane = new JPanel();
		JLabel nameLabe = new JLabel("Name");
		nameText = new JTextField();
		namePane.setAlignmentX(Component.LEFT_ALIGNMENT);
		Dimension nameTextSize = nameText.getPreferredSize();
		nameTextSize.setSize(300, nameTextSize.getHeight());
		nameText.setPreferredSize(nameTextSize);
		namePane.add(nameLabe);
		namePane.add(nameText);
		paneObj[0] = namePane;
		namePane.setAlignmentX(Component.LEFT_ALIGNMENT);
		mainPane.add(namePane);

		JPanel dirPane = new JPanel();
		JLabel dirLabe = new JLabel("Directory");
		dirText = new JTextField();
		Dimension dirTextSize = dirText.getPreferredSize();
		dirTextSize.setSize(200, dirTextSize.getHeight());
		dirText.setPreferredSize(dirTextSize);
		dirButton = new JButton("Open");
		dirButton.addActionListener(this);
		dirPane.add(dirLabe);
		dirPane.add(dirText);
		dirPane.add(dirButton);
		paneObj[1] = dirPane;
		dirPane.setAlignmentX(Component.LEFT_ALIGNMENT);
		mainPane.add(dirPane);
		
		int ret = JOptionPane.showConfirmDialog(
			parentComp, 
			mainPane,
			SwingFrame.TITLE,
			JOptionPane.YES_NO_OPTION);
			
		return ret;
	}

	////////////////////////////////////////////////
	// ActionListener
	////////////////////////////////////////////////
	
	public void actionPerformed(ActionEvent e)
	{
		JFileChooser fc = new JFileChooser(); 
		fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		if (fc.showOpenDialog(dirButton) == JFileChooser.APPROVE_OPTION) { 
			File selFile = fc.getSelectedFile();
			dirText.setText(selFile.getPath());
		} 
		Object srcObj = e.getSource();
	}
}
