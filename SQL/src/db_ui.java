
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
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;

import java.sql.*;




public class db_ui {
	
	static String msg =" ";
	static String select_msg = "";
	static String select_msg2 = "";
	static int count_select = 0;
	static String db_result = "";
	static String state_msg = "";
	static int lock = 1;
	static String pwd = "dlfgns12"; // 이부분만 바꿔주세요
	static String select_depart = "";
	static String table_header = "";
	static String table_contents = "";
	static String show_table_msg = "";
	static String choose_button_get = "";
	

	public static String DB_Access() throws SQLException, IOException {
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
        
        // lock이 0이면 이것을 실행
        // db에 select Dname from department 실행하여 Dname들을 가져옴
        if(lock == 0) {
        	String stmt1="select Dname from department";
        	PreparedStatement p=conn.prepareStatement(stmt1);
        	p.clearParameters();
        	ResultSet r=p.executeQuery();
        	//System.out.println(r.getString(1));
        	while(r.next()){
            	String result = "";
            	for(int i=0; i<count_select+1; i++) 
            	result += r.getString(i+1) + " ";
            	db_result += result;
            	//System.out.println(result);
            }
        	//System.out.println(db_result);
        	
        	Statement stmt = conn.createStatement();
            //Retrieving the data
            ResultSet rs = stmt.executeQuery("Show tables");
            while(rs.next()) {
            	show_table_msg += rs.getString(1) + " ";
            }   	
        	return db_result;
        }
        
        if(lock == 2) {
        	String stmt1="select * from " + choose_button_get;
        	PreparedStatement p=conn.prepareStatement(stmt1);
        	p.clearParameters();
        	ResultSet r=p.executeQuery();
        	
        	String[] columnNames = null;
            ResultSetMetaData rsmd = r.getMetaData();
            int columnCount = rsmd.getColumnCount(); 
            columnNames = new String[columnCount]; 
            for(int i=1; i<=columnCount; i++) {
                // Put column name into array
                columnNames[i-1] = rsmd.getColumnName(i); 
            }
            for(int i=1; i<=columnCount; i++) {
                // Put column name into array
                columnNames[i-1] = rsmd.getColumnName(i); 
                //System.out.println(columnNames[i-1]);
                table_header += rsmd.getColumnName(i) + " "; 
            }
            
            //System.out.println(table_header);

            r=p.executeQuery();
            while(r.next()){
            	String result = "";
            	for(int i=0; i<columnCount; i++) {
            		//System.out.println(r.getString(i+1));
            		result += r.getString(i+1) + "  ";
            	}
            	table_contents += result + "\n";
            	//System.out.println(result);
            }
            //System.out.println(table_contents);
        	return "";
        }
        
        
        String select = msg + " ";
        String from = "employee";
        //String where = "123456789";
        // 조건추가...
        
        int fromIndex = -1;
        while ((fromIndex = select.indexOf(",", fromIndex + 1)) >= 0) {
          count_select++;
        }
        
        int dno =0;
        int super_ssn=0;
        if(msg.contains("Dno")) {
        	dno =1;
        	//System.out.println("dno is in");
        }
        if(msg.contains("Super_ssn")) {
        	super_ssn =1;
        	//System.out.println("super_ssn is in");
        }
        String stmt1="";
        if(dno == 0 && super_ssn==0) {
        	stmt1="select "+ select + "from "+ from;  //+" where "+"ssn ="+"?";
        }
        if(dno == 1 && super_ssn==0) {
        	select = select.replaceAll("Dno", "");
        	stmt1="select "+ select + "Dname from department,employee where Dnumber = Dno";
        	msg = msg.replaceAll("Dno", "Dname");
        }
        if(dno == 0 && super_ssn==1) {
        	select = select.replaceAll(",Super_ssn", "");
        	select = "f."+select;
        	select = select.substring(0, select.length()-1);
        	select = select.replaceAll(",", ",f.");
        	//System.out.print("find - ");
        	//System.out.println(select);
        	stmt1 = "SELECT "+select+" ,CONCAT_WS(' ', e.Fname, e.Minit, e.Lname) from employee e,employee f where e.Ssn = f.super_ssn";
        	stmt1 = stmt1.replaceAll("f.Super_ssn ,", "");
        }
        if(dno == 1 && super_ssn==1) {
        	// SELECT f.Fname, f.ssn, CONCAT_WS(' ', e.Fname, e.Minit, e.Lname) AS Supervisior, Dname FROM employee e,employee f, department where e.Ssn = f.super_ssn and e.Dno = Dnumber;
        	select = select.replaceAll(",Super_ssn", "");
        	select = "f."+select;
        	select = select.substring(0, select.length()-1);
        	select = select.replaceAll(",", ",f.");
        	select = select.replaceAll("f.Super_ssn,","");
        	//System.out.print("find - ");
        	//System.out.println(select);
        	stmt1 = "SELECT "+select+" ,CONCAT_WS(' ', e.Fname, e.Minit, e.Lname), Dname from employee e,employee f, department where e.Ssn = f.super_ssn";
        	stmt1 = stmt1.replaceAll("f.Dno","");
        	stmt1 = stmt1.replaceAll(",C","C");
        	stmt1 = stmt1 + " and f.Dno = Dnumber ";
        	System.out.println(stmt1);
        	//System.out.println(select);
        	//System.out.println(stmt1);
        }
        
        if(!select_depart.equals("전체")) {
        	if(!stmt1.contains("department")) {
        		stmt1 = stmt1.replaceAll("from", "from department,");
        	}
        	if(!stmt1.contains("where")) {
        		stmt1 = stmt1+ " where Dnumber = dno and Dname = " + '"' + select_depart + '"';
        	} else {
        		if(!stmt1.contains("f.")) {
        			stmt1 = stmt1.replaceAll("where", " where Dnumber = dno and Dname = " + '"' + select_depart + '"' + " and");
            	} else {
            		stmt1 = stmt1.replaceAll("where", " where Dnumber = f.dno and Dname = " + '"' + select_depart + '"' + " and "  );
            	}
        	}
        }
        
        System.out.println(stmt1);
        System.out.println("\n\n\n");
        
        // select f.ssn ,e.Fname, e.Minit, e.Lname from employee e, employee f where f.Super_ssn = e.Ssn;
       
        // SELECT f.Fname, f.Lname, f.ssn, CONCAT_WS(' ', e.Fname, e.Minit, e.Lname) AS Supervisior FROM employee e,employee f where e.Ssn = f.super_ssn;
        // SELECT Fname,Minit,Lname,ssn, CONCAT_WS(' ', e.Fname, e.Minit, e.Lname) AS Supervisior FROM employee e,employee f where e.Ssn = f.super_ssn
        
        
        // select Dname from department,employee where Dnumber = Dno;

        // select f.Fname, f.Minit, f.Lname, f.ssn ,e.Fname, e.Minit, e.Lname, Dname  from employee e, employee f, department where f.Super_ssn = e.Ssn and f.Dno=Dnumber;
        
        // update employee set Salary = "value" where ssn = 'target_ssn';

        PreparedStatement p = conn.prepareStatement(stmt1);
        //System.out.println(p);
        //System.out.println("Enter a Social Security Number: ");
        //Statement의 첫번째 ?에 넣는다.
        p.clearParameters();
        //System.out.println(p);
        //p.setNString(1, where);
        //System.out.println(p);
        ResultSet r=p.executeQuery();
        //System.out.println(r);

        
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
		return stmt1;
	}
	
