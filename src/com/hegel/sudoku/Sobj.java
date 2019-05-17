package com.hegel.sudoku;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

public class Sobj {
    int x;// 所属行号[0-8]
    int y;// 所属列号[0-8]
    int grid;// 所属子格号[0-8]
    ArrayList<Character> values;// 可能值的集合
    boolean isOnly;
    
    ArrayList<Character> guessValues;// 假设值的集合
    boolean isSupposed;// 此格是否处于猜测状态
    Character supposedValue;// 未知格的假设值
    int supposedValueID;// 未知格的假设值在其可能值集合中的位置

    public Sobj(int xIndex, int yIndex, char c) {
        x = xIndex;
        y = yIndex;
        grid = getGrid(x, y);
        values = new ArrayList<Character>();
        if (c == '0') {
            values.addAll(Utils.NUM9);
        } else {
            values.add(Character.valueOf(c));
        }
        checkValues();
        cleanSupposed();
    }

    public String toString() {
        return "(" + x + "," + y + "," + grid + ")-->" + values.toString();
    }
    
    public void checkValues() {
        if (values.size() == 1) {
            isOnly = true;
        } else {
            isOnly = false;
        }
    }
    
    /**
     * 清除猜测状态
     */
    public void cleanSupposed() {
        isSupposed = false;
        supposedValue = null;
        supposedValueID = -1;
    }
    
    /**
     * 对未知格进行猜测
     * @return
     */
    public boolean guess() {
        boolean bl = false;
        if (supposedValueID < guessValues.size()-1) {
            isSupposed = true;
            supposedValueID++;
            supposedValue = values.get(supposedValueID);
            bl = true;
        }
        return bl;
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
