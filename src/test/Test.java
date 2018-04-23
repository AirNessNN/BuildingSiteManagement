package test;

import component.AnComboBoxEditor;

import javax.swing.JFrame;
import javax.swing.JScrollPane;

import application.AnUtils;
import component.AnDateComboPanel;
import component.AnTable;

import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;
import java.awt.Font;

public class Test extends JFrame{
	String[] names= {
			"asdasda",
			"asqweqweq"
	};
	String[][] data={
			{"1","2","3","4"},
			{"5","6","7","8"},
			{"9","10","11","12"}
	};
	
	public Test() {
		getContentPane().setFont(new Font("微软雅黑 Light", Font.PLAIN, 14));
		
		setSize(1049, 800);
		getContentPane().setLayout(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(0, 0, 703, 761);
		getContentPane().add(scrollPane);
		AnTable table=new AnTable();
		scrollPane.setViewportView(table);
		
		JComboBox comboBox = new JComboBox();
		comboBox.setEditable(true);
		comboBox.setModel(new DefaultComboBoxModel(new String[] {"all", "item1", "fuasdkj", "sfnaks", "4546654", "11121", "萨达所"}));
		comboBox.setBounds(835, 6, 143, 28);
		getContentPane().add(comboBox);





		table.setColumn(names);
		table.addColumn("好啊啊");
		table.addColumn("qweqw");
		table.fillData(data);
		table.setCellColumnEdited(0 ,false);

		AnComboBoxEditor editor=new AnComboBoxEditor();
		editor.setEditable(false);
		editor.addItem("哈哈哈哈");
		editor.addItem("嘻嘻嘻嘻");
		editor.addItem("嘿嘿嘿嘿");
		table.addComponentCell(editor,1,3);

		AnDateComboPanel dateComboPanel=new AnDateComboPanel();
		dateComboPanel.setLocation(711, 97);
		getContentPane().add(dateComboPanel);
	}

	
	public static void main(String[] args) {
		
		AnUtils.setLookAndFeel(AnUtils.LOOK_AND_FEEL_NIMBUS);
		
		new Test().setVisible(true);




	}
}
