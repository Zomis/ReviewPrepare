package net.zomis.reviewprepare.util;

import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Region;
import javafx.stage.Stage;

/**
 * Handle FXML Launching
 *
 * @author Bhathiya
 */
public class FxmlTools {

    /**
     * Launch a FXML based interface
     *
     * @param FXML class path to FXML
     * @param title title for the window
     * @param stage stage to render the FXML on
     * @param parameters parameters to pass to controller
     * @throws IOException
     */
    public static void launchFxml(String FXML, String title, Stage stage,
            Object[] parameters) throws IOException {

        stage.setTitle(title);

        FXMLLoader loader = new FXMLLoader(FxmlTools.class.getResource(
                FXML));
        Region root = loader.load();
        Object controller = loader.getController();
        if (controller instanceof Launchable) {
            Launchable launchable = (Launchable) controller;
            launchable.launch(stage, parameters);
        }

        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}
