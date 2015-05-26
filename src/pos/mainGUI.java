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

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Date;


public class mainGUI implements ActionListener{
	private JFrame frame = new JFrame(); // action listener ����� ����
	private JPanel panel = new JPanel();
	
	// �α��� �ʵ�
	private JLabel idLabel = new JLabel("���̵�");
	private JLabel pwdLabel = new JLabel("��й�ȣ");
	private JTextField idInput = new JTextField();
	private JPasswordField pwdInput = new JPasswordField();
	private JButton loginButton = new JButton("�α���");
	
	// �Ĵ���� �ʵ�
	private JPanel tablePanel = new JPanel();
	private JPanel orderPanel = new JPanel();
	private JPanel menuPanel = new JPanel();
	private JPanel regPanel = new JPanel();
	private JButton button[] = new JButton[20];
	
	// �޴� �ʵ�
	private JButton orderButton = new JButton("�ֹ�");
	private JButton cancelButton = new JButton("���");
	private JButton payButton = new JButton("����");
	private JTextField customerNameInput = new JTextField();
	private JComboBox<Integer> tableNameInput = new JComboBox<Integer>();
	
	// ��� �ʵ�
	private JTabbedPane tabbedPane = new JTabbedPane();
	private JPanel customerPane = new JPanel();
	private JPanel salesPane = new JPanel();
	private JPanel workerPane = new JPanel();
	private JPanel menuPane = new JPanel();
	
	private JButton customerSignUp = new JButton("ȸ������");
	private JButton customerCheck = new JButton("ȸ����ȸ");
	private JButton menuSignUp = new JButton("�޴����");
	private JButton menuCheck = new JButton("�޴���ȸ");
	private JButton workerSignUp = new JButton("�����߰�");
	private JButton workerCheck = new JButton("������ȸ");
	private JComboBox<Date> salesComboBox = new JComboBox<Date>();
	
	private JTextField customerInput = new JTextField();
	private JTextField menuInput = new JTextField();
	private JTextField workerInput = new JTextField();
	
	private JTextField customerTextField = new JTextField();
	private JTextField salesTextField = new JTextField();
	private JTextField workerTextField = new JTextField();
	private JTextField menuTextField = new JTextField();
	
	// ���̵� ���
	private String username;
	private String password;
	
	// ����
	private static Connection dbTest;

	

	@Override
	public void actionPerformed(ActionEvent e) {
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
		frame.setTitle("�Ĵ� �ֹ�����");
		frame.setSize(755, 1000);

		frame.setVisible(true);
	}
	
	private void layoutTitle() {
		JLabel label = new JLabel("�Ĵ� �ֹ�����");

		label.setFont(new Font("����", Font.BOLD,30));
		label.setHorizontalAlignment(JLabel.CENTER);
		label.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		label.setLayout(null);
		label.setBounds(10,10,720,70);
		frame.add(label);
	}
	
	private void currentTable() {
		tablePanel.setBounds(10, 80, 355, 430);
		tablePanel.setLayout(new GridLayout(5,4,10,10));
		tablePanel.setBorder(new TitledBorder("���̺�"));
		tablePanel.setFont(new Font("����", Font.BOLD, 15));
		
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
		orderPanel.setBorder(new TitledBorder("�ֹ�����"));
		orderPanel.setFont(new Font("����", Font.BOLD, 15));
		orderPanel.setLayout(null);
		
		JTextField orderText = new JTextField();
		JLabel customerName = new JLabel("����");
		JLabel tableName = new JLabel("���̺��");
		
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
		menuPanel.setBorder(new TitledBorder("�޴�"));
		menuPanel.setFont(new Font("����", Font.BOLD, 15));
		
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
		regPanel.setBorder(new TitledBorder("���/��ȸ"));
		regPanel.setFont(new Font("����", Font.BOLD, 15));
		regPanel.setLayout(null);
		
		tabbedPane.setBounds(10,20,335,400);
		tabbedPane.addTab("��", customerPane);
		tabbedPane.addTab("����", salesPane);
		tabbedPane.addTab("����", workerPane);
		tabbedPane.addTab("�޴�", menuPane);
		
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
}