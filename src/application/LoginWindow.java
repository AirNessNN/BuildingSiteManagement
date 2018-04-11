package application;

import javax.swing.*;
import java.awt.Color;
import java.awt.Toolkit;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import component.*;
import resource.Resource;

import java.awt.Font;

public class LoginWindow extends JFrame{
	
	//组件
	private JPanel panel=null;
	private AnImageButton anImageButton=null;
	
	private JLabel labRodingMessage =null;
	
	private AnTextButton btnRegister=null;
	private AnTextButton btnFindPassword=null;
	
	//登录结果回调
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
		setTitle("登录");
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
		btnRegister=new AnTextButton("注册");
		btnRegister.setLocation(10, 234);
		panel.add(btnRegister);
		btnRegister.addActionListener(e -> {
            // TODO Auto-generated method stub
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
        });
		//忘记密码
		btnFindPassword=new AnTextButton("忘记密码");
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
		
		
		
		//找回密码
		
		
		// TODO Auto-generated constructor stub
		init();
	}


	private void showMesage(){
		labRodingMessage.setVisible(true);
	}
	
	private void login() {
		showMesage();
		if(resultCallback!=null) {
			resultCallback.loginResult(user.getText(), password.getText());
		}
	}
}
