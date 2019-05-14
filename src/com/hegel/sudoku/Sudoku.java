package com.hegel.sudoku;

public class Sudoku {
	
	
	static Sobj[] allSobjs = new Sobj[81];
	
	static Sobj[][] rows = new Sobj[9][9];
	static Sobj[][] columns = new Sobj[9][9];
	static Sobj[][] grids = new Sobj[9][9];
	

	public static void main(String[] args) {
		allSobjs = Utils.initAllSobjs(Problem.Issue2);
		rows = Utils.getRowSobjs(allSobjs);
		columns = Utils.getColumnSobjs(allSobjs);
		grids = Utils.getGridSobjs(allSobjs);
		
		
//		Utils.printSudoku(rows);
		
	}

}
