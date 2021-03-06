package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import javax.swing.JOptionPane;

public class Client {

	public static void main(String[] args) throws IOException {
		String serverAddress = JOptionPane
				.showInputDialog("Enter IP Address of a machine that is\n"
						+ "running the date service on port 9090:");
		Socket s = new Socket(serverAddress, 9090);
		
		BufferedReader input = new BufferedReader(new InputStreamReader(s.getInputStream()));
		
		String answer = input.readLine();
		
		PrintWriter writer = new PrintWriter(s.getOutputStream());
		writer.write("megkaptam ocsem!");
		
		System.out.println("answer: " + answer);
		//JOptionPane.showMessageDialog(null, answer);
		writer.close();
		System.exit(0);
	}

}