    static class setGUI extends JFrame{
        setGUI() throws SQLException, IOException{
        	
            setTitle("직원 검색용 프로그램");
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            this.setLayout(new FlowLayout());
            String total_depart[] = {"전체", "부서별"};
            // deptart에 select DEPARTMENT from department
            lock = 0;
            String department_list = DB_Access();
            lock = 1;
            // lock 하고 DB_Access를 하게 되면 db에 접속해서 
            // 가져온 Dname들을 department_list에 넣어서 스크롤바를 만들어줌
            
            department_list = "전체 " + department_list;
            //System.out.println(department_list);
            String depart[] = department_list.split(" ");
            
            JComboBox<String> combo1 = new JComboBox<String>(total_depart);
            add( combo1, BorderLayout.NORTH);
            
            JComboBox<String> combo2 = new JComboBox<String>(depart);
            // department_list에는 research같은 값을 넣은적이 없지만, db에서 가져온 dname을 넣어준것
            
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
            JButton choose_button = new JButton("선택");
            this.add(choose_button);
            
            JPanel panel1 = new JPanel();
            JPanel panel = new JPanel();
            String[] header = new String[0];  // 헤더 = columm 설정
            String[] show_table_header = new String[0];
            String [][] contents = new String[0][]; // content는 안에 들어갈 내용물들
            DefaultTableModel model = new DefaultTableModel(contents,header); // 테이블모델 선언
            DefaultTableModel show_table_model = new DefaultTableModel(contents,show_table_header);
            JTable table =  new JTable(model); // 테이블을 만들고 그 안에 바로 앞에 선언한 model을 넣어줌
            JTable show_table =  new JTable(show_table_model);
            JScrollPane jscp1 = new JScrollPane(table);
            JScrollPane jscp2 = new JScrollPane(show_table);

            
            jscp1.setPreferredSize (new Dimension(1380,200));
            jscp2.setPreferredSize (new Dimension(1380,200));
            jscp1.setBorder(BorderFactory.createTitledBorder("Search"));
            this.add(jscp1);
            
            String show_table_radio[] = show_table_msg.split(" ");
            JRadioButton radio[] = new JRadioButton[show_table_radio.length];
            ButtonGroup group = new ButtonGroup();
            for(int i=0; i<show_table_radio.length; i++) {
            	radio[i] = new JRadioButton(show_table_radio[i]);
            	group.add(radio[i]);
            	panel.add(radio[i]);
            }
            
            panel.setPreferredSize (new Dimension(1000,30));
            this.add(panel); // 라디오 버튼

            JPanel panel2 = new JPanel();
            
            panel2.add(jscp2,BorderLayout.SOUTH);
            panel2.setBorder(BorderFactory.createTitledBorder("Tables"));
            this.add(panel2); // tables
            
            JTextArea txt  = new JTextArea(10,40);
            panel1.setBorder(BorderFactory.createTitledBorder("상태창"));
            panel1.add(txt,BorderLayout.SOUTH);
            this.add(panel1); // 입력창
            
            JPanel empty_space = new JPanel();
            empty_space.setBorder(BorderFactory.createTitledBorder("삭제"));
            empty_space.setPreferredSize (new Dimension(460,210));
            empty_space.setBackground(Color.YELLOW);
            this.add(empty_space);
            
            JPanel empty_space2 = new JPanel();
            empty_space2.setBorder(BorderFactory.createTitledBorder("삽입"));
            empty_space2.setPreferredSize (new Dimension(460,210));
            empty_space2.setBackground(Color.GREEN);
            this.add(empty_space2);
            // 화면구성 
            
            ActionListener show_table_contents = new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					table_header = "";
					table_contents = "";
					choose_button_get = e.getActionCommand();
	            	lock = 2;
	            	try {
						DB_Access();
					} catch (SQLException | IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
	            	lock = 1;
	            	
	            	// show_table_header에는 값?
	            	// contetns에는 내용 
	            	state_msg = "you choose " + choose_button_get;
	            	txt.setText(state_msg);
	            	
	            	String table_header_split[] = table_header.split(" ");
	            	String table_content_split[] = table_contents.split("\n");
	            	String[][] table_contents_split = new String[table_content_split.length][];
	            	for(int i=0; i<table_content_split.length; i++) {
	            		table_contents_split[i] = table_content_split[i].split("  ");
	            	}
	            	show_table.setModel(new DefaultTableModel(table_contents_split,table_header_split));
	            	show_table.repaint();
				}
            };
            
