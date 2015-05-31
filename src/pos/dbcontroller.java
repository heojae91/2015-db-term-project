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
	
	public dbcontroller() throws SQLException  {
		/*
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
		*/
		
		readTextFile();
	}

	private int connectDB() throws SQLException {
		try {
			// JDBC Driver Loading
			Class.forName("oracle.jdbc.OracleDriver");
			dbControl = DriverManager.getConnection("jdbc:oracle:thin:"+
		"@localhost:1521:XE", username, password);
			JOptionPane.showMessageDialog(frame, "로그인 되었습니다!");
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
				+ "primary key	(customername),\n"
				+ ");";
		
		String staffSql = "create table staff(\n"
				+ "staffname	varchar(10)	not null,\n"
				+ "staffnumber	integer	not null,\n"
				+ "rank	varchar(10)	not null,\n"
				+ "sales	integer not null,\n"
				+ "primary key	(staffname)\n"
				+ ");";
		
		String menuSql = "create table menu(\n"
				+ "menuname	varchar(20)	not null,\n"
				+ "price	integer	not null,\n"
				+ "menunumber	integer not null,\n"
				+ "primary key	(menuname)\n"
				+ "primary key	(menunumber)\n"
				+ ");";
		
		String orderedSql = "create table ordered(\n"
				+ "ordernumber	integer not null,\n"
				+ "menuname	varchar(20) not null,\n"
				+ "tablenumber	integer not null,\n"
				+ "staffname	integer not null,\n"
				+ "customername	varchar(10) not null,\n"
				+ "primary key(ordernumber, tablenumber),\n"
				+ "foreign key(menuname) references menu,\n"
				+ "foreign key(staffname) references staff,\n"
				+ "foreign key(customername) references customer,\n"
				+ ");";
		
		String dailyresultSql = "create table dailyresult(\n"
				+ "day	date not null\n"
				+ "revenue	integer not null\n"
				+ "bestmenu	varchar(20) not null\n"
				+ "worstmenu varchar(20) not null,\n"
				+ "primary key (day),\n"
				+ ");";
		
		String sql = "";	
		try{
			 String[] tablename = {"customer","staff","menu","ordered","dailyresult"};
			 for(int i = 0; i<tablename.length; i++){		
				 //db에 이미 table이 생성이 되어있는지를 check, 없다면 생성, 있다면 생성하지 않음
				 if(checkTableExist(tablename[i]) == false){
					 System.out.println(tablename[i]);
					 sql = query[i];
					 stmt = DBconnection.prepareStatement(sql);
			    	 rs=stmt.executeQuery();	         
			         DBconnection.commit();
					}			
			 }         
	    }
	    catch(SQLException e){	    		
	    		JOptionPane.showMessageDialog(null, e);	 
		    	DBconnection.rollback();	    	
	    }finally{
	    	 stmt.close();
		     rs.close();
	    }
	}
	
	public void readTextFile()
	{
       System.out.println("Reading File from Java code");
       //Name of the file
       String fileName="C:/Users/Ashton Heo/workspace/2015-db-term-project/data.txt";
       try{

          //Create object of FileReader
          FileReader inputFile = new FileReader(fileName);

          //Instantiate the BufferedReader Class
          BufferedReader bufferReader = new BufferedReader(inputFile);

          //Variable to hold the one line data
          String line;

          // Read file line by line and print on the console
          while ((line = bufferReader.readLine()) != null)   {
            System.out.println(line);
          }
          //Close the buffer reader
          bufferReader.close();
       }catch(Exception e){
          System.out.println("Error while reading file line by line:" + e.getMessage());                      
       }

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		/*
		if (e.getSource() == loginButton) {
			username = idInput.getText();
			password = new String(pwdInput.getPassword());
			
			try {
				loginFlag = connectDB();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			if (loginFlag == 1) {
				new mainGUI().layout();
				frame.setVisible(false);
			}
			*/
		}
	
	public boolean checkTableNames(String[] tableNames) throws SQLException
	{
		boolean tableNameFlag = false;
		String sqlTableName = "select table_name from user_tables;";
		stmt = dbControl.prepareStatement(sqlTableName);
		rs = stmt.executeQuery();
		
		while (rs.next())
		{
			for (int i = 0; i < tableNames.length; i++)
			{
				if (rs.getString("table_name").equals(tableNames[i]))
				{
					tableNameFlag = true;
				}
			}
		}
		return tableNameFlag;
	}
	
	public static void main(String[] args) throws SQLException {
		new dbcontroller().readTextFile();;
	}

}