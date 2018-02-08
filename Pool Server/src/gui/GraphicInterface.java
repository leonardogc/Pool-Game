package gui;

import java.awt.EventQueue;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class GraphicInterface{

	public JFrame frame;
	public JPanel panel;
	
	public InetAddress player1Inet;
	public InetAddress player2Inet;
	public int player1Port;
	public int player2Port;
	
	/**
	 * Launch the application.
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		byte[] buf=new byte[1024];
		ServerSocket ts=new ServerSocket(25565);
		Socket cli = ts.accept();
		cli.getInputStream().read(buf);
		
		
		if(!new String(buf).substring(0, 7).equals("connect")) {
			cli.close();
			ts.close();
			System.out.println("Unable to connect to client");
			return;
		}
		
		InetAddress player1Inet = cli.getInetAddress();
		int player1Port = cli.getPort();
		
		cli.getOutputStream().write("accepted".getBytes());
		
		cli.getOutputStream().write("1".getBytes());
		
		cli.close();
		ts.close();
		
		System.out.println("Player 1 Connected");
		
		//////
		
		ts=new ServerSocket(25565);
		cli = ts.accept();
		cli.getInputStream().read(buf);
		
		
		if(!new String(buf).substring(0, 7).equals("connect")) {
			cli.close();
			ts.close();
			System.out.println("Unable to connect to client");
			return;
		}
		
		InetAddress player2Inet = cli.getInetAddress();
		int player2Port = cli.getPort();
		
		cli.getOutputStream().write("accepted".getBytes());
		
		cli.getOutputStream().write("2".getBytes());
		
		cli.close();
		ts.close();
		
		System.out.println("Player 2 Connected");
		
		
		
		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GraphicInterface window = new GraphicInterface(player1Inet,player1Port,player2Inet,player2Port);
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	
	public GraphicInterface(InetAddress player1Inet, int player1Port, InetAddress player2Inet, int player2Port) {
		this.player1Inet=player1Inet; 
		this.player1Port=player1Port;
		this.player2Inet=player2Inet;
		this.player2Port=player2Port;
		
		System.out.println(""+player1Inet.getHostAddress()+":"+player1Port);
		System.out.println(""+player2Inet.getHostAddress()+":"+player2Port);
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
