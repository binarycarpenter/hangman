package ui;

import game.GameStatus;
import game.TurnResult;
import words.Dictionary;
import words.Letter;
import words.Phrase;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * An implementation of the UserInteractions interface that's specific to the command line terminal.
 * The game will be played by printing the game state and some ascii art to the command line (System.out),
 * and reading input data from what the user types in the terminal (System.in)
 */
public class CommandLineUserInteractions implements UserInteractions {
    private static final String HIDDEN_CHAR = "-"; // display this char instead of a letter that hasn't been guessed yet

    private final Scanner scanner; // scans user input from the command line
    private final Dictionary dictionary; // the dictionary to validate any given words are real words

    // load the dictionary when we first create the UI
    public CommandLineUserInteractions() {
        scanner = new Scanner(System.in);
        dictionary = new Dictionary(this);
    }

    /**
     * Get the phrase to guess from the user
     */
    @Override
    public Phrase getPhraseToGuess() {
        Phrase phrase = parseObjectFromUserInput("enter a word or phrase for the other player to guess:",
                                                 this::getPhraseToGuessFromInput);
        clearScreen(); // push the phrase the user entered off the screen so the guessing player doesn't see it
        return phrase;
    }

    // validates that the given input phrase is not empty, consists of valid words, and no invalid characters
    // if these conditions are not met, then returns null instead of the Phrase
    private Phrase getPhraseToGuessFromInput(String input) {
        for (String word : input.split(" ")) {
            if (!dictionary.isValidWord(word)) {
                System.out.println(String.format("phrase contains invalid word '%s'", word));
                return null;
            }
        }

        return phraseFromString(input);
    }

    // validates that the given input phrase is not empty and contains no invalid characters
    // if these conditions are not met, then returns null instead of the Phrase
    private Phrase phraseFromString(String phrase) {
        List<Letter> letters = new ArrayList<>();
        for (char c : phrase.toCharArray()) {
            try {
                letters.add(Letter.fromChar(c));
            }
            catch (IllegalArgumentException e) {
                System.out.println(String.format("'%s' not allowed in phrase, only letters and spaces", c));
                return null;
            }
        }

        if (letters.stream().allMatch(Objects::isNull)) {
            System.out.println("phrase must contain at least one letter");
            return null;
        }

        // all validations passed, create a Phrase from these letters
        return new Phrase(letters);
    }

    /**
     * Prints all relevant game state to the screen to inform user about next guess, or about the end of the game
     */
    @Override
    public void displayGameState(Phrase phraseToGuess,
                                 Set<Letter> correctlyGuessedLetters,
                                 Set<Letter> incorrectlyGuessedLetters,
                                 Set<Phrase> incorrectlyGuessedPhrases,
                                 GameStatus gameStatus) {
        // print ascii hangman
        System.out.println(AsciiHangman.getAsciiHangman(incorrectlyGuessedLetters.size() + incorrectlyGuessedPhrases.size()));

        // print the phrase we're trying to guess, with place holders for letters that aren't guessed yet
        System.out.println(phraseToGuess.asStringWithHiding(correctlyGuessedLetters, gameStatus, HIDDEN_CHAR));

        // remind the user of which letters they've guessed incorrectly
        System.out.println("wrong letters: " + incorrectlyGuessedLetters.stream()
                                                                        .sorted()
                                                                        .map(Letter::name)
                                                                        .collect(Collectors.joining(" ")));

        // remind the user of which phrases they've guessed incorrectly
        System.out.println("wrong phrases: " + incorrectlyGuessedPhrases.stream()
                                                                        .map(Phrase::asRawString)
                                                                        .sorted()
                                                                        .map(phrase -> String.format("'%s'", phrase))
                                                                        .collect(Collectors.joining(", ")));
        // if the game is over, let the user know why
        if (gameStatus.isGameOver()) {
            System.out.println("GAME OVER!!\n\n" + gameStatus.getDescription().toUpperCase());
        }
    }

    /**
     * Prompt the user to make a guess at either a single letter or a whole phrase
     */
    @Override
    public TurnResult getTurnResult(Set<Letter> availableToGuessLetters) {
        return parseObjectFromUserInput("guess a letter, or a try to guess the whole phrase:",
                                 str -> {
                                     if (str.length() == 1) { // if they entered a single character, they guessed a letter
                                         return getGuessedLetter(str, availableToGuessLetters);
                                     }

                                     // otherwise they must have guessed a whole phrase, use it if it parses into a Phrase
                                     Phrase phrase = phraseFromString(str);
                                     return phrase == null ? null : TurnResult.guessedPhrase(phrase);
                                 });
    }

    // create a TurnResult for guessing a Letter, from the given guessed String and available letters
    private TurnResult getGuessedLetter(String str, Set<Letter> availableToGuessLetters) {
        try {
            // if this String doesn't parse to a Letter, exception will be caught below
            Letter guessedLetter = Letter.fromString(str);

            // if this letter isn't contained in the available Set, it must have already been guessed
            if (!availableToGuessLetters.contains(guessedLetter)) {
                System.out.println(String.format("'%s' has already been guessed", guessedLetter));
                return null;
            }

            // we've got a valid Letter to guess, build the TurnResult from it
            return TurnResult.guessedLetter(guessedLetter);
        }
        catch (IllegalArgumentException e) {
            System.out.println(String.format("'%s' is not a valid letter", str));
            return null;
        }
    }

    /** The number of wrong guesses until the hangman is fully built and the game is over */
    @Override
    public int getMaxWrongGuesses() {
        return AsciiHangman.MAX_WRONG;
    }

    /** Get a response from the user for whether they want to play again */
    @Override
    public boolean wantToPlayAgain() {
        return getBooleanFromUser("Do you want to play another game?");
    }

    // tells the user to answer the given prompt with (case insensitive) y or n, and turn that into a boolean
    private boolean getBooleanFromUser(String prompt) {
        return parseObjectFromUserInput(prompt + " (y/n)",
                                        (str) -> {
                                            switch (str.toLowerCase()) {
                                                case "y":
                                                    return true;
                                                case "n":
                                                    return false;
                                                default:
                                                    System.out.println("reply with either a 'y' or 'n'");
                                                    return null;
                                            }
                                        });
    }

    // prompt the user for some input, and then use the given parsing function to turn the user response text into
    // some object. If the parsing fails, it should return null and print some error message, and we'll try repeatedly
    // until we get some response that successfully parses
    private <T> T parseObjectFromUserInput(String prompt, Function<String, T> parser) {
        T result = null;
        while (result == null) {
            System.out.println(prompt);
            result = parser.apply(scanner.nextLine());
        }
        return result;
    }

    // just print a bunch of blank lines so the terminal is cleared
    private void clearScreen() {
        IntStream.range(0, 100).forEach(i -> System.out.println());
    }

    /** let the user know the dictionary failed to load */
    @Override
    public void displayDictionaryFailedToLoadMessage(IOException e) {
        System.out.println("dictionary failed to load, will not validate words:\n" + e);
    }
}
