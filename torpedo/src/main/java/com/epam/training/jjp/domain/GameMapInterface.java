package com.epam.training.jjp.domain;

import java.util.List;

public interface GameMapInterface {
	
	List<Ship> getShips();
	
	boolean shoot(int x, int y);
	
	boolean isThereAnyShipLeft();
	
	void placeShipToRandomePosition(Ship ship);
	
	int getDefaultSize();
	
	int getSize();
	
	void setSize(int size);

	String printMap();
}