            for(int i=0; i<show_table_radio.length; i++) {
            	radio[i].addActionListener(show_table_contents);
            }
            
            ActionListener select_person = new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					// TODO Auto-generated method stub
					state_msg = "";
					state_msg = "you press 선택 button";
					
					
					
					
					
					
					
					
					
					
					txt.setText(state_msg);
				}
            };
            
            choose_button.addActionListener(select_person);
            
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
					select_depart = "";
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
					
					select_depart = select_msg + select_msg2;
					
					
					
					if(msg.equals("")) {
						msg = "Fname,Minit,Lname,ssn,Bdate,Address,Sex,Salary,Super_ssn,Dno";
					}
					
					try {
						DB_Access();
						//System.out.println(msg);
						//System.out.println(db_result);
						//textbox.append(msg+"\n");
						//textbox.append(db_result);
						msg = msg.replaceAll("Super_ssn", "Supervisor");
						msg = msg.replaceAll("Dno", "Department");
						String array[] = msg.split(",");
						String array2[] = db_result.split("\n");
						
						for(int i=0; i<array2.length; i++) {
							//System.out.println(array2[i]);
						}
						String[][] array3 = new String[array2.length][];
						for(int i=0; i<array2.length; i++) {
							array3[i] = array2[i].split("  ");
						}
						//array는 전처리리된 헤더
						//array3 는 전처리된 데이터
						
						table.setModel(new DefaultTableModel(array3,array));
						table.repaint();
						// Jtable에 체크박스 달기... 
						// ex.. https://i.stack.imgur.com/SmVEG.jpg

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