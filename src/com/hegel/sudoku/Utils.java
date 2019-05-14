package com.hegel.sudoku;

import java.util.ArrayList;

public class Utils {

	/*
	 * NUM9为包含数字1~9的HashSet
	 */
	@SuppressWarnings("serial")
	static final ArrayList<Character> NUM9 = new ArrayList<Character>() {
		{
			for (char i = '1'; i <= '9'; i++) {
				add(i);
			}
		}
	};
	
	static int shouldUpdate = 0;

	static Sobj[] initAllSobjs(String[] issue) {
		Sobj[] allSobjs = new Sobj[81];
		for (int i = 0; i < issue.length; i++) {
			char[] chars = issue[i].toCharArray();
			for (int j = 0; j < chars.length; j++) {
				allSobjs[i * 9 + j] = new Sobj(i, j, chars[j]);
			}
		}

		return updateAllSobjs(allSobjs);
	}

	static Sobj[] updateAllSobjs(Sobj[] allSobjs) {
		System.out.println("updateAllSobjs");
		Sobj[][] rows = getRowSobjs(allSobjs);
		Sobj[][] columns = getColumnSobjs(allSobjs);
		Sobj[][] grids = getGridSobjs(allSobjs);
		for (Sobj sobj : allSobjs) {
			sobj = Utils.updateSobj(sobj, rows[sobj.x], columns[sobj.y],
					grids[sobj.grid]);
		}
		printSudoku(getRowSobjs(allSobjs));
		while (shouldUpdate-- > 0) {
			allSobjs = updateAllSobjs(allSobjs);
			
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

	static Sobj updateSobj(Sobj sobj, Sobj[] row, Sobj[] column, Sobj[] grid) {
		if (!sobj.isOnly) {
			for (Sobj robj : row) {
				if (robj.isOnly) {
					sobj.values.removeAll(robj.values);
				}
			}
			for (Sobj cobj : column) {
				if (cobj.isOnly) {
					sobj.values.removeAll(cobj.values);
				}
			}
			for (Sobj gobj : grid) {
				if (gobj.isOnly) {
					sobj.values.removeAll(gobj.values);
				}
			}

			sobj.updateBool();
			if (sobj.isOnly) {
				System.out.println("(" + sobj.x + ", " + sobj.y + ") get only!");
				shouldUpdate++;
			}
		}

		return sobj;
	}

	static void printSudoku(Sobj[][] rows) {
		for (Sobj[] row : rows) {
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
