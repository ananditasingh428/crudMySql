package testProject;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class Database {
	static Connection conn;
	static ResultSet rs = null;

	public Database() {

			try {			
				
				Class.forName("com.mysql.cj.jdbc.Driver");
				conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/mydb", "root", null);
				if(conn == null) {
					System.out.println("Couldn't connect to the database");
					System.exit(1);
				}
				else System.out.println("Successfully connected to the database");				
			} catch (SQLException | ClassNotFoundException e) {
				e.printStackTrace();
			}
	}
	
	public void insert(int uid, String uname, int age) {
	
		try {
			String  query = "insert into sampletable values (?, ?, ?);";
			PreparedStatement prepStm = conn.prepareStatement(query);
			
			prepStm.setInt(1, uid);
			prepStm.setString(2, uname);
			prepStm.setInt(3, age);
			
			prepStm.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	
	public void delete(int uid) {
		
		try {
			Statement stm = conn.createStatement();
			stm.executeUpdate("delete from sampletable where uid =" + uid + ";");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public ResultSet getAllData() throws SQLException {
		
			return conn.createStatement().executeQuery("select uid, uname, age from sampletable;");
	}
	
	public void printResultSet(ResultSet rs) {
		boolean isMore;
		
		try {
			while(isMore = rs.next()) {
				System.out.print(rs.getInt(1)+", ");
				System.out.print(rs.getString(2) + ", ");
				System.out.println(rs.getString(3));
				
			}
			System.out.println();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public ResultSet searchByName(String uname) throws SQLException {
		PreparedStatement prepStm =  conn.prepareStatement("select uid, uname, age from sampletable where uname = ?");
		prepStm.setString(1, uname);
		return prepStm.executeQuery();
	}
	
	public void update(int uid, String currName) throws SQLException {
		PreparedStatement prepStm =  conn.prepareStatement("update sampletable set uname = ? where uid = ?");
		prepStm.setString(1, currName);
		prepStm.setInt(2, uid);
		prepStm.executeUpdate();
	}
	
	public static void main(String[] args) throws SQLException {
		Scanner sc = new Scanner(System.in);
		Database db = new Database();
		
		
		int decision;
		
		do {
			System.out.println("Enter what you want to do\n1.Insert\n2.Update Name\n3.Delete\n4.Read all\n5.Search by name\n6.Exit");
			switch(decision = sc.nextInt()) {
				case 1:
					System.out.println("Enter User id, User name and User age");
					db.insert(sc.nextInt(), sc.next(), sc.nextInt());
					break;
				case 2:
					System.out.println("Enter the uid of the person's whose name should be updated and new name");
					db.update(sc.nextInt(), sc.next());
					break;
				case 3:
					System.out.println("Enter the uid to delete");
					db.delete(sc.nextInt());
					break;
				case 4:
					db.printResultSet(db.getAllData());
					break;
				case 5:
					System.out.println("Enter the name to search");
					db.printResultSet(db.searchByName(sc.next()));
					break;
				case 6:
					System.out.println("Goodbye");
					conn.close();
					break;
				default:
					System.out.println("Please Enter a digit between 1-6");
					break;
			}
						
		} while(decision != 6);
		
}

	}


