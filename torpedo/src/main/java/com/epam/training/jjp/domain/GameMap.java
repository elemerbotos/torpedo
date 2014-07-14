package com.epam.training.jjp.domain;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GameMap implements GameMapInterface {
	private static final int SIZE_OF_SHIP_MAP = 4;
	private int[][] coordinates;
	private int sizeX;
	private int sizeY;
	private List<Ship> ships;
	private static final int DEFAULT_SIZE = 30;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(Game.class);
	
	private int numberOfShipTiles = 0;
	
	public int getDefaultSize() {
		return DEFAULT_SIZE;
	}
	
	public GameMap(final int sizeX, final int sizeY) {
		this.sizeX = sizeX;
		this.sizeY = sizeY;
		coordinates = new int[sizeX][sizeY];
		initCoordinates();
		ships = new ArrayList<>();
	}
	
	public GameMap() {
		sizeX = DEFAULT_SIZE;
		sizeY = DEFAULT_SIZE;
		ships = new LinkedList<>();
		coordinates = new int[sizeX][sizeY];
		initCoordinates();
	}
	
	public List<Ship> getShips() {
		return ships;
	}
	
	public void placeShip(Ship ship, int x, int y)
			throws IndexOutOfBoundsException, IllegalArgumentException {
		if (!isThePlaceFree(ship, x, y)) {
			throw new IllegalArgumentException("Place is not free! ");
		}
		int numOfShips = ships.size();
		
		for(int i = 0 ; i < 4 ; ++i) {
			for(int j = 0 ; j < 4 ; ++j) {
				if(ship.isThereShip(i,j)) {
					coordinates[x+i][y+j] = numOfShips + 1;
				}
			}
		}
		
		numberOfShipTiles += ship.getHealthPoints();
		ships.add(ship);
	}
	
	public int shoot(int x, int y) {
		if(coordinates[x][y] < 0 || coordinates[x][y] == 99999) {
			throw new IllegalArgumentException("Not the first try! ");
		}
		int result = 0;
		if(coordinates[x][y] > 0) {
			Ship victim = updateShipData(x, y);
			inCaseSank(victim, x, y);
			
			coordinates[x][y] *= -1;
			--numberOfShipTiles;
			
			result = victim == null ? 2 : 1;
		}
		if(coordinates[x][y] == 0) {
			coordinates[x][y] = 99999;
		}
		return result;
	}

	private void inCaseSank(Ship victim, int x, int y) {
		if(victim != null) {
			LOGGER.info("A " + victim.getType() + " sank id: " + coordinates[x][y]);
		}
	}
	
	public boolean isThereAnyShipLeft() {
		return numberOfShipTiles > 0;
	}
	
	public void setShips(List<Ship> ships, List<Integer> xs, List<Integer> ys) {
		for(int i = 0; i < ships.size() ; ++i) {
			placeShip(ships.get(i), xs.get(i), ys.get(i));
		}
		
	}

	private Ship updateShipData(int x, int y) {
		int id = getIndexFromID(x, y);
		Ship victim = ships.remove(id);
		victim.sufferHit();
		ships.add(victim);
		return victim.getHealthPoints() > 0 ? null : victim;
	}

	private int getIndexFromID(int x, int y) {
		return coordinates[x][y]-1;
	}
	
	private boolean isThePlaceFree (Ship ship, int x, int y) {
		boolean free = true;
		for(int i = 0 ; i < 4 && free ; ++i) {
			for(int j = 0 ; j < 4 && free ; ++j) {
				free &= checkCoordinate(ship.isThereShip(i,j), x+i, y+j);
			}
		}
		return free;
	}

	private boolean checkCoordinate(boolean isThereShip, int x, int y) {
		if(isThereShip) {
			return coordinates[x][y] == 0;
		}
		return true;
	}
	
	public void placeShipToRandomePosition(Ship ship) {
		LOGGER.info("Try to place ship : " + ship.getType());
		boolean placed = false;
		
		while (!placed) {
			int x = (int) generateRandome(SIZE_OF_SHIP_MAP, sizeX);
			int y = (int) generateRandome(SIZE_OF_SHIP_MAP, sizeY);
			
			try{
				placeShip(ship, x, y);
				placed = true;
			} catch(IndexOutOfBoundsException|IllegalArgumentException|NullPointerException e) {
				
			}
		}
		LOGGER.info("Ship placed: " + ship.getType());
	}
	
	private double generateRandome(int moreThan, int lessThan) {
		final float MULTIPLIER = 100f;
		return ((Math.random() * (float) lessThan * MULTIPLIER) % (lessThan + moreThan))
				- moreThan;
	}
	
	@Override
	public String toString() {
		StringBuilder strBuilder = new StringBuilder();
		for(int i = 0 ; i < coordinates.length ; ++i) {
			strBuilder.append("\n");
			for(int j = 0; j < coordinates[i].length ; ++j) {
				strBuilder.append(coordinates[i][j] + " ");
			}
		}
		return strBuilder.toString();
	}
	
	public int getSizeX() {
		return sizeX;
	}
	
	public int getSizeY() {
		return sizeY;
	}
	
	public void setSize(final int sizeX, final int sizeY) {
		this.sizeY = sizeY;
		this.sizeX = sizeX;
		coordinates = new int[sizeX][sizeY];
		initCoordinates();
	}

	@Override
	public String printMap() {
		StringBuilder strBuilder = new StringBuilder();
		for(int i = 0; i < coordinates.length ; ++i) {
			for(int j = 0; j < coordinates[i].length ; ++j) {
				if(coordinates[i][j] > 0 && coordinates[i][j] != 99999) {
					strBuilder.append("X ");
				} else if(coordinates[i][j] == 0){
					strBuilder.append(". ");
				} else if(coordinates[i][j] == 99999){
					strBuilder.append("* ");
				} else {
					strBuilder.append("O ");
				}
			}
			strBuilder.append("\n");
		}
		return strBuilder.toString();
	}
	
	private void initCoordinates() {
		for(int[] num : coordinates) {
			Arrays.fill(num, 0);
		}
	}

	@Override
	public int getHP() {
		return numberOfShipTiles;
	}
}
