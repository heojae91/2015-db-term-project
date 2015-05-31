package pos;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;

// 로그인과 등록화면을 책임지는 새로운 프래임과 패널

public class subGUI {
	private JFrame frame = new JFrame(); 
	private JPanel panel = new JPanel();
	private JPanel outerPanel = new JPanel();
	
	int loginFlag = 1;
	

	// 등록화면을 위한 새로운 친구들
	private JLabel nameLabel = new JLabel("이름넣는곳");
	private JLabel birthLabel = new JLabel("생일(4자리)");
	private JLabel contactLabel = new JLabel("연락처");
	private JLabel priceLabel = new JLabel("가격");
	private JLabel rankLabel = new JLabel("직급");
	private JTextField firstInput = new JTextField();
	private JTextField secondInput = new JTextField();
	private JTextField thirdInput = new JTextField();
	private JComboBox<String> choiceRank = new JComboBox<String>();
	private JButton firstButton = new JButton();
	private JButton secondButton = new JButton();

	// 로그인 필드
	private JLabel idLabel = new JLabel("아이디");
	private JLabel pwdLabel = new JLabel("비밀번호");
	private JTextField idInput = new JTextField();
	private JPasswordField pwdInput = new JPasswordField();
	private JButton loginButton = new JButton("로그인");

	
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
		frame.setTitle("메뉴 등록");
		frame.setSize(320,200);

		firstButton.setText("회원등록");
		secondButton.setText("취소");
		
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
		frame.setTitle("메뉴 등록");
		frame.setSize(320,170);
		
		firstButton.setText("등록");
		secondButton.setText("취소");
		
		nameLabel.setText("메뉴이름");
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
		frame.setTitle("직원 등록");
		frame.setSize(320,170);
		
		firstButton.setText("등록");
		secondButton.setText("취소");
		
		firstButton.addActionListener(new RegStaffActionListener());
		secondButton.addActionListener(new RegStaffActionListener());
		
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
				JOptionPane.showMessageDialog(null, "취소되었습니다 !");
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
				JOptionPane.showMessageDialog(null, "취소되었습니다 !");
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
				JOptionPane.showMessageDialog(null, "취소되었습니다 !");
				frame.setVisible(false);
			}
			else if (e.getSource() == choiceRank)
			{
				System.out.println(choiceRank.getSelectedItem().toString());
			}
		}
	}
}
