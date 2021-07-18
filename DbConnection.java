import java.sql.*;
import java.util.*;
class DbConnection{
	String url = "jdbc:mysql://127.0.0.1:3306/app";
	String login = "root";
	String pass = "";
	String table1,table2 ;
	Connection con = null;
	Map <Integer,Map<Long,UserData>> dbHashMap = new HashMap<>();
	public void getConnection()throws Exception{
		if(con==null)
			con= DriverManager.getConnection(url,login,pass);
	}
	public void closeConnection(){
		if(con!=null)
			con=null;
	}
	public void createTables(String table1,String table2) throws Exception{
		Class.forName("com.mysql.cj.jdbc.Driver");
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
			String sql2 = "create table if not exists "+this.table2+" ( no BIGINT unsigned not null, balance INTEGER not null, id INTEGER not null,PRIMARY KEY(no) )";
			statement.executeUpdate(sql2);
		}
		//return true;
	}
	public void insertUser(Customer customer) throws Exception{
		if(customer == null)
			return ;
		Class.forName("com.mysql.cj.jdbc.Driver");
		getConnection();
		try(Statement statement = con.createStatement()){
			String sql="insert into "+table1+" values("+customer.getId()+",'"+customer.getName()+"')";
			statement.executeUpdate(sql);
		}
		System.out.println("Customer details inserted successfully");
	}
	public void insertAccountDetails(Account account) throws Exception{
		if(account==null)
			return ;
		Class.forName("com.mysql.cj.jdbc.Driver");
		getConnection();
		try(Statement statement = con.createStatement()){
			String sql = "insert into "+table2+" values("+account.getAccountNo()+","+account.getBalance()+","+account.getId()+")";
			statement.executeUpdate(sql);		
		}
		System.out.println("Account details inserted successfully");
	}
	public Map<Integer,Map<Long,UserData>> retrieveUsers()throws Exception{
		Class.forName("com.mysql.cj.jdbc.Driver");
		//if(con==null)
			//System.out.println("bb");
		getConnection();
		try(Statement statement = con.createStatement();ResultSet rs = statement.executeQuery("select a.id,a.name,b.no,b.balance from "+table1+" as a inner join "+table2+" as b on a.id=b.id")){
			while(rs.next()){
				/*System.out.print(rs.getInt("id")+" "+rs.getString("name")+" "+rs.getLong("no")+" "+rs.getInt("balance"));
				System.out.println();*/
				UserData userData = new UserData();
				userData.setId(rs.getInt("id"));
				userData.setAccountNo(rs.getLong("no"));
				userData.setBalance(rs.getInt("balance"));
				userData.setName(rs.getString("name"));
				Map<Long,UserData> userMap = dbHashMap.getOrDefault(rs.getInt("id"),new HashMap<Long,UserData>());
				userMap.put(rs.getLong("no"),userData);
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
		closeConnection();
		return dbHashMap;
	}
}