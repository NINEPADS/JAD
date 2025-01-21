import java.util.Random;
import java.util.Scanner;
import java.lang.Exception;
import java.util.ArrayList;
import java.util.Collections;
public class Sukudo {
	public static void main(String argv[]) {
		Scanner scanner = new Scanner(System.in);
		System.out.println("Please enter the operation of Sukudo (Solve/Generate)");
		String ans = scanner.nextLine();
		if(ans.equals("Generate")) {
			sukudu suk = new sukudu();
			suk.generateSukudo();
		}
		else if(ans.equals("Solve")) {
			sukudu suk = new sukudu();
			suk.solveSukudo();
		}
		scanner.close();
	}
}

class position {
	int x;
	int y;
	position (int x, int y) {
		this.x = x;
		this.y = y;
	}
	public boolean equals(Object obj) {
		if(obj == this) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if(obj instanceof position) {
			position pos = (position) obj;
			if(this.x == pos.x && this.y == pos.y)
				return true;
		}
		return false;
	}
}

class sukudu {
	private int prompt_num;
	private int mask_matrix[][];
	private int value_matrix[][];
	private int puzzle_matrix[][];
	private int fixed_matrix[][];
	private ArrayList<position> pos_list[];
	sukudu() {
		mask_matrix = new int[9][9];
		value_matrix = new int [9][9];
		puzzle_matrix = new int [9][9];
		fixed_matrix = new int [9][9];
		pos_list= new ArrayList[9];
		for(int i=0; i<9; i++) {
			pos_list[i] = new ArrayList<position>();
		}
	}
	void generateSukudo() {
		try {
			Scanner scanner = new Scanner(System.in);
			System.out.println("Please enter the prompt number: ");
			prompt_num = scanner.nextInt();
			System.out.println("Please enter the mask matrix: ");
			for (int i=0; i<9; i++) {
				for (int j=0; j<9; j++) {
					int mask = scanner.nextInt();
					position pos = new position(i, j);
					pos_list[mask].add(pos);
					mask_matrix[i][j] = mask;
				}
			}
			scanner.nextLine();
			System.out.println("Do you need the sukudo must have solution? (Yes/No)");
			String ans = scanner.nextLine();
			scanner.close();
			//generate
			System.out.println("Generating...");
			if(ans.equals("Yes")) {
				if(solve()) {
					//number swap
					swapNum();
					//random clean
					randomFill();
					System.out.println("Successfully generated!");
				}	
				else
					throw new Exception("This mask matrix has no solutions!");
			}
			else if(ans.equals("No")) {
				randomFillNumber();
			}
			
		} catch(Exception e) {
			System.out.println(e.getMessage());
		}
		//test
//		System.out.println("Answer: ");
//		printValueMatrix();
		System.out.println("Puzzle: ");
		printPuzzleMatrix();	
	}
	void solveSukudo() {
		Scanner scanner = new Scanner(System.in);
		System.out.println("Please enter the Sukudo: ");
		for(int i=0; i<9; i++) {
			for(int j=0; j<9; j++) {
				value_matrix[i][j] = scanner.nextInt();
				fixed_matrix[i][j] = value_matrix[i][j];
			}
		}
		System.out.println("Please enter the mask matrix: ");
		for (int i=0; i<9; i++) {
			for (int j=0; j<9; j++) {
				int mask = scanner.nextInt();
				position pos = new position(i, j);
				pos_list[mask].add(pos);
				mask_matrix[i][j] = mask;
			}
		}
		scanner.close();
		solve();
		System.out.println("Answer: ");
		printValueMatrix();
	}
	boolean checkNum(int i, int j, int num) {		
		for(int m=0; m<9; m++) {
			if(value_matrix[m][j] == num) 
				return false;
		}
		for(int n=0; n<9; n++) {
			if(value_matrix[i][n] == num)
				return false;
		}
		
		int mask = mask_matrix[i][j];
		for(position p : pos_list[mask]) {
			if(value_matrix[p.x][p.y] == num)
				return false;
		}
		return true;
	}
	boolean solve() {
		boolean back = false;
		for(int i=0; i<9; i++) {
			for(int j=0; j<9; j++) {
				//System.out.println(i + ", " + j);
				if(back && fixed_matrix[i][j] != 0) {
					if(j>0) {
						j = j - 2;
						back = true;
					} 
					else if(i>0) {
						back = true;
						i--;
						j = 7;
					} 
					else {
						return false;
					}
					continue;
				}
				if(fixed_matrix[i][j] != 0) {
					value_matrix[i][j] = fixed_matrix[i][j];
					continue;
				}
				boolean changed = false;
				int k = back? value_matrix[i][j] + 1: 1;
				for(; k<10; k++) {
					if(checkNum(i, j, k)) {
						value_matrix[i][j] = k;
						changed = true;
						back = false;
						break;
					}
				}
				if(!changed) {
					value_matrix[i][j] = 0;
					if(j>0) {
						j = j - 2;
						back = true;
					} 
					else if(i>0) {
						back = true;
						i--;
						j = 7;
					} 
					else {
						return false;
					}
				}
				//printValueMatrix();
			}
		}
		return true;
	}
	void randomFill() {
		for(int i=0; i<prompt_num; i++) {
			 int row, col;
			 long seed = System.currentTimeMillis();
		     Random random = new Random(seed);
		     do {
                row = random.nextInt(9);
                col = random.nextInt(9);
            } while (puzzle_matrix[row][col] != 0);
            puzzle_matrix[row][col] = value_matrix[row][col];
		}
	}
	void swapNum() {
		long seed = System.currentTimeMillis();
	    Random random = new Random(seed);
		ArrayList<Integer> numbers = new ArrayList<>();
		for (int i = 1; i <= 9; i++) {
            numbers.add(i);
        }
		Collections.shuffle(numbers, random);
		int arr[] = new int[10];
		for(int i=0; i<9; i++) {
			arr[numbers.get(i)] = numbers.get(8-i);
		}
		for(int i=0; i<9; i++) {
			for(int j=0; j<9; j++) {
				value_matrix[i][j] = arr[value_matrix[i][j]];
			}
		}
	}
	void printValueMatrix() {
		for(int i=0; i<9; i++) {
			for(int j=0; j<9; j++) {
				System.out.print(value_matrix[i][j] + " ");
			}
			System.out.println();
		}
	}
	void printPuzzleMatrix() {
		for(int i=0; i<9; i++) {
			for(int j=0; j<9; j++) {
				System.out.print(puzzle_matrix[i][j] + " ");
			}
			System.out.println();
		}
	}
	void randomFillNumber() {
		for(int i=0; i<prompt_num; i++) {
			 int row, col, value;
			 long seed = System.currentTimeMillis();
		     Random random = new Random(seed);
		     boolean flag = false;
		     do {
		    	 flag = false;
		    	 row = random.nextInt(9);
		    	 col = random.nextInt(9);
		    	 value = random.nextInt(9) + 1;
		    	 if(puzzle_matrix[row][col] != 0)
		    		 flag = true;
		    	 for(int m=0; m<9; m++) {
		    		 if(puzzle_matrix[m][col] == value)
		    		 {
		    			 flag = true;
		    			 break;
		    		 }
		    	 }
		    	 for(int n=0; n<9; n++) {
		    		 if(puzzle_matrix[row][n] == value)
		    		 {
		    			 flag = true;
		    			 break;
		    		 }
		    	 }
		    	 int mask = mask_matrix[row][col];
		    	 for(position p : pos_list[mask]) {
		    		 if(puzzle_matrix[p.x][p.y] == value) {
		    			 flag = true;
		    			 break;
		    		 }
		    	 }
		    	 if(!flag)
		    		 puzzle_matrix[row][col] = value;	 
		     } while(flag);
//		     printPuzzleMatrix();
//		     System.out.println();
		}
	}
	
}