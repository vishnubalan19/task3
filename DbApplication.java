package com.bankapplication.dbapplication;
import java.util.Map;
import java.util.HashMap;
import java.util.Scanner;
import com.bankapplication.account.Account;
import com.bankapplication.customer.Customer;
import com.bankapplication.dbconnection.DbConnection;
public class DbApplication{
	int id = 1;
	DbConnection dbConnection = new DbConnection();
	Map <Integer,Customer> customerMap = new HashMap<>();
	Map <Integer,Map<Long,Account>> dbHashMap = new HashMap<>();
	public void getUserChoice()throws Exception{
		Scanner scanner = new Scanner(System.in);
		System.out.println("Database Application");
		System.out.println("1. Create User details table and Account table ");
		System.out.println("2. Insert values into User details table and Account table");
		System.out.println("3. Get User and Account information");
		System.out.println("4. Exit");
		int choice = scanner.nextInt();
		switch(choice){
			case 1:
				String table1,table2;
				System.out.println("Enter the table name which contains user id and user name");
				table1=scanner.next();
				System.out.println("Enter the table name which contains account no, account balance and id");
				table2=scanner.next();
				//if(dbConnection.createTables(table1,table2))
				dbConnection.createTables(table1,table2);
				System.out.println("Tables are created successfully");
				//else
					//System.out.println("Enter unique table names");
				getUserChoice();
				break;
			case 2:
				System.out.println("Enter the no. of users");
				int noOfUsers=scanner.nextInt();
				while(noOfUsers-->0){
					System.out.println("Enter the user name");
					String name = scanner.next();
					Customer customer = new Customer();
					customer.setId(id);
					customer.setName(name);
					customerMap.put(id,customer);
					dbConnection.insertUser(customer);
					System.out.println("Enter the no. of accounts for user");
					int noOfAccounts = scanner.nextInt();
					while(noOfAccounts-->0){
						System.out.println("Enter the accountno and balance");
						long accountno = scanner.nextLong();
						int balance = scanner.nextInt();
						Account account = new Account();
						account.setAccountNo(accountno);
						account.setId(id);
						account.setBalance(balance);
						dbConnection.insertAccountDetails(account);
					}
					id++;
				}
				getUserChoice();
				break;
			case 3:
				dbHashMap = dbConnection.retrieveUsers();
				boolean flag=true;
				while(flag){
					System.out.println("1. Find users");
					System.out.println("2. Exit");
					System.out.println("Enter your choice");
					int curChoice = scanner.nextInt();
					switch(curChoice){
					case 1 :
						System.out.println("Enter id");
						int id = scanner.nextInt();
						if(dbHashMap.containsKey(id)){
							Map<Long,Account> tempMap = dbHashMap.get(id);
							for(Long t : tempMap.keySet()){
								System.out.print(tempMap.get(t).getId()+" "+customerMap.get(tempMap.get(t).getId()).getName()+" "+tempMap.get(t).getAccountNo()+" "+tempMap.get(t).getBalance());
								System.out.println();
							}
						}
						else{
							System.out.println("Enter valid user id");
						}
						break;
					case 2 :
						flag=false;
						break;
					default :
						System.out.println("Enter valid choice");
						break;
					}
				}
				getUserChoice();
				break;
			case 4:
				break;
			default:
				System.out.println("Enter appropriate choice");
				getUserChoice();
				break;
		}
		//System.out.println("");
	}
	public static void main(String [] args) throws Exception{
		new DbApplication().getUserChoice();
	}
}