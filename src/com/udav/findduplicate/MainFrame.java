package com.udav.findduplicate;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import java.util.ArrayList;
import java.io.File;

public class MainFrame extends JFrame {
	private JPanel topPanel;
	private JScrollPane middlePanel;
	private JPanel bottomPanel;
	private JButton searchButton, deleteButton, exitButton, selectPathButton;
	public JTextField pathTextField;
	
	private ArrayList<ArrayList<File>> fileDuplicateArray;
	private ArrayList<ArrayList<JCheckBox>> checkBoxArray = new ArrayList<ArrayList<JCheckBox>>();
	
	public MainFrame() {
		Toolkit kit = Toolkit.getDefaultToolkit() ;
        Dimension screenSize = kit.getScreenSize() ;
        int x = screenSize.width;
        int y = screenSize.height;
        //setResizable(false);
        setBounds(x/4, y/4, 600, 600);
        setTitle("FindDuplicate");
        
        
        topPanel = new JPanel(new FlowLayout());
        topPanel.setVisible(true);
        middlePanel = new JScrollPane();
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
        pathTextField = new JTextField();
        pathTextField.setPreferredSize(new Dimension(200, 25));
        pathTextField.setText("c:\\");
        topPanel.add(pathTextField, null);
        topPanel.add(selectPathButton);
                
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
	
	private void fillMiddlePanel(){
		JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
        panel.setVisible(true);
        middlePanel.add(panel);
        
        for (int i=0; i<fileDuplicateArray.size(); i++) {
        	ArrayList<File> subArray = fileDuplicateArray.get(i);
        	JPanel tmpPanel = new JPanel();
        	tmpPanel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        	tmpPanel.setLayout(new BoxLayout(tmpPanel, BoxLayout.PAGE_AXIS));
        	tmpPanel.add(new JLabel(fileDuplicateArray.get(i).get(0).getName()+" "+fileDuplicateArray.get(i).get(0).length()));
        	ArrayList<JCheckBox> tmpCheckBoxArray = new ArrayList<JCheckBox>();
        	for (int j=0; j<subArray.size(); j++){
        		JCheckBox tmpJCheckBox = new JCheckBox(subArray.get(j).getAbsolutePath()); 
        		tmpPanel.add(tmpJCheckBox);
        		tmpCheckBoxArray.add(tmpJCheckBox);
        		tmpPanel.revalidate();
        	}
        	checkBoxArray.add(tmpCheckBoxArray);
        	panel.add(tmpPanel);
        	panel.revalidate();
        }

        middlePanel.setViewportView(panel);
	}
	
	private void deleteSelectedDuplicate() {
		for (int i=0; i<checkBoxArray.size(); i++) {
			ArrayList<JCheckBox> tmpCheckBoxArray = checkBoxArray.get(i);
			ArrayList<File> tmpFileDuplicateArray = fileDuplicateArray.get(i);
			for (int j=0; j<tmpCheckBoxArray.size(); j++) {
				if (tmpCheckBoxArray.get(j).isSelected()) {
					System.out.println("delete "+j);
					tmpFileDuplicateArray.get(j).delete();
				}
			}
		}
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
        		searchButton.setEnabled(false);
        		Finder finder = new Finder(pathTextField.getText());
        		fileDuplicateArray = finder.getFileDuplicateArray();
        		fillMiddlePanel();
        		searchButton.setEnabled(true);
        	}
        	if (event.getSource() == deleteButton) {
        		// удаляем 
        		deleteSelectedDuplicate();
        	}
        	if (event.getSource() == exitButton){
        		 System.exit(0);
        	}
        }
    }
}
