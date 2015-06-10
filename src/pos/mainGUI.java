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

import pos.subGUI.LoginActionListener;
import pos.subGUI.RegCustomerActionListener;
import pos.subGUI.RegMenuActionListener;
import pos.subGUI.RegStaffActionListener;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Date;


public class mainGUI implements ActionListener {
	int staffLoginFlag;
	private static dbcontroller dbc;
	private JPanel panel = new JPanel();
	
	private JFrame frame = new JFrame(); // action listener 사용을 위함
	private JFrame secondaryFrame = new JFrame();
	private String filePath;
	private int loginFlag; // 스태프로 로그인 되어 있으면 1, 아니면 0 -> sql 파일 불러올때 사용
	private int currentTable; // 현재 입력된 테이블 정보
	private String currentStaff;
	private String currentMenu; // 선택된 메뉴 정보

	private String[] customerTable;
	
	// 메뉴바
	private JMenuBar mb = new JMenuBar();
	private JMenu fileMenu = new JMenu("메뉴");
	private JMenuItem openItem = new JMenuItem("열기");
	private JMenuItem loginItem = new JMenuItem("로그인");

	// 파일 선택
	private JFileChooser fileChooser = new JFileChooser();
	
	
	// 식당관리 필드
	private JPanel tablePanel = new JPanel();
	private JPanel orderPanel = new JPanel();
	private JPanel menuPanel = new JPanel();
	private JPanel regPanel = new JPanel();
	private JButton tableButton[] = new JButton[20];
	
	// 메뉴 필드
	private JButton orderButton = new JButton("주문");
	private JButton cancelButton = new JButton("취소");
	private JButton payButton = new JButton("결제");
	private JTextField customerNameInput = new JTextField();
	private JComboBox<Integer> tableNameInput = new JComboBox<Integer>();
	private JButton menuButton[] = new JButton[20];
	
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
	private JComboBox<String> salesComboBox = new JComboBox<String>();
	
	private JTextField customerInput = new JTextField();
	private JTextField menuInput = new JTextField();
	private JTextField workerInput = new JTextField();
	
	private JTextArea customerTextField = new JTextArea();
	private JTextArea salesTextField = new JTextArea();
	private JTextArea workerTextField = new JTextArea();
	private JTextArea menuTextField = new JTextArea();
	
	private JLabel nameLabel = new JLabel("이름넣는곳");
	private JLabel birthLabel = new JLabel("생일(4자리)");
	private JLabel contactLabel = new JLabel("연락처");
	private JLabel priceLabel = new JLabel("가격");
	private JLabel rankLabel = new JLabel("직급");
	private JTextField firstInput = new JTextField();
	private JTextField secondInput = new JTextField();
	private JTextField thirdInput = new JTextField();
	private JComboBox<String> choiceRank = new JComboBox<String>();
	private JButton regAcceptButton = new JButton();
	private JButton regCancelButton = new JButton();

	private JTextArea orderText = new JTextArea();
	
	// 로그인 필드
	private JLabel idLabel = new JLabel("아이디");
	private JLabel pwdLabel = new JLabel("비밀번호");
	private JTextField idInput = new JTextField();
	private JPasswordField pwdInput = new JPasswordField();
	private JButton loginButton = new JButton("로그인");

	@Override
	public void actionPerformed(ActionEvent e) {
		
	}
		
	public void createMenuBar() {
		openItem.addActionListener(new MenuBarActionListener());
		loginItem.addActionListener(new MenuBarActionListener());
		
		
		fileMenu.add(openItem);
		fileMenu.add(loginItem);
		
		mb.add(fileMenu);
		frame.setJMenuBar(mb);
	}
	
	public void layout() throws SQLException {
		
		frame.setVisible(false);
		frame = new JFrame();
		frame.setLayout(null);
		
		layoutTitle();
		currentTable();
		orderTable();
		menuTable();
		regTable();
		createMenuBar();
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setTitle("식당 주문관리");
		frame.setSize(655, 820);
		frame.setVisible(true);
		}	
	
	private void layoutTitle() {
		JLabel label = new JLabel("식당 주문관리");

		label.setFont(new Font("굴림", Font.BOLD,30));
		label.setHorizontalAlignment(JLabel.CENTER);
		label.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		label.setLayout(null);
		label.setBounds(10,10,620,60);
		frame.add(label);
	}
	
