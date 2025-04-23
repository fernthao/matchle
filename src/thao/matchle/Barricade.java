package thao.matchle;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Barricade class provides methods to validate and process guesses in a word game.
 * Instead of throwing exceptions, it uses Optional to indicate the validity of a guess.
 */
final class Barricade  {
    /**
     * Validates the guess against the corpus and expected length.
     * @param guess the guess string
     * @param corpus the corpus to check against
     * @param expectedLength the expected length of the guess
     * @return an Optional containing the NGram if valid, or empty if invalid
     */
    static Optional<NGram> validatedGuess(String guess, Corpus corpus, int expectedLength) {
        Objects.requireNonNull(guess, "Guess cannot be null");
        Objects.requireNonNull(corpus, "Corpus cannot be null");
        
        if (guess.isBlank()) {
            System.out.println("Guess cannot be blank. Try again");
            return Optional.empty();
        }
        if (guess.length() != expectedLength) {
            System.out.println("Guess must have " + expectedLength + " letters");
            return Optional.empty();
        }
        if (!corpus.contains(NGram.from(guess))) {
            System.out.println("Guess not in corpus");
            return Optional.empty();
        }

        return Optional.of(NGram.from(guess));
    }

    /**
     * Processes a valid guess and updates the history with the result.
     * @param history the list of previous guess results
     * @param key the key NGram to match against
     * @param guessInpuString the guess string
     * @param corpus the corpus to check against
     * @param expectedLength the expected length of the guess
     * @return true if the guess is valid and processed, false otherwise
     */
    static boolean makeValidGuess(List<GuessResult> history, NGram key, String guessInpuString, Corpus corpus, int expectedLength) {
        Optional<NGram> validatedGuessOpt = Barricade.validatedGuess(guessInpuString, corpus, expectedLength);
        if (validatedGuessOpt.isEmpty()) {
            return false; // Return false if the guess is invalid
        }

        NGram validatedGuess = validatedGuessOpt.get();
        NGramMatcher matcher = NGramMatcher.of(key, validatedGuess);
        GuessResult result = matcher.match();
        history.add(result);
        return true; // Return true if the guess is valid and processed
    }
}
