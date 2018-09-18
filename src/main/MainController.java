package main;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringReader;
import java.net.URL;
import java.nio.file.Files;
import java.util.Optional;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioMenuItem;
import javafx.scene.control.TextArea;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage; 

public class MainController implements Initializable, ClipboardOwner 
{

	@FXML private MenuBar mb;
	
	//for mb MenuBar
	@FXML private Menu mFile;
	@FXML private Menu mEdit;
	@FXML private Menu mFormat;
	@FXML private Menu mView;
	@FXML private Menu mHelp;
	@FXML private Menu mSetting;
	
	//for mFile Menu
	@FXML private MenuItem miNew;
	@FXML private MenuItem miOpen;
	@FXML private MenuItem miSave;
	@FXML private MenuItem miSaveAs;
	@FXML private MenuItem miClose;
	@FXML private MenuItem miExit;
	
	//for mEdit Menu
	@FXML private MenuItem miUndo;
	@FXML private MenuItem miCut;
	@FXML private MenuItem miCopy;
	@FXML private MenuItem miPaste;
	@FXML private MenuItem miDelete;
	@FXML private MenuItem miFindAndReplace;
	@FXML private MenuItem miSelectAll;
	
	//for mFormat Menu
	@FXML private RadioMenuItem miWrapText;
	
	//for miView Menu
	@FXML private RadioMenuItem miStatusBar;
	
	//for mHelp Menu
	@FXML private MenuItem miGetHelp;
	@FXML private MenuItem miAboutUs;
	
	//for mSetting Menu
	@FXML private MenuItem miPreferences;
	
	//for TextArea
	@FXML private TextArea ta;
	@FXML private Label lblFilename;
	@FXML private Label lblStatusBar;
	
	private File currentFile = null;
	private boolean isFileOpened = false;
	private String previousText = "";
	
	// initializable implemented method
	public void initialize(URL arg0, ResourceBundle arg1) {
		lblFilename.setText("untitled");

	}
	// ClipboardOwner implemented method
	public void lostOwnership(Clipboard arg0, Transferable arg1) {
		//gk ngapa-ngapain
	}
	
	@FXML public void doNew()
	{
		if (!isFileOpened)
		{
			if (ta.getText().trim().equals(""))
			{
				lblFilename.setText("untitled");
				previousText = "";
			}
			else
			{
				Alert alert = new Alert(AlertType.CONFIRMATION);
				alert.setTitle("Hamada Text Editor");
				alert.setHeaderText("Save ?");
				alert.setContentText("Do you want to save current text ?");
				Optional<ButtonType> result = alert.showAndWait();
				if (result.get() == ButtonType.OK){
				    doSaveAs();
				} 
				else 
				{
				   ta.setText("");
				}
			}
		}
		else
		{
			if (isContentModified(currentFile, ta.getText()))
			{
				Alert alert = new Alert(AlertType.CONFIRMATION);
				alert.setTitle("Hamada Text Editor");
				alert.setHeaderText("Save ?");
				alert.setContentText("Do you want to save this modified text file ?");
				Optional<ButtonType> result = alert.showAndWait();
				
				if (result.get() == ButtonType.OK)
				{
				    doSave();
				}
				else {
				   ;
				}
			}
			else
			{
				ta.setText("");
				lblFilename.setText("untitled");
			}
		}
	}
	
