/******************************************************************
*
*	MediaGate for CyberLink
*
*	Copyright (C) Satoshi Konno 2004
*
*	File: SwingFrame.java
*
*	Revision;
*
*	01/24/04
*		- first revision.
*	
******************************************************************/

package org.cybergarage.mediagate.frame.swing;

import java.io.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;

import org.cybergarage.util.*;
import org.cybergarage.upnp.std.av.server.*;
import org.cybergarage.upnp.std.av.server.directory.file.*;
import org.cybergarage.mediagate.*;
import org.cybergarage.mediagate.frame.*;

public class SwingFrame extends MediaFrame implements ActionListener, ListSelectionListener, WindowListener
{
	////////////////////////////////////////////////
	// Static Constants
	////////////////////////////////////////////////
	
	public static String TITLE = "Cyber Media Gate";
	
	private static GraphicsDevice graphDevice;
	private static GraphicsConfiguration graphGC;
	
	static
	{
		GraphicsEnvironment graphEnv = GraphicsEnvironment.getLocalGraphicsEnvironment();
		graphDevice = graphEnv.getDefaultScreenDevice();
		graphGC = graphDevice.getDefaultConfiguration();
	}
	
	private final static int FRAME_WIDTH = 640;
	private final static int FRAME_HEIGHT = 480;
	
	////////////////////////////////////////////////
	// Member
	////////////////////////////////////////////////
	
	private DisplayMode orgDispMode;
	private JFrame frame;
	
	private DirectoryPane dirPane;
	private ContentPane conPane;
	
	private JButton addButton;
	private JButton delButton;
	private JButton quitButton;
	
	////////////////////////////////////////////////
	// Constuctor
	////////////////////////////////////////////////
	
	public SwingFrame(MediaGate mgate, boolean hasAddDelButtons)
	{
		super(mgate);
		
		frame = new JFrame(graphGC);
		frame.setTitle(TITLE);
		orgDispMode = graphDevice.getDisplayMode();
		DisplayMode mode = new DisplayMode(FRAME_WIDTH, FRAME_HEIGHT, 32, DisplayMode.REFRESH_RATE_UNKNOWN);

		// Window Listener
		frame.addWindowListener(this);
		
		// Split Pane
		JSplitPane mainSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, false);
		frame.getContentPane().add(mainSplitPane, BorderLayout.CENTER);

		// Left Panel		
		JPanel leftPane = new JPanel();
		BoxLayout leftPaneLayout = new BoxLayout(leftPane, BoxLayout.Y_AXIS);
		leftPane.setAlignmentX(0.5f);
		//leftPaneLayout.setAlignmentX(0.5f);
		leftPane.setLayout(leftPaneLayout);
		
		// Directory List
		//leftPane.add(new JLabel("Directory"));
		dirPane = new DirectoryPane(mgate);
		dirPane.getList().addListSelectionListener(this);
		dirPane.getScrollPane().setPreferredSize(new Dimension(FRAME_WIDTH/5,FRAME_HEIGHT/*-(FRAME_HEIGHT/10)*/));
		leftPane.add(dirPane);
		
		if (hasAddDelButtons == true) {
			// Add/Delete Panel
			JPanel addelPane = new JPanel();
			addelPane.setLayout(new BoxLayout(addelPane, BoxLayout.X_AXIS));
			leftPane.add(addelPane);
			// Add Directory
			addButton = new JButton("Add");
			addButton.addActionListener(this);
			addelPane.add(addButton);
			// Delete Directory
			delButton = new JButton("Del");
			delButton.addActionListener(this);
			addelPane.add(delButton);
		}
		
		// Quit Button
		JPanel quitPane = new JPanel();
		quitButton = new JButton("Quit");
		quitButton.addActionListener(this);
		quitPane.add(quitButton);
		leftPane.add(quitPane);
		mainSplitPane.setLeftComponent(leftPane);

		// Element		
		conPane = new ContentPane();
		JScrollPane conScrPane = new JScrollPane(conPane);
		mainSplitPane.setRightComponent(conScrPane);

		frame.setSize(FRAME_WIDTH, FRAME_HEIGHT);
		frame.show();
		
