package com.hegel.sudoku;

import java.util.ArrayList;

public class Sudoku {

    private String[] condition;
    private String[] result;
    private Sobj[] allSobjs;
    private Sobj[][] rowSobjs;
    private Sobj[][] columnSobjs;
    private Sobj[][] gridSobjs;

    public Sudoku() {
    }

    public void inputCondition(String[] _condition) {
        condition = _condition;
    }

    public String[] getResult() {
        return result;
    }

    public void calculate() {
        if (!isConditionFormatRight()) {
            System.out.println("条件格式不对，停止计算");
            return;
        } else {
            System.out.println("条件格式正确，开始计算");
        }

        // Step1. 初始化所有九宫格对象
        allSobjs = Utils.initAllSobjs(condition);

        // Step2. 按行，列，子格这个三种分配方式划分所有九宫格对象
        rowSobjs = Utils.getRowSobjs(allSobjs);
        columnSobjs = Utils.getColumnSobjs(allSobjs);
        gridSobjs = Utils.getGridSobjs(allSobjs);
        Utils.printSudoku(rowSobjs);
        countUnknownSobj();

        // Step3. 循环压缩未知格对象的取值范围
        loopCompress();
        Utils.printSudoku(rowSobjs);
        System.out.println("Step3. isCalculateOver: " + isCalculateOver());

        // Step4. 循环查找未知格对象的可能值范围中是否存在唯一值
        loopFindUnique();
        Utils.printSudoku(rowSobjs);
        System.out.println("Step4. isCalculateOver: " + isCalculateOver());

        // Step5. 到了这一步，简单的数独题目已经被解决，如果还未解决则使用数对法
        if (isCalculateOver()) {
        	updateResult(); 
        } else {
        	findPairForGrid();
        }

    }

    private void findPairForGrid() {
		// TODO Auto-generated method stub
    	Sobj p1 = null, p2 = null;
    	for (Sobj[] grids : gridSobjs) {
    		for (Sobj sobj : grids) {
    			if (sobj.values.size() == 2) {
    				
    				if (p1 == null) {
    					p1  = sobj;
					} else {
						p2 = sobj;
						break;
					}
				}
    		}
    		if (p1 != null && p2 != null) {
    			if(isValuesSame(p1, p2)) {
    				// 使用数对排除法
    				if(compressSobjForGridPair(p1)) {
    					loopCompress();
    					loopFindUnique();
    					Utils.printSudoku(rowSobjs);
    				}
    			}
			}
    		
    		p1 = null;
    		p2 = null;
    	}
	}
    
    private boolean compressSobjForGridPair(Sobj obj) {
    	boolean bl = false;
    	for (Sobj sobj : gridSobjs[obj.grid]) {
    		if (sobj.values.size() > 2) {
				sobj.values.removeAll(obj.values);
				sobj.checkValues();
				if (sobj.isOnly) {
					bl = true;
				}
			}
    	}
    	return bl;
    }
    
    private boolean isValuesSame(Sobj p1, Sobj p2) {
		boolean bl = false;
		ArrayList<Character> v1 = new ArrayList<>(p1.values);
		ArrayList<Character> v2 = new ArrayList<>(p2.values);
		if (v1.size() == v2.size()) {
			v1.removeAll(v2);
			if (v1.isEmpty()) {
				bl = true;
				System.out.println("p1 = "+p1.toString());
        		System.out.println("p2 = "+p2.toString());
			}
		}
		return bl;
	}
    
	private void updateResult() {
    	result = new String[9];
        int i = 0;
        for (Sobj[] rows : rowSobjs) {
            String str = "";
            for (Sobj obj : rows) {
                str += obj.values.get(0).toString();
            }
            result[i] = str;
            i++;
        }
    }

    private void guess(int pos, int id) {
        System.out.println("guess pos = " + pos);
        Sobj obj = allSobjs[pos];
        Character temp = obj.values.get(id);
        obj.values.clear();
        obj.values.add(temp);

        loopCompress();
        Utils.printSudoku(rowSobjs);
        loopFindUnique();
        Utils.printSudoku(rowSobjs);
    }

    /**
     * 按顺序依次对81个格中的未知格进行可能值范围的缩减操作， 若缩减后可能值只有一个，则将其转换为已知格，并跳转到第一个格重新开始；
     */
    private void loopCompress() {
        int count = 0;
        int i = 0;
        while (i < allSobjs.length) {
            Sobj obj = allSobjs[i];
            boolean bl = compressUnknownSobj(obj);
            if (bl) {
                i = 0;
                count++;
            } else {
                i++;
            }
        }
//        System.out.println("loopCompress count = " + count);
    }

