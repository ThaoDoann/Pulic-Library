/**
 * Student Name: Doan Ngoc Phuong Thao
 * Student ID: 991466176
 */
package data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * @author Christine
 *
 */
public class Library {
	Scanner input = new Scanner (System.in);
	Connection con = null;
	PreparedStatement pst;
	ResultSet rs = null;

	/**
	 * print all book information in library table
	 * @throws Exception if can not connect to database
	 */
	public void displayLibraryData () throws Exception {
		try {
			con = ConnectionLibrary.getConnection();
			pst = con.prepareStatement("SELECT * from LIBRARY ");
			rs = pst.executeQuery();	
			
			System.out.println("--- LIBRARY INFORMATION ---");
			System.out.println("BranchCode   BookCode      BorrowDate           ReturnDate    FineAmount");
			while (rs.next()) {
				String branchCode = rs.getString("BRANCHCODE").toString();
				String bookCode = rs.getString("BOOKCODE");
				String borrowDate = rs.getString("BORROWDATE");
				String returnDate = rs.getString("RETURNDATE").toString();
				String fineAmount = rs.getString("FINEAMOUNT");
				System.out.printf("%-15s %-10s %-20s %-15s %-7s\n", 
						branchCode, bookCode, borrowDate, returnDate, fineAmount);
			};
		}catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * display library menu to select
	 * @exception throw an exception if the input is invalid
	 */
	public int selectFromLibraryMenu () {
		int menuSelection =0;
		boolean validInput = false;
		do {
			System.out.println("1. Add Data                       \n2. Update Data"
						         + "\n3. delete data 			  \n4. Calculate fine amount "
						         + "\n5. Total fine amount of each branch   \n6. Display table data   \n7. Quit");
			System.out.print("Please choose an option: ");
			try {
				menuSelection = input.nextInt();
				if (menuSelection <1 || menuSelection > 7) {
					throw new Exception ();
				}
				validInput = true;
			}catch (Exception e) {
				System.out.println("Invalid input. Please enter again");
				input.nextLine();
			}
		} while (!validInput);
		
		return menuSelection;
	}

	/**
	 * add a new record to Library table by asking for inputs
	 * @throws Exception when any input is not valid. 
	 * Keep asking until user enters a valid input
	 */
	public void addData()  throws Exception {
		boolean validInput = false;
		do { 
			String insertQuery = "Insert into LIBRARY (BRANCHCODE, BOOKCODE, BORROWDATE, "
					+ "RETURNDATE, FINEAMOUNT) values (?,?,?,?,?)";
			try {
				con = ConnectionLibrary.getConnection();
				pst = con.prepareStatement(insertQuery);
	
				System.out.print("Please enter a branch code: ");
					int branchCode = input.nextInt(); 
				System.out.print("Please enter a book code: ");
					int bookCode = input.nextInt();
					input.nextLine();
				System.out.print("Please enter a borrow date in YYYY-MM-DD format: ");
					String borrowDate = input.nextLine();
				System.out.print("Please enter a return date in YYYY-MM-DD format: ");
					String returnDate = input.nextLine();
				System.out.print("Please enter a fine amount: ");
					double fineAmount = input.nextDouble();
				
				pst.setInt(1, branchCode);
				pst.setInt(2, bookCode);
				pst.setString(3, borrowDate);
				pst.setString(4, returnDate);
				pst.setDouble(5, fineAmount);
	
				pst.executeUpdate();
				displayLibraryData();
	            System.out.println("-----------------Add complete----------------\n");
	            validInput = true;
			} catch (Exception e) {
				System.out.println("Invalid input. Please enter again");
				input.nextLine();
			} finally {
				ConnectionLibrary.closeConnection(con, pst, null);
			}
		}while(!validInput);
		BookLibrary.selectLibrary();
	}

	/**
	 * update record by asking for a book code
	 * we can update by my fields up to user choice
	 * display book information before and  after updating so user can see the change
	 * @throws Exception when an error occur
	 */
	public void updateData() throws Exception {
		boolean validInput = false;
		do {
			String updateQuery = "Update LIBRARY set FINEAMOUNT=? where BOOKCODE=? and BRANCHCODE =?";
			try {
				con = ConnectionLibrary.getConnection();
				displayLibraryData();
				pst = con.prepareStatement(updateQuery);
	
				
				
				System.out.print("Please enter a  book code: ");
					int bookCode = input.nextInt(); 
				System.out.print("Please enter branch code: ");
					int branchCode = input.nextInt();
				System.out.print("Please enter a fine amount to update: ");
					double fineAmount = input.nextDouble();
				
				pst.setDouble(1, fineAmount);
				pst.setInt(2, bookCode);
				pst.setInt(3,  branchCode);
	
				int updateNum = pst.executeUpdate();
				if(updateNum == 0 )
					throw new IllegalArgumentException("This branch code or book code does not exit in database. Please enter a valid one");
				
				displayLibraryData();
				
	            System.out.println("-----------------update complete----------------\n ");
	            validInput = true;
			} catch (IllegalArgumentException ex) {
				System.out.println(ex.getMessage());
			}catch (Exception e) {
				System.out.println("Invalid input. Please enter again");
				e.printStackTrace();
				input.nextLine();
			} finally {
				ConnectionLibrary.closeConnection(con, pst, null);
			}
		}while(!validInput);
		BookLibrary.selectLibrary();
	}
	
	/**
	 * delete a record from the table by asking a book code and branch Code
	 * display book table after deleting
	 * @throws Exception when user enters an invalid input
	 */
	public void deleteData() throws Exception {
		boolean validInput = false;
		String deleteQuery = "DELETE from LIBRARY Where BOOKCODE = ? and BRANCHCODE = ?";
		do {
			try {
				con = ConnectionLibrary.getConnection();
				displayLibraryData();
				pst = con.prepareStatement(deleteQuery);
	 
				System.out.print("Please enter a branch code: ");
				int branchCode = input.nextInt(); 
				System.out.print("Please enter a book code: ");
				int bookCode = input.nextInt();
				
				pst.setInt(1, bookCode);
				pst.setInt(2,  branchCode);
				
				int deleteNum = pst.executeUpdate();
				if(deleteNum == 0 )
					throw new IllegalArgumentException("This branch code or book code does not exit in database. Please enter a valid one");
				
				displayLibraryData();
	            System.out.println("-----------------Delete complete----------------");
	            validInput = true;
			} catch (IllegalArgumentException ex) {
				System.out.println(ex.getMessage());
			} catch (Exception e) {
				System.out.println("Invalid input. Please enter again");
				e.printStackTrace();
				input.next();
			} finally {
				ConnectionLibrary.closeConnection(con, pst, null);
			}
		}while(!validInput);
		BookLibrary.selectLibrary();
	}

	/**
	 * calculate the fine amount based on 2 different dates
	 * keep asking until inputs are valid
	 * @throws Exception
	 */
	public void calcFineAmount() throws Exception {
		boolean validInput = false;
		double fineAmount = 0.0;
		do {
			String query = "SELECT DATEDIFF(?, ?)";
	
			try {
				con = ConnectionLibrary.getConnection();
				pst = con.prepareStatement(query);
				
				input.nextLine();
				System.out.print("Please enter a  borrow date in YYYY-MM-DD format: ");
					String borrowDate = input.nextLine();
					
				System.out.print("Please enter a return date in YYYY-MM-DD format: ");
					String returnDate = input.nextLine();
				
				pst.setString(1, returnDate);
				pst.setString(2, borrowDate);

				rs = pst.executeQuery();
				
				if(rs.next()) {
					int days = rs.getInt(1);
					if (days > 30 ) {
						fineAmount = (days -30) *0.50 ;
						System.out.println("Late " + (days -30) +" days");
					}
				}
				System.out.println("The fine amount is " + fineAmount + "$");
				System.out.println("-----------------------------------------------\n");
				validInput  = true;
			} catch (Exception e) {
				System.out.println("Invalid input. Please enter again");
				input.next();
			} finally {
				ConnectionLibrary.closeConnection(con, pst, rs);
			}
		}while(!validInput);
		BookLibrary.selectLibrary();
	}
	
	/**
	 * calculate the total fine amount of a certain branch
	 * close connection when finish a query
	 * @throws Exception
	 */
	
	public void calcTotalFineAmount() throws Exception {
		boolean validInput = false;
		do {
			try {
				con = ConnectionLibrary.getConnection();
				displayLibraryData();
				pst = con.prepareStatement("SELECT SUM(FINEAMOUNT) FROM LIBRARY WHERE BRANCHCODE = ?");
				 
				System.out.print("Please enter branch code: ");
				int branchCode = input.nextInt();
				
				pst.setInt(1,  branchCode);
				
				rs = pst.executeQuery();
				
				if (rs.next()) {
					double totalFineAmount = rs.getDouble(1);
					System.out.println("The total fine amount of Branch Code "+branchCode+" is "+ totalFineAmount);
				}
	            System.out.println("-----------------------------------------------\n");
	            validInput = true;
			} catch (Exception e) {
				System.out.println("Invalid input. Please enter again");
				input.nextLine();
			} finally {
				ConnectionLibrary.closeConnection(con, pst, rs);
			}
		}while(!validInput);
		BookLibrary.selectLibrary();
	}
	
	public void listLibraryInformation () throws Exception {
		try {
			displayLibraryData();
		}catch (Exception e) {
			System.out.println("An error occurs");
			input.next();
		}finally {
			ConnectionLibrary.closeConnection(con, pst, rs);
		}
		BookLibrary.selectBranch();
	}

}
