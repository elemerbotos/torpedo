package com.epam.training.jjp.domain;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Game extends Thread {
	private GameMapInterface mapFirstPlayers;
	private GameMapInterface mapSecondPlayers;
	private List<Byte[][]> shipMaps;
	private List<Byte> multiplier;
	private List<Byte> healthPoints;
	private Byte theEnemysHP;

	private Player player1;
	private Player player2;

	private GameModes mode;
	private static final Logger LOGGER = LoggerFactory.getLogger(Game.class);
	private static String shipMapsPath = "ships.txt";

	private static String gameType;

	public Game() {
		mapFirstPlayers = new GameMap();
		mapSecondPlayers = new GameMap();
	}

	public Game(String type, int x, int y) {
		gameType = type;

		mapSecondPlayers = new GameMap(x, y);
		player2 = new MyTorpedoAI();
	}

	private void readShipMaps() throws IOException {
		Path file = Paths.get(shipMapsPath);
		BufferedReader reader = Files.newBufferedReader(file,
				Charset.defaultCharset());
		String[] lines = new String[5];
		while ((lines[0] = reader.readLine()) != null) {
			for (int i = 1; i < 5; ++i) {
				lines[i] = reader.readLine();
			}
			Byte[][] map = generateShipMap(lines);
			shipMaps.add(map);
			healthPoints.add(computeHealthPoints(map));
			multiplier.add(Byte.parseByte(lines[4]));
		}
		reader.close();
		
	}

	private Byte sum(List<Byte> healthPoints) {
		int sum = 0;
		for (int i = 0; i < healthPoints.size(); ++i) {
			sum += healthPoints.get(i);
		}
		return (byte) sum;
	}

	private Byte[][] generateShipMap(String[] lines) {
		Byte[][] shipMap = new Byte[4][4];
		shipMapSetZero(shipMap);
		for (int i = 0; i < 4; ++i) {
			for (int j = 0; j < 4; ++j) {
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
		player1.setMapSize(mapFirstPlayers.getDefaultSize(),
				mapFirstPlayers.getDefaultSize());
	}

	public void setPlayer2(Player player) {
		player2 = player;
		player2.setMapSize(mapSecondPlayers.getDefaultSize(),
				mapSecondPlayers.getDefaultSize());
	}

	public GameModes getMode() {
		return mode;
	}

	public void startGame(BufferedReader in, PrintWriter out)
			throws InterruptedException, IOException {
		LOGGER.info("Game init started");
		initShips();
		initOneMap();
		LOGGER.info("Game init finished");

		if (gameType.equalsIgnoreCase("client")) {
			clientSideGameCycle(in, out);
		} else {
			serverSideGameCycle(in, out);
		}

		LOGGER.info("Game over");
	}

	private void clientSideGameCycle(BufferedReader in, PrintWriter out)
			throws InterruptedException, IOException {
		while (exitCondition()) {
			sendFire(out);
			String message;
			receiveAnswerOnFire(in);

			handleEnemysLoose(in, out);

			if (theEnemysHP != 0) {
				message = receiveShootAndGenerateAnswer(in);

				out.println(message);
				LOGGER.info("sent: " + message);

				handleMyLoose(in, out);

			}
		}
	}

	private void handleMyLoose(BufferedReader in, PrintWriter out)
			throws IOException {
		String message;
		if (!mapSecondPlayers.isThereAnyShipLeft()) {
			out.println("LOST");
			LOGGER.info("sent: LOST");
			message = in.readLine().trim();
			LOGGER.info("message: " + message);
			if (!message.equals("WON")) {
				errorInput(message);
			}
		}
	}

	private String receiveShootAndGenerateAnswer(BufferedReader in)
			throws IOException {
		String message = in.readLine().trim();
		LOGGER.info("message: " + message);
		if (message.startsWith("FIRE")) {
			String[] coords = message.split(" ");
			int isHit = mapSecondPlayers.shoot(Integer.parseInt(coords[1]),
					Integer.parseInt(coords[2]));
			if (isHit == 1) {
				message = "HIT";
			} else if (isHit == 2) {
				message = "SUNK";
			} else {
				message = "MISS";
			}

			return message;
		} else {
			errorInput(message);
			return null;
		}
	}

	private void receiveAnswerOnFire(BufferedReader in) throws IOException {
		String message = in.readLine().trim();
		LOGGER.info("message: " + message);
		if (message.equals("HIT") || message.equals("SUNK")
				|| message.equals("MISS")) {

		} else
			errorInput(message);
		boolean isThereHit = message.equals("HIT") || message.equals("SUNK");
		if (isThereHit) {
			--theEnemysHP;
		}
		player2.Hit(isThereHit);
	}

	private void sendFire(PrintWriter out) {
		Coordinate coordinate2 = player2.selectCoordinate();
		out.println("FIRE " + coordinate2.getX() + " " + coordinate2.getY());
		LOGGER.info("sent: " + "FIRE " + coordinate2.getX() + " " + coordinate2.getY());
	}

	private boolean exitCondition() {
		return mapSecondPlayers.isThereAnyShipLeft() && theEnemysHP > 0;
	}

	private void serverSideGameCycle(BufferedReader in, PrintWriter out)
			throws InterruptedException, IOException {
		while (exitCondition()) {

			String message = receiveShootAndGenerateAnswer(in);

			out.println(message);
			LOGGER.info("sent: " + message);

			handleMyLoose(in, out);

			if (mapSecondPlayers.isThereAnyShipLeft()) {
				sendFire(out);

				receiveAnswerOnFire(in);

				handleEnemysLoose(in, out);
			}
		}
	}

	private void handleEnemysLoose(BufferedReader in, PrintWriter out)
			throws IOException {
		String message;
		if (theEnemysHP == 0) {
			out.println("WON");
			LOGGER.info("sent: won");
			message = in.readLine().trim();
			LOGGER.info("message: " + message);
			if (!message.equals("LOST")) {
				errorInput(message);
			}
		}
	}

	private void errorInput(String message) throws IllegalArgumentException {
		if (message.startsWith("ERROR")) {
			throw new IllegalArgumentException("Error message received: "
					+ message);
		} else {
			throw new IllegalArgumentException(
					"Wrong message, expected: hit, sunk, miss, error - Received message: "
							+ message);
		}
	}

	public void printMaps() {
		StringBuilder strBuilder = new StringBuilder();
		strBuilder.append("My map: \r\n");
		strBuilder.append(mapSecondPlayers.printMap());
		System.out.println(strBuilder.toString());
	}

	public void initOneMap() {
		player2.setMapSize(mapSecondPlayers.getSizeX(), mapSecondPlayers.getSizeY());
		player2.setShips(generateShips());

		if (player2 instanceof TorpedoAI) {
			List<Ship> ships = ((TorpedoAI) player2).getShips();
			for (Ship ship : ships) {
				mapSecondPlayers.placeShipToRandomePosition(ship);
			}
		}
		LOGGER.info("AI calculated: " + String.valueOf(mapSecondPlayers.getHP()) + " HPs" );
		theEnemysHP = (byte) mapSecondPlayers.getHP();
	}

	private Byte computeHealthPoints(Byte[][] mx) {
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

	private void initShips() throws IOException {
		initArrays();
		readShipMaps();

		//initTempPrint();
	}

	private void initArrays() {
		shipMaps = new ArrayList<>();
		healthPoints = new ArrayList<>();
		multiplier = new ArrayList<>();
	}

	private void shipMapSetZero(Byte[][] map) {
		for (int x = 0; x < map.length; ++x) {
			for (int y = 0; y < map[x].length; ++y) {
				map[x][y] = 0;
			}
		}
	}

	@Override
	public String toString() {
		return "First players map: \n\r" + mapFirstPlayers.toString()
				+ "\n\rSecond Players map: \n\r" + mapSecondPlayers.toString();
	}

	@Override
	public void run() {
		// startGame(null, null);
	}

	public int getMapSizeX() {
		return mapFirstPlayers.getSizeX();
	}

	public int getMapSizeY() {
		return mapFirstPlayers.getSizeY();
	}

}
