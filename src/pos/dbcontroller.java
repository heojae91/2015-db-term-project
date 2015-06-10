package pos;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class dbcontroller implements ActionListener {
	private String username;
	private String password;
	private int loginFlag = 1;

	private JLabel idLabel = new JLabel("���̵�");
	private JLabel pwdLabel = new JLabel("��й�ȣ");
	private JTextField idInput = new JTextField();
	private JPasswordField pwdInput = new JPasswordField();
	private JButton loginButton = new JButton("�α���");
	private JPanel panel = new JPanel();
	private JFrame frame = new JFrame();
	
	private String[] menuList = new String[20];
	
	// sql�� ������ �� �༮��
	private Connection dbControl;
	private PreparedStatement stmt;
	private ResultSet rs;
	
	public dbcontroller() throws SQLException  {
		panel.setLayout(null);
		
		idLabel.setBounds(20,10,60,30);
		pwdLabel.setBounds(20,50,60,30);
		idInput.setBounds(100,10,80,30);
		pwdInput.setBounds(100,50,80,30);
		loginButton.setBounds(200,25,80,35);
		
		panel.add(idLabel);
		panel.add(pwdLabel);
		panel.add(idInput);
		panel.add(loginButton);
		panel.add(pwdInput);

		
		frame.add(panel);
		
		frame.setTitle("Login");
		frame.setSize(320,130);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		
		loginButton.addActionListener(this);
	}

	private int connectDB() throws SQLException {
		try {
			// JDBC Driver Loading
			Class.forName("oracle.jdbc.OracleDriver");
			dbControl = DriverManager.getConnection("jdbc:oracle:thin:"+
		"@localhost:1521:XE", username, password);
			JOptionPane.showMessageDialog(frame, "�α��� �Ǿ����ϴ�!");
			createTables();
			return 1;
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, "���̵� Ȥ�� ��й�ȣ�� �߸� �Է��Ͽ����ϴ�");
			return 0;
		} catch (Exception e) {
			return 0;
		}
	}
	
	private void createTables() throws SQLException{
		String customerSql = "create table customer (\n"
				+ "customername	varchar(10)	not null,\n"
				+ "birthday	integer	not null check((birthday >= 0 and birthday<=1231) or birthday=9999),\n"
				+ "customernumber	integer not null check(customernumber<10000),\n"
				+ "contact	integer not null check (contact <= 9999 and contact >= 0),\n"
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
				+ "primary key	(menuname)\n"
				+ ")";
		
		String orderedSql = "create table ordered(\n"
				+ "orderdate	date not null,\n"
				+ "menuname	varchar(20) not null,\n"
				+ "tablenumber	integer not null,\n"
				+ "staffname	varchar(10) not null,\n"
				+ "customername	varchar(10) not null,\n"
				+ "price	integer not null,\n"
				+ "flag	integer not null check (flag = 0 or flag = 1 or flag = 2),"
				+ "foreign key(menuname) references menu,\n"
				+ "foreign key(staffname) references staff\n"
				+ ")";
		try {
			String[] tablename = { "customer", "staff", "menu", "ordered"};
			String[] sqlNames = {customerSql, staffSql, menuSql, orderedSql};
			for (int i = 0; i < tablename.length; i++) {
				// db�� �̹� table�� ������ �Ǿ��ִ����� check, ���ٸ� ����, �ִٸ� �������� ����
				if (checkTableNames(tablename[i]) == false) {
					stmt = dbControl.prepareStatement(sqlNames[i]);
					rs = stmt.executeQuery();
					
					String sql = "insert into staff values('������', 9999, 'Supervisor', 0)";
					
					stmt = dbControl.prepareStatement(sql);
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
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == loginButton) {
			username = idInput.getText();
			password = new String(pwdInput.getPassword());
			try {
				loginFlag = connectDB();
				if (loginFlag == 1) {
					new mainGUI().layout();
					frame.setVisible(false);
				}
			} catch (SQLException e1) {
				JOptionPane.showMessageDialog(null, "���̵� Ȥ�� ��й�ȣ�� �߸� �Է��Ͽ����ϴ�");
			}
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
	
	public void readTextFile(String fileName) {
		// Name of the file
		String[] splittedString = new String[0];
		
		try {
			
			// Create object of FileReader
			FileReader inputFile = new FileReader(fileName);

			// Instantiate the BufferedReader Class
			BufferedReader bufferReader = new BufferedReader(inputFile);

			// Variable to hold the one line data
			String line;

			// Read file line by line and print on the console
			
			int attributes = 0;

			while ((line = bufferReader.readLine()) != null)
			{
				if (line.matches("\\d+"))
				{
					attributes += 1;
				}
				else {
					splittedString = splitLine(line);
					switch (attributes) {
						case 1:
							//customer
							regCustomer(splittedString[0], Integer.parseInt(splittedString[1]), Integer.parseInt(splittedString[2]), splittedString[3]);
							break;
						case 2:
							//staff
							regStaff(splittedString[0], makeRandom("staff"), splittedString[1]);
							break;
						case 3:
							// menu
							stmt = dbControl.prepareStatement("select count(menuname) from menu");
							rs = stmt.executeQuery();
							rs.next();
							
							if (rs.getInt("count(menuname)") < 20)
								regMenu(splittedString[0], splittedString[1]);
							break;
					}
				}
			}
			// Close the buffer reader
			bufferReader.close();
		}
		catch (Exception e) {
			System.out.println("Error while reading file line by line:"
				+ e.getMessage());
		}

	}
	
	public String[] splitLine(String line)
	{
		String[] splittedStrings;
		splittedStrings = line.split("\t");
		return splittedStrings;
	}
	
	public int makeRandom(String type) throws SQLException
	{
		int random = (int)(Math.random() * 10000);
		while (findDuplicated(type, random))
		{
			random = (int)(Math.random() * 10000);
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
			finally
			{
				stmt.close();
				rs.close();
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
			return true; // staff Ȥ�� customer �̿��� ���� ������ ���
		}

		return flag;
	}

	public void regCustomer(String customerName, int birthday, int contact, String grade)
	{
		try 
		{
			int currentPurchases;
			
			if (grade.equals("Gold"))
				currentPurchases = 1000000;
			else if (grade.equals("Silver"))
				currentPurchases = 500000;
			else if (grade.equals("Bronze"))
				currentPurchases = 300000;
			else
				currentPurchases = 0;
				
			String sql = "insert into customer values('" + customerName + "', " + birthday + ", " + makeRandom("customer") + ", " + contact + ", '" + grade + "'," + currentPurchases + ")";
			stmt = dbControl.prepareStatement(sql);
			rs = stmt.executeQuery();
			dbControl.commit();
		}
		catch (SQLException e) // �̸��� �ߺ��ǰų� ��ȣ�� 10000���� �Ѿ�� ���� �߻�
		{
			try {
				dbControl.rollback();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			JOptionPane.showMessageDialog(null, "�߸��� �Է��Դϴ�!");
		}
	}
	
	public void regCustomer(String customerName, int birthday, int contact)
	{
		try 
		{
			String sql = "insert into customer values('" + customerName + "', " + birthday + ", " + makeRandom("customer") + ", " + contact + ", 'Normal', 0)";
			stmt = dbControl.prepareStatement(sql);
			rs = stmt.executeQuery();
			dbControl.commit();
		}
		catch (SQLException e) // �̸��� �ߺ��ǰų� ��ȣ�� 10000���� �Ѿ�� ���� �߻�
		{
			try {
				dbControl.rollback();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			JOptionPane.showMessageDialog(null, "�߸��� �Է��Դϴ�!");
		}
	}

	
	public void regStaff(String staffName, String rank)
	{
		try 
		{
			String sql = "insert into staff values('" + staffName + "', " + makeRandom("staff") + ", '" + rank + "', 0)";
			stmt = dbControl.prepareStatement(sql);
			rs = stmt.executeQuery();
			dbControl.commit();
			JOptionPane.showMessageDialog(new JFrame(), "�Է� �Ϸ�!");
		}
		catch (SQLException e) // �̸��� �ߺ��ǰų� ��ȣ�� 10000���� �Ѿ�� ���� �߻�
		{
			try {
				dbControl.rollback();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			JOptionPane.showMessageDialog(null, "�߸��� �Է��Դϴ�!");
		}
	}
	
	public void regStaff(String staffName, int staffnumber, String rank)
	{
		try 
		{
			String sql = "insert into staff values('" + staffName + "', " + staffnumber + ", '" + rank + "', 0)";
			stmt = dbControl.prepareStatement(sql);
			rs = stmt.executeQuery();
			dbControl.commit();
		}
		catch (SQLException e) // �̸��� �ߺ��ǰų� ��ȣ�� 10000���� �Ѿ�� ���� �߻�
		{
			try {
				dbControl.rollback();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			JOptionPane.showMessageDialog(null, "�߸��� �Է��Դϴ�!");
		}
	}
	
	public int regMenu(String menuName, String price)
	{
		int result = 0;
		try
		{
			String countSql = "select count(menuName) from menu";
			stmt = dbControl.prepareStatement(countSql);
			rs = stmt.executeQuery();
			rs.next();
			
			if (rs.getInt("count(menuname)") < 20)
			{
				String sql = "insert into menu values('" + menuName + "', " + price + ")";
				stmt = dbControl.prepareStatement(sql);
				rs = stmt.executeQuery();
				stmt.close();
				rs.close();
				dbControl.commit();
				result = 1;
				JOptionPane.showMessageDialog(null, "�Է� �Ϸ�!");
			}
			else
			{
				JOptionPane.showMessageDialog(null, "�޴��� ���� �ʹ� �����ϴ�!");
			}
		}
		catch (SQLException e)
		{
			try {
				dbControl.rollback();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			JOptionPane.showMessageDialog(null, "�߸��� �Է��Դϴ�!");
		}
		return result;
	}
	
	public String[] getMenu()
	{
		int menuIndex = 0;
		String sql = "select menuname from menu";
		try {
			stmt = dbControl.prepareStatement(sql);
			rs = stmt.executeQuery();
			
			for (int i = 0; i < 20; i++)
			{
				menuList[i] = "";
			}
			
			while (rs.next())
			{
				menuList[menuIndex] = rs.getString("menuname");
				menuIndex += 1;
			}
		}
		catch (SQLException e)
		{
			JOptionPane.showMessageDialog(null, "�޴��� �ҷ����µ� �����߽��ϴ�!");
		}
		
		return menuList;
	}
	
	public int staffLogin(String id, String pwd)
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
				{
					staffLoginFlag = 2;
					JOptionPane.showMessageDialog(null, "Supervisor�� �α��� �Ǽ̽��ϴ�");
				}
				else
				{
					staffLoginFlag = 1;
					JOptionPane.showMessageDialog(null, "Staff�� �α��� �Ǽ̽��ϴ�");
				}
			}
			else
			{
				JOptionPane.showMessageDialog(null, "�߸��� ȸ�������Դϴ�!");
			}
		}
		catch (SQLException e)
		{
			JOptionPane.showMessageDialog(null, "�߸��� ȸ�������Դϴ�!"); // �̸��� ���ų� ȸ����ȣ�� ���� �ʴ� ���
		}
		return staffLoginFlag;
	}
	
	public int addOrdered(String menuName, int tableNumber, String staffName, String customerName)
	{
		int result = 0;
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		Date today = new Date();
		String stringToday = formatter.format(today);

		try
		{
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
			sql = "insert into ordered values('" + stringToday + "', '" + menuName + "', " + tableNumber + ", '" + staffName + "', '" + customerName + "', " + price + ", 2)";
			stmt = dbControl.prepareStatement(sql);
			rs = stmt.executeQuery();
			dbControl.commit();
			
			stmt.close();
			rs.close();
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
			JOptionPane.showMessageDialog(null, "�߸��� �ֹ��Դϴ�!");
		}
		return result;
	}
	public boolean checkTable(int tableNumber)
	{
		boolean result;
		try
		{
			String sql = "select tablenumber from ordered where tablenumber = " + tableNumber + "and flag >= 1";
			stmt = dbControl.prepareStatement(sql);
			rs = stmt.executeQuery();
			rs.next();
			stmt.close();
			rs.close();
			result = true;
		}
		catch (SQLException e)
		{
			JOptionPane.showMessageDialog(null, "���̺��� ����ֽ��ϴ�");
			result = false;
		}
		return result;
	}
	
	public int pressOrdered(int tableNumber)
	{
		int result;
		try
		{
			if (checkTable(tableNumber)) {
				String sql = "update ordered set flag = 1 where tablenumber = " + tableNumber + " and flag = 2";
				stmt = dbControl.prepareStatement(sql);
				rs = stmt.executeQuery();
				
				stmt.close();
				rs.close();
				dbControl.commit();
				
				result = 1;
			}
			else 
				result = 0;
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
			JOptionPane.showMessageDialog(null, "�ֹ� ���� ����!");
		}
		return result;
	}
	
	public int removeOrdered(int tableNumber)
	{
		int result = 0;
		try {
			if (checkTable(tableNumber))
			{
				String sql = "delete from ordered where tablenumber = " + tableNumber + " and flag = 1 or flag = 2";

				stmt = dbControl.prepareStatement(sql);
				rs = stmt.executeQuery();
				
				stmt.close();
				rs.close();
	
				dbControl.commit();
				result = 1;
			}
		} catch (SQLException e) {
			try {
				dbControl.rollback();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			JOptionPane.showMessageDialog(null, "��� ����!");
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
			sql = "select sum(price) from ordered where tablenumber = " + tableNumber + " and flag >= 1";
			stmt = dbControl.prepareStatement(sql);
			rs = stmt.executeQuery();
			rs.next();
			
			String total = "�� ���űݾ� : \t";
			
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
			JOptionPane.showMessageDialog(null, "����!");
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
			
			customerName = rs.getString("customername");
			staffName = rs.getString("staffname");
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
			JOptionPane.showMessageDialog(null, "�������� �� �����ϴ�!");
		}
		return result;
	}
		
	public void updateRecord(String customerName, String staffName, int totalPrice)
	{
		try {
			String sql;
			if (!customerName.equals("��ȸ��"))
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
			JOptionPane.showMessageDialog(null, "ó���������� ���� �߻�");
		}
	}
	
	public String[] customerCheck(String customerName)
	{
		String sql = "select * from customer where customername = '" + customerName + "'";
		String[] result;

		try {

			stmt = dbControl.prepareStatement(sql);
			rs = stmt.executeQuery();
			rs.next();
			result = new String[6];
			result[0] = rs.getString("customername");
			result[1] = rs.getString("customernumber");
			result[2] = rs.getString("birthday");
			result[3] = rs.getString("contact");
			result[4] = rs.getString("grade");
			result[5] = rs.getString("purchases");
			
		} catch (SQLException e) {
			result = new String[1];
			result[0] = "�˻���� ����!";
			JOptionPane.showMessageDialog(null, "���� �߻�!");
		}
		
		return result;
	}
	
	public String[] staffCheck(String staffName)
	{
		String sql = "select * from staff where staffname = '" + staffName + "'";
		String[] result;
		try {
			stmt = dbControl.prepareStatement(sql);
			rs = stmt.executeQuery();
			rs.next();
			result = new String [3];
			result[0] = rs.getString("staffname");
			result[1] = rs.getString("rank");
			result[2] = rs.getString("sales");
			
		} catch (SQLException e) {
			result = new String[1];
			result[0] = "�˻���� ����!";
			JOptionPane.showMessageDialog(null, "���� �߻�!");
		}
		
		return result;
	}
	
	public String[] menuCheck(String menuName)
	{
		String sql = "select * from menu where menuname = '" + menuName + "'";
		String[] result;
		
		try
		{
			stmt = dbControl.prepareStatement(sql);
			rs = stmt.executeQuery();
			rs.next();
			result = new String[2];
			result[0] = rs.getString("menuname");
			result[1] = rs.getString("price");
		}
		catch (SQLException e)
		{
			result = new String[1];
			result[0] = "�˻���� ����!";
			JOptionPane.showMessageDialog(null, "���� �߻�!");
		}
		return result;
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
		}
		catch (SQLException e)
		{
			JOptionPane.showMessageDialog(null, "�� ���� ��ȸ�� �ȵ˴ϴ�!");
		}
		return result;
	}
	
	public String[] fillDateCombobox()
	{
		String[] result;
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

		try {
			String sql = "select count(od) from (select orderdate od from ordered group by orderdate)";

			stmt = dbControl.prepareStatement(sql);
			rs = stmt.executeQuery();
			rs.next();
			
			int countdate = rs.getInt("count(od)");
			
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
			stmt.close();
			rs.close();
			return result;

		} catch (SQLException e) {
			result = new String[1];
			result[0] = "";
			return result;
		}
	}
	
	public String[] getCustomerTable()
	{
		String name[] = new String[20];
		for (int i = 0; i < 20; i++)
		{
			try {
				String sql = "select customername from ordered where tablenumber= " + (i + 1) + "and flag >= 1";
				stmt = dbControl.prepareStatement(sql);
				rs = stmt.executeQuery();
				rs.next();
				name[i] = rs.getString("customername");
				stmt.close();
				rs.close();
			} catch (SQLException e) {
				name[i] = "";
			}
		}
		return name;
	}
	
	public boolean getColor(int tableNumber)
	{
		String sql = "select tablenumber from ordered where tablenumber = " + tableNumber + " and flag >= 1";
		boolean result;
		try {
			stmt = dbControl.prepareStatement(sql);
			rs = stmt.executeQuery();
			rs.next();
			
			result = rs.getInt("tablenumber") == tableNumber;
			stmt.close();
			rs.close();
			
			return result;
		} catch (SQLException e) {
			result = false;
		}
		
		return result;
		}
}