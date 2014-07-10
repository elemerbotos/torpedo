package com.epam.training.jjp.app;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;



import com.epam.training.jjp.domain.Game;
import com.epam.training.jjp.domain.GameModes;
import com.epam.training.jjp.domain.MyTorpedoAI;

import javafx.application.Application;
import javafx.stage.Stage;


public class App extends Application {

	
	
	public static void main(String[] args) throws FileNotFoundException, UnsupportedEncodingException {
		PrintWriter writer = new PrintWriter("the-file-name.txt", "UTF-8");
		Game game = new Game();
		game.setMode(GameModes.TWO_AI);
		game.setPlayer1(new MyTorpedoAI());
		game.setPlayer2(new MyTorpedoAI());
		game.initMaps();
		writer.append(game.toString());
		writer.close();
		game.Run();
	}

	@Override
	public void start(Stage arg0) throws Exception {
		// TODO Auto-generated method stub
		
	}

}
