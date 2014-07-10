package com.epam.training.jjp.domain;

import java.util.ArrayList;
import java.util.List;

public class Game {
	private GameMapInterface mapFirstPlayers;
	private GameMapInterface mapSecondPlayers;
	private byte[][][] shipMaps;
	private byte[] multiplier;
	private byte[] healthPoints;

	private Player player1;
	private Player player2;
	
	private GameModes mode;

	public Game() {
		generateShipMaps();
		mapFirstPlayers = new GameMap();
		mapSecondPlayers = new GameMap();
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
	
	public void Run() {
		initMaps();
		
		gameCycle();
		
		decideWinner();
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

	private void gameCycle() {
		while(mapFirstPlayers.isThereAnyShipLeft() && mapSecondPlayers.isThereAnyShipLeft() ) {
			Coordinate coordinate1 = player1.selectCoordinate();
			Coordinate coordinate2 = player2.selectCoordinate();
			mapSecondPlayers.shoot(coordinate1.getX(), coordinate1.getY());
			if(mapSecondPlayers.isThereAnyShipLeft()) {
				mapFirstPlayers.shoot(coordinate2.getX(), coordinate2.getY());
			}
		}
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

	private byte getHealthPoints(byte[][] mx) {
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
		for (int i = 0; i < shipMaps.length; ++i) {
			for (int j = 0; j < multiplier[i]; ++j) {
				ships.add(new Ship(shipMaps[i], healthPoints[i], i));
			}
		}
		return ships;
	}

	private void generateShipMaps() {
		shipMaps = new byte[5][4][4];
		healthPoints = new byte[5];
		multiplier = new byte[5];
		shipMapsSetZero();

		setShipTypeOne(0);
		multiplier[0] = 4;
		healthPoints[0] = getHealthPoints(shipMaps[0]);

		setShipTypeTwo(1);
		multiplier[1] = 4;
		healthPoints[1] = getHealthPoints(shipMaps[1]);

		setShipTypeThree(2);
		multiplier[2] = 4;
		healthPoints[2] = getHealthPoints(shipMaps[2]);

		setShipTypeFour(3);
		multiplier[3] = 4;
		healthPoints[3] = getHealthPoints(shipMaps[3]);

		setShipTypeFive(4);
		multiplier[4] = 3;
		healthPoints[4] = getHealthPoints(shipMaps[4]);
	}

	private void setShipTypeFive(int i) {
		shipMaps[i][1][0] = 1;
		shipMaps[i][1][1] = 1;
		shipMaps[i][1][2] = 1;
		shipMaps[i][0][1] = 1;
	}

	private void setShipTypeFour(int i) {
		shipMaps[i][0][0] = 1;
		shipMaps[i][0][1] = 1;
		shipMaps[i][0][2] = 1;
		shipMaps[i][0][3] = 1;
	}

	private void setShipTypeThree(int i) {
		shipMaps[i][0][0] = 1;
		shipMaps[i][0][1] = 1;
		shipMaps[i][0][2] = 1;
	}

	private void setShipTypeTwo(int i) {
		shipMaps[i][0][0] = 1;
		shipMaps[i][0][1] = 1;
	}

	private void setShipTypeOne(int i) {
		shipMaps[i][0][0] = 1;
	}

	private void shipMapsSetZero() {
		for (int i = 0; i < 5; ++i) {
			for (int x = 0; x < 4; ++x) {
				for (int y = 0; y < 4; ++y) {
					shipMaps[i][x][y] = 0;
				}
			}
		}
	}
	
	@Override
	public String toString() {
		return "First players map: \n\r" + mapFirstPlayers.toString() + "\n\rSecond Players map: \n\r" + mapSecondPlayers.toString();
	}

}
