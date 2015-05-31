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
	private subGUI sgui = new subGUI();
	private JFrame frame = new JFrame(); // action listener 사용을 위함
	private String filePath;
	private int loginFlag = sgui.loginFlag; // 스태프로 로그인 되어 있으면 1, 아니면 0 -> sql 파일 불러올때 사용
	private int currentTable; // 현재 입력된 테이블 정보

	
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
	
	
	public void createMenuBar() {
		openItem.addActionListener(new MenuBarActionListener());
		loginItem.addActionListener(new MenuBarActionListener());
		
		
		fileMenu.add(openItem);
		fileMenu.add(loginItem);
		
		mb.add(fileMenu);
		frame.setJMenuBar(mb);
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
		frame.setSize(655, 820);
		createMenuBar();
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
		
        for (int i = 0; i < button.length; i++)
        {
        	button[i] = new JButton(String.valueOf(i+1));
        	button[i].addActionListener(new TableActionListener());
        	button[i].setBackground(Color.WHITE);
        	tablePanel.add(button[i]);
        }
        
		frame.add(tablePanel);
	}
	
	private void orderTable() {
		orderPanel.setBounds(325,80,305,330);
		orderPanel.setBorder(new TitledBorder("주문내역"));
		orderPanel.setFont(new Font("굴림", Font.BOLD, 15));
		orderPanel.setLayout(null);
		
		JTextField orderText = new JTextField();
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
		
		frame.add(orderPanel);

	}
	
	private void menuTable() {
		menuPanel.setBounds(10,420,305,330);
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
		
		salesComboBox.addActionListener(new RegTableActionListener());
		
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
	
	class TableActionListener implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
			for (int i = 0; i < 20; i++) {
				button[i].setBackground(Color.WHITE);
			}

			for (int i = 0; i < 20; i++)
			{
				if (e.getSource() == button[i])
				{
					currentTable = i+1;
					button[i].setBackground(Color.RED);
				}
			}
		}
	}
	
	
	class MenuBarActionListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == loginItem)
			{
				sgui.createLoginMenu();
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
			}
		}
	}
	
	class OrderActionListener implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{	
			if (e.getSource() == orderButton)
			{
				tableNameInput.getSelectedItem().toString();

			}
			else if (e.getSource() == cancelButton)
			{
				
			}
			else if (e.getSource() == payButton)
			{
				tableNameInput.getSelectedItem().toString();

			}
		}
	}
	
	class RegTableActionListener implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
			if (e.getSource() == customerSignUp)
			{
				sgui.createRegCustomer();
			}
			else if (e.getSource() == customerCheck)
			{
				
			}
			else if (e.getSource() == salesComboBox)
			{
				
			}
			else if (e.getSource() == workerSignUp)
			{
				sgui.createRegStaff();
			}
			else if (e.getSource() == workerCheck)
			{
				
			}
			else if (e.getSource() == menuSignUp)
			{
				sgui.createRegMenu();
				
			}
			else if (e.getSource() == menuCheck)
			{
				
			}
		}
	}
}