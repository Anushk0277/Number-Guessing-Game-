import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

public class NumberGuessingGameGUI extends JFrame {

    private Random random = new Random();

    private int numberToGuess;
    private int maxAttempts;
    private int attempts;
    private int maxNumber;
    private int bestScore = Integer.MAX_VALUE;

    private JComboBox<String> difficultyCombo;
    private JTextField guessField;
    private JButton guessButton;
    private JButton playAgainButton;
    private JLabel feedbackLabel;
    private JLabel attemptsLabel;
    private JLabel bestScoreLabel;

    public NumberGuessingGameGUI() {
        setTitle("Number Guessing Game");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();

        // Difficulty selection
        JLabel difficultyLabel = new JLabel("Select Difficulty:");
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        add(difficultyLabel, gbc);

        difficultyCombo = new JComboBox<>(new String[] { "Easy (1-50, 10 attempts)", "Medium (1-100, 7 attempts)", "Hard (1-500, 10 attempts)" });
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        add(difficultyCombo, gbc);

        // Attempts label
        attemptsLabel = new JLabel("Attempts left: ");
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.NONE;
        gbc.insets = new Insets(10, 0, 0, 0);
        add(attemptsLabel, gbc);

        // Best score label
        bestScoreLabel = new JLabel("Best score: N/A");
        gbc.gridy = 2;
        add(bestScoreLabel, gbc);

        // Guess input
        JLabel guessLabel = new JLabel("Enter your guess:");
        gbc.gridy = 3;
        gbc.gridwidth = 1;
        gbc.gridx = 0;
        add(guessLabel, gbc);

        guessField = new JTextField(10);
        gbc.gridx = 1;
        add(guessField, gbc);

        // Guess button
        guessButton = new JButton("Guess");
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        add(guessButton, gbc);

        // Feedback label
        feedbackLabel = new JLabel(" ");
        feedbackLabel.setForeground(Color.BLUE);
        gbc.gridy = 5;
        add(feedbackLabel, gbc);

        // Play again button
        playAgainButton = new JButton("Play Again");
        playAgainButton.setEnabled(false);
        gbc.gridy = 6;
        add(playAgainButton, gbc);

        // Initialize game on start
        setupGame();

        // Listeners
        difficultyCombo.addActionListener(e -> setupGame());

        guessButton.addActionListener(e -> makeGuess());

        guessField.addActionListener(e -> makeGuess()); // Enter key triggers guess

        playAgainButton.addActionListener(e -> setupGame());
    }

    private void setupGame() {
        int diffIndex = difficultyCombo.getSelectedIndex();

        switch (diffIndex) {
            case 0:
                maxNumber = 50;
                maxAttempts = 10;
                break;
            case 1:
                maxNumber = 100;
                maxAttempts = 7;
                break;
            case 2:
                maxNumber = 500;
                maxAttempts = 10;
                break;
            default:
                maxNumber = 100;
                maxAttempts = 7;
        }

        numberToGuess = random.nextInt(maxNumber) + 1;
        attempts = 0;

        attemptsLabel.setText("Attempts left: " + (maxAttempts - attempts));
        feedbackLabel.setText("I'm thinking of a number between 1 and " + maxNumber + ".");
        guessField.setText("");
        guessField.setEnabled(true);
        guessButton.setEnabled(true);
        playAgainButton.setEnabled(false);

        bestScoreLabel.setText("Best score: " + (bestScore == Integer.MAX_VALUE ? "N/A" : bestScore));
    }

    private void makeGuess() {
        String guessText = guessField.getText().trim();
        int guess;

        if (guessText.isEmpty()) {
            feedbackLabel.setText("Please enter a guess.");
            return;
        }

        try {
            guess = Integer.parseInt(guessText);
        } catch (NumberFormatException e) {
            feedbackLabel.setText("Invalid input! Enter a number.");
            return;
        }

        attempts++;

        if (guess == numberToGuess) {
            feedbackLabel.setText("Congratulations! You guessed it in " + attempts + " attempts.");
            updateBestScore();
            endGame();
        } else {
            if (attempts >= maxAttempts) {
                feedbackLabel.setText("Game over! The number was " + numberToGuess + ".");
                endGame();
                return;
            }

            String hint = guess < numberToGuess ? "Too low." : "Too high.";

            if (Math.abs(guess - numberToGuess) <= 5) {
                hint += " You're very close!";
            }

            feedbackLabel.setText(hint);
            attemptsLabel.setText("Attempts left: " + (maxAttempts - attempts));
            guessField.setText("");
        }
    }

    private void updateBestScore() {
        if (attempts < bestScore) {
            bestScore = attempts;
            bestScoreLabel.setText("Best score: " + bestScore);
        }
    }

    private void endGame() {
        guessField.setEnabled(false);
        guessButton.setEnabled(false);
        playAgainButton.setEnabled(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            NumberGuessingGameGUI gui = new NumberGuessingGameGUI();
            gui.setVisible(true);
        });
    }
}
