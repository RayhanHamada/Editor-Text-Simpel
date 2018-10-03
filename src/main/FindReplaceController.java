package main;

/*
 * @author RayhanHamada
 * 
 */

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;

public class FindReplaceController implements Initializable
{

	@FXML private TextField tfFind;
	@FXML private TextField tfReplace;
	
	@FXML private CheckBox cbCaseInsensitive;
	@FXML private CheckBox cbWholeWord;
	@FXML private CheckBox cbRegex;
	
	@FXML private RadioButton rbForward;
	@FXML private RadioButton rbBackward;
	
	@FXML private Button btnFind;
	@FXML private Button btnReplace;
	@FXML private Button btnFindReplace;
	@FXML private Button btnReplaceAll;
	
	private ToggleGroup tg;
	
	
	//for change
	public void initialize(URL arg0, ResourceBundle arg1) {
		tg = new ToggleGroup();
		rbForward.setToggleGroup(tg);
		rbForward.setToggleGroup(tg);
		
		
		
	}

}
