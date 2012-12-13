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
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.RowSpec;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

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
		contentPanel.setLayout(new FormLayout(new ColumnSpec[] {
				ColumnSpec.decode("199px"),
				ColumnSpec.decode("233px:grow"),
				FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
				ColumnSpec.decode("137px"),
				FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
				ColumnSpec.decode("215px"),},
			new RowSpec[] {
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				RowSpec.decode("23px"),
				RowSpec.decode("23px"),
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.UNRELATED_GAP_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.LINE_GAP_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,}));
		{
			rdbtnSearchFileOnly = new JRadioButton("Search file only this extension:");
			contentPanel.add(rdbtnSearchFileOnly, "1, 2");
		}
		
		{
			rdbtnSearchAllFiles = new JRadioButton("Search all files");
			rdbtnSearchAllFiles.setSelected(true);
			contentPanel.add(rdbtnSearchAllFiles, "1, 1");
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
			chckbxDocuments.setEnabled(false);
			contentPanel.add(chckbxDocuments, "1, 3, left, top");
		}
		{
			chckbxVideo = new JCheckBox("video files(avi;mov;mkv)");
			chckbxVideo.setEnabled(false);
			contentPanel.add(chckbxVideo, "1, 4, left, top");
		}
		{
			chckbxPictures = new JCheckBox("pictures(jpg;gif;bmp;png)");
			chckbxPictures.setEnabled(false);
			contentPanel.add(chckbxPictures, "1, 5, left, top");
		}
		{
			chckbxOther = new JCheckBox("Other");
			chckbxOther.setEnabled(false);
			contentPanel.add(chckbxOther, "1, 6, left, top");
		}
		{
			textFieldExtensions = new JTextField();
			textFieldExtensions.setEnabled(false);
			contentPanel.add(textFieldExtensions, "2, 6, fill, default");
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
						if (chckbxDocuments.isSelected())
							extensions += "doc;txt;docx;odt;xls;";
						if (chckbxPictures.isSelected())
							extensions += "jpg;png;bmp;";
						if (chckbxVideo.isSelected())
							extensions += "avi;mov;mkv";
						//нужно проверять содержание текстового поля
						if (chckbxOther.isSelected())
							extensions += textFieldExtensions.getText();
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
