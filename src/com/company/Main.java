package com.company;
import java.util.Random;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int possible, length;
        String str = "";

        try {
            System.out.println("Input the length of the secret code:");
            str = scanner.next();
            length = Integer.parseInt(str);
            System.out.println("Input the number of possible symbols in the code:");
            str = scanner.next();
            possible = Integer.parseInt(str);
        } catch (Exception e) {
            System.out.printf("Error: \"%s\" isn't a valid number.", str);
            return;
        }

        if (possible < length || length == 0) {
            System.out.printf("Error: it's not possible to generate a code with a length of %d with %d unique symbols.", length, possible);
            return;
        }

        if (possible > 36) {
            System.out.println("Error: maximum number of possible symbols in the code is 36 (0-9, a-z).");
            return;
        }

        Grader grader = new Grader(length, possible);

        System.out.println(grader.message());
        System.out.println("Okay, let's start a game!");

        int attempt = 0;

        while (!grader.isOver()) {
            grader.resetResult();
            System.out.printf("Turn %d:\n", ++attempt);
            System.out.println(grader.tryToGuess(scanner.next()));
            if (grader.isOver()) {
                System.out.println("Congratulations! You guessed the secret code.");
            }
        }
    }
}

class Grader {
    private final String pseudoRandomNumber;
    private final BullsAndCows result;
    private final int length;
    private final int possible;

    Grader(int length, int possible) {
        this.possible = possible;
        this.length = length;
        pseudoRandomNumber = generatePseudoRandomNumber();
        result = new BullsAndCows();
    }

    String message() {
        return "The secret is prepared: " + "*".repeat(length) +
                " (" +
                "0-" + (possible > 9 ? "9" : String.valueOf(possible)) +
                (possible > 9 ? ", a-" + (char) (possible - 11 + 'a') : "") +
                ").";
    }

    private String generatePseudoRandomNumber() {
        Random random = new Random();
        StringBuilder sb = new StringBuilder(" ");
        String listPossibleValues = "0123456789abcdefghijklmnopqrstuvwxyz";

        for (int i = 0; i < length; i++) {
            int n = random.nextInt(possible - 1);
            if (sb.toString().contains(String.valueOf(listPossibleValues.charAt(n)))) {
                --i;
            } else {
                sb.append(listPossibleValues.charAt(n));
            }
        }

        return sb.toString().trim();
    }

    private void search(String inputCode) {
        int length = pseudoRandomNumber.length();
        int bulls = 0;
        int cows = 0;

        for (int i = 0; i < inputCode.length(); i++) {
            for (int j = 0; j < length; j++) {
                if (inputCode.charAt(j) == pseudoRandomNumber.charAt(i)) {
                    if (j == i) {
                        result.setBulls(++bulls);
                    } else {
                        result.setCows(++cows);
                    }
                }
            }
        }
    }

    void resetResult(){
        result.setBulls(0);
        result.setCows(0);
    }

    String tryToGuess(String inputCode) {
        search(inputCode);
        return "Grade: " + result.information();
    }

    boolean isOver() {
        return result.getBulls() == length;
    }
}

class BullsAndCows {

    private int bulls;

    private int cows;

    BullsAndCows() {
        bulls = 0;
        cows = 0;
    }

    public void setBulls(int bulls) {
        this.bulls = bulls;
    }

    public void setCows(int cows) {
        this.cows = cows;
    }

    public int getBulls() {
        return bulls;
    }

    public String information() {

        if (bulls != 0 && cows != 0) {
            return bulls + (bulls == 1 ? " bull and " : " bulls and ") + cows + (cows == 1 ? " cow" : " cows");
        } else if (cows != 0){
            return cows + (cows == 1 ? " cow" : " cows");
        } else if (bulls != 0) {
            return bulls + (bulls == 1 ? " bull" : " bulls");
        } else {
            return "None.";
        }
    }
}