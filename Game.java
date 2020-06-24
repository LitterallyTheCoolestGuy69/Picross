import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;

import javax.swing.SwingUtilities;
import javax.swing.JPanel;

public class Game extends JPanel implements KeyListener, MouseListener, MouseMotionListener{
	private boolean selecting;
	private String selectedSize = "";
	private int size;
	private boolean isRightClick;
	private boolean mouseDrag = false;
	private int dragX, dragY;
	private boolean enteredWrong = false;
	private boolean[][] board;
	private boolean[][] displayed;
	private boolean[][] wrong;
	private boolean[][] selected;
	private int mistakes = 0;
	private double percentage;
	public Game() {
		addKeyListener(this);
		setFocusable(true);
		addMouseListener(this);
		addMouseMotionListener(this);
		selecting = true;
	}
	
	public void mouseClicked(MouseEvent e) { }
	
	public void keyPressed(KeyEvent e) { 
		boolean valid;
		if(selecting){
			if(e.getKeyCode() >= 48 && e.getKeyCode() <= 57){
				selectedSize = selectedSize + (e.getKeyCode()-48);
			}
			else if(e.getKeyCode() == KeyEvent.VK_BACK_SPACE){
				try{
					selectedSize = selectedSize.substring(0,selectedSize.length()-1);
				}
				catch(Exception ex) {
					selectedSize = "";
				}
			}
			else if(e.getKeyCode() == KeyEvent.VK_ENTER){
				valid = true;
				try {
					size = Integer.parseInt(selectedSize);
					if(size < 5 || size > 20){
						valid = false;
					}
				}
				catch(Exception ex){
					valid = false;
				}
				if(!valid){
					enteredWrong = true;
				}
				else {
					selecting = false;
					generateBoard(size);
				}
				selectedSize = "";
			}
		}
		update();
	}
	public void update() {
		
		setSize(510,541);
		setSize(510,540);
	}
	public void paint(Graphics g){
		int fontSize;
		if(selecting){
			g.setColor(Color.LIGHT_GRAY);
			g.fillRect(0, 0, 600, 600);
			if(enteredWrong){
				g.setColor(Color.red);
				fontSize = 18;
				g.setFont(new Font("Courier New", 0, fontSize));
				g.drawString("Invalid size. Size must be between 5 and 20", 250 - (int)(("Invalid size. Size must be between 5 and 20".length()/2)*(0.61*fontSize)),200);
			}
			g.setColor(Color.black);
			fontSize = 24;
			g.setFont(new Font("Courier New", 0, fontSize));
			g.drawString("Welcome to Picross", 250 - (int)(("Welcome to Picross".length()/2)*(0.61*fontSize)), 150);
			g.drawString("Type in the size you'd like", 250 - (int)(("Type in the size you'd like".length()/2)*(0.61*fontSize)), 180);
		
			fontSize = 200;
			g.setFont(new Font("Courier New", 0, fontSize));
			g.drawString(selectedSize, 250 - (int)((selectedSize.length()/2.0)*(0.61*fontSize)), 400);
		}
		else {
			g.setColor(Color.LIGHT_GRAY);
			g.fillRect(0,0,600,600);
			for(int i = 0; i < size; i++) {
				for(int j = 0; j < size; j++) {
					if(displayed[i][j]) {
						if(board[i][j]) {
							g.setColor(Color.cyan);
						}
						else {
							g.setColor(new Color(245,245,245));
						}
					}
					else {
						g.setColor(new Color(252,252,252));
					}
					g.fillRect(200 + i*(300/size),200 + j*(300/size), (300/size), (300/size));
				}
			}
			for(int i = 0; i < size; i++)
			{
				for(int j = 0; j < size; j++) {
					if(selected[j][i] && !displayed[j][i]) {
						if(isRightClick) {
							g.setColor(new Color(248,248,248));
						}
						else {
							g.setColor(new Color(100,230,230));
						}
						g.fillRect(200 + j*(300/size), 200 + i*(300/size), (300/size), (300/size));
					}
				}
			}
			
			
			
			g.setColor(new Color(240,240,240));
			for(int i = 0; i <= size; i++){
				g.drawLine(200, 200 + i*(300/size), 500, 200 + i*(300/size));
				g.drawLine(200 + i*(300/size), 200, 200 + i*(300/size), 500);
			}
			g.setColor(Color.red);
			for(int i = 0; i < size; i++) {
				for(int j = 0; j < size; j++) {
					if(wrong[i][j]) {
						g.drawLine(200 + (int)((i+0.25)*(300/size)), 
								   200 + (int)((j+0.25)*(300/size)),
								   200 + (int)((i+0.75)*(300/size)), 
								   200 + (int)((j+0.75)*(300/size)));
						g.drawLine(200 + (int)((i+0.75)*(300/size)), 
								   200 + (int)((j+0.25)*(300/size)),
								   200 + (int)((i+0.25)*(300/size)), 
								   200 + (int)((j+0.75)*(300/size)));
					}
				}
			}
			
			int count;
			ArrayList<Integer> holder;
			g.setFont(new Font("Courier New", 0, (300/size) - 8));
			g.setColor(Color.black);
			for(int i = 0; i < size; i++) {
				count = 0;
				holder = new ArrayList<Integer> ();
				for(int j = 0; j < size; j++) {
					if(board[i][j]) {
						count++;
					}
					else {
						if(count != 0) {
							holder.add(count);
							count = 0;
						}
					}
				}
				if(count != 0) {
					holder.add(count);
				}
				for(int j = 0; j < holder.size(); j++) {
					g.drawString(""+holder.get(j), 200 + (int)((i+0.35)*(300/size)) , 180 - ((300/size)*(holder.size() - (j+1))));
				}
			}
			
			for(int i = 0; i < size; i++) {
				count = 0;
				holder = new ArrayList<Integer> ();
				for(int j = 0; j < size; j++) {
					if(board[j][i]) {
						count++;
					}
					else {
						if(count != 0) {
							holder.add(count);
							count = 0;
						}
					}
				}
				if(count != 0) {
					holder.add(count);
				}
				for(int j = 0; j < holder.size(); j++) {
					g.drawString(""+holder.get(j), 200 - ((300/size)*(holder.size() - (j))), 200 + (int)((i+0.65)*(300/size)));
				}
			}
		}
	}

	
	public void generateBoard(int size) {
		board = new boolean[size][size];
		wrong = new boolean[size][size];
		selected = new boolean[size][size];
		displayed = new boolean[size][size];
		for(int i = 0; i < size; i++) {
			for(int j = 0; j < size; j++) {
				double x = Math.random();
				board[i][j] = x > 0.5;
				wrong[i][j] = false;
				displayed[i][j] = false;
				selected[i][j] = false;
			}
		}

		
	}
	
