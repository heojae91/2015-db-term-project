import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.DriverManager;
import java.sql.SQLException;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Date;


public class main implements ActionListener{
	private JFrame frame = new JFrame(); // action listener 사용을 위함
	private JPanel panel = new JPanel();
	
	// 로그인 필드
	private JLabel idLabel = new JLabel("아이디");
	private JLabel pwdLabel = new JLabel("비밀번호");
	private JTextField idInput = new JTextField();
	private JPasswordField pwdInput = new JPasswordField();
	private JButton loginButton = new JButton("로그인");
	
	// 식당관리 필드
	private JPanel tablePanel = new JPanel();
	private JPanel orderPanel = new JPanel();
	private JPanel menuPanel = new JPanel();
	private JPanel regPanel = new JPanel();
	private JButton button[] = new JButton[20];
	
	// 메뉴 필드
	private JButton orderButton = new JButton("주문");
	private JButton cancelButton = new JButton("취소");
	private JButton payButton = new JButton("결제");
	private JTextField customerNameInput = new JTextField();
	private JComboBox<Integer> tableNameInput = new JComboBox<Integer>();
	
	// 등록 필드
	private JTabbedPane tabbedPane = new JTabbedPane();
	private JPanel customerPane = new JPanel();
	private JPanel salesPane = new JPanel();
	private JPanel workerPane = new JPanel();
	private JPanel menuPane = new JPanel();
	
	private JButton customerSignUp = new JButton("회원가입");
	private JButton customerCheck = new JButton("회원조회");
	private JButton menuSignUp = new JButton("메뉴등록");
	private JButton menuCheck = new JButton("메뉴조회");
	private JButton workerSignUp = new JButton("직원추가");
	private JButton workerCheck = new JButton("직원조회");
	private JComboBox<Date> salesComboBox = new JComboBox<Date>();
	
	private JTextField customerInput = new JTextField();
	private JTextField menuInput = new JTextField();
	private JTextField workerInput = new JTextField();
	
	private JTextField customerTextField = new JTextField();
	private JTextField salesTextField = new JTextField();
	private JTextField workerTextField = new JTextField();
	private JTextField menuTextField = new JTextField();
	
	// 아이디 비번
	private String username;
	private String password;
	
	// 연결
	private static Connection dbTest;

