package net.zomis.reviewprepare;

import java.util.List;
import javafx.application.Application;
import javafx.stage.Stage;
import net.zomis.reviewprepare.util.FxmlTools;

/**
 * Review Prepare Application Startup point
 *
 */
public class ReviewPrepareApplication extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {

        final Parameters params = getParameters();
        final List<String> parameters = params.getRaw();

        if (parameters.isEmpty()) {
            //show the stage
            FxmlTools.launchFxml("/fxml/ReviewPrepare.fxml", "Review Prepare",
                    primaryStage, null);
        } else {
            //use parameters 
            ReviewPreparer.start(parameters.toArray(
                    new String[parameters.size()]));
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
