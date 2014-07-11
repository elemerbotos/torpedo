package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;

public class Server {
	public static void main(String[] args) throws IOException {
		ServerSocket listener = new ServerSocket(9090);
		try {
			while (true) {
				Socket socket = listener.accept();
				try {
					PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
					
					out.println(new Date().toString());
					
					BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
					
					String answer = input.readLine();
					
					System.out.println("answer from client: " + answer);
				} finally {
					socket.close();
				}
			}
		} finally {
			listener.close();
		}
	}

}
