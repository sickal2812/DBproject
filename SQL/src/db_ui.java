
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
import javax.swing.table.TableColumn;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


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
	static int how_many_employee = 0;
	static int how_many_col = 0;
	static int select_all = 0;
	static String insert_string = "";
	static int seleted_table_button = -1;
	static JTextArea txt  = new JTextArea(13,40);
	static String selected_employee;
	static String[][] array3;
	static String throwed_ssn ;
	static JTextField get_salary = new JTextField(10);
	static JTextField set_salary;
	static String change_value;
	
	public static int set_table_button(int k) {
		seleted_table_button = k;
		return 0;
	}
	
	public  boolean  validationDate(String checkDate){
	   try{
	         SimpleDateFormat  dateFormat = new  SimpleDateFormat("yyyy-MM-dd");

	         dateFormat.setLenient(false);
	         dateFormat.parse(checkDate);
	         return  true;

	       }catch (ParseException  e){
	         return  false;
	       }
	}

	
	public static void delete_employee_where_ssn () throws SQLException, IOException {
		lock = 7;
		DB_Access();
		lock = 1;
	}
	
	
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
        
        if(lock == 7) {
        	String stmt1="delete from employee where ssn = " + throwed_ssn;
        	PreparedStatement p=conn.prepareStatement(stmt1);
        	p.executeLargeUpdate();
        	return "";
        }
        
        if(lock == 20) {
        	String stmt1 = "update employee set salary= " + change_value + " where ssn = " + throwed_ssn;
        	PreparedStatement p=conn.prepareStatement(stmt1);
        	p.executeLargeUpdate();
        	return "";
        }
        
        if(lock == 11) {
        	String stmt1="select salary from employee where ssn = " + throwed_ssn;
        	PreparedStatement p=conn.prepareStatement(stmt1);
        	p.clearParameters();
        	ResultSet r=p.executeQuery();
        	String result = "";
        	while(r.next()){
            	result = "";
            	result += r.getString(1) + " ";
            	db_result += result;
            	//System.out.println(result);
            }
        	return result;
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
        
        if(lock == 3) { // insert
        	String[] slice_insert_string = insert_string.split("  ");
        	String stmt1 = "insert into employee values('"+slice_insert_string[0]+"','"+slice_insert_string[1]+"','"+ slice_insert_string[2]+"','"+ slice_insert_string[3]+"','" + slice_insert_string[4]+"','"+ slice_insert_string[5]+"','"+slice_insert_string[6]+"','"+ slice_insert_string[7]+"','"+ slice_insert_string[8] +"','"+slice_insert_string[9]+"')";
        	System.out.println(stmt1);
        	PreparedStatement p=conn.prepareStatement(stmt1);
        	p.executeUpdate(stmt1);

        	return "";
        	
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
        	if(!select_depart.equals("전체")) {
            	stmt1 = stmt1 + ",department where dno = dnumber and dname = " +  '"' + select_depart + '"';
            }
        }
        if(dno == 1 && super_ssn==0) {
        	select = select.replaceAll("Dno", "");
        	stmt1="select "+ select + "Dname from department LEFT JOIN employee ON Dnumber = Dno";
        	msg = msg.replaceAll("Dno", "Dname");
        	if(!select_depart.equals("전체")) {
            	stmt1 = stmt1 + " where dname = " +  '"' + select_depart + '"';
            }
        }
        if(dno == 0 && super_ssn==1) {
        	select = select.replaceAll(",Super_ssn", "");
        	select = "f."+select;
        	select = select.substring(0, select.length()-1);
        	select = select.replaceAll(",", ",f.");
        	//System.out.print("find - ");
        	//System.out.println(select);
        	stmt1 = "SELECT "+select+" ,CONCAT_WS(' ', e.Fname, e.Minit, e.Lname) from employee f LEFT JOIN employee e ON f.super_ssn = e.ssn";
        	stmt1 = stmt1.replaceAll("f.Super_ssn ,", "");
        	//System.out.println(select);
        	//System.out.println(stmt1);
        	if(!select_depart.equals("전체")) {
            	stmt1 = stmt1 + " LEFT JOIN department ON f.dno = Dnumber AND Dname= " +'"'+ select_depart+'"' + " where Dname IS NOT NULL ";
            }
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
        	stmt1 = "SELECT "+select+" ,CONCAT_WS(' ', e.Fname, e.Minit, e.Lname), Dname from employee f LEFT JOIN employee e ON f.super_ssn = e.ssn LEFT JOIN department ON f.dno = dnumber";
        	stmt1 = stmt1.replaceAll("f.Dno","");
        	stmt1 = stmt1.replaceAll(",C","C");
        	stmt1 = stmt1 + " and f.Dno = Dnumber ";
        	//System.out.println(stmt1);
        	//System.out.println(select);
        	if(!select_depart.equals("전체")) {
        		stmt1 = stmt1 + "where Dname= " +'"'+ select_depart+'"' + " AND Dname IS NOT NULL ";
            }
        
        	System.out.println(stmt1);
        	
        }
        System.out.println(stmt1);
        
        //System.out.println(stmt1);
        System.out.println("\n");
        
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
        	how_many_employee += 1;
        	String result = "";
        	for(int i=0; i<count_select+1; i++) 
        	result += r.getString(i+1) + "  ";
        	db_result += result+"\n";
        	//System.out.println(result);
        }
        //System.out.println(db_result+ "\n\n");
        //System.out.println(db_result);
 
        
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
            this.add(combo1, BorderLayout.NORTH);
            
            JComboBox<String> combo2 = new JComboBox<String>(depart);
            // department_list에는 research같은 값을 넣은적이 없지만, db에서 가져온 dname을 넣어준것
            
            // String DB_all_depart = select department from * 
            // db의 모든 depart를 불러와서 
            // for  combo2.additem(DB_all_depart[i]); 같이 넣어 줄 수 있을것 같네요
            
            this.add(combo2, BorderLayout.NORTH);
            combo2.setEnabled(false);
            JCheckBox chk1 = new JCheckBox("name",false);
            JCheckBox chk2 = new JCheckBox("ssn",false);
            JCheckBox chk3 = new JCheckBox("Bdate",false);
            JCheckBox chk4 = new JCheckBox("Address",false);
            JCheckBox chk5 = new JCheckBox("Sex",false);
            JCheckBox chk6 = new JCheckBox("Salary",false);
            JCheckBox chk7 = new JCheckBox("Supervisor",false);
            JCheckBox chk8 = new JCheckBox("Department",false);
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
            
            
            panel1.setBorder(BorderFactory.createTitledBorder("상태창"));
            panel1.add(txt,BorderLayout.SOUTH);
            this.add(panel1); // 입력창
            
            JPanel empty_space = new JPanel();
            empty_space.setBorder(BorderFactory.createTitledBorder("기능"));
            empty_space.setPreferredSize (new Dimension(460,260));
            empty_space.setBackground(Color.YELLOW);
            
            JButton delete_employee_button = new JButton("선택한 직원 삭제");
            delete_employee_button.setPreferredSize (new Dimension(440,30));
            empty_space.add(delete_employee_button);
            
            JButton edit_salary = new JButton("선택한 직원 급여수정");
            edit_salary.setPreferredSize (new Dimension(440,30));
            JLabel get_salary_label = new JLabel("현재 선택직원 급여: ");
            JLabel set_salary_label = new JLabel("수정할 급여 ");
            set_salary = new JTextField(10);
            empty_space.add(edit_salary);
            
            empty_space.add(get_salary_label);
            empty_space.add(get_salary);
            empty_space.add(set_salary_label);
            empty_space.add(set_salary);
                
            
            this.add(empty_space);
            
            JPanel empty_space2 = new JPanel();
            empty_space2.setBorder(BorderFactory.createTitledBorder("삽입"));
            empty_space2.setPreferredSize (new Dimension(460,260));
            empty_space2.setBackground(Color.GREEN);
            
            JPanel new_insert = new JPanel();
            
            JTextField textPeriod1 = new JTextField(10);
            JLabel labelPeriod1 = new JLabel("Fname: ");
            new_insert.add(labelPeriod1);
            new_insert.add(textPeriod1);
            
            JTextField textPeriod2 = new JTextField(11);
            JLabel labelPeriod2 = new JLabel("Minit: ");
            new_insert.add(labelPeriod2);
            new_insert.add(textPeriod2);
            
            JTextField textPeriod3 = new JTextField(10);
            JLabel labelPeriod3 = new JLabel("Lname: ");
            new_insert.add(labelPeriod3);
            new_insert.add(textPeriod3);
            
            JTextField textPeriod4 = new JTextField(12);
            JLabel labelPeriod4 = new JLabel("ssn: ");
            new_insert.add(labelPeriod4);
            new_insert.add(textPeriod4);
            
            JTextField textPeriod5 = new JTextField(11);
            JLabel labelPeriod5 = new JLabel("Bdate: ");
            new_insert.add(labelPeriod5);
            new_insert.add(textPeriod5);
            
            JPanel new_insert2 = new JPanel();
            
            JTextField textPeriod6 = new JTextField(9);
            JLabel labelPeriod6 = new JLabel("Address: ");
            new_insert2.add(labelPeriod6);
            new_insert2.add(textPeriod6);
            
            JTextField textPeriod7 = new JTextField(12);
            JLabel labelPeriod7 = new JLabel("sex: ");
            new_insert2.add(labelPeriod7);
            new_insert2.add(textPeriod7);
            
            JTextField textPeriod8 = new JTextField(9);
            JLabel labelPeriod8 = new JLabel("Salary: ");
            new_insert2.add(labelPeriod8);
            new_insert2.add(textPeriod8);
            
            JTextField textPeriod9 = new JTextField(8);
            JLabel labelPeriod9 = new JLabel("Super_ssn: ");
            new_insert2.add(labelPeriod9);
            new_insert2.add(textPeriod9);
            
            JTextField textPeriod10 = new JTextField(11);
            JLabel labelPeriod10 = new JLabel("Dno: ");
            new_insert2.add(labelPeriod10);
            new_insert2.add(textPeriod10);
            
            new_insert.setPreferredSize (new Dimension(180,150));
            new_insert2.setPreferredSize (new Dimension(180,150));
            JButton insert_button = new JButton("INSERT EMPLOYEE");

            empty_space2.add(new_insert);
            empty_space2.add(new_insert2);
            empty_space2.add(insert_button);
            this.add(empty_space2);
            // 화면구성 
            
            ActionListener event_edit_employee = new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					//System.out.println("edit");
					String check_regex = "(?<=^| )\\d+(\\.\\d+)?(?=$| )";
					change_value = set_salary.getText();
					if(change_value.matches(check_regex)) {
						if(change_value.contains(".")) {
						} else {
							change_value = change_value + ".00";
						}
						try {
							lock = 20;
							DB_Access();
							lock = 1;
						} catch (SQLException | IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						//System.out.println(throwed_ssn);
						//System.out.println("correct");
					} else {
						//System.out.println("deny");
						txt.setText("올바른 급여를 입력하세요.\n" + "문자나 '11.' 같은 형식은 허용되지않습니다");
					}
				}
            	
            };
            
            edit_salary.addActionListener(event_edit_employee);
            
            ActionListener delete_employee = new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					// TODO Auto-generated method stub
					if(selected_employee == null || selected_employee == "") {
						txt.setText("선택된 직원이 없습니다");
					} else {
						txt.setText(" Name: " + selected_employee  + " // SSN : " + throwed_ssn +" 삭제합니다");
						try {
							delete_employee_where_ssn();
						} catch (SQLException | IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}

						
						//
					}
				}
            };
            
            delete_employee_button.addActionListener(delete_employee);
            
            ActionListener insert_employee = new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					// TODO Auto-generated method stub
					selected_employee = "";
					state_msg = "insert";
					txt.setText(state_msg);
					String plz_fill = "";
					if(textPeriod1.getText().equals("")) {
						plz_fill += "Fname을 채우세요\n";
					} if(textPeriod2.getText().equals("")) {
						plz_fill += "Minit을 채우세요\n";
					} if(textPeriod3.getText().equals("")) {
						plz_fill += "Lname을 채우세요\n";
					} if(textPeriod4.getText().equals("")) {
						plz_fill += "ssn을 채우세요\n";
					} if(textPeriod5.getText().equals("")) {
						plz_fill += "Bdate를 채우세요\n";
					} if(textPeriod6.getText().equals("")) {
						plz_fill += "Address를 채우세요\n";
					} if(textPeriod7.getText().equals("")) {
						plz_fill += "sex를 채우세요\n";
					} if(textPeriod8.getText().equals("")) {
						plz_fill += "Salary를 채우세요\n";
					} if(textPeriod9.getText().equals("")) {
						plz_fill += "Super_ssm을 채우세요\n";
					} if(textPeriod10.getText().equals("")) {
						plz_fill += "Dno을 채우세요\n";
					}
					int error_check = 0;
					if(textPeriod2.getText().length() != 1) {
						JOptionPane.showMessageDialog(null, "Minit은 한글자여야합니다");
						error_check = 1;
					}
					if(textPeriod2.getText().length() > 9) {
						JOptionPane.showMessageDialog(null, "Minit은 한글자여야합니다");
						error_check = 1;
					}
					if(textPeriod7.getText().length() != 1) {
						JOptionPane.showMessageDialog(null, "Sex는 한글자여야합니다");
						error_check = 1;
					}
					
					try{
				         SimpleDateFormat  dateFormat = new  SimpleDateFormat("yyyy-MM-dd");
				         dateFormat.setLenient(false);
				         dateFormat.parse(textPeriod5.getText());
				         
				       }catch (ParseException e1){
				    	   JOptionPane.showMessageDialog(null, "올바르지 않은 날짜형식");
				    	   error_check =1;
				    }
					
					if(!plz_fill.equals("")) {
						JOptionPane.showMessageDialog(null, plz_fill);
					} else if(error_check == 0) {
						System.out.println("success");
						insert_string = textPeriod1.getText() + "  " + 
								textPeriod2.getText() + "  " + 
								textPeriod3.getText() + "  " + 
								textPeriod4.getText() + "  " + 
								textPeriod5.getText() + "  " +
								textPeriod6.getText() + "  " +
								textPeriod7.getText() + "  " +
								textPeriod8.getText() + "  " +
								textPeriod9.getText() + "  " +
								textPeriod10.getText();
						lock = 3;
						
						try {
							DB_Access();
						} catch (SQLException | IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						lock = 1;
						
					}
				}
            	
            };
            insert_button.addActionListener(insert_employee);
            
            
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
					

					// 선택기능

					txt.setText(state_msg);
				}
            };

            
            ActionListener summit_listener = new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					// TODO Auto-generated method stub
					System.out.println("검색 ");
					get_salary.setText("");
					selected_employee = "";
					state_msg = "";
					state_msg += "검색" + "\n";
					msg =" ";
					select_msg = "";
					select_msg2 = "";
					count_select = 0;
					select_depart = "";
					how_many_employee = 0;
					select_all = 0;
					seleted_table_button = -1;
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
					//System.out.println("select -" + msg);
					//state_msg += "select -" + msg +"\n";
					//System.out.println("from -" + " " + select_msg + select_msg2);
					//state_msg += "from -" + " " + select_msg + select_msg2 + "\n";
					txt.setText(state_msg);
					
					select_depart = select_msg + select_msg2;
					
					
					
					if(msg.equals("")) {
						msg = "Fname,Minit,Lname,ssn,Bdate,Address,Sex,Salary,Super_ssn,Dno";
						select_all =1;
					}
					
					try {
						DB_Access();
						//System.out.println(msg);
						//System.out.println(db_result);
						//textbox.append(msg+"\n");
						//textbox.append(db_result);
						msg = msg.replaceAll("Super_ssn", "Supervisor");
						msg = msg.replaceAll("Dno", "Department");
						msg = msg + ", ";
						String array[] = msg.split(",");
						String array2[] = db_result.split("\n");
						
						for(int i=0; i<array2.length; i++) {
							//System.out.println(array2[i]);
						}
						array3 = new String[array2.length][];
						for(int i=0; i<array2.length; i++) {
							array3[i] = array2[i].split("  ");
						}
						//array는 전처리리된 헤더
						//array3 는 전처리된 데이터
						DefaultTableModel new_model = new DefaultTableModel(array3,array);
			        
						table.setModel(new_model);
						TableColumn column = table.getColumnModel().getColumn(array.length-1);

						column.setMinWidth(20);
			            column.setMaxWidth(20);
			            column.setPreferredWidth(20);
			            
			            //System.out.println(how_many_employee);
			            state_msg = "검색된 직원수 : " + how_many_employee;
			            txt.setText(state_msg);
			            
						//table.getColumn(" ").setPreferredWidth(150);
						DefaultTableCellRenderer renderer = new MyDefaultTableCellRenderer();
						table.getColumn(" ").setCellRenderer(renderer);

						JCheckBox box = new JCheckBox();
						table.getColumn(" ").setCellEditor(new DefaultCellEditor(box));
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
            
            
            setSize(1500, 900);
            setVisible(true);
        }
    }

    public static void main(String[] args) throws SQLException, IOException{
        new setGUI();
    }
}









// 수정해야될것 

/*
 * 
 * 
 * 
 * 1.delete 구현 / salary 수정구현
 * 2.직원선택 최적화
 * 
 * headqarters 안보이는 현상 수정
 * 
 * 
 */






