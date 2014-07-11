package com.epam.training.jjp.domain;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.epam.training.jjp.display.UserInterface;

public class Game extends Thread{
	private GameMapInterface mapFirstPlayers;
	private GameMapInterface mapSecondPlayers;
	private List<byte[][]> shipMaps;
	private List<Byte> multiplier;
	private List<Byte> healthPoints;

	private Player player1;
	private Player player2;
	
	private GameModes mode;
	private UserInterface ui;
	private static final Logger LOGGER = LoggerFactory.getLogger(Game.class);
	private static String shipMapsPath = "ships.txt";
	
	private String[][][] displayOnUI;

	public Game() {
		mapFirstPlayers = new GameMap();
		mapSecondPlayers = new GameMap();
	}
	
	private void readShipMaps() throws IOException {
		Path file = Paths.get(shipMapsPath);
		BufferedReader reader = Files.newBufferedReader(file, Charset.defaultCharset());
		String[] lines = new String[5];
		while((lines[0] = reader.readLine()) != null) {
			for(int i = 1; i < 5 ; ++i) {
				lines[i] = reader.readLine();
			}
			byte[][] map = generateShipMap(lines);
			shipMaps.add(map);
			healthPoints.add(computeHealthPoints(map));
			multiplier.add(Byte.parseByte(lines[4]));
		}

	}

	private byte[][] generateShipMap(String[] lines) {
		byte[][] shipMap = new byte[4][4];
		shipMapSetZero(shipMap);
		for(int i = 0; i < 4 ; ++i) {
			for(int j = 0; j < 4 ; ++j) {
				shipMap[i][j] = (byte) ((lines[i].charAt(j) == '.') ? 0 : 1);
			}
		}
		
		return shipMap;
	}

	public void setMode(GameModes mode) {
		this.mode = mode;
	}
	
	public void setPlayer1(Player player) {
		player1 = player;
		player1.setMapSize(mapFirstPlayers.getDefaultSize());
	}
	
	public void setPlayer2(Player player) {
		player2 = player;
		player2.setMapSize(mapSecondPlayers.getDefaultSize());
	}
	
	public GameModes getMode() {
		return mode;
	}
	
	public void Start() {
		LOGGER.info("Game init started");
		initShips();
		initMaps();
		LOGGER.info("Game init finished");
		
		try {
			gameCycle();
			
			decideWinner();
		} catch (InterruptedException e) {
			LOGGER.info("Game interrupted! ");
			e.printStackTrace();
		}
		
		LOGGER.info("Game over");
	}

	private void decideWinner() {
		if(!mapFirstPlayers.isThereAnyShipLeft() && !mapSecondPlayers.isThereAnyShipLeft()) {
			System.out.println("There is no winner! ");
		} else if(!mapFirstPlayers.isThereAnyShipLeft()) {
			System.out.println("Second player WON!");
		} else {
			System.out.println("First player WON!");
		}
		
	}

	private void gameCycle() throws InterruptedException {
		while(mapFirstPlayers.isThereAnyShipLeft() && mapSecondPlayers.isThereAnyShipLeft() ) {
			Thread.sleep(100);
			Coordinate coordinate1 = player1.selectCoordinate();
			boolean isThereHit = mapSecondPlayers.shoot(coordinate1.getX(), coordinate1.getY());
			player1.setPreviousTryIsAHit(isThereHit);
			handleHit(isThereHit, coordinate1, 1);
			if(mapSecondPlayers.isThereAnyShipLeft()) {
				Coordinate coordinate2 = player2.selectCoordinate();
				isThereHit = mapFirstPlayers.shoot(coordinate2.getX(), coordinate2.getY());
				handleHit(isThereHit, coordinate2, 0);
			}
		}
	}

	private void handleHit(boolean isThereHit, Coordinate coordinate, int playernumber) {
		if(isThereHit) {
			displayOnUI[coordinate.getX()][coordinate.getY()][playernumber] = "O";
			printMaps();
		} else {
			displayOnUI[coordinate.getX()][coordinate.getY()][playernumber] = "X";
		}
	}
	
