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

	
	public GraphicInterface(Client c) {
		initialize(c);
	}
	
	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize(Client c) {
		frame = new JFrame();
		frame.setBounds(0, 0, 1366, 735);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		panel = new GraphicsAndListeners(this,c);
		panel.setBounds(0, 0, 1400, 800);
		frame.getContentPane().add(panel);
	}
}
