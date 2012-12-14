package com.udav.findduplicate;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JCheckBox;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

public class SettingsDialog extends JDialog {

	private static final long serialVersionUID = 1L;
	
	private final JPanel contentPanel = new JPanel();
	private JTextField textFieldExtensions;
	private JRadioButton rdbtnSearchFileOnly;
	private JRadioButton rdbtnSearchAllFiles;
	
	private JCheckBox chckbxDocuments;
	private JCheckBox chckbxVideo;
	private JCheckBox chckbxPictures;
	private JCheckBox chckbxOther;
	
	private String extensions;
	
	/**
	 * Create the dialog.
	 */
	public SettingsDialog() {
		setResizable(false);
		setModal(true);
		Toolkit kit = Toolkit.getDefaultToolkit() ;
        Dimension screenSize = kit.getScreenSize() ;
        int x = screenSize.width;
        int y = screenSize.height;
        setBounds(x/4, y/4, 450, 300);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		{
			rdbtnSearchFileOnly = new JRadioButton("Search file only this extension:");
			rdbtnSearchFileOnly.setBounds(5, 28, 199, 23);
			contentPanel.add(rdbtnSearchFileOnly);
		}
		
		{
			rdbtnSearchAllFiles = new JRadioButton("Search all files");
			rdbtnSearchAllFiles.setBounds(5, 5, 199, 23);
			rdbtnSearchAllFiles.setSelected(true);
			contentPanel.add(rdbtnSearchAllFiles);
		}
		
		ButtonGroup group = new ButtonGroup();
		group.add(rdbtnSearchAllFiles);
		group.add(rdbtnSearchFileOnly);
		
		 // Регистрируем обработчик событий для кнопок
		  RadioListener myListener = new RadioListener();
		  rdbtnSearchAllFiles.addActionListener(myListener);
		  rdbtnSearchFileOnly.addActionListener(myListener);
		
		{
			chckbxDocuments = new JCheckBox("documents(doc;xls;txt)");
			chckbxDocuments.setHorizontalAlignment(SwingConstants.LEFT);
			chckbxDocuments.setBounds(15, 49, 189, 23);
			chckbxDocuments.setEnabled(false);
			contentPanel.add(chckbxDocuments);
		}
		{
			chckbxVideo = new JCheckBox("video files(avi;mov;mkv)");
			chckbxVideo.setBounds(15, 72, 189, 23);
			chckbxVideo.setEnabled(false);
			contentPanel.add(chckbxVideo);
		}
		{
			chckbxPictures = new JCheckBox("pictures(jpg;gif;bmp;png)");
			chckbxPictures.setBounds(15, 95, 189, 23);
			chckbxPictures.setEnabled(false);
			contentPanel.add(chckbxPictures);
		}
		{
			chckbxOther = new JCheckBox("Other");
			chckbxOther.setHorizontalAlignment(SwingConstants.LEFT);
			chckbxOther.setBounds(15, 118, 189, 23);
			chckbxOther.setEnabled(false);
			contentPanel.add(chckbxOther);
		}
		{
			textFieldExtensions = new JTextField();
			textFieldExtensions.setBounds(204, 121, 233, 20);
			textFieldExtensions.setEnabled(false);
			contentPanel.add(textFieldExtensions);
			textFieldExtensions.setColumns(10);
		}
		
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.setActionCommand("OK");
				okButton.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent arg0) {
						extensions = "";
						// тут будут траблы с точкозапятой )
						if (chckbxDocuments.isSelected()){
							extensions = addSemicolon(extensions);
							extensions += "doc;txt;docx;odt;xls";
						}
						if (chckbxPictures.isSelected()){
							extensions = addSemicolon(extensions);
							extensions += "jpg;png;bmp";
						}
						if (chckbxVideo.isSelected()){
							extensions = addSemicolon(extensions);
							extensions += "avi;mov;mkv";
						}
						//нужно проверять содержание текстового поля
						if (chckbxOther.isSelected()){
							extensions = addSemicolon(extensions);
							extensions += textFieldExtensions.getText();
						}
						System.out.println(extensions);
						setVisible(false);
					}
				});
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("Cancel");
				cancelButton.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent arg0) {
						dispose();
					}
				});
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
		
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setVisible(true);
	}
	
	private String addSemicolon(String str) {
		if ((str.length() > 0) && (str.charAt(str.length()-1) != ';')) {
			str += ";";
		}
		return str;
	}
	
	public String getExtensions() {
		return extensions;
	}
	
	private void changeEnabledComponent(boolean enable){
		chckbxDocuments.setEnabled(enable);
		chckbxVideo.setEnabled(enable);
		chckbxPictures.setEnabled(enable);
		chckbxOther.setEnabled(enable);
		textFieldExtensions.setEnabled(enable);
	}
	
	class RadioListener implements ActionListener {
		 public void actionPerformed(ActionEvent e){
			 if (e.getSource() == rdbtnSearchAllFiles) {
				 changeEnabledComponent(false);
			 } else {
				 changeEnabledComponent(true);
			 }
		 }
	 }

}
