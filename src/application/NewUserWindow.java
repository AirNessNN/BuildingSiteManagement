package application;

import javax.swing.JFrame;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JLabel;
import javax.swing.JOptionPane;

import SwingTool.MyButton;
import compoent.AnTextField;

import java.awt.Font;
import java.awt.Color;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class NewUserWindow extends JFrame{
	
	interface IUserCallback{
		public void callback(User user);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private static NewUserWindow window=null;
	
	private IUserCallback callback=null;
	private AnTextField boxUser;
	private AnTextField boxPassword;
	private AnTextField boxCheckPassword;
	private AnTextField boxQuestion;
	private AnTextField boxAnswer;
	
	
	//���
	
	
	
	//����
	private NewUserWindow() {
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosed(WindowEvent e) {
				dispose();
				window=null;
			}
		});
		init();
		getContentPane().setBackground(Color.WHITE);
		setResizable(false);
		setIconImage(Toolkit.getDefaultToolkit().getImage(NewUserWindow.class.getResource("/resource/login.png")));
		getContentPane().setLayout(null);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		
		JLabel label = new JLabel("\u6CE8\u518C\u7BA1\u7406\u8D26\u6237");
		label.setFont(new Font("΢���ź� Light", Font.PLAIN, 30));
		label.setBounds(10, 10, 305, 41);
		getContentPane().add(label);
		
		JLabel label_1 = new JLabel("������ʹ�ô��˻������ص��ʲ�����Ա�͹�������");
		label_1.setFont(new Font("΢���ź� Light", Font.PLAIN, 12));
		label_1.setBounds(12, 60, 474, 15);
		getContentPane().add(label_1);
		
		MyButton btnAchieve = new MyButton("\u5B8C\u6210");
		btnAchieve.setBounds(368, 297, 93, 23);
		getContentPane().add(btnAchieve);
		//ȷ���¼�
		btnAchieve.addActionListener(e -> {
            // TODO Auto-generated method stub
            String userName,password,passwordCheck,question,answer;
            userName=boxUser.getText();
            password=boxPassword.getText();
            passwordCheck=boxCheckPassword.getText();
            question=boxQuestion.getText();
            answer=boxAnswer.getText();

            //�û���
            if(userName.length()==0) {
                boxUser.setErrorBorder();
                JOptionPane.showMessageDialog(boxUser, "����д�û���","ע����ʾ",JOptionPane.ERROR_MESSAGE);
                return;
            }

            //�����
            if(password.length()>=6) {
                for(int i=0;i<password.length();i++) {
                    char c=password.charAt(i);
                    if(!(c>='a'&&c<='z'||c>='A'&&c<='Z'||c>='0'&&c<='9')) {
                        boxPassword.setErrorBorder();
                        JOptionPane.showMessageDialog(boxPassword, "���벻����Ҫ��","ע����ʾ",JOptionPane.ERROR_MESSAGE);
                        break;
                    }
                }
            }else {
                boxPassword.setErrorBorder();
                JOptionPane.showMessageDialog(boxPassword, "���벻����Ҫ��","ע����ʾ",JOptionPane.ERROR_MESSAGE);
                return;
            }
            //ȷ�������
            if(!password.equals(passwordCheck)) {
                boxCheckPassword.setErrorBorder();
                JOptionPane.showMessageDialog(boxCheckPassword, "������������벻��ȷ","ע����ʾ",JOptionPane.ERROR_MESSAGE);
                return;
            }

            //�����
            if(question.length()==0) {
                boxQuestion.setErrorBorder();
                JOptionPane.showMessageDialog(boxQuestion, "����д����","ע����ʾ",JOptionPane.ERROR_MESSAGE);
                return;
            }

            //�𰸿�
            if(answer.length()==0) {
                boxAnswer.setErrorBorder();
                JOptionPane.showMessageDialog(boxAnswer, "����д����","ע����ʾ",JOptionPane.ERROR_MESSAGE);
                return;
            }

            //ȫ���Ѿ�������
            User user=new User();
            user.userName=userName;
            user.password=password;
            user.question=question;
            user.answer=answer;
            if(callback!=null) {
                callback.callback(user);
            }
            dispose();
            window=null;
        });
		
		MyButton btnCanncel = new MyButton("\u53D6\u6D88");
		btnCanncel.setBounds(473, 297, 93, 23);
		getContentPane().add(btnCanncel);
		btnCanncel.addActionListener(e -> {
            // TODO Auto-generated method stub
            dispose();
            window=null;
        });
		
		JLabel lblNewLabel = new JLabel("\u7528\u6237\u540D\uFF1A");
		lblNewLabel.setFont(new Font("΢���ź�", Font.PLAIN, 14));
		lblNewLabel.setBounds(10, 113, 66, 18);
		getContentPane().add(lblNewLabel);
		
		boxUser = new AnTextField();
		boxUser.setFont(new Font("΢���ź�", Font.PLAIN, 12));
		boxUser.setBounds(89, 113, 114, 22);
		getContentPane().add(boxUser);
		boxUser.setColumns(10);
		boxUser.setMaxLength(10);
		
		boxPassword = new AnTextField();
		boxPassword.setBounds(89, 144, 114, 22);
		getContentPane().add(boxPassword);
		boxPassword.setColumns(10);
		
		JLabel lblNewLabel_1 = new JLabel("\u5BC6\u7801\uFF1A");
		lblNewLabel_1.setFont(new Font("΢���ź�", Font.PLAIN, 14));
		lblNewLabel_1.setBounds(10, 146, 66, 18);
		getContentPane().add(lblNewLabel_1);
		
		JLabel label_2 = new JLabel("\u786E\u8BA4\u5BC6\u7801\uFF1A");
		label_2.setFont(new Font("΢���ź�", Font.PLAIN, 14));
		label_2.setBounds(10, 177, 80, 18);
		getContentPane().add(label_2);
		
		boxCheckPassword = new AnTextField();
		boxCheckPassword.setBounds(89, 175, 114, 22);
		getContentPane().add(boxCheckPassword);
		boxCheckPassword.setColumns(10);
		
		JLabel lblNewLabel_2 = new JLabel("��������10���ַ���Ϊ��������ס���뾡��ʹ��Ӣ�Ļ����֣�");
		lblNewLabel_2.setFont(new Font("΢���ź�", Font.PLAIN, 12));
		lblNewLabel_2.setBounds(221, 115, 345, 18);
		getContentPane().add(lblNewLabel_2);
		
		JLabel label_3 = new JLabel("\uFF08\u5B57\u6BCD\u6216\u6570\u5B57\uFF0C\u4E0D\u8D85\u8FC720\u4E2A\uFF0C\u4E0D\u5C0F\u4E8E6\u4E2A\uFF09");
		label_3.setFont(new Font("΢���ź�", Font.PLAIN, 12));
		label_3.setBounds(221, 147, 345, 18);
		getContentPane().add(label_3);
		
		JLabel label_4 = new JLabel("\uFF08\u786E\u8BA4\u60A8\u7684\u5BC6\u7801\uFF09");
		label_4.setFont(new Font("΢���ź�", Font.PLAIN, 12));
		label_4.setBounds(221, 179, 345, 18);
		getContentPane().add(label_4);
		
		JLabel label_5 = new JLabel("\u95EE\u9898\uFF1A");
		label_5.setFont(new Font("΢���ź�", Font.PLAIN, 14));
		label_5.setBounds(10, 246, 60, 18);
		getContentPane().add(label_5);
		
		boxQuestion = new AnTextField();
		boxQuestion.setBounds(89, 246, 274, 22);
		getContentPane().add(boxQuestion);
		boxQuestion.setColumns(10);
		boxQuestion.setMaxLength(50);
		
		JLabel lblNewLabel_3 = new JLabel("\uFF08\u7528\u4E8E\u627E\u56DE\u5BC6\u7801\uFF09");
		lblNewLabel_3.setFont(new Font("΢���ź�", Font.PLAIN, 12));
		lblNewLabel_3.setBounds(10, 276, 131, 18);
		getContentPane().add(lblNewLabel_3);
		
		JLabel label_6 = new JLabel("\u7B54\u6848\uFF1A");
		label_6.setFont(new Font("΢���ź�", Font.PLAIN, 14));
		label_6.setBounds(381, 246, 53, 18);
		getContentPane().add(label_6);
		
		boxAnswer = new AnTextField();
		boxAnswer.setBounds(452, 245, 114, 22);
		getContentPane().add(boxAnswer);
		boxAnswer.setColumns(10);
		boxAnswer.setMaxLength(50);
		// TODO Auto-generated constructor stub

	}
	
	
	//����ģʽ�ķ���
	public static NewUserWindow getWindow() {
		if(window==null) {
			window=new NewUserWindow();
		}
		return window;
	}
	
	
	
	
	//����
	/**
	 * ��ʼ��
	 */
	private void init() {
		this.setTitle("���û�ע��");
		this.setSize(584, 361);
	}
	
	
	
	/**
	 * �������ݻص�
	 * @param callback
	 */
	public void setCallback(IUserCallback callback) {
		this.callback=callback;
	}
	
	
	
	/**
	 * ��տؼ�����
	 */
	public void clearCompoent() {
		boxAnswer.setText("");
		boxCheckPassword.setText("");
		boxPassword.setText("");
		boxQuestion.setText("");
		boxUser.setText("");
	}
}
