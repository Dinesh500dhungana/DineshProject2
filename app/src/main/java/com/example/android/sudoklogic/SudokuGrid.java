package com.example.android.sudoklogic;

/**
 * Created by dines on 2018-01-27.
 */

import android.util.Log;

import java.util.*;

import static android.R.id.input;

public class SudokuGrid {
    private int noOfValuesFinalized=0;
    private boolean noSolutionAvailableForThis=false;
    private SudokuUnit[][] sudokuUnit = new SudokuUnit[9][9];
    private final static String LOG_TAG = MainActivity.class.getSimpleName();

    public SudokuGrid(){}
    public SudokuGrid(int X[][]){

        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                sudokuUnit[i][j] = new SudokuUnit(i, j);
                if (X[i][j] != 0) {
                    sudokuUnit[i][j].setFinalValue(X[i][j]);
                    this.noOfValuesFinalized++;
                }

            }
        }
    }

    public boolean performFirstLogic(){
        //This will return true if additional values were finalized within this method
        int previousNoOfValuesFinalized = this.noOfValuesFinalized;
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
                    int possibleValueSize=sudokuUnit[i][j].removeTheseFromPossibleValues(existingValues);
                    if (possibleValueSize==0) noSolutionAvailableForThis=true;
                    if (possibleValueSize==1) noOfValuesFinalized++;

                }
            }
        }
        if (this.noOfValuesFinalized > previousNoOfValuesFinalized) return true;
        else return false;
    }

    public boolean performSecondLogic(){
        //returns true if any new value is finalized within this method
        int previousNoOfValuesFinalized = this.noOfValuesFinalized;
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
                                        //sudokuUnit[i][l].removeTheseFromPossibleValues(existingValues);
                                        int possibleValueSize=sudokuUnit[i][l].removeTheseFromPossibleValues(existingValues);
                                        if (possibleValueSize==0) noSolutionAvailableForThis=true;
                                        if (possibleValueSize==1) noOfValuesFinalized++;
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
                                        //sudokuUnit[i][l].removeTheseFromPossibleValues(existingValues);
                                        int possibleValueSize=sudokuUnit[l][j].removeTheseFromPossibleValues(existingValues);
                                        if (possibleValueSize==0) noSolutionAvailableForThis=true;
                                        if (possibleValueSize==1) noOfValuesFinalized++;
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
                                                int possibleValueSize=sudokuUnit[3 * thisBoxX + m][3 * thisBoxY + n].removeTheseFromPossibleValues(existingValues);
                                                //sudokuUnit[i][l].removeTheseFromPossibleValues(existingValues);
                                                //int possibleValueSize=sudokuUnit[i][j].removeTheseFromPossibleValues(existingValues);
                                                if (possibleValueSize==0) noSolutionAvailableForThis=true;
                                                if (possibleValueSize==1) noOfValuesFinalized++;
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                //if (sudokuUnit[i][j].getValueFinalized()) A[i][j] = sudokuUnit[i][j].getFinalValue();
            }
        }
        if (this.noOfValuesFinalized > previousNoOfValuesFinalized) return true;
        else return false;
    }

    public void printSudoku(){
        String str = "";
        for (int x = 0; x < 9; x++) {
            for (int y = 0; y < 9; y++) {
                //System.out.print(Sudoku[x][y] + "|");
                if (!this.sudokuUnit[x][y].getValueFinalized()) {
                    str = str + " " + "|";
                } else {
                    str = str + this.sudokuUnit[x][y].getFinalValue() + "|";
                }
            }
            str = str + '\n';
        }
        Log.d(LOG_TAG, str);
    }

    public void printPossibleValues(){
        String str = "";
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (this.sudokuUnit[i][j].getValueFinalized()) {
                    str = str + this.sudokuUnit[i][j].getFinalValue() + "        ";
                } else {
                    int count=9-this.sudokuUnit[i][j].getNoOfPossibleValues();
                    for (Integer integer : this.sudokuUnit[i][j].getPossibleValues()) {
                        str = str + integer;
                    }
                    for (int k = 0; k < count; k++) {
                        str=str+' ';
                    }
                    /*if (this.sudokuUnit[i][j].getPossibleValues().size() == 2) {
                        str = str + '\t';
                    }*/
                }
                str = str + "|";
            }
            str = str + '\n';
        }
        Log.d(LOG_TAG, str);
    }


}
