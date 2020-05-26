package ui;

import java.util.Map;

/**
 * This class is responsible for creating the ascii hangman image and modifying it based on how many wrong guesses
 * the guessing player has made so far
 */
public class AsciiHangman {

    // ascii hangman with conditional characters (i.e. one that only shows up after a certain number of wrong guesses)
    // replaced with the number of wrong guesses required to actually show that character
    private static final String ASCII_HANGMAN =
            " +-----+\n" +
            " |     |\n" +
            " 1     |\n" +
            "324    |\n" +
            "5 6    |\n" +
            "       |\n" +
            " ============\n";

    // a map from number of wrong guesses, to the character that will be displayed once we've reached that many wrong guesses
    // for example, if we have at least one wrong guess, we'll replace the 1 in the picture above with O to make the head
    private static Map<Integer, String> NUM_WRONG_TO_CHAR = Map.of(
        1, "O",
        2, "|",
        3, "\\",
        4, "/",
        5, "/",
        6, "\\"
    );

    // after this many wrong guesses the picture is complete, and the game is over
    public static final int MAX_WRONG = 6;

    /** get a version of the ascii hangman with the amount of the person showing depending on the given number of wrong guesses */
    public static String getAsciiHangman(int numWrongGuesses) {
        String asciiHangman = ASCII_HANGMAN;
        for (int i = 1; i <= MAX_WRONG; i++) {
            // replace the number in the original ascii hangman with either a blank space or the char that builds the person
            String toReplace = numWrongGuesses < i ? " " : NUM_WRONG_TO_CHAR.get(i);
            asciiHangman = asciiHangman.replace(String.valueOf(i), toReplace);
        }
        return asciiHangman;
    }
}
