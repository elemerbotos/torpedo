package com.epam.training.jjp.domain;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class GameMap implements GameMapInterface {
	private int[][] coordinates;
	private final int size;
	private List<Ship> ships;
	private static final int DEFAULT_SIZE = 30;
	
	
	public GameMap(final int size) {
		this.size = size;
		ships = new ArrayList<>();
		coordinates = new int[size][size];
	}
	
	public GameMap() {
		size = DEFAULT_SIZE;
		ships = new LinkedList<>();
		coordinates = new int[size][size];
	}
	
	public List<Ship> getShips() {
		return ships;
	}
	
	public void addShip(Ship ship, int x, int y) {
		placeShip(ship, x, y);
	}
	
	public void placeShip(Ship ship, int x, int y) throws IndexOutOfBoundsException,IllegalArgumentException {
		if(!isThePlaceFree(ship, x, y)) {
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
		
		ships.add(ship);
	}
	
	public boolean shoot(int x, int y) {
		if(coordinates[x][y] < 0) {
			throw new IllegalArgumentException("Not the first try! ");
		}
		boolean result = false;
		if(coordinates[x][y] == 0) {
			updateShipData(x, y);
			
			coordinates[x][y] *= -1;
			
			result = true;
		}
		return result;
	}
	
	public boolean isThereAnyShipLeft() {
		boolean isThere = false;
		for(int i = 0, j = 0; i < size ; ++i, ++j) {
			isThere |= coordinates[i][j] > 0;
		}
		return isThere;
	}
	
	public void setShips(List<Ship> ships, List<Integer> xs, List<Integer> ys) {
		for(int i = 0; i < ships.size() ; ++i) {
			addShip(ships.get(i), xs.get(i), ys.get(i));
		}
		
	}

	private void updateShipData(int x, int y) {
		int id = getIndexFromID(x, y);
		Ship victim = ships.get(id);
		victim.sufferHit();
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
		
		boolean placed = false;
		
		while(!placed) {
			int x = (int) ((Math.random() * (float)size * (float)10) % size);
			int y = (int) ((Math.random() * (float)size * (float)10) % size);
			
			try{
				placeShip(ship, x, y);
				placed = true;
			} catch(IndexOutOfBoundsException|IllegalArgumentException e) {
				
			}
		}
	}
}
