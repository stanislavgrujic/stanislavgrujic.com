package com.stanislavgrujic.blog.memberordering;

import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

/**
 * This class is used to represent the member ordering described in the article published on stanislavgrujic.com.
 * For simplicity, the calculator does not allow entering the arbitrary numbers, but uses the predefined values.
 * The main idea of the class is to show basic member ordering ideas.
 */
public class ComplexNumberCalculator {

    // constants
    private static final String CONTINUE = "Y";

    private static final String DIGITS = "\\d+";

    private static final String ADD = "+";
    private static final String SUBTRACT = "-";

    private static final List<String> SUPPORTED_OPERATIONS = Arrays.asList(ADD, SUBTRACT);

    // public methods
    public static void main(String... args) {
        printWelcomeText();

        try (Scanner scanner = new Scanner(System.in, "UTF-8")) {
            boolean shouldContinue;
            do {
                ComplexNumber firstNumber = readNumber(scanner);
                String operation = readOperation(scanner);
                ComplexNumber secondNumber = readNumber(scanner);

                ComplexNumber result = handle(operation, firstNumber, secondNumber);
                printResult(result);

                shouldContinue = askToContinue(scanner);
            } while (shouldContinue);
        }
    }

    // private methods
    private static ComplexNumber readNumber(Scanner scanner) {
        System.out.println("Please enter a complex number r,i (e.g., 2,1):");

        do {
            String enteredNumber = scanner.nextLine();
            String[] parts = enteredNumber.split(",");

            boolean entryValid = isEntryValid(parts);
            if (!entryValid) {
                continue;
            }

            return new ComplexNumber(parts[0], parts[1]);
        } while (true);
    }

    private static boolean isEntryValid(String[] enteredNumber) {
        if (enteredNumber.length != 2) {
            System.out.println("Please enter a valid complex number.");
            return false;
        }

        for (String part : enteredNumber) {
            if (!part.matches(DIGITS)) {
                System.out.println("Both real and imaginary part can contain only digits.");
                return false;
            }
        }

        return true;
    }

    private static String readOperation(Scanner scanner) {
        System.out.println("Please enter an operation to apply ('+' or '-'):");

        do {
            String operation = scanner.nextLine();
            if (SUPPORTED_OPERATIONS.contains(operation)) {
                return operation;
            }

            System.out.println("Please enter a supported operation ('+' or '-').");
        } while (true);
    }

    private static ComplexNumber handle(String operation, ComplexNumber firstNumber, ComplexNumber secondNumber) {
        switch (operation) {
            case ADD:
                return firstNumber.add(secondNumber);
            case SUBTRACT:
                return firstNumber.subtract(secondNumber);
            default:
                throw new IllegalArgumentException("Unsupported operation: " + operation);
        }
    }

    // helper methods with little interesting logic
    private static void printWelcomeText() {
        System.out.println("This is a simple calculator for basic complex number operations.");
        System.out.println("Currently supported operations are addition (+)  and subtraction (-).");
        System.out.println("For more information about complex numbers, please visit: ");
        System.out.println("https://en.wikipedia.org/wiki/Complex_number");
    }

    private static void printResult(ComplexNumber number) {
        System.out.println(String.format("= %s", number));
    }

    private static boolean askToContinue(Scanner scanner) {
        System.out.println("Would you like another operation? (Y/y to continue)");
        String entry = scanner.nextLine();
        return CONTINUE.equalsIgnoreCase(entry);
    }

    // inner classes
    private static class ComplexNumber {

        // fields
        private int real;
        private int imaginary;

        // constructors
        private ComplexNumber(int real, int imaginary) {
            this.real = real;
            this.imaginary = imaginary;
        }

        private ComplexNumber(String real, String imaginary) {
            this.real = Integer.valueOf(real);
            this.imaginary = Integer.valueOf(imaginary);
        }

        // public methods
        public ComplexNumber add(ComplexNumber number) {
            int real = this.real + number.real;
            int imaginary = this.imaginary + number.imaginary;
            return new ComplexNumber(real, imaginary);
        }

        public ComplexNumber subtract(ComplexNumber number) {
            int real = this.real - number.real;
            int imaginary = this.imaginary - number.imaginary;
            return new ComplexNumber(real, imaginary);
        }

        @Override
        public String toString() {
            return String.format("(%d + %di)", real, imaginary);
        }

        // generated methods
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            ComplexNumber that = (ComplexNumber) o;

            if (real != that.real) return false;
            return imaginary == that.imaginary;

        }

        @Override
        public int hashCode() {
            int result = real;
            result = 31 * result + imaginary;
            return result;
        }
    }
}
