package com.udav.findduplicate;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.beans.PropertyChangeListener;

import javax.swing.Action;
import javax.swing.ButtonGroup;
import javax.swing.InputVerifier;
import javax.swing.JButton;
import javax.swing.JComponent;
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
	private JCheckBox chckbxAudio;
	
	private String extensions;
	
	/**
	 * Create the dialog.
	 */
	public SettingsDialog() {
		setResizable(false);
		//setModal(true);
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
			chckbxDocuments = new JCheckBox("documents(doc;xls;txt;...)");
			chckbxDocuments.setHorizontalAlignment(SwingConstants.LEFT);
			chckbxDocuments.setBounds(15, 49, 232, 23);
			chckbxDocuments.setEnabled(false);
			contentPanel.add(chckbxDocuments);
		}
		{
			chckbxVideo = new JCheckBox("video files(avi;mov;mkv;...)");
			chckbxVideo.setBounds(15, 72, 232, 23);
			chckbxVideo.setEnabled(false);
			contentPanel.add(chckbxVideo);
		}
		
		chckbxAudio = new JCheckBox("audio files(mp3;flac;acc;...)");
		chckbxAudio.setEnabled(false);
		chckbxAudio.setBounds(15, 95, 232, 23);
		contentPanel.add(chckbxAudio);
		{
			chckbxPictures = new JCheckBox("pictures(jpg;gif;bmp;png;...)");
			chckbxPictures.setBounds(15, 118, 232, 23);
			chckbxPictures.setEnabled(false);
			contentPanel.add(chckbxPictures);
		}
		{
			chckbxOther = new JCheckBox();
			chckbxOther.setHorizontalAlignment(SwingConstants.LEFT);
			chckbxOther.setBounds(15, 141, 232, 23);
			chckbxOther.setEnabled(false);
			chckbxOther.setAction(new Action() {
				@Override
				public void actionPerformed(ActionEvent e) {
					if (chckbxOther.isSelected())
						textFieldExtensions.setEnabled(true);
					else textFieldExtensions.setEnabled(false);
				}
				
				@Override
				public void setEnabled(boolean arg0) {}
				
				@Override
				public void removePropertyChangeListener(PropertyChangeListener arg0) {}

				@Override
				public void putValue(String arg0, Object arg1) {}
				
				@Override
				public boolean isEnabled() {
					return false;
				}
				
				@Override
				public Object getValue(String arg0) {
					return null;
				}
				
				@Override
				public void addPropertyChangeListener(PropertyChangeListener arg0) {}
			});
			chckbxOther.setText("Other");
			contentPanel.add(chckbxOther);
		}
		{
			textFieldExtensions = new JTextField();
			textFieldExtensions.setBounds(259, 141, 178, 20);
			textFieldExtensions.setEnabled(false);
			textFieldExtensions.addKeyListener(new KeyListener() {
				
				@Override
				public void keyTyped(KeyEvent e) {
					e.getKeyChar();					
				}
				
				@Override
				public void keyReleased(KeyEvent arg0) {}
				
				@Override
				public void keyPressed(KeyEvent arg0) {}
			});
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
							extensions += "jpg;png;bmp;gif;jpeg";
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
						if (chckbxAudio.isSelected()){
							extensions = addSemicolon(extensions);
							extensions += "mp3;acc;flac";
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
		chckbxAudio.setEnabled(enable);
		if (chckbxOther.isSelected()) textFieldExtensions.setEnabled(enable);
	}
	
	class RadioListener implements ActionListener {
		 public void actionPerformed(ActionEvent e){
			 if (e.getSource() == rdbtnSearchAllFiles) {
				 changeEnabledComponent(false);
				 extensions = "*";
			 } else {
				 changeEnabledComponent(true);
			 }
		 }
	 }
	
	public class NumericVerifier extends InputVerifier {
        @Override   
        public boolean verify(JComponent input) {
        	//Check type of the control
        	String text = "";
            if(input instanceof JTextField) {   
            	JTextField tf = (JTextField) input; 
            	text = tf.getText().trim(); 
            }
            boolean matches = text.matches(".[a-z][0-9]");
            input.setBackground( ( matches ) ? Color.WHITE :  Color.RED);
            
            return matches; 
        }

}
}
