package com.hegel.sudoku;

import java.util.ArrayList;

public class Sobj {
    int x;// 行[0-8]
    int y;// 列[0-8]
    int grid;// 子格[0-8]
    boolean isOnly;
    ArrayList<Character> values = new ArrayList<Character>();

    public Sobj(int xIndex, int yIndex, char c) {
        x = xIndex;
        y = yIndex;
        grid = getGrid(x, y);
        if (c == '0') {
            values.addAll(Utils.NUM9);
        } else {
            values.add(Character.valueOf(c));
        }
        checkOnlyValue();
    }

    public String toString() {
        return "x = " + x + ", y = " + y + ", grid = " + grid + ", isOnly = " + isOnly + ", values = "
                + values.toString();
    }

    public String toSimpleString() {
        return "(" + x + "," + y + "," + grid + "," + isOnly + ")-->" + values.toString();
    }

    public void checkOnlyValue() {
        if (values.size() == 1) {
            isOnly = true;
        } else {
            isOnly = false;
        }
    }

    public int getGrid(int x, int y) {
        int id = 0;
        if (x < 3) {// x = 0,1,2
            if (y < 3) {// y = 0,1,2
                id = 0;
            } else if (y > 5) {// y = 6,7,8
                id = 2;
            } else {// y = 3,4,5
                id = 1;
            }
        } else if (x > 5) {// x = 6,7,8
            if (y < 3) {// y = 0,1,2
                id = 6;
            } else if (y > 5) {// y = 6,7,8
                id = 8;
            } else {// y = 3,4,5
                id = 7;
            }
        } else {// x = 3,4,5
            if (y < 3) {// y = 0,1,2
                id = 3;
            } else if (y > 5) {// y = 6,7,8
                id = 5;
            } else {// y = 3,4,5
                id = 4;
            }
        }

        return id;
    }
}
