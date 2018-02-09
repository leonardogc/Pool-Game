package gui;

import java.awt.EventQueue;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.JFrame;
import javax.swing.JPanel;

import io.Client;

public class GraphicInterface{

	public JFrame frame;
	public JPanel panel;

	/**
	 * Launch the application.
	 * @throws IOException 
	 * @throws UnknownHostException 
	 */
	/*public static void main(String[] args) throws UnknownHostException, IOException {
		Client c= new Client("94.60.13.255",25565);
		
		c.startGame();
		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GraphicInterface window = new GraphicInterface();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}*/

	

	public GraphicInterface() {
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
