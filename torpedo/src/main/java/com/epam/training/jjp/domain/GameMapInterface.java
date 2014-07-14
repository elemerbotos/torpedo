package com.epam.training.jjp.domain;

import java.util.List;

public interface GameMapInterface {
	
	List<Ship> getShips();
	
	int shoot(int x, int y);
	
	boolean isThereAnyShipLeft();
	
	void placeShipToRandomePosition(Ship ship);
	
	int getDefaultSize();
	
	int getSizeX();
	int getSizeY();

	String printMap();

	int getHP();
}