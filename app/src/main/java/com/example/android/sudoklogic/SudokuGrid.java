package com.example.android.sudoklogic;

/**
 * Created by dines on 2018-01-27.
 */

import java.util.*;

public class SudokuGrid {


    public SudokuGrid(){}
    public SudokuGrid(int X[][]){
        SudokuUnit[][] sudokuUnit = new SudokuUnit[9][9];
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                sudokuUnit[i][j] = new SudokuUnit(i, j);
                if (X[i][j] != 0) sudokuUnit[i][j].setFinalValue(X[i][j]);
            }
        }
    }

    public boolean performFirstLogic(){
        //This will return true if additional values were finalized within this method
        int previousNoOfValuesFinalized = SudokuUnit.getNoOfValuesFinalized();
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
    }


}
