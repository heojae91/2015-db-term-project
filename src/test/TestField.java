package test;

import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.StringTokenizer;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class TestField {
	private String username;
	private String password;

	private JLabel idLabel = new JLabel("아이디");
	private JLabel pwdLabel = new JLabel("비밀번호");
	private JTextField idInput = new JTextField();
	private JPasswordField pwdInput = new JPasswordField();
	private JButton loginButton = new JButton("로그인");
	private JPanel panel = new JPanel();
	private JFrame frame = new JFrame();
	
	// sql을 가지고 놀 녀석들
	private Connection dbControl;
	private PreparedStatement stmt;
	private ResultSet rs;

	private int connectDB() throws SQLException {
		username = "system";
		password = "system";
		try {
			// JDBC Driver Loading
			Class.forName("oracle.jdbc.OracleDriver");
			dbControl = DriverManager.getConnection("jdbc:oracle:thin:"+
		"@localhost:1521:XE", username, password);
			//JOptionPane.showMessageDialog(frame, "로그인 되었습니다!");
			return 1;
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("SQLException:"+e);
			JOptionPane.showMessageDialog(null,"아이디 혹은 비밀번호가 잘못되었습니다.");
			return 0;
		} catch (Exception e) {
			System.out.println("Exception : "+e);
			return 0;
		}
	}

	private void createTables() throws SQLException{
		String customerSql = "create table customer (\n"
				+ "customername	varchar(10)	not null,\n"
				+ "birthday	integer not null check(birthday<=1231 or birthday=9999),\n"
				+ "customernumber	integer not null check(customernumber<10000),\n"
				+ "grade	varchar(10) not null,\n"
				+ "purchases	integer not null,\n"
				+ "primary key	(customername))";
		
		String staffSql = "create table staff(\n"
				+ "staffname	varchar(10)	not null,\n"
				+ "staffnumber	integer	not null,\n"
				+ "rank	varchar(10)	not null,\n"
				+ "sales	integer not null,\n"
				+ "primary key	(staffname)\n"
				+ ")";
		
		String menuSql = "create table menu(\n"
				+ "menuname	varchar(20)	not null,\n"
				+ "price	integer	not null,\n"
				+ "menunumber	integer not null,\n"
				+ "primary key	(menuname)\n"
				+ ")";
		
		String orderedSql = "create table ordered(\n"
				+ "ordernumber	integer not null,\n"
				+ "menuname	varchar(20) not null,\n"
				+ "tablenumber	integer not null,\n"
				+ "staffname	varchar(10) not null,\n"
				+ "customername	varchar(10) not null,\n"
				+ "flag	integer not null check (flag = 0 or flag = 1),"
				+ "primary key(ordernumber),\n"
				+ "foreign key(menuname) references menu,\n"
				+ "foreign key(staffname) references staff\n"
				+ ")";
		
		String dailyresultSql = "create table dailyresult(\n"
				+ "day	date not null,\n"
				+ "revenue	integer not null,\n"
				+ "bestmenu	varchar(20) not null,\n"
				+ "worstmenu varchar(20) not null,\n"
				+ "primary key (day)\n"
				+ ")";
		try {
			String[] tablename = { "customer", "staff", "menu", "ordered",
					"dailyresult" };
			String[] sqlNames = {customerSql, staffSql, menuSql, orderedSql, dailyresultSql};
			for (int i = 0; i < tablename.length; i++) {
				// db에 이미 table이 생성이 되어있는지를 check, 없다면 생성, 있다면 생성하지 않음
				if (checkTableNames(tablename[i]) == false) {
					System.out.println(tablename[i]);
					System.out.println(sqlNames[i]);
					stmt = dbControl.prepareStatement(sqlNames[i]);
					rs = stmt.executeQuery();

					dbControl.commit();
				}
			}
		} catch (SQLException e) {
			e.getStackTrace();
			dbControl.rollback();
		} finally {
			stmt.close();
			rs.close();
		}
	}
	
	public boolean checkTableNames(String tableNames) throws SQLException
	{
		boolean tableNameFlag = false;
		String sqlTableName = "select table_name from user_tables";
		stmt = dbControl.prepareStatement(sqlTableName);
		rs = stmt.executeQuery();

		while (rs.next())
		{
			if (rs.getString("table_name").toUpperCase().equals(tableNames.toUpperCase()))
				tableNameFlag = true;
		}
		return tableNameFlag;
	}

	public int makeRandom(String type) throws SQLException
	{
		int random = (int)(Math.random() * 10);
		while (findDuplicated(type, random))
		{
			random = (int)(Math.random() * 10);
		}
		return random;
	}
	
	public boolean findDuplicated(String type, int randNum) throws SQLException
	{
		boolean flag = false;
		
		String sql;
		
		if (type.equals("customer"))
		{
			sql = "select customernumber from customer";
			try
			{
				stmt = dbControl.prepareStatement(sql);
				rs = stmt.executeQuery();
				
				while (rs.next())
				{
					if (rs.getInt("customernumber") == randNum)
					{
						flag = true;
						break;
					}
				}
			}
			
			catch (SQLException e)
			{
				return true;
			}
			catch (Exception ex)
			{
				return true;
			}
		}
		else if (type.equals("staff"))
		{
			sql = "select customernumber from staff";
			try
			{
				stmt = dbControl.prepareStatement("select staffnumber from staff");
				rs = stmt.executeQuery();
				
				while (rs.next())
				{
					if (rs.getInt("staffnumber") == randNum)
					{
						flag = true;
						break;
					}
				}
			}
			
			catch (SQLException e)
			{
				return true;
			}
			catch (Exception ex)
			{
				return true;
			}
			finally
			{
				stmt.close();
				rs.close();
			}
		}
		else {
			return true; // staff 혹은 customer 이외의 값이 들어오는 경우
		}

		return flag;
	}
	
	public void insertStaff(String staffName, String rank) throws SQLException {
		try
		{
			String sql = "insert into staff values(\n"
				+ staffName + ", " + makeRandom("staff") + ", " + rank + ", 0)";
			stmt = dbControl.prepareStatement(sql);
			rs = stmt.executeQuery();
			
			dbControl.commit();
		} catch(SQLException e)
		{
			JOptionPane.showMessageDialog(null, "잘못된 입력입니다!");
			dbControl.rollback();
		}
		finally
		{
			stmt.close();
			rs.close();
		}
	}
	
	public void insertCustomer(String customerName, String rank) throws SQLException {
		try
		{
			String sql = "insert into customer values(\n"
				+ customerName + ", " + makeRandom("customer") + ", " + rank + ", 0)";
			stmt = dbControl.prepareStatement(sql);
			rs = stmt.executeQuery();
			
			dbControl.commit();
		} catch(SQLException e)
		{
			JOptionPane.showMessageDialog(null, "잘못된 입력입니다!");
			dbControl.rollback();
		}
		finally
		{
			stmt.close();
			rs.close();
		}
	}


	public static void main(String[] args) throws SQLException {
		new TestField().connectDB();
	}
}
