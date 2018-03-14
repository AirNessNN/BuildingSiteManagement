package application;

import javax.swing.JFrame;
import java.awt.Color;
import java.awt.Toolkit;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import javax.swing.JPanel;

import compoent.*;
import dbManager.DBManager;
import resource.Resource;

import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import java.io.IOException;
import javax.swing.SwingConstants;

import application.NewUserWindow.IUserCallback;

public class LoginWindow extends JFrame{
	
	//◊Èº˛
	private JPanel panel=null;
	private AnImageButton anImageButton=null;
	
	private JLabel labRodingMessage =null;
	
	private AnTextButton btnRegister=null;
	private AnTextButton btnFindPassword=null;
	
	//µ«¬ºΩ·π˚ªÿµ˜
	public ILoginResultCallback resultCallback=null;
	interface ILoginResultCallback{
		void loginResult(String user, String password);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private AnTextField user;
	private AnPasswordField password;
	
	private void init() {
		anImageButton.setActionListener((e)->{
			if(resultCallback!=null) {
				login();
			}
		});
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
	
	
	public LoginWindow() {
		setIconImage(Toolkit.getDefaultToolkit().getImage(LoginWindow.class.getResource("/resource/login.png")));
		setTitle("µ«¬º");
		setSize(500, 289);
		getContentPane().setBackground(Color.WHITE);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setResizable(false);
		setLocationRelativeTo(null);
		getContentPane().setLayout(null);
		
		
		panel = new JPanel();
		panel.setBounds(0, 0, 494, 310);
		getContentPane().add(panel);
		panel.setLayout(null);
		panel.setBackground(Color.white);
		
		//√‹¬ÎøÚ
		password = new AnPasswordField();
		password.setFont(new Font("Œ¢»Ì—≈∫⁄", Font.PLAIN, 17));
		password.setBounds(122, 192, 227, 21);
		panel.add(password);
		password.setColumns(10);
		
		anImageButton=new AnImageButton("µ«¬º ˝æ›ø‚");
		anImageButton.setLocation(367, 149);
		anImageButton.setImage(AnUtils.getImageIcon(Resource.getResource("login_normal.png")),
				AnUtils.getImageIcon(Resource.getResource("login_press.png")), 
				AnUtils.getImageIcon(Resource.getResource("login_normal.png")));
		anImageButton.setActionListener((e)->{
			login();
		});
		
		JLabel lblNewLabel_2 = new JLabel("An\u5DE5\u5730\u7BA1\u7406\u7CFB\u7EDF");
		lblNewLabel_2.setFont(new Font("µ»œﬂ Light", Font.PLAIN, 45));
		lblNewLabel_2.setBounds(35, 32, 391, 82);
		panel.add(lblNewLabel_2);
		
		labRodingMessage = new JLabel("\u6B63\u5728\u767B\u5F55...");
		labRodingMessage.setForeground(Color.WHITE);
		labRodingMessage.setFont(new Font("Œ¢»Ì—≈∫⁄", Font.PLAIN, 13));
		labRodingMessage.setHorizontalAlignment(SwingConstants.RIGHT);
		labRodingMessage.setBounds(327, 231, 155, 23);
		panel.add(labRodingMessage);
		panel.add(anImageButton);
		
		JLabel label = new JLabel("\u5BC6\u7801:");
		label.setFont(new Font("Œ¢»Ì—≈∫⁄", Font.PLAIN, 15));
		label.setBounds(35, 192, 72, 23);
		panel.add(label);
		
		JLabel lblNewLabel = new JLabel("\u7528\u6237\u540D:");
		lblNewLabel.setFont(new Font("Œ¢»Ì—≈∫⁄", Font.PLAIN, 15));
		lblNewLabel.setBounds(35, 149, 72, 23);
		panel.add(lblNewLabel);
		
		//”√ªß√˚
		user = new AnTextField();
		user.setFont(new Font("Œ¢»Ì—≈∫⁄", Font.PLAIN, 17));
		user.setBounds(122, 150, 227, 21);
		panel.add(user);
		user.setColumns(10);
		
		//◊¢≤·
		btnRegister=new AnTextButton("◊¢≤·");
		btnRegister.setLocation(10, 234);
		panel.add(btnRegister);
		btnRegister.addActionListener(e -> {
            // TODO Auto-generated method stub
            NewUserWindow newUserWindow=NewUserWindow.getWindow();
            newUserWindow.setVisible(true);
            newUserWindow.setCallback(user -> {
				// TODO Auto-generated method stub
				if(user!=null) {
					JOptionPane.showMessageDialog(null, "◊¢≤·ÕÍ≥…£¨ƒ˙œ÷‘⁄ø…“‘”√◊¢≤·µƒ’Àªßµ«¬º°£","◊¢≤·Ã· æ",JOptionPane.INFORMATION_MESSAGE);
					Application.addUser(user);
					Application.updateUserData();//∏¸–¬ ˝æ›
				}
			});
        });
		//Õ¸º«√‹¬Î
		btnFindPassword=new AnTextButton("Õ¸º«√‹¬Î");
		btnFindPassword.setLocation(70, 234);
		panel.add(btnFindPassword);
		btnFindPassword.addActionListener(e -> {
            // TODO Auto-generated method stub

        });
		
		AnImageLabel imageLabel=new AnImageLabel("login_bg.png");
		imageLabel.setSize(494, 262);
		imageLabel.setLocation(0, 289/2);
		panel.add(imageLabel);
		new Thread(() -> {
            // TODO Auto-generated method stub
            try {
                Thread.sleep(500);
                for(int i=289/2;i>0;i-=Math.log(i*10)) {
                    imageLabel.setLocation(0, i);
                    Thread.sleep(16);
                }
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }


        }).start();
		
		
		
		//’“ªÿ√‹¬Î
		
		
		// TODO Auto-generated constructor stub
		init();
	}
	
	
	private void login() {
		
		if(resultCallback!=null) {
			resultCallback.loginResult(user.getText(), password.getText());
		}
	}
}
