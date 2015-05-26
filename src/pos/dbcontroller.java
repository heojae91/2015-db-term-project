package pos;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
	
	private Connection dbControl;
	
	public dbcontroller()  {
		panel.setLayout(null);
		
		idLabel.setBounds(20,10,60,30);
		pwdLabel.setBounds(20,50,60,30);
		idInput.setBounds(100,10,80,30);
		pwdInput.setBounds(100,50,80,30);
		loginButton.setBounds(200,25,80,35);
		
		panel.add(idLabel);
		panel.add(pwdLabel);
		panel.add(idInput);
		panel.add(pwdInput);
		panel.add(loginButton);
		
		frame.add(panel);
		
		frame.setTitle("Login");
		frame.setSize(320,130);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		
		loginButton.addActionListener(this);
	}

	private int connectDB(){
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
				+ "customername	varchar(10)	not null\n"
				+ "birthday	integer not null check(birthday<=1231 or birthday=9999)\n"
				+ "customernumber	integer not null check(customernumber<10000)\n"
				+ "customergrade	varchar(10) not null\n"
				+ "primary key	customername\n"
				+ ");";
		String staffSql = "create table staff(\n"
				+ "staffname	varchar(10)	not null\n"
				+ "staffnumber	integer	not null\n"
				+ "staffgrade	varchar(10)	not null\n"
				+ "primary key	staffname\n"
				+ ");";
		String menuSql = "create table menu(\n"
				+ "menuname	varchar(20)	not null\n"
				+ "price	integer	not null\n"
				+ "primary key	menuname\n"
				+ ");";
		
	}

	
	private void showTable() throws SQLException {
		String specification = "";
		
		String sqlStr = "select count(column_name) num from cols where table_name = '"
				+ ((String) check_box.getSelectedItem()).toUpperCase() + "'";
		PreparedStatement stmt = dbTest.prepareStatement(sqlStr);
		ResultSet rs = stmt.executeQuery();
		
		rs.next();
		int number = rs.getInt("num");
		String[] tables = new String[number];
		
		sqlStr = "select column_name from cols where table_name = '"
				+ ((String) check_box.getSelectedItem()).toUpperCase() + "'";
		stmt = dbTest.prepareStatement(sqlStr);
		rs = stmt.executeQuery();
		
		for (number = 0; rs.next(); number ++) {
			tables[number] = rs.getString("column_name");
			specification += tables[number] + '\t';
		}
		
		for (specification += "\n"; number > 0; number--) {
			specification += "----------------------";
		}
		specification += "\n";
		
		sqlStr = "select * from " + (String) check_box.getSelectedItem();
		stmt = dbTest.prepareStatement(sqlStr);
		rs = stmt.executeQuery();
		
		for (number = 0; rs.next(); number++) {
			for (int i = 0; i < tables.length; i++) {
				specification += rs.getString(tables[i]) + '\t';
			}
			specification += "\n";
		}
		
		check_area.setText(specification);
		
		rs.close();
		stmt.close();
	}



	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == loginButton) {
			username = idInput.getText();
			password = new String(pwdInput.getPassword());
			
			int loginFlag = connectDB();
			
			if (loginFlag == 1) {
				new mainGUI().layout();;
			}
		}
	}
	
	public static void main(String[] args) {
		new dbcontroller();
	}

}