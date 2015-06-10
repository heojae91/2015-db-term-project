package pos;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;

// �α��ΰ� ���ȭ���� å������ ���ο� �����Ӱ� �г�

public class subGUI {
	private JFrame frame = new JFrame(); 
	private JPanel panel = new JPanel();
	
	int loginFlag = 1;
	int staffLoginFlag;
	// ���ȭ���� ���� ���ο� ģ����
	private JLabel nameLabel = new JLabel("�̸��ִ°�");
	private JLabel birthLabel = new JLabel("����(4�ڸ�)");
	private JLabel contactLabel = new JLabel("����ó");
	private JLabel priceLabel = new JLabel("����");
	private JLabel rankLabel = new JLabel("����");
	private JTextField firstInput = new JTextField();
	private JTextField secondInput = new JTextField();
	private JTextField thirdInput = new JTextField();
	private JComboBox<String> choiceRank = new JComboBox<String>();
	private JButton firstButton = new JButton();
	private JButton secondButton = new JButton();

	// �α��� �ʵ�
	private JLabel idLabel = new JLabel("���̵�");
	private JLabel pwdLabel = new JLabel("��й�ȣ");
	private JTextField idInput = new JTextField();
	private JPasswordField pwdInput = new JPasswordField();
	private JButton loginButton = new JButton("�α���");
	
	private dbcontroller dbc;
	private PreparedStatement stmt;
	private ResultSet rs;
	private JButton[] menuButton;
	

	subGUI(dbcontroller dbc, JButton[] menuButton)
	{
		this.dbc = dbc;
		this.menuButton = menuButton;
	}
	
	public void createLoginMenu() {
		frame = new JFrame();
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
		
		frame.add(panel);	
		frame.setTitle("Login");
		frame.setSize(320,130);
		frame.setVisible(true);
		
		loginButton.addActionListener(new LoginActionListener());
	}
	
	public void createRegCustomer()
	{
		frame = new JFrame();
		panel = new JPanel();
		
		firstInput.setText("");
		secondInput.setText("");
		thirdInput.setText("");

		panel.setLayout(new GridLayout(4,2,5,5));
		frame.setTitle("�޴� ���");
		frame.setSize(320,200);

		firstButton.setText("ȸ�����");
		secondButton.setText("���");
		
		nameLabel.setText("�̸�");
		nameLabel.setHorizontalAlignment(JLabel.CENTER);
		birthLabel.setHorizontalAlignment(JLabel.CENTER);
		contactLabel.setHorizontalAlignment(JLabel.CENTER);
		
		panel.add(nameLabel);
		panel.add(firstInput);
		panel.add(birthLabel);
		panel.add(secondInput);
		panel.add(contactLabel);
		panel.add(thirdInput);
		
		panel.add(firstButton);
		panel.add(secondButton);
		
		frame.add(panel);
		
		firstButton.addActionListener(new RegCustomerActionListener());
		secondButton.addActionListener(new RegCustomerActionListener());
		
		frame.setVisible(true);
	}
	
	public void createRegMenu()
	{
		frame = new JFrame();
		panel = new JPanel();
		
		firstInput.setText("");
		secondInput.setText("");

		panel.setLayout(new GridLayout(3,2,5,5));
		frame.setTitle("�޴� ���");
		frame.setSize(320,170);
		
		firstButton.setText("���");
		secondButton.setText("���");
		
		nameLabel.setText("�޴��̸�");
		nameLabel.setHorizontalAlignment(JLabel.CENTER);
		priceLabel.setHorizontalAlignment(JLabel.CENTER);

		firstInput.setSize(80, 20);
		secondInput.setSize(80,20);
		
		panel.add(nameLabel);
		panel.add(firstInput);
		panel.add(priceLabel);
		panel.add(secondInput);
		
		panel.add(firstButton);
		panel.add(secondButton);
		
		firstButton.addActionListener(new RegMenuActionListener());
		secondButton.addActionListener(new RegMenuActionListener());

		frame.add(panel);
		
		frame.setVisible(true);
	}
	
