
public class Print {
	
	void run() {
		System.out.println("inside run");
	}
	
	void display() {
		System.out.println("display method");
	}

	private static void main(String args[]) {
		System.out.println("Hello World");
		new Print().run();
	}
}
