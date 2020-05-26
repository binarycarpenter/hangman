package words;

import ui.UserInteractions;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * This class handles reading in a list of all valid words from a file and keeping them in a Set so we can
 * quickly look up whether a String is a real word
 */
public class Dictionary {
    private static final String DICTIONARY_FILE = "words_en.txt";

    private Set<String> validWords;

    /** create the dictionary by loading words from the file */
    public Dictionary(UserInteractions ui) {
        try {
            validWords = Files.lines(Paths.get(DICTIONARY_FILE)).collect(Collectors.toSet());
        }
        catch (IOException e) {
            // don't want to assume errors are printed to command line, so let UI decide how to display the error
            ui.displayDictionaryFailedToLoadMessage(e);
            validWords = Collections.emptySet();
        }
    }

    /** return whether the given word is valid. If the dictionary failed to load all words are assumed to be valid */
    public boolean isValidWord(String word) {
        return validWords.isEmpty() || validWords.contains(word);
    }
}
