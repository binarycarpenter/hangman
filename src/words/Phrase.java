package words;

import game.GameStatus;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * This class represents a phrase, which can be one or more words, made up of a ordered list of Letters.
 * Spaces in between words are represented with a null Letter
 */
public class Phrase {
    private List<Letter> orderedLetters; // letters in order, making up the word or phrase

    // unique letters used to do a quicker check on whether the phrase contains a letters
    private Set<Letter> uniqueLetters;

    public Phrase(List<Letter> letters) {
        this.orderedLetters = letters;
        this.uniqueLetters = letters.stream()
                                    .filter(Objects::nonNull) // we don't care about spaces in our set of unique letters
                                    .collect(Collectors.toSet());
    }

    /** does our phrase contain the given letter? */
    public boolean contains(Letter letter) {
        return uniqueLetters.contains(letter);
    }

    /** have all the letters in our phrase been guessed? */
    public boolean allLettersGuessed(Set<Letter> guessedLetters) {
        return guessedLetters.containsAll(uniqueLetters);
    }

    /** get the phrase as a String - print out the whole thing without worrying about hiding any unguessed letters */
    public String asRawString() {
        return orderedLetters.stream()
                             .map(Letter::asString)
                             .collect(Collectors.joining());
    }

    /** get the phrase as a String, but replace any letters that haven't been guessed with the given hidden string */
    public String asStringWithHiding(Set<Letter> correctlyGuessedLetters, GameStatus gameStatus, String hiddenString) {
        return orderedLetters.stream()
                             .map(letter -> letterAsString(letter, correctlyGuessedLetters, gameStatus, hiddenString))
                             .collect(Collectors.joining());
    }

    // return the hidden value if the given letter is non-null and we haven't guessed it, and the game is still going
    private String letterAsString(Letter letter, Set<Letter> correctlyGuessedLetters, GameStatus gameStatus, String hiddenString) {
        return letter == null ||
               correctlyGuessedLetters.contains(letter) ||
               gameStatus.isGameOver() ? Letter.asString(letter) : hiddenString;
    }
}
