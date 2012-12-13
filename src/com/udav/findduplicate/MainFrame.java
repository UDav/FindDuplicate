package com.udav.findduplicate;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.BoxLayout;
import javax.swing.JTabbedPane;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.border.BevelBorder;
import javax.swing.JProgressBar;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;

public class MainFrame extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private JTextField pathTextField;
	private JPanel topPanel;
	private JTabbedPane middlePanel;
	private JPanel bottomPanel;
	private JButton searchButton, deleteButton, exitButton, selectPathButton;
	private JLabel status;
	private JProgressBar progressBar;
	
	private ArrayList<ArrayList<File>> fileDuplicateArray;
	private ArrayList<ArrayList<JCheckBox>> fileCheckBoxArray;
	
	private ArrayList<DirectoriesDuplicateContainer> directoriesDuplicateArray;
	private ArrayList<ArrayList<JCheckBox>> directoriesCheckBoxArray;
	
	private String statistic;
	private JScrollPane filesSP;
	private JScrollPane directoriesSP;

	private JPanel directoriesPanel;
    private JPanel filesPanel; 
    private JPanel statisticPanel;
    private JMenuBar menuBar;
    private JMenu mnFile;
    private JMenuItem mntmSearch;
    private JMenuItem mntmExit;
    private JMenu mnNewMenu;
    private JMenuItem mntmAbout;
    private JMenuItem mntmSettings;
    
    private SettingsDialog settingsDialog;
	
	/**
	 * Create the frame.
	 */
	public MainFrame() {
		Toolkit kit = Toolkit.getDefaultToolkit() ;
        Dimension screenSize = kit.getScreenSize() ;
        int x = screenSize.width;
        int y = screenSize.height;
        setBounds(x/4, y/4, 600, 600);
        setMinimumSize(new Dimension(600, 600));   
        setTitle("FindDuplicate");
        
        JPanel firstPanel = new JPanel();
        getContentPane().add(firstPanel, BorderLayout.CENTER);
        firstPanel.setLayout(new BorderLayout(0, 0));
        
        topPanel = new JPanel();
        firstPanel.add(topPanel, BorderLayout.NORTH);
        topPanel.setLayout(new BorderLayout(0, 0));
        
        JLabel pathLabel = new JLabel(" Path:  ");
        pathLabel.setHorizontalAlignment(SwingConstants.LEFT);
        topPanel.add(pathLabel, BorderLayout.WEST);
        
        pathTextField = new JTextField();
        pathTextField.setHorizontalAlignment(SwingConstants.LEFT);
        topPanel.add(pathTextField);
        pathTextField.setColumns(10);
        
        ButtonAction buttonAction = new ButtonAction();
                
        selectPathButton = new JButton("...");
        selectPathButton.setPreferredSize(new Dimension(25, 25));
        selectPathButton.addActionListener(buttonAction);
        topPanel.add(selectPathButton, BorderLayout.EAST);
        
        middlePanel = new JTabbedPane(JTabbedPane.TOP);
        firstPanel.add(middlePanel, BorderLayout.CENTER);
        
        directoriesSP = new JScrollPane();
        directoriesSP.setVisible(true);
        middlePanel.addTab("Directories", null, directoriesSP, null);
        
        filesSP = new JScrollPane();
        filesSP.setVisible(true);
        middlePanel.addTab("Files", null, filesSP, null);
                
        statisticPanel = new JPanel();
        statisticPanel.setVisible(true);
        middlePanel.addTab("Statistic", null, statisticPanel, null);
        
        
        bottomPanel = new JPanel();
        firstPanel.add(bottomPanel, BorderLayout.SOUTH);
        
        
        searchButton = new JButton("Search");
        searchButton.setPreferredSize(new Dimension(100, 25));
        searchButton.addActionListener(buttonAction);
        bottomPanel.add(searchButton);
        deleteButton = new JButton("Delete");
        deleteButton.setPreferredSize(new Dimension(100, 25));
        deleteButton.addActionListener(buttonAction);
        bottomPanel.add(deleteButton);
        exitButton = new JButton("Exit");
        exitButton.setPreferredSize(new Dimension(100, 25));
        exitButton.addActionListener(buttonAction);
        bottomPanel.add(exitButton);
        
        JPanel secondPanel = new JPanel();
        secondPanel.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
        getContentPane().add(secondPanel, BorderLayout.SOUTH);
        secondPanel.setLayout(new BorderLayout(0, 0));
        
        status = new JLabel("State ");
        secondPanel.add(status, BorderLayout.WEST);
        status.setHorizontalAlignment(SwingConstants.LEFT);
        
        progressBar = new JProgressBar();
        progressBar.setStringPainted(true);
		progressBar.setMaximum(100);
        secondPanel.add(progressBar, BorderLayout.EAST);
        
        menuBar = new JMenuBar();
        getContentPane().add(menuBar, BorderLayout.NORTH);
        
        mnFile = new JMenu("File");
        menuBar.add(mnFile);
        
        mntmSearch = new JMenuItem("Search");
        mnFile.add(mntmSearch);
        
        mntmSettings = mnFile.add(new AbstractAction("Settings") {
			private static final long serialVersionUID = 1L;
			public void actionPerformed(ActionEvent e) {
            	if (settingsDialog != null)
            		settingsDialog.setVisible(true);
            	else
            		settingsDialog = new SettingsDialog();
            }
        });
            	
            	
        //mnFile.add(mntmSettings);
        
        mntmExit = new JMenuItem("Exit");
        mnFile.add(mntmExit);
        
        mnNewMenu = new JMenu("Help");
        menuBar.add(mnNewMenu);
        
        mntmAbout = new JMenuItem("About");
        mnNewMenu.add(mntmAbout);
        
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
		directoriesPanel = new JPanel();
		directoriesSP.add(directoriesPanel);
		directoriesSP.setViewportView(directoriesPanel);
        directoriesPanel.setLayout(new BoxLayout(directoriesPanel, BoxLayout.PAGE_AXIS));
        directoriesPanel.setVisible(true);
        
        filesPanel = new JPanel();
        filesSP.add(filesPanel);
        filesSP.setViewportView(filesPanel);
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
        middlePanel.revalidate();
        //middlePanel.setViewportView(tabPanel);
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
        		middlePanel.setEnabled(false);

        		progressBar.setIndeterminate(true);
        		if ((directoriesPanel != null) || (filesPanel != null)) {
        			directoriesPanel.removeAll();
        			filesPanel.removeAll();
        		}
        		String extensions = "";
        		if (settingsDialog != null) extensions = settingsDialog.getExtensions();
        		if (extensions.equals("")) extensions = "*";
        		new Finder(pathTextField.getText(), extensions/*extensionTextField.getText()*/){
        			@Override
        			protected void done() {
        				fileDuplicateArray = this.getFileDuplicateArray();
        				directoriesDuplicateArray = this.getDirectoriesDuplicateArray();
        				statistic = this.getStatistic();
        				statisticPanel.add(new JLabel(statistic));
        				statisticPanel.revalidate();
        				
        				fillMiddlePanel();
        				searchButton.setEnabled(true);
        				deleteButton.setEnabled(true);
        				middlePanel.setEnabled(true);
        				progressBar.setIndeterminate(false);
        				progressBar.setValue(100);
        				status.setText("Finish! ");
        				super.done();
        			}
        			
        			@Override
        			protected void process(List<Object> chunks) {
        				status.setText(chunks.get(0).toString());
        				
        				if (chunks.size() > 1){
        					progressBar.setIndeterminate(false);
        					progressBar.setValue(Integer.parseInt(chunks.get(1).toString()));
        				}
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
