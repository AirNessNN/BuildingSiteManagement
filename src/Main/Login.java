package Main;

import javax.swing.JFrame;
import java.awt.Color;
import java.awt.Toolkit;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JPanel;

import Compoent.AnImageButton;
import Compoent.AnImageLabel;
import Resoure.Resource;

import java.awt.BorderLayout;
import javax.swing.JTextField;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.DropMode;
import javax.swing.SwingConstants;

public class Login extends JFrame{
	
	
	private JPanel panel=null;
	private AnImageButton anImageButton=null;
	
	//µÇÂ¼½á¹û»Øµ÷
	public ILoginResultCallback resultCallback=null;
	interface ILoginResultCallback{
		public void loginResult(boolean result);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JTextField user;
	private JTextField password;
	
	private void init() {
		anImageButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				// TODO Auto-generated method stub
				super.mouseClicked(e);
				if(resultCallback!=null) {
					login();
				}
			}
		} );
		password.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				// TODO Auto-generated method stub
				super.keyReleased(e);
				if(e.getKeyCode()==KeyEvent.VK_ENTER) {
					login();
				}
			}
		});
		user.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				// TODO Auto-generated method stub
				super.keyReleased(e);
				if(e.getKeyCode()==KeyEvent.VK_ENTER) {
					login();
				}
			}
		});
	}
	
	
	public Login() {
		setIconImage(Toolkit.getDefaultToolkit().getImage(Login.class.getResource("/Resoure/login.png")));
		setTitle("µÇÂ¼");
		setSize(500, 301);
		getContentPane().setBackground(Color.WHITE);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setResizable(false);
		setLocationRelativeTo(null);
		getContentPane().setLayout(null);
		
		
		panel = new JPanel();
		panel.setBounds(0, 0, 494, 310);
		getContentPane().add(panel);
		panel.setLayout(null);
		
		password = new JTextField();
		password.setFont(new Font("Î¢ÈíÑÅºÚ", Font.PLAIN, 17));
		password.setBounds(122, 192, 227, 27);
		panel.add(password);
		password.setColumns(10);
		
		anImageButton=new AnImageButton("µÇÂ¼Êý¾Ý¿â");
		anImageButton.setLocation(367, 149);
		anImageButton.setImage(Util.getImageIcon(Resource.getResource("login_normal.png")),
				Util.getImageIcon(Resource.getResource("login_press.png")), 
				Util.getImageIcon(Resource.getResource("login_normal.png")));
		
		JLabel lblNewLabel_2 = new JLabel("An\u5DE5\u5730\u7BA1\u7406\u7CFB\u7EDF");
		lblNewLabel_2.setFont(new Font("µÈÏß Light", Font.PLAIN, 45));
		lblNewLabel_2.setBounds(35, 32, 391, 82);
		panel.add(lblNewLabel_2);
		
		JLabel lblNewLabel_1 = new JLabel("\u6B63\u5728\u767B\u5F55...");
		lblNewLabel_1.setForeground(Color.WHITE);
		lblNewLabel_1.setFont(new Font("Î¢ÈíÑÅºÚ", Font.PLAIN, 16));
		lblNewLabel_1.setHorizontalAlignment(SwingConstants.RIGHT);
		lblNewLabel_1.setBounds(327, 231, 155, 23);
		panel.add(lblNewLabel_1);
		panel.add(anImageButton);
		
		JLabel label = new JLabel("\u5BC6\u7801:");
		label.setFont(new Font("Î¢ÈíÑÅºÚ", Font.BOLD, 17));
		label.setBounds(35, 196, 72, 23);
		panel.add(label);
		
		JLabel lblNewLabel = new JLabel("\u7528\u6237\u540D:");
		lblNewLabel.setFont(new Font("Î¢ÈíÑÅºÚ", Font.BOLD, 17));
		lblNewLabel.setBounds(35, 149, 72, 23);
		panel.add(lblNewLabel);
		
		user = new JTextField();
		user.setFont(new Font("Î¢ÈíÑÅºÚ", Font.PLAIN, 17));
		user.setBounds(122, 150, 227, 27);
		panel.add(user);
		user.setColumns(10);
		
		AnImageLabel imageLabel=new AnImageLabel("login_bg.png");
		imageLabel.setSize(494, 262);
		imageLabel.setLocation(0, 0);
		panel.add(imageLabel);
		
		// TODO Auto-generated constructor stub
		init();
	}
	
	
	private void login() {
		if(resultCallback!=null) {
			if(user.getText().equals("test")&&password.getText().equals("123456")) {
				resultCallback.loginResult(true);
			}else {
				resultCallback.loginResult(false);
				password.setText(null);
				user.setText(null);
			}
		}
	}
}
