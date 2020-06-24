import javax.swing.JFrame;

public class Display {
	public static void main(String[] args) {
		JFrame screen = new JFrame();
		screen.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		screen.setResizable(false);
		screen.setSize(530,540);
		screen.setVisible(true);
		Game game = new Game();
		screen.addMouseListener(game);
		screen.addKeyListener(game);
		screen.addMouseMotionListener(game);
		screen.add(game);
		
		
	}
}
