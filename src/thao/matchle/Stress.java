package thao.matchle;

/**
 * Stress test for the Game class.
 * This test simulates a large number of operations to ensure the game can handle high load.
 */
public class Stress {
    /**
     * Main method to run the stress test.
     * @param args command line arguments
     */
    public static void main(String[] args) {
        int n = 1000000; // Number of iterations
        long startTime = System.currentTimeMillis();
        for (int i = 0; i < n; i++) {
            Game game = Game.from("stressList.txt", 20, n);
            game.makeGuess("lkjhgfdsamnbvcxzqwer");
        }
        long endTime = System.currentTimeMillis();
        System.out.println("Stress test completed in " + (endTime - startTime) + " ms");
    }
    
}