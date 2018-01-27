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

    private void printSudoku(int Sudoku[][]) {
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
    }

    private void printPossibleValues(SudokuUnit input[][]) {
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
    }

    //private Final String TAG="My Application";
    int[][] A = new int[][]{
            {3, 0, 8, 1, 6, 0, 0, 0, 0},
            {0, 5, 2, 0, 0, 4, 0, 1, 0},
            {0, 0, 0, 5, 0, 0, 6, 0, 0},
            {0, 1, 0, 0, 0, 2, 0, 0, 9},
            {5, 3, 0, 0, 0, 0, 0, 2, 0},
            {8, 0, 0, 9, 0, 0, 0, 3, 0},
            {0, 0, 5, 0, 0, 7, 0, 0, 0},
            {0, 4, 0, 3, 0, 0, 8, 7, 0},
            {0, 0, 0, 0, 1, 5, 2, 0, 0}};
    SudokuUnit[][] sudokuUnit = new SudokuUnit[9][9];     // This is the object which represents the SudokuUnit

    public void startProgram(View view) {
        int previousNoOfValuesFinalized = 0;  //used to store how many values were finalized in the previous iteration.

        //int tempVar;

        Log.d(LOG_TAG, "Before processing start");
        this.printSudoku(A);
        do {
            for (int i = 0; i < 9; i++) {
                for (int j = 0; j < 9; j++) {
                    sudokuUnit[i][j] = new SudokuUnit(i, j);
                    if (A[i][j] != 0) sudokuUnit[i][j].setFinalValue(A[i][j]);
                }

            }
            do {
                do {
                    previousNoOfValuesFinalized = SudokuUnit.getNoOfValuesFinalized();
                    for (int i = 0; i < 9; i++) {
                        for (int j = 0; j < 9; j++) {
                            if (!sudokuUnit[i][j].getValueFinalized()) {
                                Set<Integer> existingValues = new HashSet<Integer>();
                                //check values in other cells vertical to this cell
                                for (int k = 0; k < 9; k++) {
                                    if (sudokuUnit[k][j].getValueFinalized()) {
                                        existingValues.add(sudokuUnit[k][j].getFinalValue());
                                    }
                                }
                                //check values in other cells horizontal to this cell
                                for (int k = 0; k < 9; k++) {
                                    if (sudokuUnit[i][k].getValueFinalized()) {
                                        existingValues.add(sudokuUnit[i][k].getFinalValue());
                                    }
                                }
                                //check values in other cells of the same box
                                int thisBoxX = i / 3;
                                int thisBoxY = j / 3;
                                for (int k = 0; k < 3; k++) {
                                    for (int l = 0; l < 3; l++) {
                                        if (sudokuUnit[thisBoxX * 3 + k][thisBoxY * 3 + l].getValueFinalized()) {
                                            existingValues.add(sudokuUnit[thisBoxX * 3 + k][thisBoxY * 3 + l].getFinalValue());
                                        }
                                    }
                                }
                                sudokuUnit[i][j].removeTheseFromPossibleValues(existingValues);
                                if (sudokuUnit[i][j].getValueFinalized()) {
                                    A[i][j] = sudokuUnit[i][j].getFinalValue();
                                }
                            }
                        }
                    }
                    //Log.d("TAG","A = ");
                } while (previousNoOfValuesFinalized < SudokuUnit.getNoOfValuesFinalized());
                Log.d(LOG_TAG, "First Logic Completed...");
                this.printSudoku(A);
                this.printPossibleValues(sudokuUnit);
                // Start Naked pair now
                previousNoOfValuesFinalized = SudokuUnit.getNoOfValuesFinalized();
                for (int i = 0; i < 9; i++) {
                    for (int j = 0; j < 9; j++) {
                        if (!sudokuUnit[i][j].getValueFinalized() && sudokuUnit[i][j].getNoOfPossibleValues() == 2) {
                            //Check entire row
                            for (int k = 0; k < 9; k++) {
                                if (k != j && !sudokuUnit[i][k].getValueFinalized() && sudokuUnit[i][k].getNoOfPossibleValues() == 2) {
                                    if (sudokuUnit[i][j].getPossibleValues().equals(sudokuUnit[i][k].getPossibleValues())) {
                                        //if naked pair is found, eliminate these two values from all other possible values in this row
                                        Set<Integer> existingValues = new HashSet<Integer>();
                                        existingValues = sudokuUnit[i][j].getPossibleValues();
                                        for (int l = 0; l < 9; l++) {
                                            if (l != j && l != k && !sudokuUnit[i][l].getValueFinalized()) {      //cannot eliminate values from the cells where naked pair were found
                                                sudokuUnit[i][l].removeTheseFromPossibleValues(existingValues);
                                            }
                                        }
                                    }
                                }
                            }
                            //Check entire column
                            for (int k = 0; k < 9; k++) {
                                if (k != i && !sudokuUnit[k][j].getValueFinalized() && sudokuUnit[k][j].getNoOfPossibleValues() == 2) {
                                    if (sudokuUnit[i][j].getPossibleValues().equals(sudokuUnit[k][j].getPossibleValues())) {
                                        //if naked pair is found, eliminate these two values from all other possible values in this column
                                        Set<Integer> existingValues = new HashSet<Integer>();
                                        existingValues = sudokuUnit[i][j].getPossibleValues();
                                        for (int l = 0; l < 9; l++) {
                                            if (l != i && l != k && !sudokuUnit[l][j].getValueFinalized()) {      //cannot eliminate values from the cells were naked pair were found
                                                sudokuUnit[l][j].removeTheseFromPossibleValues(existingValues);
                                            }
                                        }
                                    }
                                }
                            }
                            //Check entire Box
                            int thisBoxX = i / 3;
                            int thisBoxY = j / 3;
                            for (int k = 0; k < 3; k++) {
                                for (int l = 0; l < 3; l++) {
                                    if ((3 * thisBoxX + k != i || 3 * thisBoxY + l != j) && !sudokuUnit[3 * thisBoxX + k][3 * thisBoxY + l].getValueFinalized() && sudokuUnit[3 * thisBoxX + k][3 * thisBoxY + l].getNoOfPossibleValues() == 2) {
                                        if (sudokuUnit[i][j].getPossibleValues().equals(sudokuUnit[3 * thisBoxX + k][3 * thisBoxY + l].getPossibleValues())) {
                                            //if naked pair is found, eliminate these two values from all other possible values in this Box
                                            Set<Integer> existingValues = new HashSet<Integer>();
                                            existingValues = sudokuUnit[i][j].getPossibleValues();
                                            for (int m = 0; m < 3; m++) {
                                                for (int n = 0; n < 3; n++) {
                                                    if ((m != k || n != l) && (3 * thisBoxX + m != i || 3 * thisBoxY + n != j) && !sudokuUnit[3 * thisBoxX + m][3 * thisBoxY + n].getValueFinalized()) { //cannot eliminate values from the cells were naked pair were found
                                                        sudokuUnit[3 * thisBoxX + m][3 * thisBoxY + n].removeTheseFromPossibleValues(existingValues);
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        if (sudokuUnit[i][j].getValueFinalized()) {
                            A[i][j] = sudokuUnit[i][j].getFinalValue();
                        }
                    }
                }
            } while (previousNoOfValuesFinalized < SudokuUnit.getNoOfValuesFinalized());
            Log.d(LOG_TAG, "Second Logic Completed...");
            this.printSudoku(A);
            this.printPossibleValues(sudokuUnit);
            //Start guessing at this point
            int noOfGuesses = 1;
            int[][] Atemp = A;
            int count = 0;
            for (int i = 0; i < 9; i++) {
                for (int j = 0; j < 9; j++) {
                    if (count < noOfGuesses) {
                        if (sudokuUnit[i][j].getPossibleValues().size() == 2) {
                           /*int max=2;
                           int min=1;
                           Random randomNum = new Random();
                           int indexToRemove;
                            for (:
                                 ) {
                                
                            } */
                            sudokuUnit[i][j].removeTheseFromPossibleValues(sudokuUnit[i][j].getPossibleValues());
                        }
                    }
                }
            }

        } while (SudokuUnit.getNoOfValuesFinalized() < 81);

    }
    //Log.e("I shouldn't be here");
    //System.out.println("Test");

}