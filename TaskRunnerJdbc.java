import java.util.*;
import java.sql.*;
class Account{
	private String name;
	private int balance;
	public void setName(String name){
		this.name = name;
	}
	public void setBalance(int balance){
		this.balance = balance;
	}
	public String getName(){
		return this.name;
	}
	public int getBalance(){
		return this.balance;
	}
}
class TestRunnerJdbc{
	String url = "jdbc:mysql://127.0.0.1:3306/";
	String login = "root";
	String pass = "";
	Scanner sc = new Scanner(System.in);
	String t1="";
	String t2="";
	HashMap <Integer,HashMap<Long,Account>> hm = new HashMap<>();
	public void createDatabase() throws Exception{
		Class.forName("com.mysql.cj.jdbc.Driver");
		try(Connection con = DriverManager.getConnection(url,login,pass);Statement st = con.createStatement()){
			System.out.println("Enter db name");
			String db = sc.next();
			st.executeUpdate("create database "+db);
			System.out.println("db created succesfully");
			url+=db;
		}
	}
	public void createTables() throws Exception{
		Class.forName("com.mysql.cj.jdbc.Driver");
		try(Connection con = DriverManager.getConnection(url,login,pass);Statement st = con.createStatement()){
			System.out.println("Enter the table name which contains user id and user name");
			t1 = sc.next();
			String sql1 = "create table "+t1+" (id INTEGER not null, name VARCHAR(255) not null,PRIMARY KEY(id))";
			st.executeUpdate(sql1);
			System.out.println("Enter the table name which contains account no, account balance and id");
			t2 = sc.next();
			String sql2 = "create table "+t2+" ( no BIGINT unsigned not null, balance INTEGER not null, id INTEGER not null,PRIMARY KEY(no) )";
			st.executeUpdate(sql2);
			System.out.println("tables created succesfully");
		}
	}
	public void insertUsers() throws Exception{
		Class.forName("com.mysql.cj.jdbc.Driver");
		try(Connection con = DriverManager.getConnection(url,login,pass);Statement st = con.createStatement()){
			System.out.println("Enter the no. of users");
			int n=sc.nextInt();
			int k=1;
			while(n-->0){
				System.out.println("Enter the user name");
				String name = sc.next();
				String sql="insert into "+t1+" values("+k+",'"+name+"')";
				st.executeUpdate(sql);
				System.out.println("Enter the no. of accounts for user");
				int acc = sc.nextInt();
				while(acc-->0){
					System.out.println("Enter the accountno and balance");
					long accountno = sc.nextLong();
					int balance = sc.nextInt();
					String sqla = "insert into "+t2+" values("+accountno+","+balance+","+k+")";
					st.executeUpdate(sqla);
				}
				k++;
			}
			System.out.println("users inserted succesfully");
		}
	}
	public void retrieveUsers()throws Exception{
		Class.forName("com.mysql.cj.jdbc.Driver");
		try(Connection con = DriverManager.getConnection(url,login,pass);Statement st = con.createStatement();ResultSet rs = st.executeQuery("select a.id,a.name,b.no,b.balance from "+t1+" as a inner join "+t2+" as b on a.id=b.id")){
			while(rs.next()){
				/*System.out.print(rs.getInt("id")+" "+rs.getString("name")+" "+rs.getLong("no")+" "+rs.getInt("balance"));
				System.out.println();*/
				Account ac = new Account();
				ac.setBalance(rs.getInt("balance"));
				ac.setName(rs.getString("name"));
				HashMap<Long,Account>temp = hm.getOrDefault(rs.getInt("id"),new HashMap<Long,Account>());
				temp.put(rs.getLong("no"),ac);
				hm.put(rs.getInt("id"),temp);
			}
			/*for(int k : hm.keySet()){
				System.out.println(k);
				HashMap<Long,Account> temp = hm.get(k);
				for(Long t : temp.keySet()){
					System.out.print(t+" "+temp.get(t).getName()+" "+temp.get(t).getBalance()+" ");
				}
				System.out.println();
			}*/
		}
	}
	public void getUsers(){
		System.out.println("1. Find users");
		System.out.println("2. Exit");
		System.out.println("Enter your choice");
		int n = sc.nextInt();
		switch(n){
			case 1 :
				System.out.println("Enter id");
				int id = sc.nextInt();
				if(hm.containsKey(id)){
					HashMap<Long,Account> temp = hm.get(id);
					for(Long t : temp.keySet()){
						System.out.print(t+" "+temp.get(t).getName()+" "+temp.get(t).getBalance()+" ");
						System.out.println();
					}
				}
				else{
					System.out.println("Enter valid user id");
				}
				getUsers();
				break;
			case 2 :
				break;
			default :
				System.out.println("Enter valid choice");
				getUsers();
				break;
		}
	}
	public void method() throws Exception{
		createDatabase();
		createTables();
		insertUsers();
		retrieveUsers();
		getUsers();
	}
	public static void main(String args[]) throws Exception{
		new TestRunnerJdbc().method();
	}
}