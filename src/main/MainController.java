package main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.StringReader;
import java.net.URL;
import java.nio.file.Files;
import java.util.Optional;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
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

public class MainController implements Initializable {

	@FXML private MenuBar mb;
	
	@FXML private Menu mFile;
	@FXML private Menu mEdit;
	@FXML private Menu mFormat;
	@FXML private Menu mView;
	@FXML private Menu mHelp;
	
	// for mFile Menu
	@FXML private MenuItem miNew;
	@FXML private MenuItem miOpen;
	@FXML private MenuItem miSave;
	@FXML private MenuItem miSaveAs;
	@FXML private MenuItem miClose;
	@FXML private MenuItem miExit;
	
	// for mEdit Menu
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
	
	//for mHelp
	@FXML private MenuItem miGetHelp;
	@FXML private MenuItem miAboutUs;
	
	
	//for TextArea
	@FXML private TextArea ta;
	@FXML private Label lblFilename;
	@FXML private Label lblStatusBar;
	
	private File currentFile = null;
	private boolean isOpeningFile = false;
	
	public void initialize(URL arg0, ResourceBundle arg1) {
		lblFilename.setText("untitled");
	}
	
	@FXML public void doNew()
	{
		if (!isOpeningFile)
		{
			if (ta.getText().trim().equals(""))
			{
				lblFilename.setText("untitled");
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
				} else {
				   ta.setText("");
				}
			}
		}
		else
		{
			if (isContentModified(currentFile, ta.getText()))
			{
				Alert alert = new Alert(AlertType.CONFIRMATION);
//				alert.getButtonTypes().addAll(btnOk, btnNo, btnCancel);
				alert.setTitle("Hamada Text Editor");
				alert.setHeaderText("Save ?");
				alert.setContentText("Do you want to save this modified text file ?");
				Optional<ButtonType> result = alert.showAndWait();
				if (result.get() == ButtonType.OK){
				    doSave();
				}
				else {
				   System.out.println("error");
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
				if ((text = brt.readLine()) != null)
					if (!filetext.equals(text))
						modified = true;
//				System.out.println("hello");
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
		
		if (currentFile != null) {
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
			isOpeningFile = true;
		}
		else
		{
			
		}
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
			} catch (Exception e) {
				e.printStackTrace();
			} 
		}
	}
	
	@FXML public void doExit()
	{
		System.exit(0);
	}
}
