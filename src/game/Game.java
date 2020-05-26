package game;

import ui.UserInteractions;
import words.Letter;
import words.Phrase;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * This class keeps track of the game state and implements game logic, e.g. updating state when a user takes a turn,
 * determining when the game is over.
 */
public class Game {

    private Phrase phraseToGuess; // the phrase that we'll being trying to guess for this game
    private GameStatus gameStatus; // the current status of the game

    private UserInteractions userInteractions; // a way to get input from the user

    // letters that haven't been guessed yet, starts out as a set of all the letters
    private Set<Letter> availableToGuessLetters = new HashSet<>(Arrays.asList(Letter.values()));

    private Set<Letter> correctlyGuessedLetters = new HashSet<>();   // guessed letters which were in the phrase
    private Set<Letter> incorrectlyGuessedLetters = new HashSet<>(); // guessed letters which were not in the phrase
    private Set<Phrase> incorrectlyGuessedPhrases = new HashSet<>(); // guessed phrases which were wrong

    /** sets up a new game, need to call play() to actually start guessing */
    public Game(UserInteractions userInteractions) {
        this.userInteractions = userInteractions;
        this.gameStatus = GameStatus.STARTING;
        play();
    }

    // keep asking for more guesses until the game has finished with some outcome
    private void play() {
        phraseToGuess = userInteractions.getPhraseToGuess();
        displayGameState();
        gameStatus = GameStatus.GUESSING;

        while (!gameStatus.isGameOver()) {
            gameStatus = updateFromTurnResult(userInteractions.getTurnResult(availableToGuessLetters));
            displayGameState();
        }
    }

    // display the current game state to the user
    private void displayGameState() {
        userInteractions.displayGameState(phraseToGuess, correctlyGuessedLetters, incorrectlyGuessedLetters,
                                          incorrectlyGuessedPhrases, gameStatus);
    }

    // update the state of the game based on the turn data we just got from the user
    private GameStatus updateFromTurnResult(TurnResult turnResult) {
        switch (turnResult.getType()) {
            case GUESSED_LETTER:
                return updateFromGuessedLetter(turnResult.getGuessedLetter());
            case GUESSED_PHRASE:
                return updateFromGuessedPhrase(turnResult.getGuessedPhrase());
            default:
                throw new IllegalArgumentException("unknown turn result type: " + turnResult.getType());
        }
    }

    // update game state based on the given guessed Letter
    private GameStatus updateFromGuessedLetter(Letter guessedLetter) {
        availableToGuessLetters.remove(guessedLetter); // can't guess this letter anymore

        if (phraseToGuess.contains(guessedLetter)) { // this was a correct guess
            correctlyGuessedLetters.add(guessedLetter);

            // if all letters in the phrase are now guessed, the game is over - otherwise we need to keep guessing
            return phraseToGuess.allLettersGuessed(correctlyGuessedLetters) ?
                   GameStatus.GUESSED_ALL_LETTERS_CORRECTLY : GameStatus.GUESSING;
        }
        else { // the guessed letter wasn't in the phrase
            incorrectlyGuessedLetters.add(guessedLetter);
            return incorrectGuessResult();
        }
    }

    // update game state based on the given guessed Phrase
    private GameStatus updateFromGuessedPhrase(Phrase guessedPhrase) {
        if (phraseToGuess.asRawString().equals(guessedPhrase.asRawString())) {
            return GameStatus.GUESSED_PHRASE_CORRECTLY;
        }
        else { // guessed phrase was wrong
            incorrectlyGuessedPhrases.add(guessedPhrase);
            return incorrectGuessResult();
        }
    }

    // game is over if we've hit the max number of wrong guesses, otherwise we keep guessing
    private GameStatus incorrectGuessResult() {
        return incorrectlyGuessedPhrases.size() + incorrectlyGuessedLetters.size() >= userInteractions.getMaxWrongGuesses() ?
               GameStatus.TOO_MANY_WRONG_GUESSES : GameStatus.GUESSING;
    }
}
