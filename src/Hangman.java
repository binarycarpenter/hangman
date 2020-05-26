import game.Game;
import ui.CommandLineUserInteractions;
import ui.UserInteractions;

/**
 * The main class to run the Hangman game. This class defines the type of UI we're going to use, and then just plays
 * games repeatedly until the user says they don't want a new game.
 */
public class Hangman {

    private final UserInteractions userInteractions;

    public Hangman(UserInteractions userInteractions) {
        this.userInteractions = userInteractions;
    }

    private void playUntilQuit() {
        boolean keepPlaying = true;
        while (keepPlaying) {
            new Game(userInteractions);
            keepPlaying = userInteractions.wantToPlayAgain();
        }
    }

    // creates a new Hangman game with the command line UI module, and plays repeatedly until the user quits
    // we could run the game with different UI by writing a new implementation of UserInteraction and creating it here
    public static void main(String[] args) {
        new Hangman(new CommandLineUserInteractions()).playUntilQuit();
    }
}
