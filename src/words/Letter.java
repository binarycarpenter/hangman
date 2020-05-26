package words;

import java.util.Optional;

/**
 * This enum represents all the letters that are valid to guess. I could have just used a String or Character for this,
 * but then I'd have to have regex's to validate which ones are allowed, and we'd have to worry about upper case.
 * This class handles those concerns and keeps them encapsulated here so users of this class don't need to worry about it
 */
public enum Letter
{
    A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y, Z;

    /**
     * turn the given char into a Letter
     * returns null if the char was a space (since these are allowed in the game)
     * otherwise returns the Letter that matches the char (case insensitive)
     * throws IllegalArgumentException if the char doesn't match any Letter (e.g. ':')
     */
    public static Letter fromChar(char c) {
        if (c == ' ') {
            return null;
        }

        return fromString(String.valueOf(c));
    }

    /**
     * turn the given String into a Letter
     * returns the Letter that matches the String (case insensitive)
     * throws IllegalArgumentException if the String doesn't match any Letter (e.g. a special char, or multiple chars)
     */
    public static Letter fromString(String str) {
        return Letter.valueOf(str.toUpperCase());
    }

    /**
     * turn the given Letter into a String - note that null Letter is a special case that returns a space
     */
    public static String asString(Letter letter) {
        return Optional.ofNullable(letter)
                       .map(Letter::name)
                       .orElse(" ");
    }
}
