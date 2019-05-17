package com.hegel.sudoku;

public class Main {

    public static void main(String[] args) {

        Sudoku sudoku = new Sudoku();
        sudoku.inputCondition(Problem.Issue_1);
        sudoku.calculate();

        String[] result = sudoku.getResult();
        if (result != null) {
            System.out.println("---result start---");
            for (String row : result) {
                System.out.println(row);
            }
            System.out.println("---result end---");
        }

    }

}
