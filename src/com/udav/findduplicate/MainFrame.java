package com.udav.findduplicate;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.LayoutManager;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.LayoutStyle;

import java.util.ArrayList;
import java.util.List;
import java.io.File;

public class MainFrame extends JFrame {
	private JPanel topPanel;
	private JScrollPane middlePanel;
	private JPanel bottomPanel;
	private JButton searchButton, deleteButton, exitButton, selectPathButton;
	public JTextField pathTextField;
	private JTextField extensionTextField;
	private JLabel status;
	
	private ArrayList<ArrayList<File>> fileDuplicateArray;
	private ArrayList<ArrayList<JCheckBox>> checkBoxArray;
	
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
        extensionTextField = new JTextField();
        extensionTextField.setPreferredSize(new Dimension(100, 25));
        extensionTextField.setText("*");
        topPanel.add(pathTextField, null);
        topPanel.add(selectPathButton);
        topPanel.add(extensionTextField);
                
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
        
        checkBoxArray = new ArrayList<ArrayList<JCheckBox>>();
        for (int i=0; i<fileDuplicateArray.size(); i++) {
        	ArrayList<File> subArray = fileDuplicateArray.get(i);
        	JPanel tmpPanel = new JPanel();
        	tmpPanel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        	tmpPanel.setLayout(new BoxLayout(tmpPanel, BoxLayout.PAGE_AXIS));
        	if (subArray.size() > 0)
        		tmpPanel.add(new JLabel(subArray.get(0).getName()+" "+subArray.get(0).length()/1048576+"MB "+subArray.get(0).length()%1048576+"KB"));
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
	
	private void fillMiddlePanelWhileWait() {
		//JPanel progressPanel = new JPanel();
		JProgressBar progressBar = new JProgressBar();
		progressBar.setIndeterminate(true);
		status = new JLabel();
		
		//status.setText("I'm work!");
		Box box = Box.createVerticalBox();
		box.add(status);
		box.add(progressBar);
		//progressPanel.add(status);
		//progressPanel.add(progressBar);
		
		middlePanel.setViewportView(box/*progressPanel*/);
	}
	
	private void deleteSelectedDuplicate() {
		for (int i=0; i<checkBoxArray.size(); i++) {
			ArrayList<JCheckBox> tmpCheckBoxArray = checkBoxArray.get(i);
			ArrayList<File> tmpFileDuplicateArray = fileDuplicateArray.get(i);
			for (int j=0; j<tmpCheckBoxArray.size(); j++) {
				if (tmpCheckBoxArray.get(j).isSelected()) {
					tmpFileDuplicateArray.get(j).delete();
					tmpCheckBoxArray.get(j).setEnabled(false);
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
					if (!pathTextField.getText().equals(""))
						pathTextField.setText(pathTextField.getText()+";"+choosFolder.getSelectedFile().getAbsolutePath());
					else pathTextField.setText(choosFolder.getSelectedFile().getAbsolutePath());
				}
        	}
        	if (event.getSource() == searchButton) {
        		// запускаем поиск, сравнение файлов и вывод дубликатов
        		searchButton.setEnabled(false);
        		deleteButton.setEnabled(false);

        		fillMiddlePanelWhileWait();
        		new Finder(pathTextField.getText(), extensionTextField.getText()){
        			@Override
        			protected void done() {
        				fileDuplicateArray = this.getFileDuplicateArray();
        				fillMiddlePanel();
        				searchButton.setEnabled(true);
        				deleteButton.setEnabled(true);
        				super.done();
        			}
        			
        			@Override
        			protected void process(List<Object> chunks) {
        				status.setText(chunks.get(0).toString());
        				super.process(chunks);
        			}
        		}.execute();
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
