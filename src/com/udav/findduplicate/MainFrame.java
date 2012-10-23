package com.udav.findduplicate;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class MainFrame extends JFrame {
	private JPanel topPanel;
	private JPanel middlePanel;
	private JPanel bottomPanel;
	private JButton searchButton, deleteButton, exitButton, selectPathButton;
	private JTextField pathTextField;
	
	public MainFrame() {
		Toolkit kit = Toolkit.getDefaultToolkit() ;
        Dimension screenSize = kit.getScreenSize() ;
        int x = screenSize.width;
        int y = screenSize.height;
        setResizable(false);
        setBounds(x/4, y/4, 600, 600);
        setTitle("FindDuplicate");
        
        
        topPanel = new JPanel(new FlowLayout());
        topPanel.setVisible(true);
        middlePanel = new JPanel(new FlowLayout());
        middlePanel.setVisible(true);
        bottomPanel = new JPanel(new FlowLayout());
        bottomPanel.setVisible(true);
        
        //create action
        ButtonAction buttonAction = new ButtonAction();
        
        //Fill top panel
        topPanel.add(new JLabel("Path: "));
        selectPathButton = new JButton("...");
        selectPathButton.setPreferredSize(new Dimension(25, 25));
        selectPathButton.addActionListener(buttonAction);
        //topPanel.add(new JFileChooser());
        pathTextField = new JTextField();
        pathTextField.setPreferredSize(new Dimension(200, 25));
        pathTextField.setText("c:\\");
        topPanel.add(pathTextField, null);
        topPanel.add(selectPathButton);
        
        //Fill middle panel
        middlePanel.add(new JLabel("Hello!"));
        
        //Fill bottom panel
        searchButton = new JButton("Search");
        searchButton.setPreferredSize(new Dimension(100, 25));
        searchButton.addActionListener(buttonAction);
        deleteButton = new JButton("Delete");
        deleteButton.setPreferredSize(new Dimension(100, 25));
        deleteButton.addActionListener(buttonAction);
        exitButton = new JButton("Exit");
        exitButton.setPreferredSize(new Dimension(100, 25));
        exitButton.addActionListener(buttonAction);
        bottomPanel.add(searchButton);
        bottomPanel.add(deleteButton);
        bottomPanel.add(exitButton);
        
        this.add(topPanel, BorderLayout.NORTH);
        this.add(middlePanel, BorderLayout.CENTER);
        this.add(bottomPanel, BorderLayout.SOUTH);
        
        setVisible(true);
	}
	class ButtonAction implements ActionListener {
        @Override
		public void actionPerformed(ActionEvent event) {
        	if (event.getSource() == selectPathButton) {
        		// запускать файлчузер
        		JFileChooser choosFolder = new JFileChooser();
            	choosFolder.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				int ret = choosFolder.showDialog(null, "Select");				
				if (ret == JFileChooser.APPROVE_OPTION) {
					pathTextField.setText(choosFolder.getSelectedFile().getAbsolutePath());
				}
        	}
        	if (event.getSource() == searchButton) {
        		// запускаем поиск, сравнение файлов и вывод дубликатов
        		new Finder(pathTextField.getText());
        	}
        	if (event.getSource() == deleteButton) {
        		// удаляем 
        	}
        	if (event.getSource() == exitButton){
        		 System.exit(0);
        	}
        }
    }
}
