
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemListener;

import javax.swing.*; 
 
public class db_ui {
    static class setGUI extends JFrame{
        setGUI(){
            setTitle("���� �˻��� ���α׷�");
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            
            this.setLayout(new FlowLayout());
            
            String total_depart[] = {"��ü", "�μ���"};
            String depart[] = {"��ü", "research"};
            
            JComboBox<String> combo1 = new JComboBox<String>(total_depart);
            add( combo1, BorderLayout.NORTH);

            JComboBox<String> combo2 = new JComboBox<String>(depart);
            // String DB_all_depart = select department from * 
            // db�� ��� depart�� �ҷ��ͼ� 
            // for  combo2.additem(DB_all_depart[i]); ���� �־� �� �� ������ ���׿�
            add(combo2, BorderLayout.NORTH);
            combo2.setEnabled(false);
            
            JCheckBox chk1 = new JCheckBox("name",false);
            JCheckBox chk2 = new JCheckBox("ssn",false);
            JCheckBox chk3 = new JCheckBox("Bdate",false);
            JCheckBox chk4 = new JCheckBox("Address",false);
            JCheckBox chk5 = new JCheckBox("Sex",false);
            JCheckBox chk6 = new JCheckBox("Salary",false);
            JCheckBox chk7 = new JCheckBox("super_ssn",false);
            JCheckBox chk8 = new JCheckBox("Dname",false);

            this.add(chk1);
            this.add(chk2);
            this.add(chk3);            
            this.add(chk4);
            this.add(chk5);
            this.add(chk6);      
            this.add(chk7);
            this.add(chk8);

            JButton summit_button = new JButton("�˻�");
            this.add(summit_button);
            // �⺻���� check box �ֱ�, �˻���ư
            
            ActionListener summit_listener = new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					// TODO Auto-generated method stub
					System.out.println("�˻� ");
					String msg =" ";
					
					if(combo1.getSelectedItem().toString() == "��ü") {
		            	combo2.setEnabled(false);
		            } else {
		            	combo2.setEnabled(true);
		            }
					
					if(chk1.isSelected()) {
						msg += "name ";
					}
					if(chk2.isSelected()) {
						msg += "ssn ";
					}
					if(chk3.isSelected()) {
						msg += "Bdate ";
					}
					if(chk4.isSelected()) {
						msg += "Address ";
					}
					if(chk5.isSelected()) {
						msg += "Sex ";
					}
					if(chk6.isSelected()) {
						msg += "Salary ";
					}
					if(chk7.isSelected()) {
						msg += "Super ssn  ";
					}
					if(chk8.isSelected()) {
						msg += "Dname ";
					}
					String select_msg = "";
					select_msg += combo1.getSelectedItem().toString();
					String select_msg2 = "";
					select_msg2 += combo2.getSelectedItem().toString();
					if(select_msg.equals("��ü")) {
						select_msg2 = "";
					}
					System.out.println("select -" + msg);
					System.out.println("from -" + select_msg + select_msg2);
				}
            }; 
            // �˻� ��ư�� �����ʸ� �޾Ƽ� �˻��ϰ� �Ǹ� select �� �κа� from �� �κ��� �����ɴϴ�.
            // �����δ� db�� �����ؼ� �� msg�� �迭ȭ ���Ѽ� select �迭 from �迭 ���� ����ϸ� �� �� ���׿�
            ActionListener combo1_listener = new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					// TODO Auto-generated method stub
					if(combo1.getSelectedItem().toString() == "��ü") {
		            	combo2.setEnabled(false);
		            } else {
		            	combo2.setEnabled(true);
		            }
				}
            };
            summit_button.addActionListener(summit_listener);
            combo1.addActionListener(combo1_listener);

            setSize(900, 400);
            setVisible(true);
        }
    }
    
    public static void main(String[] args){
        new setGUI();
    }
}