	private void currentTable() {
		tablePanel.setBounds(10, 80, 305, 330);
		tablePanel.setLayout(new GridLayout(5,4,10,10));
		tablePanel.setBorder(new TitledBorder("테이블"));
		tablePanel.setFont(new Font("굴림", Font.BOLD, 15));
		
        for (int i = 0; i < tableButton.length; i++)
        {
        	tableButton[i] = new JButton(String.valueOf(i+1));
        	tableButton[i].addActionListener(new TableActionListener());
        	if (dbc.getColor(i + 1))
        		tableButton[i].setBackground(Color.YELLOW); 
        	else
        		tableButton[i].setBackground(Color.WHITE);
        	tablePanel.add(tableButton[i]);
        }
        
		frame.add(tablePanel);
	}
	
	private void orderTable() {
		orderPanel.setBounds(325,80,305,330);
		orderPanel.setBorder(new TitledBorder("주문내역"));
		orderPanel.setFont(new Font("굴림", Font.BOLD, 15));
		orderPanel.setLayout(null);
		
		orderText = new JTextArea();
		JLabel customerName = new JLabel("고객명");
		JLabel tableName = new JLabel("테이블명");
		
		orderText.setBounds(10, 20, 180, 300);
		customerName.setBounds(200, 20, 100, 20);
		tableName.setBounds(200, 70, 100, 20);
		
		customerNameInput.setBounds(200, 40, 90, 20);
		tableNameInput.setBounds(200,100,90,20);
		
		for (int i = 1; i <= 20; i++) {
			tableNameInput.addItem(i);
		}
		tableNameInput.addActionListener(new TableComboBoxActionListener());
		orderButton.setBounds(200,150,90,50);
		cancelButton.setBounds(200,210,90,50);
		payButton.setBounds(200,270,90,50);
		
		customerNameInput.addActionListener(new OrderActionListener());
		tableNameInput.addActionListener(new OrderActionListener());
		orderButton.addActionListener(new OrderActionListener());
		cancelButton.addActionListener(new OrderActionListener());
		payButton.addActionListener(new OrderActionListener());
		
		orderPanel.add(orderText);
		orderPanel.add(customerName);
		orderPanel.add(tableName);
		orderPanel.add(orderButton);
		orderPanel.add(cancelButton);
		orderPanel.add(payButton);
		orderPanel.add(customerNameInput);
		orderPanel.add(tableNameInput);
		
		customerTable = dbc.getCustomerTable();
		
		frame.add(orderPanel);
	}

	private void menuTable() throws SQLException {
		menuPanel.setBounds(10,420,305,330);
		menuPanel.setLayout(new GridLayout(10,2,10,10));
		menuPanel.setBorder(new TitledBorder("메뉴"));
		menuPanel.setFont(new Font("굴림", Font.BOLD, 15));
		
		JPanel innerMenuPanel = new JPanel();
		innerMenuPanel.setLayout(new GridLayout(2, 10, 10, 10));
		
		menuButton = new JButton[20];
		String[] menuList = dbc.getMenu();
		for (int i = 0; i < menuButton.length; i++) {
			menuButton[i] = new JButton();
			menuButton[i].setText(menuList[i]);
			menuButton[i].addActionListener(new MenuButtonActionListener());
			menuPanel.add(menuButton[i]);
		}
		
		frame.add(menuPanel);
	}
	
