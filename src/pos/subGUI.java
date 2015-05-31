package pos;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;

// �α��ΰ� ���ȭ���� å������ ���ο� �����Ӱ� �г�

public class subGUI {
	private JFrame frame = new JFrame(); 
	private JPanel panel = new JPanel();
	private JPanel outerPanel = new JPanel();
	
	int loginFlag = 1;
	

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

	
	public void createLoginMenu() {
		frame = new JFrame();
		panel = new JPanel();
		idInput = new JTextField();
		pwdInput = new JPasswordField();
		
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
		frame.setVisible(true);
		
		loginButton.addActionListener(new LoginActionListener());
	}
	
	public void createRegCustomer()
	{
		frame = new JFrame();
		panel = new JPanel();
		
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