	public void createRegStaff()
	{
		frame = new JFrame();
		panel = new JPanel();

		firstInput.setText("");
		
		panel.setLayout(new GridLayout(3,2,5,5));
		frame.setTitle("���� ���");
		frame.setSize(320,170);
		
		firstButton.setText("���");
		secondButton.setText("���");
		
		firstButton.addActionListener(new RegStaffActionListener());
		secondButton.addActionListener(new RegStaffActionListener());
		
		nameLabel.setText("�̸�");
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
		panel.add(firstButton);
		panel.add(secondButton);
		
		frame.add(panel);
		frame.setVisible(true);
	}
	
	class LoginActionListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == loginButton)
			{
				loginFlag = 1;
				String id = idInput.getText();
				char[] pwdArray = pwdInput.getPassword();
				System.out.println(id);
				String pwd = "";
				for (int i = 0; i < pwdArray.length; i++)
				{
					pwd += pwdArray[i];
				}
				try {
					staffLoginFlag = dbc.staffLogin(id, pwd);
				} catch (SQLException e1) {
					JOptionPane.showMessageDialog(new JFrame(), "�α��� ������ �߸��Ǿ����ϴ�!");
				}
				frame.setVisible(false);
				
			}
		}
		
	}
	
	class RegCustomerActionListener implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
			if (e.getSource() == firstButton)
			{
				if (staffLoginFlag == 2)
				{
					String customerName = firstInput.getText();
					int birthday = Integer.parseInt(secondInput.getText());
					int contact = Integer.parseInt(thirdInput.getText());
					
					try {
						dbc.regCustomer(customerName, birthday, contact);
						JOptionPane.showMessageDialog(nameLabel, "�Է� �Ϸ�");
					} catch (SQLException e1) {
						JOptionPane.showMessageDialog(nameLabel, "�߸��� �Է��Դϴ�!");
					}
				}
				else
				{
					JOptionPane.showMessageDialog(null, "Supervisor�� ���� �α��� �ϼ���");
				}
				firstInput.setText("");
				secondInput.setText("");
				
				frame.setVisible(false);

			}
			else if (e.getSource() == secondButton)
			{
				JOptionPane.showMessageDialog(null, "��ҵǾ����ϴ� !");
				frame.setVisible(false);
			}
		}
	}
	
	class RegMenuActionListener implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
			if (e.getSource() == firstButton)
			{
				if (staffLoginFlag == 2)
				{
					String menuName = firstInput.getText();
					int menuPrice = Integer.parseInt(secondInput.getText());
					
					dbc.regMenu(menuName, menuPrice);
					JOptionPane.showMessageDialog(nameLabel, "�Է� �Ϸ�");
					for (int i = 0; i < 20; i++)
					{
						menuButton[i].repaint();
						System.out.println(1);
					}
				}
				else
				{
					JOptionPane.showMessageDialog(null, "Supervisor�� ���� �α��� �ϼ���");
				}
				firstInput.setText("");
				secondInput.setText("");
				
				frame.setVisible(false);

			}
			else if (e.getSource() == secondButton)
			{
				JOptionPane.showMessageDialog(null, "��ҵǾ����ϴ� !");
				frame.setVisible(false);
			}
		}
	}
	
	class RegStaffActionListener implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
			if (e.getSource() == firstButton)
			{
				if (staffLoginFlag == 2)
				{
					String staffName = firstInput.getText();
					String staffRank = (String)choiceRank.getSelectedItem();
					
					try {
						dbc.regStaff(staffName, staffRank);
					} catch (SQLException e1) {
						JOptionPane.showMessageDialog(new JFrame(), "�߸��� �Է��Դϴ�");
						e1.printStackTrace();
					}
				}
				else
				{
					JOptionPane.showMessageDialog(new JFrame(), "Supervisor�� ���� �α��� �ϼ���");
				}
				firstInput.setText("");
				frame.setVisible(false);
				
			}
			else if (e.getSource() == secondButton)
			{
				JOptionPane.showMessageDialog(null, "��ҵǾ����ϴ� !");
				frame.setVisible(false);
			}
			else if (e.getSource() == choiceRank)
			{
				System.out.println(choiceRank.getSelectedItem().toString());
			}
		}
	}
}