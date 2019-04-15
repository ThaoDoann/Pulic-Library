/**
 * Student Name: Doan Ngoc Phuong Thao
 * Student ID: 991466176
 */
package data;

import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

/**.
 * 
 * @author Christine
 *
 */
public class BookLibrary {
	static Scanner input = new Scanner (System.in);
	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		selectTable();
	}   
	
	/**
	 * give options for user to choose
	 * choose a table or quit by a friendly message
	 */
	public static void selectTable () {
		boolean validInput = false;
		do {
			try {
				System.out.println("\nSELECT A TABLE\n1. Books\n2. Branch\n3. Library\n4. Quit");
				System.out.print("Please enter 1, 2 or 3 to select a table or 4 to quit: ");	
				int tableSelection = input.nextInt();

				switch (tableSelection) {
				  case 1: selectBook(); break;
				  case 2:  selectBranch () ; break;
				  case 3:  selectLibrary () ; break;
				  case 4: System.out.println("Thank you. Have a good day!"); 
				  			System.exit(0);
				  			break;
				  default: throw new Exception();
				}
				validInput = true;
			}catch (Exception e) {
				System.out.println("Invalid input. Enter a valid number to select an option\n");
				input.nextLine();
			}
		} while (!validInput );
	}
	
	/**
	 * this method will execute each book method up to user option number
	 */
	public static void selectBook () {
		System.out.println("\n-----------BOOK TABLE-------------");
		Books book = new Books();
		try {
			int menuSelected = book.selectFromBookMenu();
		
			switch (menuSelected) {
				case 1: book.addData() ; break;
				case 2: book.updateData();  break;
				case 3: book.deleteData();   break;
				case 4: book.searchData();  break;
				case 5: book.booksNotInBeaches(); break;
				case 6: book.listBookInformation();  break;
				case 7: selectTable();
			}			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * this method will execute each branch method up to user option number
	 */
	public static void selectBranch ( ) {
		System.out.println("\n-----------BRANCH TABLE-------------");
		Branch branch = new Branch();
		int menuSelected = branch.selectFromBranchMenu();
	
		try {
			switch (menuSelected) {
				case 1: branch.addData(); break;
				case 2: branch.updateData(); break;
				case 3: branch.deleteData(); break;
				case 4: branch.searchData(); break;
				case 5: branch.techBooksInEachBranch(); break;
				case 6: branch.listBranchInformation();break;
				case 7: selectTable();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 * this method will execute each library method up to user option number
	 */
	public static void selectLibrary ( ) {
		System.out.println("\n-----------LIBRARY TABLE-------------");
		Library library = new Library();
		int menuSelected = library.selectFromLibraryMenu();
		try {
			switch (menuSelected) {
				case 1: library.addData(); break;
				case 2: library.updateData(); break;
				case 3: library.deleteData(); break;
				case 4: library.calcFineAmount(); break;
				case 5: library.calcTotalFineAmount(); break;
				case 6: library.listLibraryInformation(); break;
				case 7: selectTable();
			}		

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
