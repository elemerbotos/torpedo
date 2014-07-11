package com.epam.training.jjp.app;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;

import com.epam.training.jjp.display.UserInterface;
import com.epam.training.jjp.domain.Game;
import com.epam.training.jjp.domain.GameModes;
import com.epam.training.jjp.domain.MyTorpedoAI;


public class App {

	
	
	public static void main(String[] args) throws FileNotFoundException, UnsupportedEncodingException {
		Game game = new Game();
		game.setMode(GameModes.TWO_AI);
		game.setPlayer1(new MyTorpedoAI());
		game.setPlayer2(new MyTorpedoAI());
		
		UserInterface ui = new UserInterface();
		ui.setSize(game.getMapSize());
		game.setUI(ui);
		

		Thread gameThread = new Thread(game);
		gameThread.start();
		Thread uiThread = new Thread(ui);
		ui.setGameThread(gameThread);
		uiThread.start();
	}

}