	public boolean isContentModified(File file, String text)
	{
		String filetext = "";
		boolean modified = false;
		try {
			BufferedReader brf = new BufferedReader(new FileReader(file));
			BufferedReader brt = new BufferedReader(new StringReader(text));
			
			while ((filetext = brf.readLine()) != null || (text = brt.readLine()) != null)
			{
				if (filetext != text) modified = true;
			}
			brf.close();
			brt.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return modified;
	}
	
	@FXML public void doOpen()
	{
		ExtensionFilter extFilter = new ExtensionFilter("Text file (*.txt)", "*.txt");
		FileChooser fc = new FileChooser();
		fc.getExtensionFilters().add(extFilter);
		currentFile = fc.showOpenDialog(null);
		
		if (!isFileOpened) {
			try {
				byte[] fileBytes = Files.readAllBytes(currentFile.toPath());
				char singleChar;
				for (byte b : fileBytes) {
					singleChar = (char) b;
					ta.setText(ta.getText() + singleChar);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			lblFilename.setText(currentFile.getName());
			isFileOpened = true;
		}
		else
		{
			ta.setText("");
			try {
				byte[] fileBytes = Files.readAllBytes(currentFile.toPath());
				char singleChar;
				for (byte b : fileBytes) {
					singleChar = (char) b;
					ta.setText(ta.getText() + singleChar);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			lblFilename.setText(currentFile.getName());
			isFileOpened = true;
		}
		previousText = ta.getText();
	}
	
	@FXML public void doSave()
	{
		if (currentFile != null) 
		{
			try {
				FileWriter fileWriter = new FileWriter(currentFile);
				fileWriter.write(ta.getText());
				fileWriter.flush();
				fileWriter.close();
				previousText = ta.getText();
			} catch (Exception e) {
				e.printStackTrace();
			} 
		} else if (currentFile == null && !ta.getText().equals(""))
		{
			doSaveAs();
		}
	}
	
	@FXML public void doClose()
	{
		currentFile = null;
		ta.setText("");
		lblFilename.setText("untitled");
		isFileOpened = false;
	}
	
	@FXML public void doSaveAs()
	{
		String text = ta.getText();
		ExtensionFilter extFilter = new ExtensionFilter("Text file (*.txt)", "*.txt");
		FileChooser fc = new FileChooser();
		fc.getExtensionFilters().add(extFilter);
		File file = fc.showSaveDialog(null);
		if (file != null) {
			try {
				FileWriter fileWriter = new FileWriter(file);
				fileWriter.write(text);
				fileWriter.flush();
				fileWriter.close();
				previousText = ta.getText();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	@FXML public void doExit()
	{
		System.exit(0);
	}
	
	// for edit menu
	
	@FXML public void doUndo()
	{
		String tempText = ta.getText();
		ta.setText(previousText);
		previousText = tempText;
	}
	
	@FXML public void doCut()
	{
		StringSelection stringSelection = new StringSelection(ta.getSelectedText());
	    Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
	    clipboard.setContents(stringSelection, this);
	    
	    int start = Integer.valueOf(ta.getSelection().toString().split(", ")[0]);
	    int end = Integer.valueOf(ta.getSelection().toString().split(", ")[1]);
	    
	    StringBuffer buffer = new StringBuffer(ta.getText());
	    ta.setText(buffer.replace(start, end, "").toString());
	    
	}
	
	@FXML public void doCopy()
	{
		StringSelection stringSelection = new StringSelection(ta.getSelectedText());
	    Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
	    clipboard.setContents(stringSelection, this);
	}
	
	@FXML public void doPaste()
	{
		String result = "";
	    Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
	    Transferable contents = clipboard.getContents(null);
	    boolean hasTransferableText = (contents != null) && contents.isDataFlavorSupported(DataFlavor.stringFlavor);
	    if (hasTransferableText) {
	      try {
	        result = (String)contents.getTransferData(DataFlavor.stringFlavor);
	      }
	      catch (Exception e){
	        e.printStackTrace();
	      }
	    }
	}
	
	@FXML public void doDelete()
	{
		StringBuffer buffer = new StringBuffer(ta.getText());
		int start = Integer.valueOf(ta.getSelection().toString().split(", ")[0]);
		int end = Integer.valueOf(ta.getSelection().toString().split(", ")[1]);
		ta.setText(buffer.replace(start, end, "").toString());
	}
	
	@FXML public void doFandR()
	{
		try {
			Parent fr = FXMLLoader.load(getClass().getResource("/main/findreplace.fxml"));
			Scene scene = new Scene(fr);
			Stage sfr = new Stage();
			sfr.setScene(scene);
			sfr.setTitle("Find and Replace");
			sfr.setResizable(false);
			sfr.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@FXML public void doSelectAll()
	{
		ta.selectAll();
	}
	
	//for Format Menu
	@FXML public void toggleWrapText()
	{
		ta.setWrapText(false);
		if (miWrapText.isSelected()) ta.setWrapText(true);
	}
	
	//for View Menu
	@FXML public void toggleStatusBar()
	{
		lblStatusBar.setVisible(false);
		if (miStatusBar.isSelected()) lblStatusBar.setVisible(true);
	}
	
	

	
}
