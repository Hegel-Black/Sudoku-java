package com.hegel.sudoku;

import java.util.ArrayList;

public class Sobj {
	int x;
	int y;
	int grid;
	boolean isOnly;
	boolean isOK;
	ArrayList<Character> values = new ArrayList<Character>();

	public Sobj(int xIndex, int yIndex, Character c) {
		x = xIndex;
		y = yIndex;
		grid = getGrid(x, y);
		values.add(c);
		updateBool();
	}

	public String outXY() {
		return "x = " + x + ", y = " + y + ", grid = " + grid + ", isOnly = " + isOnly;
	}
	
	public String outValues() {
		return "values = " + values.toString();
	}
	
	public String toString() {
		return "x = " + x + ", y = " + y + ", grid = " + grid + ", isOnly = "
				+ isOnly + ", values = " + values.toString();
	}
	
	public void updateBool() {
		if (values.isEmpty()) {
			System.out.println("(" + x + ", " + y + ") is error!");
			isOK = false;
			isOnly = false;
		} else {
			isOK = true;
			if (values.size() == 1) {
				if (values.contains('0')) {
					values.clear();
					values.addAll(Utils.NUM9);
					isOnly = false;
				} else {
					isOnly = true;
				}
			} else {
				isOnly = false;
			}
		}
	}

	public int getGrid(int x, int y) {
		int id = 0;
		if (x < 3) {
			if (y < 3) {
				id = 0;
			} else if (y > 5) {
				id = 2;
			} else {
				id = 1;
			}
		} else if (x > 5) {
			if (y < 3) {
				id = 6;
			} else if (y > 5) {
				id = 8;
			} else {
				id = 7;
			}
		} else {
			if (y < 3) {
				id = 3;
			} else if (y > 5) {
				id = 5;
			} else {
				id = 4;
			}
		}

		return id;
	}
}
