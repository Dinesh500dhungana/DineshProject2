package com.example.android.sudoklogic;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import static android.R.attr.x;
import static android.R.attr.y;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    private final static String LOG_TAG = MainActivity.class.getSimpleName();

    /*private void printSudoku(int Sudoku[][]) {
        String str = "";
        for (int x = 0; x < 9; x++) {
            for (int y = 0; y < 9; y++) {
                //System.out.print(Sudoku[x][y] + "|");
                if (Sudoku[x][y] == 0) {
                    str = str + " " + "|";
                } else {
                    str = str + Sudoku[x][y] + "|";
                }
            }
            str = str + '\n';
        }
        Log.d(LOG_TAG, str);
    }*/

    /*private void printPossibleValues(SudokuUnit input[][]) {
        String str = "";
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (input[i][j].getValueFinalized()) {
                    str = str + input[i][j].getFinalValue() + '\t';
                } else {
                    for (Integer integer : input[i][j].getPossibleValues()) {
                        str = str + integer;

                    }
                    if (input[i][j].getPossibleValues().size() == 2) {
                        str = str + '\t';
                    }
                }
                str = str + '\t' + "|";
            }
            str = str + '\n';
        }
        Log.d(LOG_TAG, str);
    }*/

    //private Final String TAG="My Application";
    int[][] A = new int[][]{
            {0, 0, 0, 0, 0, 5, 0, 0, 4},
            {0, 3, 0, 1, 7, 0, 2, 5, 0},
            {0, 5, 8, 6, 0, 0, 0, 0, 1},
            {0, 0, 2, 0, 8, 0, 0, 4, 0},
            {5, 0, 0, 0, 9, 0, 0, 0, 8},
            {0, 9, 0, 0, 5, 0, 3, 0, 0},
            {3, 0, 0, 0, 0, 7, 1, 9, 0},
            {0, 2, 5, 0, 1, 9, 0, 6, 0},
            {4, 0, 0, 5, 0, 0, 0, 0, 0}};

    public void startProgram(View view) {
        SudokuGrid sudokuGrid = new SudokuGrid(A);

        Log.d(LOG_TAG, "Before processing start");
        sudokuGrid.printSudoku();
        sudokuGrid.printPossibleValues();

        do {
            while (sudokuGrid.performFirstLogic()) {
            } //perform first logic until no more values are finalized
        } while (sudokuGrid.performSecondLogic());

        Log.d(LOG_TAG, "After first and second logic");
        sudokuGrid.printSudoku();
        sudokuGrid.printPossibleValues();

        Log.d(LOG_TAG, "Program Complete....");

    }
    //Log.e("I shouldn't be here");
    //System.out.println("Test");

}