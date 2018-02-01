package com.example.android.sudoklogic;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
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


    int[][] A = new int[][]{
            {7, 0, 8, 0, 3, 0, 2, 0, 0},
            {6, 0, 0, 0, 0, 5, 3, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0},
            {9, 0, 0, 0, 0, 0, 0, 5, 0},
            {0, 4, 1, 0, 0, 0, 7, 3, 0},
            {0, 5, 0, 0, 0, 0, 0, 0, 9},
            {0, 0, 0, 8, 0, 0, 0, 0, 0},
            {0, 0, 5, 7, 0, 0, 0, 0, 3},
            {0, 0, 7, 0, 4, 0, 8, 0, 1}};

    public void startProgram(View view) {
        SudokuGrid sudokuGrid = new SudokuGrid(A);
        boolean puzzleSolved = false;

        Log.d(LOG_TAG, "Before processing start");
        sudokuGrid.printSudoku();
        sudokuGrid.printPossibleValues();

        /*do {
            while (sudokuGrid.performFirstLogic()) {
            } //perform first logic until no more values are finalized
        } while (sudokuGrid.performSecondLogic());*/
        puzzleSolved=sudokuGrid.solveGridWithFirstAndSecondLogic();

        if (sudokuGrid.getNoSolutionAvailableForThis())
            Log.d(LOG_TAG, "No solution available for this grid. Wrong puzzle...");

        Log.d(LOG_TAG, "After first and second logic");
        sudokuGrid.printSudoku();
        sudokuGrid.printPossibleValues();

        //If the sudoku hasn't been solved yet, we will start guessing

        if ( !puzzleSolved && sudokuGrid.getNoOfValuesFinalized() < 81) {
            //SudokuGrid sudokuGridTemp=sudokuGrid;
            Log.d(LOG_TAG, "Guessing one number..");

            for (int i = 0; i < 9; i++) {
                for (int j = 0; j < 9; j++) {
                    //Set<Integer> possibleValues= sudokuGrid.getPossibleValues(i,j);
                    Integer[] possibleValues=sudokuGrid.getPossibleValues(i,j).toArray(new Integer[0]);
                    //for (Integer integer : possibleValues) {  // this iterator is not working somehow. fix this tomorrow.
                    for (int k = 0; k <possibleValues.length ; k++) {
                        SudokuGrid sudokuGridTemp = new SudokuGrid(sudokuGrid.getMatrix());
                        puzzleSolved = sudokuGridTemp.makeOneGuess(i, j, possibleValues[k]);
                        if (puzzleSolved ){
                            Log.d(LOG_TAG, "After guessing at i=" + i + " j=" + j + "Guessed value =" + possibleValues[k] );
                            sudokuGridTemp.printSudoku();
                            sudokuGridTemp.printPossibleValues();
                            break;  //exit loop
                        }
                    }
                    if (puzzleSolved) break;
                }
                if (puzzleSolved) break;

            }

        }

        //If the sudoku hasn't been solved yet, we will start guessing two variables
        if (!puzzleSolved && sudokuGrid.getNoOfValuesFinalized() < 81) {
            //SudokuGrid sudokuGridTemp=sudokuGrid;
            Log.d(LOG_TAG, "Guessing two numbers..");
            //boolean puzzleSolved = false;
            for (int i = 0; i < 9; i++) {
                for (int j = 0; j < 9; j++) {
                    //Set<Integer> possibleValues= sudokuGrid.getPossibleValues(i,j);
                    Integer[] possibleValues=sudokuGrid.getPossibleValues(i,j).toArray(new Integer[0]);
                    //for (Integer integer : possibleValues) {  // this iterator is not working somehow. fix this tomorrow.
                    for (int k = 0; k <possibleValues.length ; k++) {
                        for (int l = 0; l <9 ; l++) {
                            for (int m = 0; m <9 ; m++) {
                                Integer[] possibleValues2=sudokuGrid.getPossibleValues(l,m).toArray(new Integer[0]);
                                SudokuGrid sudokuGridTemp2 = new SudokuGrid(sudokuGrid.getMatrix());
                                for (int n = 0; n < possibleValues2.length; n++) {
                                    puzzleSolved = sudokuGridTemp2.makeTwoGuesses(i, j, possibleValues[k],l,m,possibleValues2[n]);
                                    //Log.d(LOG_TAG, "After making two guesses at i=" + i + " j=" + j + "Guessed value =" + possibleValues[k] + " l=" + l+ " m=" + m + "possibleValue2[n]="+ possibleValues2[n]  );
                                    if (puzzleSolved ){
                                        Log.d(LOG_TAG, "After making two guesses at i=" + i + " j=" + j + "Guessed value =" + possibleValues[k] + " l=" + l+ " m=" + m + "possibleValue2[n]="+ possibleValues2[n]  );
                                        sudokuGridTemp2.printSudoku();
                                        sudokuGridTemp2.printPossibleValues();
                                        break;  //exit loop
                                    }
                                }
                            if (puzzleSolved) break;
                            }
                        if (puzzleSolved) break;
                        }
                    if (puzzleSolved) break;
                    }
                if (puzzleSolved) break;
                }
            if (puzzleSolved) break;
            }

        }
        Log.d(LOG_TAG,"Program Completed....");
    }

    //Log.d(LOG_TAG,"Program Completed....");

}
//Log.e("I shouldn't be here");
//System.out.println("Test");