    /**
     * 按顺序依次对81个格中的未知格进行唯一值查找，若找到唯一值，则调用{@link #loopCompress()}
     * 对全体未知格进行可能值范围的缩减操作，然后并跳转到第一个格重新开始；
     */
    private void loopFindUnique() {
        int count = 0;
        int i = 0;
        while (i < allSobjs.length) {
            Sobj obj = allSobjs[i];
            boolean bl = findUniqueForUnknownSobj(obj);
            if (bl) {
                // System.out.println("(" + obj.x + ", " + obj.y + ") findUniqueForUnknownSobj:"
                // + obj.values.toString());
                loopCompress();
                i = 0;
                count++;
            } else {
                i++;
            }
        }
//        System.out.println("loopFindUnique count = " + count);
    }

    /**
     * 在未知格的可能值中查找是否存在一个值，在它所属的行九格或列九格或子九格是唯一的， 若找到则将其转化为已知格
     * 
     * @param sobj
     *            未知格实例
     * @return 如果存在唯一值，则返回true，否则返回false
     */
    private boolean findUniqueForUnknownSobj(Sobj sobj) {
        boolean bl = false;
        if (!sobj.isOnly) {
            // 在行九格中查找
            ArrayList<Character> rowTemp = new ArrayList<>(sobj.values);
            for (Sobj robj : rowSobjs[sobj.x]) {
                if (!robj.isOnly && sobj.y != robj.y) {
                    rowTemp.removeAll(robj.values);
                }
            }
            if (rowTemp.size() == 1) {
                sobj.values = rowTemp;
                sobj.checkValues();
                return true;
            }
            // 在列九格中查找
            ArrayList<Character> columnTemp = new ArrayList<>(sobj.values);
            for (Sobj cobj : columnSobjs[sobj.y]) {
                if (!cobj.isOnly && sobj.x != cobj.x) {
                    columnTemp.removeAll(cobj.values);
                }
            }
            if (columnTemp.size() == 1) {
                sobj.values = columnTemp;
                sobj.checkValues();
                return true;
            }
            // 在子九格中查找
            ArrayList<Character> gridTemp = new ArrayList<>(sobj.values);
            for (Sobj gobj : gridSobjs[sobj.grid]) {
                if (!gobj.isOnly && (sobj.x != gobj.x || sobj.y != gobj.y)) {
                    gridTemp.removeAll(gobj.values);
                }
            }
            if (gridTemp.size() == 1) {
                sobj.values = gridTemp;
                sobj.checkValues();
                return true;
            }
        }

        return bl;
    }

    /**
     * 缩减未知格的可能值范围，即在[1-9]的范围内移除行九格、列九格、子九格中已知格的值， 若可能值只有一个，则将其转化为已知格
     * 
     * @param sobj
     *            未知格实例
     * @return 如果缩减后的可能值只有一个，则返回true，否则返回false
     */
    private boolean compressUnknownSobj(Sobj sobj) {
        boolean bl = false;
        if (sobj.values.size() > 1) {
            for (Sobj robj : rowSobjs[sobj.x]) {
                if (robj.isOnly) {
                    sobj.values.remove(robj.values.get(0));
                }
            }
            for (Sobj cobj : columnSobjs[sobj.y]) {
                if (cobj.isOnly) {
                    sobj.values.remove(cobj.values.get(0));
                }
            }
            for (Sobj gobj : gridSobjs[sobj.grid]) {
                if (gobj.isOnly) {
                    sobj.values.remove(gobj.values.get(0));
                }
            }
            sobj.checkValues();
            if (sobj.isOnly) {
                bl = true;
            }
        }

        return bl;
    }

    /**
     * 判断题目格式是否正确， 正确的条件格式是一个字符串数组，内含9个字符串，每个字符串长度也为9，字符为[0-9]
     * 
     * @return
     */
    private boolean isConditionFormatRight() {
        boolean bl = false;

        if (condition != null) {
            if (condition.length == 9) {
                int checkChar = 0;
                loop: for (int i = 0; i < 9; i++) {
                    if (condition[i].length() == 9) {
                        char[] row = condition[i].toCharArray();
                        for (int j = 0; j < 9; j++) {
                            if (row[j] >= '0' && row[j] <= '9') {
                                checkChar++;
                            } else {
                                bl = false;
                                System.out.println("条件的第" + (i + 1) + "行字符串的第" + (j + 1) + "个字符不是数字");
                                break loop;
                            }
                        }
                    } else {
                        bl = false;
                        System.out.println("条件的第" + (i + 1) + "行字符串的长度不为9");
                        break;
                    }
                }
                if (checkChar == 9 * 9) {
                    bl = true;
                }
            } else {
                bl = false;
                System.out.println("条件字符串数组长度不为9");
            }
        } else {
            bl = false;
            System.out.println("条件为空");
        }

        return bl;
    }
    
    private int countUnknownSobj() {
        int unknown = 0;
        for (Sobj obj : allSobjs) {
            if (obj.values.size() != 1) {
                unknown++;
            }
        }
        System.out.println("未知格数量：" + unknown);
        return unknown;
    }

    /**
     * 判断题目是否计算完毕
     * 
     * @return 若所有的格都是已知格则返回true，否则返回false
     */
    private boolean isCalculateOver() {
        return countUnknownSobj() == allSobjs.length;
    }

}
