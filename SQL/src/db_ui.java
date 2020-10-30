
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemListener;
import java.io.IOException;
import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import java.sql.*;


public class db_ui {
	static String msg =" ";
	static String select_msg = "";
	static String select_msg2 = "";
	static int count_select = 0;
	static String db_result = "";
	static String state_msg = "";
	
	static String pwd = "dlfgns12"; // 이부분만 바꿔주세요
	
	public static void DB_Access() throws SQLException, IOException {
		Connection conn = null;
		state_msg = "";
		db_result = "";
        // 연결
        try {
            Class.forName("com.mysql.cj.jdbc.Driver"); //JDBC 드라이버 연결
            // 접속 url과 사용자, 비밀번호
            String url="jdbc:mysql://localhost:3306/mydb?serverTimezone=UTC";
            String user="root";
            conn = DriverManager.getConnection(url, user, pwd);
            System.out.println("정상적으로 연결되었습니다.");

        } catch (SQLException e) {
            System.err.println("연결할 수 없습니다.");
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            System.err.println("드라이버를 로드할 수 없습니다.");
            e.printStackTrace();
        }
        
        String select = msg + " ";
        String from = "employee";
        //String where = "123456789";
        // 조건추가...
        
        int fromIndex = -1;
        while ((fromIndex = select.indexOf(",", fromIndex + 1)) >= 0) {
          count_select++;
        }
        
        String stmt1="select "+ select + "from "+ from;  //+" where "+"ssn ="+"?";
        PreparedStatement p=conn.prepareStatement(stmt1);
        //System.out.println(p);
        
        //System.out.println("Enter a Social Security Number: ");
        //Statement의 첫번째 ?에 넣는다.
        p.clearParameters();
        //System.out.println(p);
        
        

        //p.setNString(1, where);
        //System.out.println(p);
        
        ResultSet r=p.executeQuery();
        //System.out.println(r);
        
        // select 에 super_ssn 이나 Dno이 들어가있으면
        // if문 처리해서 super_ssn -> select e.name from employee e, employee f where f.super_ssn = e.ssn
        // select f.ssn ,e.Fname, e.Minit, e.Lname from employee e, employee f where f.Super_ssn = e.Ssn;
        // 
        // dno만 들어간 경우
        // select /select/ ,Dname from department,employee where Dnumber = Dno;
        //
        // dno, super_ssn 둘다 들어간 경우
        // select f.Fname, f.Minit, f.Lname, f.ssn ,e.Fname, e.Minit, e.Lname, Dname  from employee e, employee f, department where f.Super_ssn = e.Ssn and f.Dno=Dnumber;
        
        // update employee set Salary = "value" where ssn = 'target_ssn';
        
        while(r.next()){
        	String result = "";
        	for(int i=0; i<count_select+1; i++)
        		result += r.getString(i+1) + "  ";
        		db_result += result+"\n";
			//System.out.println(result);
        }
        
        // 해제
        try {
            if (conn != null)
                conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        // db 연결해서 select하는거 어떻게?

	}
	
    static class setGUI extends JFrame{
        setGUI() throws SQLException, IOException{
        	
            setTitle("직원 검색용 프로그램");
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            
            this.setLayout(new FlowLayout());
            
            String total_depart[] = {"전체", "부서별"};
            String depart[] = {"전체", "research"};
            // deptart에 select DEPARTMENT from department
            
            JComboBox<String> combo1 = new JComboBox<String>(total_depart);
            add( combo1, BorderLayout.NORTH);

            JComboBox<String> combo2 = new JComboBox<String>(depart);
            // String DB_all_depart = select department from * 
            // db의 모든 depart를 불러와서 
            // for  combo2.additem(DB_all_depart[i]); 같이 넣어 줄 수 있을것 같네요
            add(combo2, BorderLayout.NORTH);
            combo2.setEnabled(false);
            
            JCheckBox chk1 = new JCheckBox("name",false);
            JCheckBox chk2 = new JCheckBox("ssn",false);
            JCheckBox chk3 = new JCheckBox("Bdate",false);
            JCheckBox chk4 = new JCheckBox("Address",false);
            JCheckBox chk5 = new JCheckBox("Sex",false);
            JCheckBox chk6 = new JCheckBox("Salary",false);
            JCheckBox chk7 = new JCheckBox("super_ssn",false);
            JCheckBox chk8 = new JCheckBox("Dno",false);

            this.add(chk1);
            this.add(chk2);
            this.add(chk3);            
            this.add(chk4);
            this.add(chk5);
            this.add(chk6);      
            this.add(chk7);
            this.add(chk8);

            JButton summit_button = new JButton("검색");
            this.add(summit_button);
            // 기본적인 check box 넣기, 검색버튼
            
            JPanel panel = new JPanel();
            
            
            String[] header = new String[0];
            String [][] contents = new String[0][];
            DefaultTableModel model = new DefaultTableModel(contents,header);
            JTable table =  new JTable(model);
            JScrollPane jscp1 = new JScrollPane(table);
            jscp1.setPreferredSize (new Dimension(1380,400));
            panel.add(jscp1);
            
            JButton choose_button = new JButton("선택");
            panel.add(choose_button,BorderLayout.SOUTH); // 왜 아래쪽에 안나오는지..?

            this.add(panel,BorderLayout.CENTER);

            JTextArea txt  = new JTextArea(10,80);
            this.add(txt);
            
            ActionListener summit_listener = new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					// TODO Auto-generated method stub
					System.out.println("검색 ");
					state_msg = "";
					state_msg += "검색" + "\n";
					msg =" ";
					select_msg = "";
					select_msg2 = "";
					count_select = 0;
					if(combo1.getSelectedItem().toString() == "전체") {
		            	combo2.setEnabled(false);
		            } else {
		            	combo2.setEnabled(true);
		            }
					
					if(chk1.isSelected()) {
						msg += "Fname Minit Lname ";
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
						msg += "Super_ssn ";
					}
					if(chk8.isSelected()) {
						msg += "Dno ";
					}
					select_msg += combo1.getSelectedItem().toString();
					select_msg2 += combo2.getSelectedItem().toString();
					if(select_msg.equals("전체")) {
						select_msg2 = "";
					} else if(select_msg.equals("부서별")) {
						select_msg = "";
					}
					msg= msg.replace(" ", ",");	
					msg = msg.substring(0, msg.length() - 1);
					if(msg.length()>0) {
						msg = msg.substring(1, msg.length());
					}
					System.out.println("select -" + msg);
					state_msg += "select -" + msg +"\n";
					System.out.println("from -" + " " + select_msg + select_msg2);
					state_msg += "from -" + " " + select_msg + select_msg2 + "\n";
					txt.setText(state_msg);
					
					try {
						DB_Access();
						//System.out.println(msg);
						//System.out.println(db_result);
						//textbox.append(msg+"\n");
						//textbox.append(db_result);
						String array[] = msg.split(",");
						String array2[] = db_result.split("\n");
						for(int i=0; i<array2.length; i++) {
							System.out.println(array2[i]);
						}
						String[][] array3 = new String[array2.length][];
						for(int i=0; i<array2.length; i++) {
							array3[i] = array2[i].split("  ");
						}
						//array는 전처리리된 헤더
						//array3 는 전처리된 데이터
						
						table.setModel(new DefaultTableModel(array3,array));
						
						// Jtable에 체크박스 달기... 
						// ex.. https://i.stack.imgur.com/SmVEG.jpg

						table.repaint();
					} catch (SQLException | IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
            };
            
            
            ActionListener combo1_listener = new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					// TODO Auto-generated method stub
					if(combo1.getSelectedItem().toString() == "전체") {
		            	combo2.setEnabled(false);
		            } else {
		            	combo2.setEnabled(true);
		            }
				}
            };
            summit_button.addActionListener(summit_listener);
            combo1.addActionListener(combo1_listener);
            setSize(1500, 800);
            setVisible(true);
        }
    }
    
    public static void main(String[] args) throws SQLException, IOException{
        new setGUI();
    }

}