	public void printUIMaps() {
		StringBuilder strBuilder = new StringBuilder();
		strBuilder.append("PLayer1: ");
		for(int i = 0; i < displayOnUI.length ; ++i ) {
			strBuilder.append("\n");
			for(int j = 0; j < displayOnUI[i].length ; ++j) {
				strBuilder.append(displayOnUI[i][j][0] + " ");
			}
		}
		strBuilder.append("\nPLayer2: ");
		for(int i = 0; i < displayOnUI.length ; ++i ) {
			strBuilder.append("\n");
			for(int j = 0; j < displayOnUI[i].length ; ++j) {
				strBuilder.append(displayOnUI[i][j][1] + " ");
			}
		}
		System.out.println(strBuilder.toString());
	}
	
	public void printMaps() {
		StringBuilder strBuilder = new StringBuilder();
		strBuilder.append("PLayer1: \n");
		strBuilder.append(mapFirstPlayers.printMap());
		strBuilder.append("PLayer2: \n");
		strBuilder.append(mapSecondPlayers.printMap());
		System.out.println(strBuilder.toString());
	}

	public void initMaps() {
		player1.setShips(generateShips());
		player2.setShips(generateShips());
		if(player1 instanceof TorpedoAI) {
			List<Ship> ships = ((TorpedoAI) player1).getShips();
			for(Ship ship : ships) {
				mapFirstPlayers.placeShipToRandomePosition(ship);
			}
		} else {
			// TODO with AI this is not used
		}
		
		if(player2 instanceof TorpedoAI) {
			List<Ship> ships = ((TorpedoAI) player2).getShips();
			for(Ship ship : ships) {
				mapSecondPlayers.placeShipToRandomePosition(ship);
			}
		} else {
			// TODO with AI this is not used
		}
	}

	private byte computeHealthPoints(byte[][] mx) {
		byte result = 0;
		for (int i = 0; i < mx.length; ++i) {
			for (int j = 0; j < mx[i].length; ++j) {
				if (mx[i][j] != 0) {
					++result;
				}
			}
		}
		return result;
	}

	private List<Ship> generateShips() {
		List<Ship> ships = new ArrayList<Ship>();
		for (int i = 0; i < shipMaps.size(); ++i) {
			for (int j = 0; j < multiplier.get(i); ++j) {
				ships.add(new Ship(shipMaps.get(i), healthPoints.get(i), i));
			}
		}
		return ships;
	}

	private void initShips() {
		initArrays();
		try {
			readShipMaps();
		} catch (IOException e) {
			LOGGER.error("Init ships failed because could not read " + shipMapsPath);
			e.printStackTrace();
		}
		
		initTempPrint();
	}

	private void initArrays() {
		shipMaps = new ArrayList<>();
		healthPoints = new ArrayList<>();
		multiplier = new ArrayList<>();
		displayOnUI = new String[mapFirstPlayers.getSize()][mapFirstPlayers.getSize()][2];
	}

	private void initTempPrint() {
		for(int i = 0; i < displayOnUI.length ; ++i ) {
			for(int j = 0; j < displayOnUI[i].length ; ++j) {
				displayOnUI[i][j][0] = ".";
			}
		}
		for(int i = 0; i < displayOnUI.length ; ++i ) {
			for(int j = 0; j < displayOnUI[i].length ; ++j) {
				displayOnUI[i][j][1] = ".";
			}
		}
	}


	private void shipMapSetZero(byte[][] map) {
		for (int x = 0; x < map.length; ++x) {
			for (int y = 0; y < map[x].length; ++y) {
				map[x][y] = 0;
			}
		}
	}
	
	@Override
	public String toString() {
		return "First players map: \n\r" + mapFirstPlayers.toString() + "\n\rSecond Players map: \n\r" + mapSecondPlayers.toString();
	}

	@Override
	public void run() {
//		synchronized (this) {
//			try {
//				LOGGER.info("Game thread waiting for user to start! ");
//				this.wait(5000);
//			} catch (InterruptedException e) {
//				e.printStackTrace();
//			}
//		}
		Start();
	}

	public void setUI(UserInterface ui) {
		this.ui = ui;	
	}

	public int getMapSize() {
		return mapFirstPlayers.getSize();
	}

}
