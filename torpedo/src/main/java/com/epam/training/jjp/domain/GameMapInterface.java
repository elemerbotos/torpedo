package com.epam.training.jjp.domain;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public interface GameMapInterface {
	
	List<Ship> getShips();
	
	void setShips(List<Ship> ships, List<Integer> xs, List<Integer> ys);
	
	boolean shoot(int x, int y);
	
	boolean isThereAnyShipLeft();
}
