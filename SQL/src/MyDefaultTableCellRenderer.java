
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.Component;
import java.io.IOException;
import java.sql.SQLException;

import javax.swing.AbstractButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;


public class MyDefaultTableCellRenderer extends DefaultTableCellRenderer {
	static int select_row_table=0;
	@Override
	public Component getTableCellRendererComponent(JTable table, Object value,boolean isSelected, boolean hasFocus, int row, int column) {
		Component comp = null;
		comp = new JCheckBox();
		db_ui test2 = new db_ui();
		
		//System.out.println(test2.msg);
		/*
		if(!test2.msg.contains("Fname") || !test2.msg.contains("ssn")) {
			((AbstractButton) comp).setEnabled(true);
		} */
		
		if(isSelected == true) {
			System.out.println(test2.msg);
			((AbstractButton) comp).setSelected(true);
			if(test2.msg.contains("Fname") && test2.msg.contains("ssn")) {
				test2.txt.setText("선택한 직원 : " + test2.array3[row][0] + " " + test2.array3[row][1] + " " + test2.array3[row][2]);
				test2.selected_employee = test2.array3[row][0] + " " + test2.array3[row][1] + " " + test2.array3[row][2];
				test2.throwed_ssn = test2.array3[row][3];
				
				try {
					test2.lock = 11;
					String DB_get_salary = test2.DB_Access();
					test2.lock = 1;
					test2.get_salary.setText(DB_get_salary);
				} catch (SQLException | IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				//test2.get_salary.setText = "";
			} else {
				((AbstractButton) comp).setSelected(false);
				//System.out.println("직원 선택을 하려면 Name과 SSN을 체크해주세요");
				test2.txt.setText("직원 선택을 하려면 Name과 SSN을 체크해주세요");
			}
		}
		return comp;
	}
	
	public void select_row_table_button() throws SQLException, IOException {
		db_ui test1 = new db_ui();
		test1.seleted_table_button = select_row_table;
		//System.out.println(test1.seleted_table_button);
		String column_employee = "";
		for(int i =0; i< test1.array3[test1.seleted_table_button].length; i++) {
			column_employee += test1.array3[test1.seleted_table_button][i] + " ";
		}
		if(test1.msg.contains("Fname")) {
			column_employee = test1.array3[test1.seleted_table_button][0] +" "+ test1.array3[test1.seleted_table_button][1] +" "+ test1.array3[test1.seleted_table_button][2];
			String throw_ssn = test1.array3[test1.seleted_table_button][3];
		} else {
			String throw_ssn = test1.array3[test1.seleted_table_button][0];
			
		}
		test1.txt.setText("직원 선택 : " + column_employee);

	}
}