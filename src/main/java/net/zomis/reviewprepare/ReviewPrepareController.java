package net.zomis.reviewprepare;


import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import net.zomis.reviewprepare.util.Launchable;

/**
 * FXML Controller class
 *
 */
public class ReviewPrepareController implements Initializable, Launchable {

    private Stage stage;
    
    @FXML
    private ListView<?> lvFiles;

    @FXML
    private TextArea txtQuestion;

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
        // TODO
    }

    @Override
    public void launch(Stage stage, Object[] parameters) {
        this.stage = stage;
    }

}