	private void regTable() {
		regPanel.setBounds(325,420,305,330);
		regPanel.setBorder(new TitledBorder("등록/조회"));
		regPanel.setFont(new Font("굴림", Font.BOLD, 15));
		regPanel.setLayout(null);
		
		tabbedPane.setBounds(10,20,285,300);
		tabbedPane.addTab("고객", customerPane);
		tabbedPane.addTab("매출", salesPane);
		tabbedPane.addTab("직원", workerPane);
		tabbedPane.addTab("메뉴", menuPane);
		
		customerTextField.setBounds(10,45,265,220);
		salesTextField.setBounds(10,45,265,220);
		menuTextField.setBounds(10,45,265,220);
		workerTextField.setBounds(10,45,265,220);
		
		customerTextField.setBorder(BorderFactory.createLineBorder(Color.black));
		salesTextField.setBorder(BorderFactory.createLineBorder(Color.black));
		menuTextField.setBorder(BorderFactory.createLineBorder(Color.black));
		workerTextField.setBorder(BorderFactory.createLineBorder(Color.black));

		customerInput.setBounds(10,10,70,30);
		customerSignUp.setBounds(88,10,90,30);
		customerCheck.setBounds(183,10,90,30);
		
		salesComboBox.setBounds(10,10,100,30);

		workerInput.setBounds(10,10,70,30);
		workerSignUp.setBounds(88,10,90,30);
		workerCheck.setBounds(183,10,90,30);

		menuInput.setBounds(10,10,70,30);
		menuSignUp.setBounds(88,10,90,30);
		menuCheck.setBounds(183,10,90,30);
		
		customerSignUp.addActionListener(new RegTableActionListener());
		customerCheck.addActionListener(new RegTableActionListener());
		customerInput.addActionListener(new RegTableActionListener());
		
		salesComboBox.addActionListener(new ShowResultActionListener());
		
		workerSignUp.addActionListener(new RegTableActionListener());
		workerCheck.addActionListener(new RegTableActionListener());
		workerInput.addActionListener(new RegTableActionListener());
		
		menuSignUp.addActionListener(new RegTableActionListener());
		menuCheck.addActionListener(new RegTableActionListener());
		menuInput.addActionListener(new RegTableActionListener());

		customerPane.add(customerSignUp);
		customerPane.add(customerCheck);
		customerPane.add(customerTextField);
		customerPane.add(customerInput);
		customerPane.setLayout(null);
		
		salesPane.add(salesComboBox);
		salesPane.add(salesTextField);
		salesPane.setLayout(null);
		
		String[] comboBox = dbc.fillDateCombobox();
		salesComboBox.addItem("");

		for (int i = 0; i < comboBox.length; i++)
		{
			salesComboBox.addItem(comboBox[i]);
		}

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
	
	class TableActionListener implements ActionListener // 테이블관리 
	{
		public void actionPerformed(ActionEvent e)
		{
			for (int i = 0; i < 20; i++) {
				if (!tableButton[i].getBackground().equals(Color.YELLOW))
				{
					tableButton[i].setBackground(Color.WHITE);
				}
				tableButton[i].setBorder(BorderFactory.createLineBorder(Color.BLACK));
			}

			for (int i = 0; i < 20; i++)
			{
				if (e.getSource() == tableButton[i])
				{
					orderText.setText(dbc.printOrdered(i+1));
					currentTable = i+1;
					
					String customerName = customerTable[i];
					customerNameInput.setText(customerName);

					if (customerName.equals(""))
					{
						customerNameInput.setText("");
						customerName = customerNameInput.getText();
						customerTable[currentTable - 1] = customerName;
					}

					if (!tableButton[i].getBackground().equals(Color.YELLOW))
						tableButton[i].setBackground(Color.ORANGE);
					else
						tableButton[i].setBorder(BorderFactory.createLineBorder(Color.RED));
					e.setSource(null);
				}
			}
		}
	}
	
	
	class MenuBarActionListener implements ActionListener { // 메뉴바 

		@Override
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == loginItem)
			{
				createLoginMenu();
				e.setSource(null);
				return;
			}
			
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
				menuPanel.repaint();
				dbc.readTextFile(filePath);
				String[] menuList;
				menuList = dbc.getMenu();
				for (int i = 0; i < menuButton.length; i++) {
					menuButton[i].setText(menuList[i]);
					menuButton[i].addActionListener(new MenuButtonActionListener());
					menuPanel.add(menuButton[i]);
				}

				frame.repaint();

				menuPanel.repaint();

			}
		}
	}
	
	class OrderActionListener implements ActionListener // 메뉴주문 
	{
		public void actionPerformed(ActionEvent e)
		{	
			if (staffLoginFlag == 0)
				JOptionPane.showMessageDialog(null, "일단 로그인 먼저 하세요");
			else {
				int orderFlag = 0;
				int payFlag = 0;
				int cancelFlag = 0;
				if (e.getSource() == orderButton)
				{
					String customerName = customerNameInput.getText();
					
					orderFlag = dbc.pressOrdered(currentTable);
					if (orderFlag == 1)
					{
						tableButton[currentTable - 1].setBackground(Color.YELLOW);
					}
					
					customerTable[currentTable - 1] = customerName;
					
					e.setSource(null);
				}
				else if (e.getSource() == cancelButton)
				{
					cancelFlag = dbc.removeOrdered(currentTable);
					if (cancelFlag == 1)
						tableButton[currentTable - 1].setBackground(Color.WHITE);
					else
					{
						JOptionPane.showMessageDialog(null, "테이블이 비어있습니다");
					}
					customerTable[currentTable - 1] = "";
					e.setSource(null);
				}
				else if (e.getSource() == payButton)
				{
					payFlag = dbc.payOrdered(currentTable);
					
					salesComboBox.setVisible(false);
					salesComboBox = new JComboBox<String>();
					String[] comboBox = dbc.fillDateCombobox();
					salesComboBox.addItem("");

					for (int i = 0; i < comboBox.length; i++)
					{
						salesComboBox.addItem(comboBox[i]);
					}
					salesComboBox.addActionListener(new ShowResultActionListener());
					salesComboBox.setBounds(10,10,100,30);
					salesPane.add(salesComboBox);
					salesComboBox.setVisible(true);
					
					if (payFlag == 1)
					{
						tableButton[currentTable - 1].setBackground(Color.WHITE);
						tableButton[currentTable - 1].setBorder(BorderFactory.createLineBorder(Color.BLACK));
					}
					customerTable[currentTable - 1] = "";
					
					e.setSource(null);
				}
			}
		}
	}
	
	class RegTableActionListener implements ActionListener // 주문관리
	{
		public void actionPerformed(ActionEvent e)
		{
			if (e.getSource() == customerSignUp)
			{
				createRegCustomer();
				e.setSource(null);
			}
			else if (e.getSource() == customerCheck)
			{
				String currentCustomer = customerInput.getText();
				customerInput.setText("");
				String[] result = dbc.customerCheck(currentCustomer);
				if (result.length == 1)
				{
					customerTextField.setText(result[0]);
				}
				else
				{
					String str = "고객명 : " + result[0] + '\n' + "고객ID : " + result[1] + '\n' + "생일 : " + result[2] + '\n' + "전화번호 : " + result[3] + '\n' + "고객등급 : " + result[4]
							+ '\n' + "총 구매금액 : " + result[5];
					customerTextField.setText(str);
				}
				e.setSource(null);
			}
			else if (e.getSource() == workerSignUp)
			{
				createRegStaff();
				e.setSource(null);
			}
			else if (e.getSource() == workerCheck)
			{
				String currentStaff = workerInput.getText();
				workerInput.setText("");
				String[] result = dbc.staffCheck(currentStaff);
				if (result.length == 1)
				{
					workerTextField.setText(result[0]);
				}
				else
				{
					String str = "직원명 : " + result[0] + '\n' + "직급 : " + result[1] + '\n' +  "총 실적 : " + result[2];
					workerTextField.setText(str);
				}
				e.setSource(null);
			}
			else if (e.getSource() == menuSignUp)
			{
				createRegMenu();
				e.setSource(null);
			}
			else if (e.getSource() == menuCheck)
			{
				String currentMenu = menuInput.getText();
				menuInput.setText("");
				String[] result = dbc.menuCheck(currentMenu);
				if (result.length == 1)
				{
					menuTextField.setText(result[0]);
				}
				else
				{
					String str = "메뉴명 : " + result[0] + '\n' + "가격 : " + result[1];
					menuTextField.setText(str);
				}
				e.setSource(null);
			}
		}
	}
	
	class TableComboBoxActionListener implements ActionListener // 테이블 콤보박스 
	{
		public void actionPerformed(ActionEvent e)
		{
			currentTable = (int)tableNameInput.getSelectedItem();
			for (int i = 0; i < 20; i++)
			{
				if (!tableButton[i].getBackground().equals(Color.yellow))
				{
					tableButton[i].setBackground(Color.white);
				}
				tableButton[i].setBorder(BorderFactory.createLineBorder(Color.BLACK));

			}
			if (!tableButton[currentTable - 1].getBackground().equals(Color.YELLOW))
			{
				tableButton[currentTable - 1].setBackground(Color.ORANGE);
			}
			else
			{
				tableButton[currentTable - 1].setBorder(BorderFactory.createLineBorder(Color.RED));
			}
		}
	}
	
	class MenuButtonActionListener implements ActionListener // 메뉴버튼 
	{
		public void actionPerformed(ActionEvent e)
		{
			for (int i = 0; i < 20; i++)
			{
				if (e.getSource() == menuButton[i])
				{
					if (staffLoginFlag >= 1)
					{
						currentMenu = menuButton[i].getText();
						String customerName = customerNameInput.getText();
						int menuFlag = dbc.addOrdered(currentMenu, currentTable, currentStaff, customerName);
						if (menuFlag == 1)
						{
							String order = dbc.printOrdered(currentTable);
							orderText.setText(order);
						}
						e.setSource(null);
					}
					else
						JOptionPane.showMessageDialog(null, "일단 로그인먼저!");
				}
			}
		}
	}

	
	public static void main(String[] args) throws SQLException {
		dbc = new dbcontroller();
	}

	public void createLoginMenu() {
		secondaryFrame = new JFrame();
		panel = new JPanel();
		idInput = new JTextField();
		pwdInput = new JPasswordField();
		
		panel.setLayout(null);
		
		idInput.setText("");
		pwdInput.setText("");
		
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
		
		secondaryFrame.add(panel);	
		secondaryFrame.setTitle("Login");
		secondaryFrame.setSize(320,130);
		secondaryFrame.setVisible(true);
		
		loginButton.addActionListener(new LoginActionListener());
	}
	
	public void createRegCustomer()
	{
		secondaryFrame = new JFrame();
		panel = new JPanel();
		
		firstInput.setText("");
		secondInput.setText("");
		thirdInput.setText("");

		panel.setLayout(new GridLayout(4,2,5,5));
		secondaryFrame.setTitle("메뉴 등록");
		secondaryFrame.setSize(320,200);

		regAcceptButton.setText("회원등록");
		regCancelButton.setText("취소");
		
		nameLabel.setText("이름");
		nameLabel.setHorizontalAlignment(JLabel.CENTER);
		birthLabel.setHorizontalAlignment(JLabel.CENTER);
		contactLabel.setHorizontalAlignment(JLabel.CENTER);
		
		panel.add(nameLabel);
		panel.add(firstInput);
		panel.add(birthLabel);
		panel.add(secondInput);
		panel.add(contactLabel);
		panel.add(thirdInput);
		
		panel.add(regAcceptButton);
		panel.add(regCancelButton);
		
		secondaryFrame.add(panel);
		
		regAcceptButton.addActionListener(new RegCustomerActionListener());
		regCancelButton.addActionListener(new RegCustomerActionListener());
		
		secondaryFrame.setVisible(true);
	}
	
	public void createRegMenu()
	{
		secondaryFrame = new JFrame();
		panel = new JPanel();
		
		firstInput.setText("");
		secondInput.setText("");

		panel.setLayout(new GridLayout(3,2,5,5));
		secondaryFrame.setTitle("메뉴 등록");
		secondaryFrame.setSize(320,170);
		
		regAcceptButton.setText("등록");
		regCancelButton.setText("취소");
		
		nameLabel.setText("메뉴이름");
		nameLabel.setHorizontalAlignment(JLabel.CENTER);
		priceLabel.setHorizontalAlignment(JLabel.CENTER);

		firstInput.setSize(80, 20);
		secondInput.setSize(80,20);
		
		panel.add(nameLabel);
		panel.add(firstInput);
		panel.add(priceLabel);
		panel.add(secondInput);
		
		panel.add(regAcceptButton);
		panel.add(regCancelButton);
		
		regAcceptButton.addActionListener(new RegMenuActionListener());
		regCancelButton.addActionListener(new RegMenuActionListener());

		secondaryFrame.add(panel);
		
		secondaryFrame.setVisible(true);
	}
	
	public void createRegStaff()
	{
		secondaryFrame = new JFrame();
		panel = new JPanel();

		firstInput.setText("");
		
		panel.setLayout(new GridLayout(3,2,5,5));
		secondaryFrame.setTitle("직원 등록");
		secondaryFrame.setSize(320,170);
		
		regAcceptButton.setText("등록");
		regCancelButton.setText("취소");
		
		regAcceptButton.addActionListener(new RegStaffActionListener());
		regCancelButton.addActionListener(new RegStaffActionListener());
		
		nameLabel.setText("이름");
		nameLabel.setHorizontalAlignment(JLabel.CENTER);
		rankLabel.setHorizontalAlignment(JLabel.CENTER);
		
		firstInput.setSize(80, 20);
		secondInput.setSize(80, 20);
		
		choiceRank.addItem("Supervisor");
		choiceRank.addItem("Staff");
		
		choiceRank.addActionListener(new RegStaffActionListener());
		
		panel.add(nameLabel);
		panel.add(firstInput);
		panel.add(rankLabel);
		panel.add(choiceRank);
		panel.add(regAcceptButton);
		panel.add(regCancelButton);
		
		secondaryFrame.add(panel);
		secondaryFrame.setVisible(true);
	}
	
	class LoginActionListener implements ActionListener { // 스태프로그인

		@Override
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == loginButton)
			{
				loginFlag = 1;
				String id = idInput.getText();
				char[] pwdArray = pwdInput.getPassword();
				String pwd = "";
				for (int i = 0; i < pwdArray.length; i++)
				{
					pwd += pwdArray[i];
				}
				customerTable = dbc.getCustomerTable();
				staffLoginFlag = dbc.staffLogin(id, pwd);
				currentStaff = id;
				secondaryFrame.setVisible(false);
				e.setSource(null);				
			}
		}
	}
	
	class RegCustomerActionListener implements ActionListener // 고객등록
	{
		public void actionPerformed(ActionEvent e)
		{
			if (e.getSource() == regAcceptButton)
			{
				if (staffLoginFlag == 2)
				{
					String customerName = firstInput.getText();
					int birthday = Integer.parseInt(secondInput.getText());
					int contact = Integer.parseInt(thirdInput.getText());
					
					dbc.regCustomer(customerName, birthday, contact);
					JOptionPane.showMessageDialog(nameLabel, "입력 완료");
				}
				else
				{
					JOptionPane.showMessageDialog(null, "Supervisor로 먼저 로그인 하세요");
				}
				firstInput.setText("");
				secondInput.setText("");
				
				secondaryFrame.setVisible(false);
				e.setSource(null);


			}
			else if (e.getSource() == regCancelButton)
			{
				JOptionPane.showMessageDialog(null, "취소되었습니다 !");
				secondaryFrame.setVisible(false);
				e.setSource(null);

			}
		}
	}
	
	class RegMenuActionListener implements ActionListener // 메뉴등록
	{
		public void actionPerformed(ActionEvent e)
		{
			if (e.getSource() == regAcceptButton)
			{
				int menuRegFlag = 0;
				if (staffLoginFlag == 2)
				{
					String menuName = firstInput.getText();
					String menuPrice = secondInput.getText();
					menuRegFlag = dbc.regMenu(menuName, menuPrice);
					
					if (menuRegFlag == 1)
					{
						String[] menuList;
	
						menuList = dbc.getMenu();
						for (int i = 0; i < menuButton.length; i++) {
							menuButton[i].setText(menuList[i]);
							menuButton[i].addActionListener(new MenuButtonActionListener());
							menuPanel.add(menuButton[i]);
						}
						frame.repaint();
						menuPanel.repaint();
					}
				}
				else
				{
					JOptionPane.showMessageDialog(null, "Supervisor로 먼저 로그인 하세요");
				}
				firstInput.setText("");
				secondInput.setText("");
				
				secondaryFrame.setVisible(false);
				e.setSource(null);
				}

			else if (e.getSource() == regCancelButton)
			{
				JOptionPane.showMessageDialog(null, "취소되었습니다 !");
				secondaryFrame.setVisible(false);
				e.setSource(null);
			}
		}
	}
	
	class RegStaffActionListener implements ActionListener // 스태프등록
	{
		public void actionPerformed(ActionEvent e)
		{
			if (e.getSource() == regAcceptButton)
			{
				if (staffLoginFlag == 2)
				{
					String staffName = firstInput.getText();
					firstInput.setText("");

					String staffRank = (String)choiceRank.getSelectedItem();
					
					dbc.regStaff(staffName, staffRank);
				}
				else
				{
					JOptionPane.showMessageDialog(new JFrame(), "Supervisor로 먼저 로그인 하세요");
				}
				secondaryFrame.setVisible(false);
				e.setSource(null);

				
			}
			else if (e.getSource() == regCancelButton)
			{
				JOptionPane.showMessageDialog(null, "취소되었습니다 !");
				secondaryFrame.setVisible(false);
				e.setSource(null);
			}
		}
	}
	
	class ShowResultActionListener implements ActionListener // 주문결과조회
	{
		public void actionPerformed(ActionEvent e)
		{
			String result;
			
			String selectedItem = salesComboBox.getSelectedItem().toString();
			if (selectedItem.equals(""))
			{
				result = "";
			}
			else
			{
				if (staffLoginFlag == 2)
				{
					String[] resultCheck = dbc.resultCheck(selectedItem);
					result = "금일합계 : " + resultCheck[0] + "\n---------------\n"
						+ "가장 많이 팔린 메뉴\n" + resultCheck[1] + "\n"
						+ "가장 적게 팔린 메뉴\n" + resultCheck[2] + "\n---------------\n"
						+ "누적매출 : " + resultCheck[3];
				}
				else {
					JOptionPane.showMessageDialog(null, "Supervisor로 로그인하세여");
					salesComboBox.setSelectedIndex(0);
					result = "";
				}
			}
			salesTextField.setText(result);
		}
	}
}