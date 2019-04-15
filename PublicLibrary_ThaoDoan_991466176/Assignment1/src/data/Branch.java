/**
 * Student Name: Doan Ngoc Phuong Thao
 * Student ID: 991466176
 */
package data;

import java.sql.*;
import java.util.InputMismatchException;
import java.util.Scanner;

import data.ConnectionLibrary;

/**
 * @author Christine
 *
 */
public class Branch {

	Scanner input = new Scanner (System.in);
	static Connection con = null;
	PreparedStatement pst;
	static ResultSet rs = null;
	
	/**
	 * print all book information in branch table
	 * @throws Exception if can not connect to database
	 */
	public void displayBranchData () throws Exception {
		try {
			con = ConnectionLibrary.getConnection();
			pst = con.prepareStatement("SELECT * from BRANCH ");
			rs = pst.executeQuery();					
			
			System.out.println("--- BRANCH INFORMATION ---");
			System.out.println("BranchCode      BranchName                Address                            postal Code");
			while (rs.next()) {
				String branchCode = rs.getString("BRANCHCODE").toString();
				String branchName = rs.getString("BRANCHNAME");
				String address = rs.getString("ADDRESS");
				String postalCode = rs.getString("POSTALCODE");
				System.out.printf("%-15s %-25s %-35s %-6s\n", 
						branchCode, branchName, address, postalCode);
			};
			
		}catch (Exception e) {
			System.out.println("There is an error occurs");
		} 
	}
	
