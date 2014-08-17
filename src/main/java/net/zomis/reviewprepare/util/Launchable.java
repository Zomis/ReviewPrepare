package net.zomis.reviewprepare.util;

import javafx.stage.Stage;

/**
 * Launch-able interface
 *
 */
public interface Launchable {

    /**
     * launch a user interface
     *
     * @param stage current stage
     * @param parameters
     */
    void launch(Stage stage, Object[] parameters);
}
