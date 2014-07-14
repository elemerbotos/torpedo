package com.epam.training.jjp.app;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.ServerSocket;
import java.net.Socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.epam.training.jjp.domain.Game;


public class App {
	// JANOS: ephubudw0070
	private static final String HOST = "ephubudw0080";
	private static String type;
	private Game game;
	private int mapSizeX = 30;
	private int mapSizeY = 30;
	private static final Logger LOGGER = LoggerFactory.getLogger(App.class);
	
	public static void main(String[] args) throws FileNotFoundException, UnsupportedEncodingException {
		if(args.length == 0) {
			throw new IllegalArgumentException("No argumentum! \"Server\" or \"Client\" can be given!");
		}
		if(args[0].equals("client") || args[0].equals("server")) {
			type = args[0];
		} else {
			throw new IllegalArgumentException("Wrong argumentum! \"Server\" or \"Client\" can be given!");
		}
		
		App myApp = new App();
		
		if(type.equals("server")) {
			try {
				myApp.runServer();
			} catch (IOException e) {
				LOGGER.error("Error during server start");
				e.printStackTrace();
			}
		} else {
			try {
				myApp.runClient();
			} catch (IOException e) {
				LOGGER.error("Error during client start");
				e.printStackTrace();
			}
		}
	}
	
	private void runServer() throws IOException {
		initGame();
		ServerSocket listener = null;
		try {
			listener = new ServerSocket(5000);
			Socket socket = listener.accept();
			LOGGER.info("Socket open");
			try(PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {
					BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
					out.println("SIZE " + mapSizeX + " " + mapSizeY);
					try {
						game.startGame(in, out);
					} catch (Exception e) {
						out.println("ERROR " + e.getMessage());
						LOGGER.error("from server: " + in.readLine());
						e.printStackTrace();
					}
			} finally {
				socket.close();
				LOGGER.info("Socket closed");
			}
		} finally {
			listener.close();
		}
	}
	
	private void runClient() throws IOException {
		Socket socket = new Socket(HOST, 5000);
		LOGGER.info("Socket open");
			try(PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {
				BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				setSize(in);
				initGame();
				try {
					Thread.sleep(1000);
					game.startGame(in, out);
				} catch (Exception e) {
					out.println("ERROR " + e.getMessage());
					LOGGER.error("from client: " + in.readLine());
					e.printStackTrace();
				}
			} finally {
				socket.close();
				LOGGER.info("Socket closed");
			}
	}

	private void setSize(BufferedReader in) throws IOException {
		String wholeMessage = in.readLine().trim();
		String[] message = (wholeMessage).split(" ");
		LOGGER.info(wholeMessage);
		if(!message[0].equals("SIZE")) {
			new IllegalArgumentException("First received message should begin with \"size\"");
		}
		mapSizeX = Integer.parseInt(message[1]);
		mapSizeY = Integer.parseInt(message[2]);
	}

	private void initGame() {
		game = new Game(type, mapSizeX, mapSizeY);
	}

}