	public void printBoard() {
		for(int i = 0; i < size; i++) {
			for(int j = 0; j < size; j++) {
				if(board[i][j]) {
					System.out.print("O ");
				}
				else {
					System.out.print("X ");
				}
			}
			System.out.println();
		}
	}
	public void mousePressed(MouseEvent e) { 
		if(!selecting) {
			isRightClick = SwingUtilities.isRightMouseButton(e);
			mouseDrag = true;
			dragX = (e.getX()-200)/(300/size);
			dragY = (e.getY()-200)/(300/size);
		}
	}
	public void mouseReleased(MouseEvent e) { 
		if(!selecting) {
			mouseDrag = false;
			int x = (e.getX()-200)/(300/size);
			int y = (e.getY()-200)/(300/size);
			if(x == dragX && y == dragY) {
				if(!displayed[x][y]) {
					if((SwingUtilities.isLeftMouseButton(e) && !board[x][y]) || 
					   (SwingUtilities.isRightMouseButton(e) && board[x][y])) {
						wrong[x][y] = true;
					}
					displayed[x][y] = true;
				}
			}
			else if(x == dragX) {
				for(int i = dragY; i != y + (y-dragY)/Math.abs(y-dragY); i += (y-dragY)/Math.abs(y-dragY)) {
					if(!displayed[x][i]) {
						if((SwingUtilities.isLeftMouseButton(e) && !board[x][i]) || 
						   (SwingUtilities.isRightMouseButton(e) && board[x][i])) {
							wrong[x][i] = true;
						}
						displayed[x][i] = true;
					}
				}
			}
			else if(y == dragY) {
				for(int i = dragX; i != x + (x-dragX)/Math.abs(x-dragX); i += (x-dragX)/Math.abs(x-dragX)) {
					if(!displayed[i][y]) {
						if((SwingUtilities.isLeftMouseButton(e) && !board[i][y]) || 
						   (SwingUtilities.isRightMouseButton(e) && board[i][y])) {
							wrong[i][y] = true;
						}
						displayed[i][y] = true;
					}
				}
			}
		}
		int count1 = 0, count2 = 0, count3 = 0;;
		for(int i = 0; i < size; i++) {
			for(int j = 0; j < size; j++) {
				if(wrong[i][j]) {
					count3++;
				}
				if(displayed[i][j] && board[i][j] && !wrong[i][j]) {
					count1++;
				}
				count2++;
			}
		}
		percentage = count1 / (double)count2;
		mistakes = count3;
		update();
		for(int i = 0; i < size; i++) {
			for(int j = 0; j < size; j++) {
				selected[i][j] = false;
			}
		}
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		if(!selecting) {
			if(mouseDrag) {
				for(int i = 0; i < size; i++) {
					for(int j = 0; j < size; j++) {
						selected[i][j] = false;
					}
				}
				try {
					int x = (e.getX()-200)/(300/size);
					int y = (e.getY()-200)/(300/size);
					if(x == dragX && y == dragY) {
						selected[x][y] = true;
					}
					else if(x == dragX) {
						for(int i = dragY; i != y + (y-dragY)/Math.abs(y-dragY); i += (y-dragY)/Math.abs(y-dragY)) {
							selected[x][i] = true;
						}
					}
					else if(y == dragY) {
						for(int i = dragX; i != x + (x-dragX)/Math.abs(x-dragX); i += (x-dragX)/Math.abs(x-dragX)) {
							selected[i][y] = true;
						}
					}
					update();
				}
				catch(Exception ex) { }
			}
		}
	}

	public void mouseMoved(MouseEvent e) {
		
		
	}
	public void keyReleased(KeyEvent arg0) { }
	public void keyTyped(KeyEvent arg0) { }
	public void mouseEntered(MouseEvent arg0) { }
	public void mouseExited(MouseEvent arg0) { }

}
