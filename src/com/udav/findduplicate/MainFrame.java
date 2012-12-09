package com.udav.findduplicate;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

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
import javax.swing.JTabbedPane;
import javax.swing.JTextField;

public class MainFrame extends JFrame {
	private static final long serialVersionUID = 1L;
	
	private JPanel topPanel;
	private JPanel middlePanel;
	private JPanel bottomPanel;
	private JButton searchButton, deleteButton, exitButton, selectPathButton;
	private JTextField pathTextField;
	private JTextField extensionTextField;
	private JLabel status;
	
	private ArrayList<ArrayList<File>> fileDuplicateArray;
	private ArrayList<ArrayList<JCheckBox>> fileCheckBoxArray;
	
	private ArrayList<DirectoriesDuplicateContainer> directoriesDuplicateArray;
	private ArrayList<ArrayList<JCheckBox>> directoriesCheckBoxArray;
	
	public MainFrame() {
		Toolkit kit = Toolkit.getDefaultToolkit() ;
        Dimension screenSize = kit.getScreenSize() ;
        int x = screenSize.width;
        int y = screenSize.height;
        setBounds(x/4, y/4, 600, 600);
        setMinimumSize(new Dimension(600, 600));   
        setTitle("FindDuplicate");
             
        
        topPanel = new JPanel(new FlowLayout());
        topPanel.setVisible(true);
        middlePanel = new JPanel(new BorderLayout());
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
        //pathTextField.setText("c:\\");
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
	/**
	 * Convert size from byte to mb,kb,byte
	 * @param sizeInByte
	 * @return String "n MB m KB z B"
	 */
	private String byteToString(final long sizeInByte) {
		final int KBYTE = 1024;
		final int MBYTE = 1024*1024;
		
		return "|| "+sizeInByte/MBYTE+"MB "+(sizeInByte%MBYTE)/KBYTE+"KB "+((sizeInByte%MBYTE)%KBYTE)+"B";
	}
	
	/**
	 * Display result in GUI
	 */
	private void fillMiddlePanel(){
		JPanel directoriesPanel = new JPanel();
		JScrollPane directoriesJsp = new JScrollPane(directoriesPanel);
        directoriesPanel.setLayout(new BoxLayout(directoriesPanel, BoxLayout.PAGE_AXIS));
        directoriesPanel.setVisible(true);
        
        JPanel filesPanel = new JPanel();
        JScrollPane filesJsp = new JScrollPane(filesPanel);
        filesPanel.setLayout(new BoxLayout(filesPanel, BoxLayout.PAGE_AXIS));
        filesPanel.setVisible(true);
        
        directoriesCheckBoxArray = new ArrayList<ArrayList<JCheckBox>>();
        for (int i=0; i<directoriesDuplicateArray.size(); i++) {
        	ArrayList<File> subArray = directoriesDuplicateArray.get(i).getDuplicateArray();
        	long size = directoriesDuplicateArray.get(i).getSize();
        	JPanel tmpPanel = new JPanel();
        	tmpPanel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        	tmpPanel.setLayout(new BoxLayout(tmpPanel, BoxLayout.PAGE_AXIS));
        	if (subArray.size() > 0)
        		tmpPanel.add(new JLabel("Directories duplicate "+(i+1)/*subArray.get(0).getName()*/+" "+byteToString(size)));
        	ArrayList<JCheckBox> tmpCheckBoxArray = new ArrayList<JCheckBox>();
        	for (int j=0; j<subArray.size(); j++){
        		JCheckBox tmpJCheckBox = new JCheckBox(subArray.get(j).getAbsolutePath()); 
        		tmpPanel.add(tmpJCheckBox);
        		tmpCheckBoxArray.add(tmpJCheckBox);
        		tmpPanel.revalidate();
        	}
        	directoriesCheckBoxArray.add(tmpCheckBoxArray);
        	directoriesPanel.add(tmpPanel);
        }
        
        fileCheckBoxArray = new ArrayList<ArrayList<JCheckBox>>();
        for (int i=0; i<fileDuplicateArray.size(); i++) {
        	ArrayList<File> subArray = fileDuplicateArray.get(i);
        	JPanel tmpPanel = new JPanel();
        	tmpPanel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        	tmpPanel.setLayout(new BoxLayout(tmpPanel, BoxLayout.PAGE_AXIS));
        	if (subArray.size() > 0)
        		tmpPanel.add(new JLabel(subArray.get(0).getName()+" "+byteToString(subArray.get(0).length()) ) );
        	ArrayList<JCheckBox> tmpCheckBoxArray = new ArrayList<JCheckBox>();
        	for (int j=0; j<subArray.size(); j++){
        		JCheckBox tmpJCheckBox = new JCheckBox(subArray.get(j).getAbsolutePath()); 
        		tmpPanel.add(tmpJCheckBox);
        		tmpCheckBoxArray.add(tmpJCheckBox);
        		tmpPanel.revalidate();
        	}
        	fileCheckBoxArray.add(tmpCheckBoxArray);
        	filesPanel.add(tmpPanel);
        }
        
        JTabbedPane tabPanel = new JTabbedPane();
		tabPanel.setVisible(true);
        tabPanel.addTab("Directories", directoriesJsp);
        tabPanel.addTab("Files", filesJsp);
        JPanel statisticPanel = new JPanel();
        statisticPanel.add(new JLabel("Statistic about duplicate directories and files"));
        tabPanel.addTab("Info", statisticPanel);

        middlePanel.removeAll();
        middlePanel.add(tabPanel);
        middlePanel.revalidate();
        //middlePanel.setViewportView(tabPanel);
	}
	
	/**
	 * Display progress in GUI
	 */
	private void fillMiddlePanelWhileWait() {
		JProgressBar progressBar = new JProgressBar();
		progressBar.setIndeterminate(true);
		status = new JLabel();
		
		Box box = Box.createVerticalBox();
		box.add(status);
		box.add(progressBar);
		
		middlePanel.removeAll();
		middlePanel.add(box);
		middlePanel.revalidate();
		//middlePanel.setViewportView(box);
	}
	
	/**
	 * Delete checked items
	 */
	private void deleteSelectedDuplicate() {
		for (int i=0; i<fileCheckBoxArray.size(); i++) {
			ArrayList<JCheckBox> tmpCheckBoxArray = fileCheckBoxArray.get(i);
			ArrayList<File> tmpFileDuplicateArray = fileDuplicateArray.get(i);
			for (int j=0; j<tmpCheckBoxArray.size(); j++) {
				if (tmpCheckBoxArray.get(j).isSelected()) {
					tmpFileDuplicateArray.get(j).delete();
					tmpCheckBoxArray.get(j).setEnabled(false);
				}
			}
		}
		
		for (int i=0; i<directoriesCheckBoxArray.size(); i++) {
			ArrayList<JCheckBox> tmpCheckBoxArray = directoriesCheckBoxArray.get(i);
			ArrayList<File> tmpFileDuplicateArray = directoriesDuplicateArray.get(i).getDuplicateArray();
			for (int j=0; j<tmpCheckBoxArray.size(); j++) {
				if (tmpCheckBoxArray.get(j).isSelected()) {
					System.out.println("delete "+tmpFileDuplicateArray.get(j).getAbsolutePath());
					deleteDirectory(tmpFileDuplicateArray.get(j));
					tmpCheckBoxArray.get(j).setEnabled(false);
				}
			}
		}
	}
	
	/**
     * Deletes directory with subdir and subfiles
     * @param dir - Directory to delete
     */
    private void deleteDirectory(File dir) {
        if (dir.isDirectory()) {
            File children[] = dir.listFiles();
            for (int i=0; i<children.length; i++) {
                deleteDirectory(children[i]);
            }
            dir.delete();
        } else dir.delete();
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
        				directoriesDuplicateArray = this.getDirectoriesDuplicateArray();
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
