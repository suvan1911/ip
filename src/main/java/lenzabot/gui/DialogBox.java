package lenzabot.gui;

import java.io.IOException;
import java.util.Collections;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

/**
 * Represents one message in the chat conversation.
 */
public class DialogBox extends HBox {
    @FXML
    private Label dialog;

    @FXML
    private Label avatar;

    private DialogBox(String text, String avatarText, String styleClass) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(MainWindow.class.getResource("/view/DialogBox.fxml"));
            fxmlLoader.setController(this);
            fxmlLoader.setRoot(this);
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new AssertionError("Unable to load dialog box layout", exception);
        }

        dialog.setText(text);
        avatar.setText(avatarText);
        getStyleClass().add(styleClass);
    }

    private void flip() {
        ObservableList<Node> children = FXCollections.observableArrayList(getChildren());
        Collections.reverse(children);
        getChildren().setAll(children);
        setAlignment(Pos.TOP_LEFT);
    }

    /**
     * Creates a right-aligned dialog for a user command.
     *
     * @param text Command text.
     * @return User dialog box.
     */
    public static DialogBox getUserDialog(String text) {
        return new DialogBox(text, "YOU", "user-dialog");
    }

    /**
     * Creates a left-aligned dialog for a LenZaBot response.
     *
     * @param text Response text.
     * @return LenZaBot dialog box.
     */
    public static DialogBox getBotDialog(String text) {
        DialogBox dialogBox = new DialogBox(text, "LZ", "bot-dialog");
        dialogBox.flip();
        return dialogBox;
    }
}
