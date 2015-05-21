import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.DriverManager;
import java.sql.SQLException;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;


public class POS_GUI implements ActionListener{
	private JFrame frame = new JFrame(); // action listener 사용을 위함
	private JPanel panel = new JPanel();
	
	// 로그인 필드
	private JLabel idLabel = new JLabel("아이디");
	private JLabel pwdLabel = new JLabel("비밀번호");
	private JTextField idInput = new JTextField();
	private JPasswordField pwdInput = new JPasswordField();
	private JButton loginButton = new JButton("로그인");
	
	// 피씨 필드
	private JTextArea check_area = new JTextArea();
	private JComboBox<String> check_box = new JComboBox<String>();
	private JTextField modelInput = new JTextField();
	private JComboBox<Integer> num_box = new JComboBox<Integer>();
	private JButton buyButton = new JButton("구매");
	
	// 아이디 비번
	private String username;
	private String password;
	
	// 연결
	private static Connection dbTest;

	public POS_GUI()  {
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
		
		frame.setTitle("JDBC Practice 1");
		frame.setSize(320,130);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		
		loginButton.addActionListener(this);
	}
	
	public static void main(String[] args) {
		new JDBC_Practice1();

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == loginButton) {
			username = idInput.getText();
			password = new String(pwdInput.getPassword());
			
			connectDB();
			PCstore();
		} else if (e.getSource() == check_box) {
			try {
				showTable();
			} catch (SQLException se) {
				se.printStackTrace();
			}
		} else if (e.getSource() == buyButton) {
			try {
				
			}
		}
	}
	private void connectDB(){
		try {
			// JDBC Driver Loading
			Class.forName("oracle.jdbc.OracleDriver");
			dbTest = DriverManager.getConnection("jdbc:oracle:thin:"+
		"@localhost:1521:XE", username, password);
			JOptionPane.showMessageDialog(frame, "로긴");

		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("SQLException:"+e);
		} catch (Exception e) {
			System.out.println("Exception : "+e);
		}
	}
	
	private void PCstore() {
		frame.setVisible(false);
		frame = new JFrame();
		panel = new JPanel();
		
		panel.setFont(new Font("필기체", 1, 12));
		panel.setBorder(new TitledBorder("조회"));
		panel.setBounds(380, 80, 490, 280);
		panel.setLayout(null);
		
		check_box.addItem("PC");
		check_box.addItem("Laptop");
		check_box.addItem("Printer");
		
		check_area.setBorder(new LineBorder(Color.gray, 2));
		check_area.setEditable(false);
		
		JScrollPane scroll = new JScrollPane();
		scroll.setViewportView(check_area);
		
		for (int i = 1; i <= 10; i++) {
			num_box.addItem(i);
		}
		
		check_box.setBounds(20, 40, 70, 30);
		modelInput.setBounds(100, 40, 70, 30);
		num_box.setBounds(180, 40, 50, 30);
		scroll.setBounds(10, 80, 360, 170);
		buyButton.setBounds(300, 40, 70, 30);
		
		check_box.addActionListener(this);
		
		panel.add(check_box);
		panel.add(scroll);
		panel.add(modelInput);
		panel.add(num_box);
		panel.add(buyButton);
		
		frame.add(panel);
		
		frame.setTitle("PC Store");
		frame.setSize(400, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
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
	
	private void sellItems() throws SQLException {
		int index = 1;
		String sqlStr = "insert into intersection values("+ index
				+ (String)modelInput.getSelectedText() + (String)num_box.getSelectedItem()
				+ 
	}

}
