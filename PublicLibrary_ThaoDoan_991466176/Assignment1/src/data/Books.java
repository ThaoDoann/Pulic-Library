/**
 * Student Name: Doan Ngoc Phuong Thao
 * Student ID: 991466176
 */
package data;

import java.sql.*;
import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * @author Christine
 *
 */
public class Books {
	static PreparedStatement pst;
	static Connection con = null;
	static ResultSet rs = null;
	private Scanner input = new Scanner (System.in);

	/**
	 * print all book information in Book table
	 * @throws Exception if can not connect to database
	 */
	public void displayBookData () throws Exception {
			try {			
				con = ConnectionLibrary.getConnection();
				pst = con.prepareStatement("SELECT * from BOOKS ; ");
				rs = pst.executeQuery();
				
				
				System.out.println("--- BOOK INFORMATION ---");
				System.out.println("BOOKCODE     TITLE                     AUTHOR     PRICE      TYPE              SUBJECT\n");
				while (rs.next()) {
					String bookCode = rs.getString("BOOKCODE").toString();
					String title = rs.getString("TITLE");
					String author = rs.getString("AUTHOR");
					String price = rs.getString("PRICE").toString();
					String type = rs.getString("TYPE");
					String subject = rs.getString("SUBJECT");
					System.out.printf("%-12s %-25s %-10s %-10s %-17s %-10s\n", 
								bookCode, title, author, price, type, subject);
				};
			}catch (Exception e) {
				e.printStackTrace();
			}
	}

	/**
	 * display book menu to select
	 * @exception throw an exception if the input is invalid
	 */
	public int selectFromBookMenu () {
		int menuSelection =0;
		boolean validInput = false;
		do {
			System.out.println("SELECT BOOK MENU"
					       + "\n1. Add Data                       \n2. Update Data"
					       + "\n3. Delete Data                    \n4. Search"
					       + "\n5. Books are not borrowed in \"Beaches\" branch"
					       + "\n6. Display table data             \n7. Quit");
			System.out.print("Please choose an option: ");
			try {
				menuSelection = input.nextInt();
				
				//throw an exception if the input is not available in the menu
				if (menuSelection < 1 || menuSelection > 7)
					throw new Exception ();
				validInput = true;
				
			}catch (Exception e) {
				System.out.println("Invalid input. Please enter again");
				input.next();
			}
		} while (!validInput);
		return menuSelection;
	}

	/**
	 * add a new record to Book table by asking for inputs
	 * @throws Exception when any input is not valid. 
	 * Keep asking until user enters a valid input
	 */
	public void addData() throws Exception {
		boolean validInput = false;
		do {
			String insertQuery = "Insert into BOOKS (bookcode, title, author, price, type, subject) values (?,?,?,?,?,?)";
			try {
				con = ConnectionLibrary.getConnection();
				pst = con.prepareStatement(insertQuery);
	
				System.out.print("Please enter a code number: ");
					int bookCode = input.nextInt(); 
					input.nextLine();
				System.out.print("Please enter a title: ");
					String title = input.nextLine();
				System.out.print("Please enter a author: ");
					String author = input.nextLine();
					System.out.print("Please enter a price: ");
					double price = input.nextDouble();
					input.nextLine();
				System.out.print("Please enter a type: ");
					String type = input.nextLine();
				System.out.print("Please enter a subject: ");
					String subject = input.nextLine();
				
				
				pst.setInt(1, bookCode);
				pst.setString(2, title);
				pst.setString(3, author);
				pst.setDouble(4, price);
				pst.setString(5, type);
				pst.setString(6, subject);
	
				pst.executeUpdate();
				displayBookData();
	            System.out.println("-----------------Add complete----------------");
	            
	            validInput = true;
			} catch (Exception e) {
				System.out.println("Invalid input. Please enter again");
				input.next();
			} finally {
				ConnectionLibrary.closeConnection(con, pst, null);
			}
		} while (!validInput);
		BookLibrary.selectBook();
	}

	/**
	 * update record by asking for a book code
	 * we can update by my fields up to user choice
	 * display book information before and  after updating so user can see the change
	 * @throws Exception when an error occur or when the book code entered is invalid
	 */
	public void updateData() throws Exception {
		boolean validInput = false;
		do {
			String updateQuery = null;
			try {
				con = ConnectionLibrary.getConnection();
				displayBookData();
				System.out.print("\nPlease enter a book code : ");
				int bookCode = input.nextInt(); 

				System.out.println("Please select an option to update : "
						+ "\n1. Price \n2. Type\n3. Subject");	
				int option = input.nextInt();
				input.nextLine();
				
				switch (option) {
					case 1: 
							System.out.print("Please enter a price: ");
							double price = input.nextDouble();
							updateQuery = "Update BOOKS set price="+ price+" where BOOKCODE=?";
							break;
					case 2:  
							System.out.print("Please enter a type: ");
							String type = input.nextLine();
							updateQuery = "Update BOOKS set TYPE= \'"+ type+"\' where BOOKCODE=?";
							break;
					case 3: 
							System.out.print("Please enter a subject: ");
							String subject= input.nextLine();
							updateQuery = "Update BOOKS set SUBJECT= \'"+ subject+"\' where BOOKCODE=?";
							break;
					default: throw new IllegalArgumentException ("Invalid input. Please enter a valid input.");
				}
				pst = con.prepareStatement(updateQuery);
				pst.setInt(1, bookCode);
	
				int updateNum = pst.executeUpdate();
				if(updateNum == 0 )
					throw new IllegalArgumentException("This book code does not exit in database. Please enter a valid one");
				
				displayBookData();
	            System.out.println("-----------------update complete----------------");
	            
	            validInput = true;
			}catch (IllegalArgumentException ex) {
				System.out.println(ex.getMessage());
				input.next();
			}catch (Exception e) {
				System.out.println("Invalid input. Please enter again");
				input.next();
			} finally {
				ConnectionLibrary.closeConnection(con, pst, null);
			}
		}while (!validInput);
		BookLibrary.selectBook();
	}
	
