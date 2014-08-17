package net.zomis.reviewprepare;


import java.io.File;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import net.zomis.reviewprepare.util.Launchable;

/**
 * ReviewPrepar FXML Controller
 *
 */
public class ReviewPrepareController implements Initializable, Launchable {


    private Stage stage; //to use with control fx to show messageboxes 


    

    @FXML
    private ListView<File> lvFiles;

    @FXML
    private TextArea txtQuestion;

    private ObservableList<File> filesModel;
    
    @FXML
    void btnAddFilesOnAction(ActionEvent event) {

    }

    @FXML
    void btnRemoveFilesOnAction(ActionEvent event) {

    }

    @FXML
    void btnGenerateStubOnAction(ActionEvent event) {

    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        filesModel = lvFiles.getItems();
    }

    @Override
    public void launch(Stage stage, Object[] parameters) {
        this.stage = stage;
    }

}
