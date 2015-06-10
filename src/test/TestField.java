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
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.StringTokenizer;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class TestField {
	private String username;
	private String password;
	private String id;

	private JLabel idLabel = new JLabel("아이디");
	private JLabel pwdLabel = new JLabel("비밀번호");
	private JTextField idInput = new JTextField();
	private JPasswordField pwdInput = new JPasswordField();
	private JButton loginButton = new JButton("로그인");
	private JPanel panel = new JPanel();
	private JFrame frame = new JFrame();
	
	private String[] menuList = new String[20];
	private int menuIndex = 0;
	
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
	
	public void regMenu()
	{
		try
		{
			String countSql = "select count(customernumber) from test";
			stmt = dbControl.prepareStatement(countSql);

			rs = stmt.executeQuery();
			System.out.println(rs.getInt("count(customernumber)"));

		}
		catch (SQLException e)
		{
			JOptionPane.showMessageDialog(null, "잘못된 입력입니다!");
		}
	}
	
	public String[] getMenu() throws SQLException
	{
		String sql = "select customernumber from test";
		stmt = dbControl.prepareStatement(sql);
		rs = stmt.executeQuery();
		
		for (int i = 0; i < 20; i++)
		{
			menuList[i] = "";
		}
		
		while (rs.next())
		{
			menuList[menuIndex] = rs.getString("customernumber");
			menuIndex += 1;
		}
		return menuList;
	}
	
	public int staffLogin(String id, String pwd) throws SQLException
	{
		int staffLoginFlag = 0;
		try
		{
			String sql = "select staffname, staffnumber, rank from staff where staffname ='" + id + "'";
			stmt = dbControl.prepareStatement(sql);
			rs = stmt.executeQuery();
			rs.next();
			
			if (rs.getInt("staffnumber") == Integer.parseInt(pwd))
			{
				if (rs.getString("rank").toLowerCase().equals("supervisor"))
					staffLoginFlag = 2;
				else staffLoginFlag = 1;
			}
		}
		catch (SQLException e)
		{
			JOptionPane.showMessageDialog(null, "해당하는 직원이 없습니다!"); // 이름이 없거나 회원번호가 맞지 않는 경우
		}
		
		this.id = id;
		return staffLoginFlag;
	}
	
	public String getStaffName()
	{
		return id;
	}
	
	
	public int getPrice(String[] menus)
	{
		int totalPrice = 0;
		for (int i = 0; i < 20; i++)
		{
			try 
			{
				if (menus[i] == null)
				{
					break;
				}
				String sql = "select price from menu where menuname ='" + menus[i] + "'";

				stmt = dbControl.prepareStatement(sql);
				rs = stmt.executeQuery();
				rs.next();
				stmt.close();
				rs.close();
				
				totalPrice += rs.getInt("price");
			} 
			catch (SQLException e) {
				System.out.println(i);
				JOptionPane.showMessageDialog(null, "가격 계산중 에러발생!");
			}
		}
		return totalPrice;
	}
	
	public void getDate() throws SQLException
	{
		String sql = "select day from test";
		stmt = dbControl.prepareStatement(sql);
		rs = stmt.executeQuery();
		rs.next();
		Date date = rs.getDate("day");
		Date currentDate = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		String formattedDate = formatter.format(date);
		String formatted = formatter.format(currentDate);
		System.out.println(formattedDate.equals(formatted));
	}
	
	public void getNumber() throws SQLException
	{
		stmt = dbControl.prepareStatement("select customernumber from customer");
		rs = stmt.executeQuery();
		
		rs.next();
		System.out.println(rs.getInt("customernumber"));
	}
	
	public void textField()
	{
		JFrame newFrame = new JFrame();
		newFrame.setSize(500, 500);
		JPanel newPanel = new JPanel();
		newPanel.setSize(500, 500);
		newPanel.setLayout(null);
		JTextArea jtf = new JTextArea();
		jtf.setBounds(0, 0, 500, 500);
		newPanel.add(jtf);
		newFrame.add(newPanel);
		newFrame.setVisible(true);
		newFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		StringBuilder sb = new StringBuilder();
		String s1 = "asdfasdf";
		String s2 = "asdfasdf";
		String s3 = s1 + '\n' + s2;
		jtf.setText(s3);
	}
	
	public int addOrdered(String menuName, int tableNumber, String staffName, String customerName) throws SQLException
	{
		int result = 0;
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		Date today = new Date();
		String stringToday = formatter.format(today);

		try
		{
			System.out.println(menuName);
			String sql = "select price from menu where menuname = '" + menuName + "'";
			stmt = dbControl.prepareStatement(sql);
			rs = stmt.executeQuery();
			rs.next();
			
			int price = rs.getInt("price");
			sql = "select grade from customer where customername = '" + customerName + "'";
			stmt = dbControl.prepareStatement(sql);
			rs = stmt.executeQuery();
			rs.next();
			
			String customerGrade = rs.getString("grade");
			if (customerGrade.equals("Gold"))
			{
				price = (int) (price * 0.7);
			}
			else if (customerGrade.equals("Silver"))
			{
				price = (int) (price * 0.8);
			}
			else if (customerGrade.equals("Bronze"))
			{
				price = (int) (price * 0.9);
			}
			System.out.println(stringToday + menuName + tableNumber + staffName + customerName + price + 1);
			sql = "insert into ordered values('" + stringToday + "', '" + menuName + "', " + tableNumber + ", '" + staffName + "', '" + customerName + "', " + price + ", 1)";
			stmt = dbControl.prepareStatement(sql);
			rs = stmt.executeQuery();
			dbControl.commit();
			
			stmt.close();
			rs.close();
			result = 1;
		}
		catch (SQLException e)
		{
			result = 0;
			JOptionPane.showMessageDialog(null, "잘못된 주문입니다!");
		}
		return result;
	}

	public int pressOrdered(int tableNumber)
	{
		int result;
		try
		{
			String sql = "update ordered set flag = 1 where tablenumber = " + tableNumber + " and flag = 2";
			System.out.println(sql);
			stmt = dbControl.prepareStatement(sql);
			rs = stmt.executeQuery();
			
			dbControl.commit();
			result = 1;
		}
		catch (SQLException e)
		{
			try {
				dbControl.rollback();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			result = 0;
			JOptionPane.showMessageDialog(null, "주문 변경 실패!");
		}
		return result;
	}
	
	public int removeOrdered(int tableNumber)
	{
		int result = 0;
		String sql = "delete from ordered where tablenumber = " + tableNumber + " and flag = 1 or flag = 2";
		try {
			System.out.println(sql);
			stmt = dbControl.prepareStatement(sql);
			rs = stmt.executeQuery();
			System.out.println(sql);
			stmt.close();
			rs.close();

			dbControl.commit();
			result = 1;
		} catch (SQLException e) {
			try {
				dbControl.rollback();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			JOptionPane.showMessageDialog(null, "취소 실패!");
		}
		
		return result;
	}
	
	public String printOrdered(int tableNumber)
	{
		String concatenatedString = "";
		try
		{
			String sql = "select menuname, price from ordered where tablenumber = " + tableNumber + " and flag >= 1";
			stmt = dbControl.prepareStatement(sql);
			rs = stmt.executeQuery();
			while (rs.next())
			{
				String menuname = rs.getString("menuname");
				String price = rs.getString("price");
				
				concatenatedString += menuname + '\t' + price + '\n';
			}
			concatenatedString += "----------------------\n";
			sql = "select sum(price) from ordered where tablenumber = " + tableNumber + " and flag >= 2";
			stmt = dbControl.prepareStatement(sql);
			rs = stmt.executeQuery();
			rs.next();
			
			String total = "총 구매금액 : \t";
			
			if (rs.getString("sum(price)") == null)
			{
				total += 0;
			}
			else
			{
				total += rs.getString("sum(price)");
			}
			stmt.close();
			rs.close();
			
			concatenatedString += total;
		}
		catch (SQLException e)
		{
			concatenatedString = "";
			JOptionPane.showMessageDialog(null, "에러!");
		}
		return concatenatedString;
	}
	
	public int payOrdered(int tableNumber)
	{
		int result = 0;
		try
		{
			String sql = "select price, customername, staffname from ordered where tablenumber = " + tableNumber + "and flag = 1"; 
			stmt = dbControl.prepareStatement(sql);
			rs = stmt.executeQuery();
			rs.next();
			String customerName = "";
			String staffName = "";
			int price = 0;
			System.out.println("testssy");
			
			customerName = rs.getString("customername");
			staffName = rs.getString("staffname");
			System.out.println(customerName);
			System.out.println(staffName);
			System.out.println(price);

			price += rs.getInt("price");
			
			sql = "update ordered set flag = 0 where tablenumber = " + tableNumber + "and flag = 1";
			stmt = dbControl.prepareStatement(sql);
			rs = stmt.executeQuery();
			
			stmt.close();
			rs.close();
			updateRecord(customerName, staffName, price);
			dbControl.commit();
			result = 1;
		}
		catch (SQLException e)
		{
			try {
				dbControl.rollback();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			result = 0;
			JOptionPane.showMessageDialog(null, "결제를할 수 없습니다!");
		}
		return result;
	}
		
	public void updateRecord(String customerName, String staffName, int totalPrice)
	{
		try {
			String sql;
			if (!customerName.equals("비회원"))
			{
				sql = "update customer set purchases = purchases + " + totalPrice + " where customername = '" + customerName + "'";
	
				stmt = dbControl.prepareStatement(sql);
				rs = stmt.executeQuery();
			}
			sql = "update staff set sales = sales + " + totalPrice + " where staffname = '" + staffName + "'";
			
			stmt = dbControl.prepareStatement(sql);
			rs = stmt.executeQuery();
			
			sql = "select purchases from customer where customername = '" + customerName + "'";

			stmt = dbControl.prepareStatement(sql);
			rs = stmt.executeQuery();
			rs.next();
			int currentPurchases = rs.getInt("purchases");

			if (currentPurchases >= 1000000)
				sql = "update customer set grade = 'Gold' where customername = '" + customerName + "'";
			else if (currentPurchases >= 500000)
				sql = "update customer set grade = 'Silver' where customername = '" + customerName + "'";
			else if (currentPurchases >= 300000)
				sql = "update customer set grade = 'Bronze' where customername = '" + customerName + "'";
			
			stmt = dbControl.prepareStatement(sql);
			rs = stmt.executeQuery();
			
			stmt.close();
			rs.close();
	
			dbControl.commit();
		} catch (SQLException e) {
			try {
				dbControl.rollback();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			JOptionPane.showMessageDialog(null, "처리과정에서 오류 발생");
		}
	}
	
	public String[] resultCheck(String day)
	{
		String sql;
		String[] result = new String[4];
		
		try
		{
			sql = "select sum(price) from ordered where orderdate = '" + day + "'";
			stmt = dbControl.prepareStatement(sql);
			rs = stmt.executeQuery();
			rs.next();
			
			result[0] = rs.getString("sum(price)");
			
			sql = "select menuname from ordered group by menuname having count(menuname) >= all (select count(menuname) from ordered group by menuname)";
			stmt = dbControl.prepareStatement(sql);
			rs = stmt.executeQuery();
			rs.next();
			
			result[1] = rs.getString("menuname");
			
			sql = "select menuname from ordered group by menuname having count(menuname) <= all (select count(menuname) from ordered group by menuname)";
			stmt = dbControl.prepareStatement(sql);
			rs = stmt.executeQuery();
			rs.next();
			
			result[2] = rs.getString("menuname");

			stmt = dbControl.prepareStatement(sql);
			rs = stmt.executeQuery();
			rs.next();
			
			sql = "select sum(price) from ordered";
			stmt = dbControl.prepareStatement(sql);
			rs = stmt.executeQuery();
			rs.next();
			
			result[3] = rs.getString("sum(price)");
			
			for (int i = 0; i < 4; i++)
			{
				System.out.println(result[i]);
			}
		}
		catch (SQLException e)
		{
			JOptionPane.showMessageDialog(null, "그 날은 조회가 안됩니다!");
		}
		return result;
	}
	
	public String[] fillDateCombobox()
	{
		String[] result;
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

		try {
			String sql = "select count(orderdate) from ordered group by orderdate";

			stmt = dbControl.prepareStatement(sql);
			rs = stmt.executeQuery();
			rs.next();
			
			int countdate = rs.getInt("count(orderdate)");
			
			sql = "select orderdate from ordered group by orderdate";
			
			stmt = dbControl.prepareStatement(sql);
			rs = stmt.executeQuery();
			
			result = new String[countdate];
			int index = 0;
			
			while(rs.next())
			{
				result[index] = formatter.format(rs.getDate("orderdate"));
				index += 1;
			}
			return result;

		} catch (SQLException e) {
			result = new String[1];
			result[0] = "";
			return result;
		}
		
	}

	public void getCustomerTable()
	{
		String name[] = new String[20];
		for (int i = 0; i < 20; i++)
		{
			try {
				String sql = "select customername from ordered where tablenumber= " + (i + 1);
				System.out.println(i+1);
				stmt = dbControl.prepareStatement(sql);
				rs = stmt.executeQuery();
				rs.next();
				name[i] = rs.getString("customername");
			} catch (SQLException e) {
				name[i] = "없음";
			}
			System.out.println(name[i]);
		}
	}
	
	public boolean getColor(int tableNumber)
	{
		String sql = "select tablenumber from ordered where tablenumber = " + tableNumber + " and flag = 1 or flag = 2";
		boolean result;
		try {
			stmt = dbControl.prepareStatement(sql);
			rs = stmt.executeQuery();
			result = rs.next();

		} catch (SQLException e) {
			result = false;
		}
		
		return result;
	}

	public static void main(String[] args) throws SQLException {
		TestField tf = new TestField();
		
		tf.connectDB();
		//tf.addOrdered("로제파스타", 5, "허재", "유재석");
		//tf.pressOrdered(5);
		//tf.removeOrdered(5);
		//tf.payOrdered(5);
		//tf.resultCheck("2015-06-09");
		System.out.println(tf.getColor(4));
	}
}
