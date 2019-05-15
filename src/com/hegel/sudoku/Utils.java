package com.hegel.sudoku;

import java.util.ArrayList;

public class Utils {

    /*
     * NUM9为包含数字1~9的ArrayList
     */
    static ArrayList<Character> NUM9 = new ArrayList<Character>();

    static {
        for (char i = '1'; i <= '9'; i++) {
            NUM9.add(Character.valueOf(i));
        }
    }

    static Sobj[] initAllSobjs(String[] issue) {
        Sobj[] allSobjs = new Sobj[81];
        for (int i = 0; i < 9; i++) {
            char[] chars = issue[i].toCharArray();
            for (int j = 0; j < 9; j++) {
                allSobjs[i * 9 + j] = new Sobj(i, j, chars[j]);
            }
        }
        return allSobjs;
    }

    static Sobj[][] getRowSobjs(Sobj[] allSobjs) {
        Sobj[][] rows = new Sobj[9][9];
        for (Sobj sobj : allSobjs) {
            rows[sobj.x][sobj.y] = sobj;
        }
        return rows;
    }

    static Sobj[][] getColumnSobjs(Sobj[] allSobjs) {
        Sobj[][] columns = new Sobj[9][9];
        for (Sobj sobj : allSobjs) {
            columns[sobj.y][sobj.x] = sobj;
        }
        return columns;
    }

    static Sobj[][] getGridSobjs(Sobj[] allSobjs) {
        Sobj[][] grids = new Sobj[9][9];
        for (Sobj sobj : allSobjs) {
            grids[sobj.grid][(sobj.x % 3) * 3 + sobj.y % 3] = sobj;
        }
        return grids;
    }

    static void printSudoku(Sobj[][] rowSobjs) {
        System.out.printf("------------------------------\n");
        for (Sobj[] row : rowSobjs) {
            for (Sobj sobj : row) {
                String str = "";
                for (Character character : sobj.values) {
                    str += character.toString();
                }
                System.out.printf("%-9s ", str);
            }
            System.out.println();
        }
        System.out.printf("------------------------------\n");
    }

}
