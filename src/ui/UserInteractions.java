package ui;

import game.GameStatus;
import game.TurnResult;
import words.Letter;
import words.Phrase;

import java.io.IOException;
import java.util.Set;

/**
 * This interface defines the methods that the game logic will require for interacting with the user. Both displaying
 * data to the user and taking in data from user input. Since this is just an interface, it only what the methods are.
 * Other classes will implement this interface to fill out how the interaction will actually work. This serves a couple
 * purposes.
 *
 * First, it separates different concerns, so game logic can live one place, and user interaction code can live in
 * another. This makes this organized and easy to read, which gets increasingly important as the code grows and changes.
 *
 * Also, this allows the game logic to use a UserInteractions object without caring what it's underlying type is, or how
 * it actually handles the user interactions. This makes the code modular. If we decide we want to build a different
 * UI, all we have to do is create a new class that implements this interface, and have the core application logic
 * (in this case the main method in Hangman.java) create the Game using the new UserInteractions implementation.
 * No need to change any game logic to change the UI, since they're not tied together.
 */
public interface UserInteractions {

    /** get a phrase from one user that the other player will try to guess */
    Phrase getPhraseToGuess();

    /** show the given data about the game to the user */
    void displayGameState(Phrase phraseToGuess,
                          Set<Letter> correctlyGuessedLetters,
                          Set<Letter> incorrectlyGuessedLetters,
                          Set<Phrase> incorrectlyGuessedPhrases,
                          GameStatus gameStatus);

    /** get the next guess from the guessing player - can either be a single letter or the whole phrase */
    TurnResult getTurnResult(Set<Letter> availableToGuessLetters);

    /**
     * the number of wrong guesses before the game is over and the guessing player loses
     * this arguably could be considered game logic, but since it's tied to the display of the parts of the hangman,
     * it also kinda makes sense to let the UI decide this
     */
    int getMaxWrongGuesses();

    /** after a game ends, asks the user whether they want to play another game */
    boolean wantToPlayAgain();

    /** show some message to the user to let them know there was a problem loading the dictionary  */
    void displayDictionaryFailedToLoadMessage(IOException e);
}
