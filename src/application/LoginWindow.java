package application;

import javax.swing.*;
import java.awt.Color;
import java.awt.Toolkit;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import animation.AnimationManager;
import animation.Iterator;
import component.*;
import resource.Resource;

import java.awt.Font;

public class LoginWindow extends JFrame{

	private AnImageButton anImageButton;
	
	private JLabel labRodingMessage;

	//登录结果回调
	ILoginResultCallback resultCallback=null;
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
		setTitle("登录");
		setSize(500, 289);
		getContentPane().setBackground(Color.WHITE);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setResizable(false);
		setLocationRelativeTo(null);
		getContentPane().setLayout(null);


		//组件
		JPanel panel = new JPanel();
		panel.setBounds(0, 0, 494, 310);
		getContentPane().add(panel);
		panel.setLayout(null);
		panel.setBackground(Color.white);
		
		//密码框
		password = new AnPasswordField();
		password.setFont(new Font("微软雅黑", Font.PLAIN, 17));
		password.setBounds(122, 192, 227, 21);
		panel.add(password);
		password.setColumns(10);
		
		anImageButton=new AnImageButton("登录数据库");
		anImageButton.setLocation(367, 149);
		anImageButton.setImage(AnUtils.getImageIcon(Resource.getResource("login_normal.png")),
				AnUtils.getImageIcon(Resource.getResource("login_press.png")), 
				AnUtils.getImageIcon(Resource.getResource("login_normal.png")));
		anImageButton.setActionListener((e)->{
			login();
		});
		
		JLabel lblNewLabel_2 = new JLabel("An\u5DE5\u5730\u7BA1\u7406\u7CFB\u7EDF");
		lblNewLabel_2.setFont(new Font("等线 Light", Font.PLAIN, 45));
		lblNewLabel_2.setBounds(35, 32, 391, 82);
		panel.add(lblNewLabel_2);
		
		labRodingMessage = new JLabel("正在登录...");
		labRodingMessage.setForeground(Color.WHITE);
		labRodingMessage.setFont(new Font("微软雅黑", Font.PLAIN, 13));
		labRodingMessage.setHorizontalAlignment(SwingConstants.RIGHT);
		labRodingMessage.setBounds(327, 231, 155, 23);
		labRodingMessage.setVisible(false);
		panel.add(labRodingMessage);

		panel.add(anImageButton);
		
		JLabel label = new JLabel("\u5BC6\u7801:");
		label.setFont(new Font("微软雅黑", Font.PLAIN, 15));
		label.setBounds(35, 192, 72, 23);
		panel.add(label);
		
		JLabel lblNewLabel = new JLabel("\u7528\u6237\u540D:");
		lblNewLabel.setFont(new Font("微软雅黑", Font.PLAIN, 15));
		lblNewLabel.setBounds(35, 149, 72, 23);
		panel.add(lblNewLabel);
		
		//用户名
		user = new AnTextField();
		user.setFont(new Font("微软雅黑", Font.PLAIN, 17));
		user.setBounds(122, 150, 227, 21);
		panel.add(user);
		user.setColumns(10);
		
		//注册
		AnTextButton btnRegister = new AnTextButton("注册");
		btnRegister.setSize(30, 30);
		btnRegister.setLocation(35, 226);
		panel.add(btnRegister);
		btnRegister.setActionListener(e -> {
            // TODO Auto-generated method stub
            if (e.getAction()==AnActionEvent.CILCKED){
				NewUserWindow newUserWindow=NewUserWindow.getWindow();
				newUserWindow.setVisible(true);
				newUserWindow.setCallback(user -> {
					// TODO Auto-generated method stub
					if(user!=null) {
						JOptionPane.showMessageDialog(null, "注册完成，您现在可以用注册的账户登录。","注册提示",JOptionPane.INFORMATION_MESSAGE);
						Application.addUser(user);
						Application.updateUserData();//更新数据
					}
				});
			}
        });
		//忘记密码
		AnTextButton btnFindPassword = new AnTextButton("忘记密码");
		btnFindPassword.setSize(100, 30);
		btnFindPassword.setLocation(88, 226);
		panel.add(btnFindPassword);
		btnFindPassword.setActionListener(e -> {
            // TODO Auto-generated method stub
			if (e.getAction()==AnActionEvent.CILCKED){

			}
        });
		
		AnImageLabel imageLabel=new AnImageLabel(Resource.getResource("login_bg.png"));
		imageLabel.setSize(494, 262);
		imageLabel.setLocation(0, 289/2);
		panel.add(imageLabel);

		AnimationManager animationManager=AnimationManager.getManager();
		Iterator iterator=animationManager.createAnimationIterator(289/2,10,0);
		iterator.addNode(16);
		iterator.addNode(30);
		iterator.addNode(50);
		iterator.addNode(60);
		iterator.addNode(70);
		iterator.addNode(80);
		iterator.addNode(90);
		iterator.addNode(100);
		iterator.addNode(130);
		iterator.addNode(180);
		iterator.setCallback(value -> imageLabel.setLocation(0, value+(289/2)));
		iterator.start();
		//找回密码
		
		
		// TODO Auto-generated constructor stub
		init();
	}


	private void showMessage(){
		labRodingMessage.setVisible(true);
	}
	
	private void login() {
		showMessage();
		if(resultCallback!=null) {
			resultCallback.loginResult(user.getText(), password.getText());
		}
	}
}
