package game;

/**
 * an enum that represents the various states the game can be in, including the various ways the game can end
 */
public enum GameStatus {
    STARTING(false, "the game has just started"),
    GUESSING(false, "the guessing player is in the process of taking guesses"),
    TOO_MANY_WRONG_GUESSES(true, "too many wrong guesses! guesser loses"),
    GUESSED_ALL_LETTERS_CORRECTLY(true, "all letters have been guessed correctly! guesser wins"),
    GUESSED_PHRASE_CORRECTLY(true, "the phrase was guessed correctly! guesser wins");

    private boolean isOver; // is the game over when it's in this state?
    private String description;

    GameStatus(boolean isOver, String description) {
        this.isOver = isOver;
        this.description = description;
    }

    public boolean isGameOver() {
        return this.isOver;
    }

    public String getDescription() {
        return description;
    }
}
