package thao.matchle;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import thao.matchle.GuessResult.MatchType;

final class Barricade  {
    static Optional<NGram> validatedGuess(String guess, Corpus corpus, int expectedLength) {
        Objects.requireNonNull(guess, "Guess cannot be null");

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

    static NGram validBestWorstCaseGuess(List<GuessResult> history, Corpus corpus) {
        return corpus.stream().findFirst().orElseThrow(() -> new IllegalStateException("No n-grams found in corpus"));
    }
    
    static NGram validBestAverageCaseGuess(List<GuessResult> history, Corpus corpus) {
        return corpus.stream().findFirst().orElseThrow(() -> new IllegalStateException("No n-grams found in corpus"));
    }

}
