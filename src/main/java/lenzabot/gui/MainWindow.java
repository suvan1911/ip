package lenzabot.gui;

import javafx.fxml.FXML;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import lenzabot.LenZaBot;

/**
 * Controls LenZaBot's main chat window.
 */
public class MainWindow extends AnchorPane {
    @FXML
    private ScrollPane scrollPane;

    @FXML
    private VBox dialogContainer;

    @FXML
    private TextField userInput;

    private LenZaBot lenZaBot;

    /**
     * Initializes behavior that depends on injected FXML controls.
     */
    @FXML
    public void initialize() {
        dialogContainer.heightProperty().addListener(observable -> scrollPane.setVvalue(1.0));
    }

    /**
     * Injects the chatbot and displays its initial greeting.
     *
     * @param lenZaBot Chatbot used to process commands.
     */
    public void setLenZaBot(LenZaBot lenZaBot) {
        this.lenZaBot = lenZaBot;
        dialogContainer.getChildren().add(DialogBox.getBotDialog(
                "Hi! I'm Lenza. Add a task or type `list` to see what you have planned."));
    }

    /**
     * Submits the input and displays the user's command and LenZaBot's response.
     */
    @FXML
    private void handleUserInput() {
        String input = userInput.getText().trim();
        if (input.isEmpty()) {
            return;
        }

        String response = lenZaBot.getResponse(input);
        dialogContainer.getChildren().addAll(
                DialogBox.getUserDialog(input),
                DialogBox.getBotDialog(response));
        userInput.clear();
    }
}
