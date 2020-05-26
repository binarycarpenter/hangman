package game;

import words.Letter;
import words.Phrase;

/**
 * A class representing the data we got back from the user about what they guessed this turn.
 * This is can be either a single letter or a phrase
 */
public class TurnResult {

    /** an enum representing the types of guesses - letter and phrase */
    public enum TurnType { GUESSED_LETTER, GUESSED_PHRASE }

    private TurnType type;         // the type of this turn
    private Letter guessedLetter;  // the letter that was guessed - only non-null when type is GUESSED_LETTER
    private Phrase guessedPhrase;  // the phrase that was guessed - only non-null when type is GUESSED_PHRASE

    // private so other classes can't access this directly, instead must use one of the static constructors below
    private TurnResult(TurnType type, Letter guessedLetter, Phrase guessedPhrase) {
        this.type = type;
        this.guessedLetter = guessedLetter;
        this.guessedPhrase = guessedPhrase;
    }

    /** construct a TurnResult from the given guessed Letter */
    public static TurnResult guessedLetter(Letter guessedLetter) {
        return new TurnResult(TurnType.GUESSED_LETTER, guessedLetter, null);
    }

    /** construct a TurnResult from the given guessed Phrase */
    public static TurnResult guessedPhrase(Phrase guessedPhrase) {
        return new TurnResult(TurnType.GUESSED_PHRASE, null, guessedPhrase);
    }

    public TurnType getType() {
        return type;
    }

    public Letter getGuessedLetter() {
        return guessedLetter;
    }

    public Phrase getGuessedPhrase() {
        return guessedPhrase;
    }
}
