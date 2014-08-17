package net.zomis.reviewprepare.util;

import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.Region;
import javafx.stage.Stage;

/**
 * Handle FXML Launching
 *
 */
public class FxmlTools {

    /**
     * Launch a Node on a given stage
     *
     * @param node Node to display
     * @param title title for the window
     * @param stage stage to render the Node on
     * @param parameters parameters to pass to controller
     * @throws IOException
     */
    public static void launchFxml(Node node, String title, Stage stage,
            Object[] parameters) throws IOException {
        
        stage.setTitle(title);
        Scene scene = new Scene((Region) node);
        stage.setScene(scene);
        stage.show();
    }
    
    /**
     * Launch FXML based UI on a given stage
     * @param fxmlPath path to FXML
     * @param title window title
     * @param stage stage to render the FXML on
     * @param parameters parameters to pass to controller
     * @throws IOException 
     */
    public static void launchFxml(String fxmlPath, String title, Stage stage,
            Object[] parameters) throws IOException {
        
        launchFxml(FxmlToNode(fxmlPath, stage, parameters), title, stage,
                parameters);
    }

    /**
     * Load a FXML to a node
     *
     * @param fxmlPath path to FXML
     * @param stage stage to be passed via Launch-able interface
     * @param parameters parameters to pass to controller
     * @return Node 
     * @throws IOException
     */
    public static Node FxmlToNode(String fxmlPath, Stage stage,
            Object[] parameters)
            throws IOException {
        
        Region root;
        FXMLLoader loader
                = new FXMLLoader(FxmlTools.class.getResource(fxmlPath));
        root = loader.load();
        Object controller = loader.getController();
        if (controller instanceof Launchable) {
            Launchable launchable = (Launchable) controller;
            launchable.launch(stage, parameters);
        }
        return root;
        
    }
}