	public main()  {
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
	
	public static void main(String[] args) {
		new main();

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == loginButton) {
			username = idInput.getText();
			password = new String(pwdInput.getPassword());
			
			connectDB();
			layout();
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
	
	private void layout() {
		frame.setVisible(false);
		frame = new JFrame();
		frame.setLayout(null);
		
		layoutTitle();
		currentTable();
		orderTable();
		menuTable();
		regTable();
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setTitle("식당 주문관리");
		frame.setSize(755, 1000);

		frame.setVisible(true);
	}
	
	private void layoutTitle() {
		JLabel label = new JLabel("식당 주문관리");

		label.setFont(new Font("굴림", Font.BOLD,30));
		label.setHorizontalAlignment(JLabel.CENTER);
		label.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		label.setLayout(null);
		label.setBounds(10,10,720,70);
		frame.add(label);
	}
	
	private void currentTable() {
		tablePanel.setBounds(10, 80, 355, 430);
		tablePanel.setLayout(new GridLayout(5,4,10,10));
		tablePanel.setBorder(new TitledBorder("테이블"));
		tablePanel.setFont(new Font("굴림", Font.BOLD, 15));
		
        for (int i = 0; i < button.length; i++)
        {
        	button[i] = new JButton(String.valueOf(i+1));
        	button[i].addActionListener(this);
        	tablePanel.add(button[i]);
        }
        
		frame.add(tablePanel);
	}
	
	private void orderTable() {
		orderPanel.setBounds(375,80,355,430);
		orderPanel.setBorder(new TitledBorder("주문내역"));
		orderPanel.setFont(new Font("굴림", Font.BOLD, 15));
		orderPanel.setLayout(null);
		
		JTextField orderText = new JTextField();
		JLabel customerName = new JLabel("고객명");
		JLabel tableName = new JLabel("테이블명");
		
		orderText.setBounds(10, 20, 200, 400);
		customerName.setBounds(230, 40, 100, 20);
		tableName.setBounds(230, 130, 100, 20);
		
		customerNameInput.setBounds(230, 60, 100, 20);
		tableNameInput.setBounds(230,150,100,20);
		
		for (int i = 1; i <= 20; i++) {
			tableNameInput.addItem(i);
		}
		
		orderButton.setBounds(240,250,100,50);
		cancelButton.setBounds(240,310,100,50);
		payButton.setBounds(240,370,100,50);
		
		customerNameInput.addActionListener(this);
		tableNameInput.addActionListener(this);
		orderButton.addActionListener(this);
		cancelButton.addActionListener(this);
		payButton.addActionListener(this);
		
		orderPanel.add(orderText);
		orderPanel.add(customerName);
		orderPanel.add(tableName);
		orderPanel.add(orderButton);
		orderPanel.add(cancelButton);
		orderPanel.add(payButton);
		orderPanel.add(customerNameInput);
		orderPanel.add(tableNameInput);
		
		frame.add(orderPanel);

	}
	
	private void menuTable() {
		menuPanel.setBounds(10,520,355,430);
		menuPanel.setLayout(new GridLayout(10,2,10,10));
		menuPanel.setBorder(new TitledBorder("메뉴"));
		menuPanel.setFont(new Font("굴림", Font.BOLD, 15));
		
		JPanel innerMenuPanel = new JPanel();
		innerMenuPanel.setLayout(new GridLayout(2, 10, 10, 10));
		
		JButton menu[] = new JButton[20];
		for (int i = 0; i < menu.length; i++) {
			menu[i] = new JButton(String.valueOf(i+1));
			menuPanel.add(menu[i]);
		}
		
		frame.add(menuPanel);
	}
	
	private void regTable() {
		regPanel.setBounds(375,520,355,430);
		regPanel.setBorder(new TitledBorder("등록/조회"));
		regPanel.setFont(new Font("굴림", Font.BOLD, 15));
		regPanel.setLayout(null);
		
		tabbedPane.setBounds(10,20,335,400);
		tabbedPane.addTab("고객", customerPane);
		tabbedPane.addTab("매출", salesPane);
		tabbedPane.addTab("직원", workerPane);
		tabbedPane.addTab("메뉴", menuPane);
		
		customerTextField.setBounds(10,80,310,280);
		salesTextField.setBounds(10,80,310,280);
		menuTextField.setBounds(10,80,310,280);
		workerTextField.setBounds(10,80,310,280);
		
		customerInput.setBounds(10,30,100,30);
		customerSignUp.setBounds(130,30,90,30);
		customerCheck.setBounds(227,30,90,30);
		
		salesComboBox.setBounds(10,30,100,30);

		workerInput.setBounds(10,30,100,30);
		workerSignUp.setBounds(130,30,90,30);
		workerCheck.setBounds(227,30,90,30);

		menuInput.setBounds(10,30,100,30);
		menuSignUp.setBounds(130,30,90,30);
		menuCheck.setBounds(227,30,90,30);
		
		customerSignUp.addActionListener(this);
		customerCheck.addActionListener(this);
		customerInput.addActionListener(this);
		
		salesComboBox.addActionListener(this);
		
		workerSignUp.addActionListener(this);
		workerCheck.addActionListener(this);
		workerInput.addActionListener(this);
		
		menuSignUp.addActionListener(this);
		menuCheck.addActionListener(this);
		menuInput.addActionListener(this);

		customerPane.add(customerSignUp);
		customerPane.add(customerCheck);
		customerPane.add(customerTextField);
		customerPane.add(customerInput);
		customerPane.setLayout(null);
		
		salesPane.add(salesComboBox);
		salesPane.add(salesTextField);
		salesPane.setLayout(null);
		
		workerPane.add(workerSignUp);
		workerPane.add(workerCheck);
		workerPane.add(workerTextField);
		workerPane.add(workerInput);
		workerPane.setLayout(null);
		
		menuPane.add(menuSignUp);
		menuPane.add(menuCheck);
		menuPane.add(menuTextField);
		menuPane.add(menuInput);
		menuPane.setLayout(null);
		
		regPanel.add(tabbedPane);
		frame.add(regPanel);
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
}