	/**
	 * display branch menu to select
	 * @exception throw an exception if the input is invalid
	 */
	public int selectFromBranchMenu () {
		int menuSelection =0;
		boolean validInput = false;
		do {
			try {
				System.out.println("1. Add Data                       \n2. Update Data"
						       + "\n3. Delete Data                    \n4. Search"
						       + "\n5. technology books in each branch "
						       + "\n6. Display table data             \n7. Quit");
				System.out.print("Please choose an option: ");
			
				menuSelection = input.nextInt();
				if (menuSelection <1 || menuSelection > 7) 
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
	 * add a new record to Branch table by asking for inputs
	 * @throws Exception when any input is not valid. 
	 * Keep asking until user enters a valid input
	 */
	public void addData() throws Exception {
		boolean validInput = false;
		do {
	        String addQuery = "INSERT INTO BRANCH (BRANCHCODE, BRANCHNAME, ADDRESS, POSTALCODE) VALUES(?,?,?,?)";
	        try{
	           con = ConnectionLibrary.getConnection();
		       pst = con.prepareStatement(addQuery);
	               
		       System.out.print("Please enter a branch code: ");
			       int branchCode = input.nextInt();
			       input.nextLine();
		       System.out.print("Please enter a branch name: ");
		       		String branchName = input.nextLine();
		       System.out.print("Please enter an address: ");
		       		String address = input.nextLine();
		       System.out.print("Please enter a postal code: ");
		       		String postalCode = input.nextLine();
		       
	            pst.setInt(1,branchCode);
	        	pst.setString(2,branchName);
	        	pst.setString(3,address);;
	        	pst.setString(4, postalCode);
	
	            pst.executeUpdate();
	
				displayBranchData();
	            System.out.println("-----------------Add complete----------------");
	            validInput = true;
	        }catch(Exception e){
	        	System.out.println("Invalid input. Please enter again");
	        	input.next();
	        }finally{	
	            ConnectionLibrary.closeConnection(con,pst,null);
	        }
		}while(!validInput);
		BookLibrary.selectBranch();
   }
	
	/**
	 * update record by asking for a Branch code
	 * we can update by my fields up to user choice
	 * display branch information before and  after updating so user can see the change
	 * check if the branchCode exist in database or not
	 * @throws Exception throw exception when an error occurs
	 */
	public void updateData () throws Exception {	
		boolean validInput = false;
		do {
			String updateQuery = null;
			try {
				con = ConnectionLibrary.getConnection();
				displayBranchData();
				
				System.out.print("\nPlease enter a branch code: ");
				int branchCode = input.nextInt(); 	
				
				System.out.println("Please select an option to update : "
						+ "\n1. Branch name \n2. Address\n3. Postal Code");	
				int option = input.nextInt();
				
				switch (option) {
					case 1: 
							System.out.print("Please enter a new Branch name: ");
							input.nextLine();
							String branchName = input.nextLine();
							updateQuery = "Update BRANCH set BRANCHNAME = \'"+ branchName+"\' where BRANCHCODE=?";
							break;
					case 2:  
							System.out.print("Please enter a new address: ");
							input.nextLine();
							String address = input.nextLine();
							updateQuery = "Update BRANCH set ADDRESS = \'"+ address+"\' where BRANCHCODE=?";
							break;
					case 3: 
							System.out.print("Please enter a postal code (maximum 6 characters): ");
							input.nextLine();
							String postalCode = input.nextLine();
							updateQuery = "Update BRANCH set POSTALCODE = \'"+ postalCode+"\' where BRANCHCODE=?";
							break;
					default: throw new Exception ();
				}
				pst = con.prepareStatement(updateQuery);
				pst.setInt(1, branchCode);
		
				int updateNum = pst.executeUpdate();
				if(updateNum == 0 )
					throw new IllegalArgumentException("This branch code does not exit in database. Please enter a valid one");

				displayBranchData();
	            System.out.println("-----------------Update complete----------------");
	            validInput = true;
			}catch (IllegalArgumentException ex) {
				System.out.println(ex.getMessage());
				input.next();
			}catch (Exception e) {
				System.out.println("Invalid input. Please enter a valid input.");
				input.next();
			}finally {
				ConnectionLibrary.closeConnection(con, pst, null);
			}
		}while(!validInput);
		BookLibrary.selectBranch();
	}
	
	/**
	 * delete a record from the table by asking a branch code
	 * display book table after deleting
	 * @throws Exception when user enters an invalid input
	 */
	
	public void deleteData () throws Exception {
		boolean validInput = false;
		String deleteQuery = "DELETE from BRANCH Where BRANCHCODE=?;";
		do {
			try {
				con = ConnectionLibrary.getConnection();
				displayBranchData();
				pst = con.prepareStatement(deleteQuery);		
				
				System.out.print("Please enter a branch code: ");
			       int branchCode = input.nextInt();
			       
				pst.setInt(1, branchCode);
					
				pst.executeUpdate();
	
				displayBranchData();
	            System.out.println("-----------------Delete complete----------------");
	            validInput = true;
			}catch (Exception e) {
				System.out.println("Invalid input. Please enter a valid input.");
				input.next();
			}finally {
				ConnectionLibrary.closeConnection(con, pst, null);
			}
		}while(!validInput);
		BookLibrary.selectBranch();
	}
	
	/**
	 * search for branch information base on branch name or postal code
	 * display all matching input 
	 * @throws Exception
	 */
	public void searchData() throws Exception {
		boolean validInput = false;
		do {
			try {
				String searchQuery = "";
		
				System.out.print("    1. Search by Branch name    \n2. Search by postal code\n"
						+"Please enter an option: ");
				int searchOption = input.nextInt();
				input.nextLine();
				
				if (searchOption == 1)
					searchQuery = "SELECT * from BRANCH where BRANCHNAME LIKE ?";
		
				else // this case we always make sure that the other option is searching by postalCode
					searchQuery = "SELECT * from BRANCH where POSTALCODE LIKE ?";
	
			
				con = ConnectionLibrary.getConnection();
				pst = con.prepareStatement(searchQuery);
	
				System.out.print("Please enter something to search: ");
				String search = input.nextLine();
				
				pst.setString(1, "%" + search + "%");
	
				rs = pst.executeQuery();
				System.out.println(
						"BranchCode      BranchName              Address                    PostalCode");
				while (rs.next()) {
					String branchCode = rs.getString("BRANCHCODE").toString();
					String BRANCHNAME = rs.getString("BRANCHNAME");
					String ADDRESS = rs.getString("ADDRESS");
					String POSTALCODE = rs.getString("POSTALCODE");
					System.out.printf("%-15s %-25s %-25s %-6s\n", branchCode, BRANCHNAME, ADDRESS, POSTALCODE);
				};
				validInput = true;
			} catch (Exception e) {
				System.out.println("Invalid input. Please enter a valid input.");
				input.next();
			} finally {
				ConnectionLibrary.closeConnection(con, pst, rs);
			}
		}while(!validInput);
		BookLibrary.selectBranch();
	}
	
	
	/**
	 * display number of technology books in each branch by an input branch code
	 * @throws Exception
	 */
	public void techBooksInEachBranch() throws Exception {
		boolean validInput = false;
		do {
			try {
				String query = "select * from BOOKS  where TYPE = \'Technology\' "
						+ " and BOOKCODE in (select count(BOOKCODE) from LIBRARY where BRANCHCODE = ?);";
				con = ConnectionLibrary.getConnection();
				pst = con.prepareStatement(query);
				
				System.out.print("Please enter a branch code: ");
				int branchCode = input.nextInt();
				
				pst.setInt(1, branchCode);
				rs = pst.executeQuery();					
				if (rs.next()) {
					int numberOfBooks = rs.getInt(1);
					System.out.println("There are " + numberOfBooks + " number of technology books"
							+ " of the branch code " + branchCode);
				};
				validInput = true;
			}catch (Exception e) {
				System.out.println("Invalid input. Please enter a valid input.");
				e.printStackTrace();
				input.next();
			} finally{
				ConnectionLibrary.closeConnection(con, pst, rs);
			}
		}while(!validInput);
		BookLibrary.selectBranch();
	}
	
	public void listBranchInformation () throws Exception {
		try {
			displayBranchData();
		}catch (Exception e) {
			System.out.println("An error occurs");
			input.next();
		}finally {
			ConnectionLibrary.closeConnection(con, pst, rs);
		}
		BookLibrary.selectBranch();
	}
}

