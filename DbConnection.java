package com.bankapplication.dbconnection;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.Map;
import java.util.HashMap;
import com.bankapplication.account.Account;
import com.bankapplication.customer.Customer;
public class DbConnection{
	String url = "jdbc:mysql://127.0.0.1:3306/app";
	String login = "root";
	String pass = "";
	String table1,table2 ;
	Connection con = null;
	PreparedStatement ps1 = null, ps2=null;
	Map <Integer,Map<Long,Account>> dbHashMap = new HashMap<>();
	public void getConnection()throws Exception{
		Class.forName("com.mysql.cj.jdbc.Driver");
		if(con==null){
			con= DriverManager.getConnection(url,login,pass);
		}
	}
	public void closeConnection() throws Exception{
		if(con!=null)
			con.close();
			//con=null;
	}
	public void getStatement() throws Exception{
		if(ps1==null){
			ps1 = con.prepareStatement("insert into "+this.table1+" values(?,?)");
			//System.out.println("hi");
		}
		if(ps2==null)
			ps2 = con.prepareStatement("insert into "+this.table2+" values(?,?,?)");
	}
	public void closeStatement() throws Exception{
		if(ps1!=null&&ps2!=null){
			ps1.close();
			ps2.close();
		}
	}
	public void createTables(String table1,String table2) throws Exception{
		getConnection();
		/*DatabaseMetaData dbm = con.getMetaData();
		ResultSet tables = dbm.getTables(null, null,table1, null);
		if(tables.next())
			return false;
		tables = dbm.getTables(null, null,table2, null);
		if(tables.next())
			return false;
		tables.close();*/
		this.table1=table1;
		this.table2=table2;
		try(Statement statement = con.createStatement()){
			String sql1 = "create table if not exists "+this.table1+" (id INTEGER not null, name VARCHAR(255) not null,PRIMARY KEY(id))";
			statement.executeUpdate(sql1);
			String sql2 = "create table if not exists "+this.table2+" ( accountno BIGINT unsigned not null, balance INTEGER not null, id INTEGER not null,PRIMARY KEY(accountno), FOREIGN KEY (id) REFERENCES "+this.table1+" (id) )";
			statement.executeUpdate(sql2);
		}
		//return true;
	}
	public void insertUser(Customer customer) throws Exception{
		if(customer == null)
			return ;
		getConnection();
		getStatement();
		/*try(Statement statement = con.createStatement()){
			String sql="insert into "+table1+" values("+customer.getId()+",'"+customer.getName()+"')";
			statement.executeUpdate(sql);
		}*/
		ps1.setInt(1,customer.getId());
		ps1.setString(2,customer.getName());
		ps1.executeUpdate();
		System.out.println("Customer details inserted successfully");
	}
	public void insertAccountDetails(Account account) throws Exception{
		if(account==null)
			return ;
		getConnection();
		/*try(Statement statement = con.createStatement()){
			String sql = "insert into "+table2+" values("+account.getAccountNo()+","+account.getBalance()+","+account.getId()+")";
			statement.executeUpdate(sql);		
		}*/
		ps2.setLong(1,account.getAccountNo());
		ps2.setInt(2,account.getBalance());
		ps2.setInt(3,account.getId());
		ps2.executeUpdate();
		System.out.println("Account details inserted successfully");
	}
	public Map<Integer,Map<Long,Account>> retrieveUsers()throws Exception{
		//if(con==null)
			//System.out.println("bb");
		getConnection();
		try(Statement statement = con.createStatement();ResultSet rs = statement.executeQuery("select id,accountno,balance from "+table2)){
			while(rs.next()){
				/*System.out.print(rs.getInt("id")+" "+rs.getString("name")+" "+rs.getLong("no")+" "+rs.getInt("balance"));
				System.out.println();*/
				Account account = new Account();
				account.setId(rs.getInt("id"));
				account.setAccountNo(rs.getLong("accountno"));
				account.setBalance(rs.getInt("balance"));
				Map<Long,Account> userMap = dbHashMap.getOrDefault(rs.getInt("id"),new HashMap<Long,Account>());
				userMap.put(rs.getLong("accountno"),account);
				dbHashMap.put(rs.getInt("id"),userMap);
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
		closeStatement();
		closeConnection();
		return dbHashMap;
	}
}