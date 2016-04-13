//Matthew Fishman
//SER320 Midterm
//The purpose of this class is to run 
// the checkbook application and change screens
import javax.swing.JFrame;

public class Main extends JFrame {
	public Main(){
		super("Midterm Application");
		
		
		this.setSize(100, 100);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setResizable(false);
		this.setVisible(true);
		
		
		Table table = new Table();
		this.add(table);
		this.setSize(1200,600);
		
		
		}
	public static void main(String[] args) {
		new Main();
	}

}
