//Matthew Fishman
//SER320 Midterm
//The purpose of this class is to provide
//the main screen for the checkbook application
import java.awt.BorderLayout;
import java.awt.Checkbox;
import java.awt.GridLayout;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;


public class Table extends JPanel {
	private JButton add, quit, remove;
	private JLabel firstLabel, lastLabel, typeLabel, amountLabel, idLabel, balanceLabel, totalBalance,dayLabel,monthLabel,yearLabel, doneLabel;
	private TextField first,last,amount,id,day,month,year;
	private Checkbox done;
	private Double balance = 0.0; 
	private String[] types = {"Withdrawal", "Deposit"};
	private java.sql.Connection con=null;
	private ArrayList<Integer>ids = new ArrayList<Integer>();
	private ArrayList<TextField>textBoxes = new ArrayList<TextField>(); 
	


	public Table(){
		super();
		setLayout(new BorderLayout());
		
		//Panel to make the forms to deposit or withdraw
		JPanel fields = new JPanel();
		GridLayout fieldGrid = new GridLayout(9,2);
		fields.setLayout(fieldGrid);
		
		
		//Labels and fields for the top of the screen
		firstLabel = new JLabel("First Name:", SwingConstants.CENTER);
		first = new TextField("Enter First Name", 12);
		lastLabel = new JLabel("Last Name:", SwingConstants.CENTER);
		last = new TextField("Enter Last Name", 20);
		typeLabel = new JLabel("Select A Type:", SwingConstants.CENTER);
		final JComboBox typesList = new JComboBox(types);
		amountLabel = new JLabel("Enter Amount:", SwingConstants.CENTER);
		amount = new TextField("Enter An Amount", 20);
		idLabel = new JLabel("Enter Transaction ID:", SwingConstants.CENTER);
		id = new TextField("ID", 20);
		dayLabel = new JLabel("Day(in numbers): ", SwingConstants.CENTER);
		day = new TextField("Day",2);
		monthLabel = new JLabel("Month(in numbers): ", SwingConstants.CENTER);
		month = new TextField("Month",2);
		yearLabel = new JLabel("Year(in numbers): ", SwingConstants.CENTER);
		year = new TextField("Year",2);
		doneLabel = new JLabel("Done?", SwingConstants.CENTER);
		done = new Checkbox("Yes");
		
		
		fields.add(firstLabel);
		fields.add(first);
		fields.add(lastLabel);
		fields.add(last);
		fields.add(typeLabel);
		fields.add(typesList);
		fields.add(amountLabel);
		fields.add(amount);
		fields.add(idLabel);
		fields.add(id);
		fields.add(dayLabel);
		fields.add(day);
		fields.add(monthLabel);
		fields.add(month);
		fields.add(yearLabel);
		fields.add(year);
		fields.add(doneLabel);
		fields.add(done);
		
		//textboxes added to arrayList
		textBoxes.add(first);
		textBoxes.add(last);
		textBoxes.add(amount);
		textBoxes.add(id);
		textBoxes.add(day);
		textBoxes.add(month);
		textBoxes.add(year);
		
		//Makes the table, header and sets size of table panel and setting table specs
		final DefaultTableModel model = new DefaultTableModel();
		final JTable table = new JTable(model);
        table.setAutoCreateRowSorter(true);
		model.addColumn("First Name"); 
		model.addColumn("Last Name"); 
		model.addColumn("Transaction");
		model.addColumn("Amount");
		model.addColumn("ID");
		model.addColumn("Date");
		JTableHeader header = table.getTableHeader();
		header.setReorderingAllowed(false);
	    JScrollPane scrollPane = new JScrollPane(table);
	   
		header.setResizingAllowed(false);
		JPanel tablePanel = new JPanel();
		GridLayout tableGrid = new GridLayout(3,1);
		tablePanel.setLayout(tableGrid);
		tablePanel.add(header);
		tablePanel.add(scrollPane);
		
		//Panel to show the total balance
		JPanel balancePanel = new JPanel();
		GridLayout balanceGrid = new GridLayout(1,2);
		balancePanel.setLayout(balanceGrid);
		balanceLabel = new JLabel("Total Balance: ", SwingConstants.CENTER);
		totalBalance = new JLabel(String.valueOf(balance), SwingConstants.CENTER);
		balancePanel.add(balanceLabel);
		balancePanel.add(totalBalance);
		

		//Panel to make all the buttons on the bottom of the screen	
		JPanel buttons = new JPanel();
		GridLayout buttonGrid = new GridLayout(1,3);
		buttons.setLayout(buttonGrid);
		add =  new JButton("Add");
		add.setEnabled(false);
		quit = new JButton("Quit");
		remove = new JButton("Remove");
		buttons.add(add);
		buttons.add(quit);
		buttons.add(remove);
		
		//Adding all the panels to the main JPanel
		this.add(fields, BorderLayout.NORTH);
		this.add(tablePanel, BorderLayout.CENTER);
		//this.add(sortPanel, BorderLayout.WEST);
		this.add(balancePanel, BorderLayout.EAST);
		this.add(buttons, BorderLayout.SOUTH);
		
		//connecting to the datebase and loading the data into the table
		try
	    {
	        Class.forName("com.mysql.jdbc.Driver");
	        con=DriverManager.getConnection("jdbc:mysql://localhost:3306/midterm","student","student");
	        java.sql.Statement stmt =con.createStatement();
	        ResultSet myRs = stmt.executeQuery("select * from transactions");
	        while (myRs.next()){
	        	model.addRow(new Object[]{myRs.getString("first_name"),
						myRs.getString("last_name"), 
						myRs.getString("transaction"), 
						myRs.getDouble("amount"), 
						myRs.getInt("id_text"),
						myRs.getString("date")});
	        	ids.add(myRs.getInt("id_text"));
	        	double tempBalance = myRs.getDouble("amount");
	        	if(myRs.getString("transaction").matches("Deposit")){
					balance = balance + tempBalance;
					totalBalance.setText(String.valueOf(balance));	
				}
				else{
					balance = balance - tempBalance;
					totalBalance.setText(String.valueOf(balance));
					
				}
	        	
			}
	    }
	    catch (Exception e){
			e.printStackTrace();
		}
		
		//Listener for first name field
		first.addFocusListener(new FocusListener() {
            public void focusLost(FocusEvent arg0) {
            }
            public void focusGained(FocusEvent arg0) {
            	if(first.getText().matches("Enter First Name"))
            	first.setText("");
            }
        });
		//Listener for last name field
		last.addFocusListener(new FocusListener() {
            public void focusLost(FocusEvent arg0) {
            }
            public void focusGained(FocusEvent arg0) {
            	if(last.getText().matches("Enter Last Name"))
            	last.setText("");
            }
        });
		//listener for amount field checks if the amount is a double
		amount.addFocusListener(new FocusListener() {
			public void focusLost(FocusEvent arg0) {
            	if(isDouble(amount.getText())){
	            		add.setEnabled(true);
            	}
            	else{
            		amount.setText("Please insert a number for amount");
            		add.setEnabled(false);
            	}	
            }
            public void focusGained(FocusEvent arg0) {
            	if(amount.getText().matches("Enter An Amount") || amount.getText().matches("Please insert a number for amount")){
            		amount.setText("");
            		add.setEnabled(false);
            	}
            }
        });
		//listener for id field
		//checks if the id is an int and is not used
		id.addFocusListener(new FocusListener() {
			public void focusLost(FocusEvent arg0) {
            	if(isInt(id.getText())){
            		int tempId = Integer.parseInt(id.getText());
            		boolean idPass = true;
            		for(Integer i: ids){
            			if(tempId == i){
            				idPass = false;
            			}
            		}
            		if(idPass == false){
            			add.setEnabled(false);
            			id.setText("Id already exists please insert a new one");
            		}
            		else{
            			add.setEnabled(true);
            		}
            	}
            	else{
            		id.setText("Please insert a number for id");
            		add.setEnabled(false);
            	}	
            }
            public void focusGained(FocusEvent arg0) {
            	if(id.getText().matches("ID") || id.getText().matches("Please insert a number for id")){
            		id.setText("");
            		add.setEnabled(false);
            	}
            	else if(id.getText().matches("Id already exists please insert a new one")){
            		id.setText("");
            		add.setEnabled(false);
            	}
            		
            }
        });
		//listener for the day field checks if the day is a number between 1 and 31
		day.addFocusListener(new FocusListener() {
            public void focusLost(FocusEvent arg0) {
            	if(isInt(day.getText())){
            		int tempDay = Integer.parseInt(day.getText());
            		if(tempDay > 31 || tempDay < 1){
            				add.setEnabled(false);
            				day.setText("Insert a two digit number between 1 and 31 please");
            			}
            	
	            	else{
	            		add.setEnabled(true);
	            	}
            	}
            	else{
            		day.setText("Insert a two digit number between 1 and 31 please");
            		add.setEnabled(false);
            	}	
            }
            public void focusGained(FocusEvent arg0) {
            	if(day.getText().matches("Day") || day.getText().matches("Insert a two digit number between 1 and 31 please")){
            		day.setText("");
            		add.setEnabled(false);

            	}
            }
        });
		//listener for the month feild checks if the month is a number between 1 and 12
		month.addFocusListener(new FocusListener() {
            public void focusLost(FocusEvent arg0) {
            	if(isInt(month.getText())){
            		int tempMonth = Integer.parseInt(month.getText());
            		if(tempMonth > 12 || tempMonth < 1){
            				add.setEnabled(false);
            				month.setText("Insert a two digit number between 1 and 12 please");
            			}
            	
	            	else{
	            		add.setEnabled(true);
	            	}
            	}
            	else{
            		month.setText("Insert a two digit number between 1 and 12 please");
            		add.setEnabled(false);
            	}	
            }
            public void focusGained(FocusEvent arg0) {
            	if(month.getText().matches("Month") || month.getText().matches("Insert a two digit number between 1 and 12 please")){
            		month.setText("");
            		add.setEnabled(false);

            	}
            }
        });
		//listener for the year field checks if the year is a number between 19990 and 2015
		year.addFocusListener(new FocusListener() {
            public void focusLost(FocusEvent arg0) {
            	if(isInt(year.getText())){
            		int tempYear = Integer.parseInt(year.getText());
            		if(tempYear < 1989 || tempYear > 2016){
            				add.setEnabled(false);
            				year.setText("Insert a four digit number between 1990 and 2015 please");
            			}
            	
	            	else{
	            		add.setEnabled(true);
	            	}
            	}
            	else{
            		year.setText("Insert a four digit number between 1990 and 2015 please");
            		add.setEnabled(false);
            	}	
            }
            public void focusGained(FocusEvent arg0) {
            	if(year.getText().matches("Year") || year.getText().matches("Insert a four digit number between 1990 and 2015 please")){
            		year.setText("");
            		add.setEnabled(false);

            	}
            }
        });
		//listener for add button
		//adds info to JTabel on click
		add.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent event){
				
				
				
				//grabs the data from the fields
				String firstName = first.getText();
				String lastName = last.getText();
				String amountText = amount.getText();
				String transactionText = (String) typesList.getSelectedItem();
				int idText = new Integer(Integer.parseInt(id.getText()));
				
				String dayNum = day.getText();
				String monthNum = month.getText();
				String yearNum = year.getText();
				
				//puts the date variables together as the date
				String dateText = (dayNum + "/" + monthNum + "/" + yearNum);
				
				//sends the data to the SQL database
				try{
			    
			        PreparedStatement pstmt=con.prepareStatement("INSERT INTO midterm.transactions (id_text,first_name,last_name,amount,transaction,date) values (,?,?,?,?,?)");
			        pstmt.setInt(1,idText);
			        pstmt.setString(2,firstName);
			        pstmt.setString(3,lastName);
			        pstmt.setString(4,amountText);
			        pstmt.setString(5,transactionText);
			        pstmt.setString(6, dateText);
			        pstmt.executeUpdate();

			    }
			    catch (Exception e){
//					e.printStackTrace();
				}
				//adds the data to the JTable
				model.addRow(new Object[]{firstName, 
						lastName, 
						transactionText, 
						new Double(Double.parseDouble(amount.getText())), 
						idText,
						dateText});
				//change the total balance
				if(transactionText.matches("Deposit")){
					balance = balance + Double.parseDouble(amount.getText());
					totalBalance.setText(String.valueOf(balance));
				}
				else{
					balance = balance - Double.parseDouble(amount.getText());
					totalBalance.setText(String.valueOf(balance));
				}
				for(TextField t: textBoxes){
					t.setText("");
				}
			}
		});
		//listener to quit the program
		quit.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent event){
				System.exit(0);
			}
		});
		remove.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent event){
				if (table.getSelectedRow() != -1) {
		            // remove selected row from the model
					
					int tempRow = table.getSelectedRow();
					int tempId = (int) table.getValueAt(tempRow, 4);
					Double tempBal = (Double) table.getValueAt(tempRow, 3);
					if(((String) table.getValueAt(tempRow, 2)).matches("Deposit")){
						balance = balance - tempBal;
						totalBalance.setText(String.valueOf(balance));	
					}
					else{
						balance = balance + tempBal;
						totalBalance.setText(String.valueOf(balance));
						
					}
				
					//removes the data from the datebase
					try 
					 {  
						
				        PreparedStatement pstmt=con.prepareStatement("DELETE FROM transactions WHERE id_text =?");
				        pstmt.setInt(1,tempId);
				        pstmt.executeUpdate();
					 }
					 catch(Exception e)
					 {
//							e.printStackTrace();
					 }
					
					model.removeRow(table.getSelectedRow());
					
				}
				
			}
		});
		
	}
	//method to verify an int 
	public static boolean isInt(String str)  
	{  
	  try  
	  {  
	    int i = Integer.parseInt(str);  
	  }  
	  catch(NumberFormatException nfe)  
	  {  
	    return false;  
	  }  
	  return true;  
	}
	//method to verify a double
	public static boolean isDouble(String str)  
	{  
	  try  
	  {  
	    Double d = Double.parseDouble(str);  
	  }  
	  catch(NumberFormatException nfe)  
	  {  
	    return false;  
	  }  
	  return true;  
	}
	
	
}
