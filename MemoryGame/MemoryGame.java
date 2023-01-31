package MemoryGame;

import edu.princeton.cs.introcs.StdDraw;

import java.awt.Color;
import java.awt.Font;
import java.util.Random;

public class MemoryGame {
    /** The width of the window of this game. */
    private int width;
    /** The height of the window of this game. */
    private int height;
    /** The current round the user is on. */
    private int round;
    /** The Random object used to randomly generate Strings. */
    private Random rand;
    /** Whether or not the game is over. */
    private boolean gameOver;
    /** Whether or not it is the player's turn. Used in the last section of the
     * spec, 'Helpful UI'. */
    private boolean playerTurn;
    /** The characters we generate random Strings from. */
    private static final char[] CHARACTERS = "abcdefghijklmnopqrstuvwxyz".toCharArray();
    /** Encouraging phrases. Used in the last section of the spec, 'Helpful UI'. */
    private static final String[] ENCOURAGEMENT = {"You can do this!", "I believe in you!",
                                                   "You got this!", "You're a star!", "Go Bears!",
                                                   "Too easy for you!", "Wow, so impressive!"};

    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Please enter a seed");
            return;
        }

        long seed = Long.parseLong(args[0]);
        MemoryGame game = new MemoryGame(40, 40, seed);
        game.startGame();
    }

    public MemoryGame(int width, int height, long seed) {
        /* Sets up StdDraw so that it has a width by height grid of 16 by 16 squares as its canvas
         * Also sets up the scale so the top left is (0,0) and the bottom right is (width, height)
         */
        this.width = width;
        this.height = height;
        StdDraw.setCanvasSize(this.width * 16, this.height * 16);
        Font font = new Font("Monaco", Font.BOLD, 30);
        StdDraw.setFont(font);
        StdDraw.setXscale(0, this.width);
        StdDraw.setYscale(0, this.height);
        StdDraw.clear(Color.BLACK);
        StdDraw.enableDoubleBuffering();

        this.rand = new Random(seed);
    }

    public String generateRandomString(int n) {
        // Generate random string of letters of length n
        String res = "";
        for (int i = 0; i < n; i++) {
            int randInt = rand.nextInt(26);
            res += CHARACTERS[randInt];
        }
        return res;
    }

    public void drawFrame(String s) {
        /* Take the input string S and display it at the center of the screen,
        * with the pen settings given below. */
        StdDraw.clear(Color.BLACK);
        StdDraw.setPenColor(Color.WHITE);
        Font fontBig = new Font("Monaco", Font.BOLD, 30);
        StdDraw.setFont(fontBig);
        StdDraw.text(this.width / 2, this.height / 2, s);

        // If the game is not over, display encouragement, and let the user know if they
        // should be typing their answer or watching for the next round.
        StdDraw.line(0, height * 0.93, width, height * 0.93);
        Font fontSmall = new Font("Monaco", Font.BOLD, 20);
        StdDraw.setFont(fontSmall);
        StdDraw.textLeft(width * 0.01, height * 0.96, "Round: " + round);
        StdDraw.textRight(width * 0.99, height * 0.96, ENCOURAGEMENT[rand.nextInt(7)]);

        if (!playerTurn) {
            StdDraw.text(this.width / 2, height * 0.96, "Watch!");
        } else {
            StdDraw.text(this.width / 2, height * 0.96, "Type");
        }

        StdDraw.show();
    }

    public void flashSequence(String letters) {
        // Display each character in letters, making sure to blank the screen between letters

        for (int i = 0; i < letters.length(); i++) {
            drawFrame(letters.substring(i, i + 1));
            StdDraw.pause(1000);
            drawFrame("");
            StdDraw.pause(500);
        }

    }

    public String solicitNCharsInput(int n) {
        // Read n letters of player input
        drawFrame("");
        int count = 0;
        String res = "";
        while (count < n) {
            while (!StdDraw.hasNextKeyTyped()) {
                StdDraw.pause(0);
            }

            res = res + StdDraw.nextKeyTyped();
            drawFrame(res);
            count += 1;
        }

        return res;
    }

    public void startGame() {
        // Set any relevant variables before the game starts
        this.gameOver = false;
        round = 1;

        // Establish Engine loop
        while (!gameOver) {
            // Display start round quote
            drawFrame("Round: " + round);
            StdDraw.pause(1000);

            // Display Random String
            String randString = generateRandomString(round);
            flashSequence(randString);
            playerTurn = true;

            // User turn
            String userInput = solicitNCharsInput(round);
            playerTurn = false;

            // Determine whether passed the round or not
            if (!userInput.equals(randString)) {
                gameOver = true;
                break;
            }

            round += 1;
        }

        this.drawFrame("Game Over! You made it to round: " + this.round);
    }

}