		//frame.setUndecorated(true);
		//graphDevice.setFullScreenWindow(frame);
		//graphDevice.setDisplayMode(mode);
	}
	
	////////////////////////////////////////////////
	// ActionListener
	////////////////////////////////////////////////
	
	public void actionPerformed(ActionEvent e)
	{
		Object srcObj = e.getSource();
	
		if (srcObj == addButton)
			addDirectory();
		else if (srcObj == delButton)
			deleteDirectory();
		else if (srcObj == quitButton)
			exit();
	}

	private void addDirectory()
	{
		DirectoryAddPane dirAddPane = new DirectoryAddPane(frame);
		int ret = dirAddPane.showDialog();
		if (ret == JOptionPane.OK_OPTION) {
			String name = dirAddPane.getName();
			if (name == null || name.length() <= 0) {
				JOptionPane.showMessageDialog(
					frame, 
					"Please input a friendly name for the selected directory",
					TITLE,
					JOptionPane.QUESTION_MESSAGE);
				return;
			}
			String dir = dirAddPane.getDirectory();
			if (dir == null) {
				JOptionPane.showMessageDialog(
					frame, 
					"Please select a directory you want to add",
					TITLE,
					JOptionPane.WARNING_MESSAGE);
				return;
			}
			File dirFile = new File(dir);
			if (dirFile.exists() == false) {
				JOptionPane.showMessageDialog(
					frame, 
					"Your selected directory is not found",
					TITLE,
					JOptionPane.WARNING_MESSAGE);
				return;
			}
			
			MediaGate mgate = getMediaGate();
			MediaServer mserver = mgate.getMediaServer();
			FileDirectory fileDir = new FileDirectory(name, dir);
			mserver.getContentDirectory().addDirectory(fileDir);
			dirPane.update(mgate);
		}
	}
	
	private void deleteDirectory()
	{	
		int selIdx = dirPane.getList().getSelectedIndex();
		if (selIdx < 0) {
			JOptionPane.showMessageDialog(
				frame, 
				"Please select a directory you want to delete",
				TITLE,
				JOptionPane.WARNING_MESSAGE);
			return;
		}
		
		String dirStr = (String)dirPane.getList().getSelectedValue();
		
		int ret = JOptionPane.showConfirmDialog(
			frame, 
			"Are you delete the selected directory (" + dirStr + ") ?",
			TITLE,
			JOptionPane.YES_NO_OPTION);
		
		if (ret == JOptionPane.OK_OPTION) {
			MediaGate mgate = getMediaGate();
			MediaServer mserver = mgate.getMediaServer();
			mserver.getContentDirectory().removeDirectory(dirStr);
			dirPane.update(mgate);
		}
	}
	
	////////////////////////////////////////////////
	// ListSelectionListener
	////////////////////////////////////////////////
	
	public void valueChanged(ListSelectionEvent e)
	{
		//int selIdx = e.getLastIndex();
		int selIdx = dirPane.getList().getSelectedIndex();
		if (selIdx < 0)
			return;
		MediaServer mserver = getMediaGate().getMediaServer();
		Directory dir = mserver.getContentDirectory(selIdx);
		Debug.message("valueChanged = " + dir.getFriendlyName());
		conPane.update(dir);
	}

	////////////////////////////////////////////////
	// WindowListener
	////////////////////////////////////////////////
	
	public void windowActivated(WindowEvent e) 
	{
	}
	
	public void windowClosed(WindowEvent e) 
	{
	}
	
	public void windowClosing(WindowEvent e) 
	{
		exit();
	}
	
	public void windowDeactivated(WindowEvent e) 
	{
	}
	
	public void windowDeiconified(WindowEvent e) 
	{
	}
	
	public void windowIconified(WindowEvent e) 
	{
	}
	
	public void windowOpened(WindowEvent e) 
	{
	}

	////////////////////////////////////////////////
	// exit
	////////////////////////////////////////////////

	public void exit()
	{
		getMediaGate().stop();
		graphDevice.setFullScreenWindow(null);
		System.exit(0);
	}
}
