package gui;

import java.awt.EventQueue;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class GraphicInterface{

	public JFrame frame;
	public JPanel panel;
	public int player;

	/**
	 * Launch the application.
	 * @throws IOException 
	 * @throws UnknownHostException 
	 */
	public static void main(String[] args) throws UnknownHostException, IOException {
		Socket ts=new Socket("94.60.13.255", 25565);

		ts.getOutputStream().write("connect".getBytes());

		byte[] b=new byte[1024];
		ts.getInputStream().read(b);

		System.out.println(new String(b));

		if(!new String(b).substring(0, 8).equals("accepted")) {
			ts.close();
			return;
		}
		
		ts.getInputStream().read(b);
		
		int player=Integer.parseInt(new String(b).substring(0, 1));

		System.out.println("Connection Successful");

		ts.close();

		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GraphicInterface window = new GraphicInterface(player);
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	
	/**
	 * Create the application.
	 * @param player 
	 */
	public GraphicInterface(int player) {
		this.player=player;
		System.out.println("You are Player: "+player);
		initialize();
	}
	
	
	
	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(0, 0, 1366, 735);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		panel = new GraphicsAndListeners(this);
		panel.setBounds(0, 0, 1400, 800);
		frame.getContentPane().add(panel);
	}
}