	/**
	 * delete a record from the table by asking a book code
	 * display book table after deleting
	 * @throws Exception when user enters an invalid input
	 */
	public void deleteData() throws Exception {
		boolean validInput = false;
		String deleteQuery = "DELETE from BOOKS Where BOOKCODE = ?";
		do {
			try {
				con = ConnectionLibrary.getConnection();
				displayBookData();
				pst = con.prepareStatement(deleteQuery);
	
				System.out.print("Please enter a code number to delete a book: ");
				int bookCode = input.nextInt(); 
				
				pst.setInt(1, bookCode);
				
				pst.executeUpdate();
				
				displayBookData();
	            System.out.println("-----------------Delete complete----------------");
	            validInput = true;
			} catch (Exception e) {
				System.out.println("Invalid input. Please enter again");
				e.printStackTrace();
				input.next();
			} finally {
				ConnectionLibrary.closeConnection(con, pst, null);
			}
		}while(!validInput);
		BookLibrary.selectBook();
	}
	
	/**
	 * search for book information base on author name or title
	 * display all matching input 
	 * @throws Exception
	 */
	public void searchData() throws Exception {
		boolean validInput = false;
		do {
			try {
				String searchQuery = "";
		
				System.out.print("    1. Search by author name\n    2. Search by title\n");
				System.out.print("Please enter an option: ");
				int searchOption = input.nextInt();
				input.nextLine();
				
				if (searchOption == 1)
					searchQuery = "SELECT * from BOOKS where AUTHOR LIKE ?";
				else // this case we always make sure that the other option is searching by title
					searchQuery = "SELECT * from BOOKS where TITLE LIKE ?";
		
			
				con = ConnectionLibrary.getConnection();
				pst = con.prepareStatement(searchQuery);
	
				System.out.print("Please enter something to search: ");
				String search = input.nextLine();
				
				pst.setString(1, "%" + search + "%");
	
				rs = pst.executeQuery();
				System.out.println("BOOKCODE     TITLE                     AUTHOR     PRICE      TYPE              SUBJECT\n");
				while (rs.next()) {
					String bookCode = rs.getString("BOOKCODE").toString();
					String title = rs.getString("TITLE");
					String author = rs.getString("AUTHOR");
					String price = rs.getString("PRICE").toString();
					String type = rs.getString("TYPE");
					String subject = rs.getString("SUBJECT");
					System.out.printf("%-12s %-25s %-10s %-10s %-17s %-10s\n", bookCode, title, author, price, type,
							subject);
				};
				validInput = true;
			} catch (Exception e) {
				System.out.println("Invalid input. Please enter again");
				input.next();
			} finally {
				ConnectionLibrary.closeConnection(con, pst, rs);
			}
		}while(!validInput);
		BookLibrary.selectBook();
	}
	
	/**
	 * display book information which are not in Beaches branch
	 * close connection when finish executing a query
	 */
	public void booksNotInBeaches () throws Exception {
			String query = "select * from BOOKS where BOOKCODE != "
							+"(select BOOKCODE from LIBRARY where BRANCHCODE = "
							+" (select BRANCHCODE from BRANCH Where BRANCHNAME = 'beaches'))";
			try {
				con = ConnectionLibrary.getConnection();
				pst = con.prepareStatement(query);
				rs = pst.executeQuery();
				
				System.out.println("BOOKCODE     TITLE                     AUTHOR     PRICE      TYPE              SUBJECT\n");
				while (rs.next()) {
					String bookCode = rs.getString("BOOKCODE").toString();
					String title = rs.getString("TITLE");
					String author = rs.getString("AUTHOR");
					String price = rs.getString("PRICE").toString();
					String type = rs.getString("TYPE");
					String subject = rs.getString("SUBJECT");
					System.out.printf("%-12s %-25s %-10s %-10s %-17s %-10s\n", 
								bookCode, title, author, price, type, subject);
				};
			}catch (Exception e) {
				e.printStackTrace();
				input.next();
			}finally {
				ConnectionLibrary.closeConnection(con, pst, rs);
			}
			BookLibrary.selectBook();
	}
	
	public void listBookInformation () throws Exception {
		try {
			displayBookData();
		}catch (Exception e) {
			System.out.println("An error occurs");
			input.next();
		}finally {
			ConnectionLibrary.closeConnection(con, pst, rs);
		}
		BookLibrary.selectBook();
	}

}
