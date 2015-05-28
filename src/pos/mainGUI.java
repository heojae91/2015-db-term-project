package pos;

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
import javax.swing.filechooser.FileNameExtensionFilter;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Date;


public class mainGUI implements ActionListener {
	private JFrame frame = new JFrame(); // action listener 사용을 위함
	private String filePath;
	private int loginFlag = 1; // 스태프로 로그인 되어 있으면 1, 아니면 0 -> sql 파일 불러올때 사용

	// 로그인과 등록화면을 책임지는 새로운 프래임과 패널
	private JFrame secondFrame = new JFrame(); 
	private JPanel secondPanel = new JPanel();
	
	// 등록화면을 위한 새로운 친구들
	private JLabel nameLabel = new JLabel("이름넣는곳");
	private JLabel birthLabel = new JLabel("생일(4자리)");
	private JLabel contactLabel = new JLabel("연락처");
	private JLabel priceLabel = new JLabel("가격");
	private JTextField firstInput = new JTextField();
	private JTextField secondInput = new JTextField();
	private JTextField thirdInput = new JTextField();
	private JComboBox<String> choiceRank = new JComboBox<String>();
	private JButton firstButton = new JButton();
	private JButton secondButton = new JButton();
	
	// 메뉴바
	private JMenuBar mb = new JMenuBar();
	private JMenu fileMenu = new JMenu("메뉴");
	private JMenuItem loginItem = new JMenuItem("로그인");
	private JMenuItem openItem = new JMenuItem("열기");
	
	// 파일 선택
	private JFileChooser fileChooser = new JFileChooser();
	
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
	
	// 연결
	private static Connection dbTest;

	

	@Override
	public void actionPerformed(ActionEvent e) {
	}
	
	public void createLoginMenu() {
		secondPanel = new JPanel();
		secondPanel.setLayout(null);
		
		idLabel.setBounds(20,10,60,30);
		pwdLabel.setBounds(20,50,60,30);
		idInput.setBounds(100,10,80,30);
		pwdInput.setBounds(100,50,80,30);
		loginButton.setBounds(200,25,80,35);
		
		secondPanel.add(idLabel);
		secondPanel.add(pwdLabel);
		secondPanel.add(idInput);
		secondPanel.add(pwdInput);
		secondPanel.add(loginButton);
		
		secondFrame.add(secondPanel);
		
		secondFrame.setTitle("Login");
		secondFrame.setSize(320,130);
		secondFrame.setVisible(true);
		
		loginButton.addActionListener(new LoginActionListener());
	}
	
	public void createMenuBar() {
		openItem.addActionListener(new OpenActionListener());
		loginItem.addActionListener(new LoginActionListener());
		
		fileMenu.add(openItem);
		fileMenu.add(loginItem);
		
		mb.add(fileMenu);
		frame.setJMenuBar(mb);
	}
	
	public void createRegCustomer()
	{
		secondFrame = new JFrame();
		secondPanel = new JPanel();
		
		firstButton.setText("회원등록");
		secondButton.setText("취소");
		
		secondPanel.add(nameLabel);
		secondPanel.add(firstInput);
		secondPanel.add(birthLabel);
		secondPanel.add(secondInput);
		secondPanel.add(contactLabel);
		secondPanel.add(thirdInput);
		
		secondPanel.add(firstButton);
		secondPanel.add(secondButton);
		
		secondFrame.add(secondPanel);
		
		secondFrame.setVisible(true);
	}
	
	public void createRegMenu()
	{
		
	}
	public void layout() {
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
		frame.setSize(755, 1020);
		createMenuBar();
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
        	button[i].setBackground(Color.WHITE);
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
	
	class LoginActionListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == loginItem)
			{
				loginFrame = new JFrame();
				createLoginMenu();
			}
			else if (e.getSource() == loginButton)
			{
				loginFlag = 1;
				loginFrame.setVisible(false);
			}
		}
		
	}
	
	class OpenActionListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent arg0) {
			if (loginFlag == 0) {
				JOptionPane.showMessageDialog(null, "로그인 되지 않았습니다");
			}
			
			else
			{
				FileNameExtensionFilter filter = new FileNameExtensionFilter("TXT Files", "txt");
				fileChooser.setFileFilter(filter);
				
				int ret = fileChooser.showOpenDialog(null);
				if (ret != JFileChooser.APPROVE_OPTION) {
					JOptionPane.showMessageDialog(null, "파일이 선택되지 않았습니다.");
					return;
				}
				filePath = fileChooser.getSelectedFile().getPath();
			}
		}
	}
}