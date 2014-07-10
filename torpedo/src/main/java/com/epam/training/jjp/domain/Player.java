package com.epam.training.jjp.domain;

import java.util.List;

public class Player {
	protected static final float RANDOME_MULTIPLIER = 100f;
	protected List<Ship> ships;
	protected boolean[][] map;
	protected int size = 0;
	
	void setShips(List<Ship> ships) {
		this.ships = ships;
	}
	
	public Coordinate selectCoordinate() {
		return new Coordinate(0,0);
	}
	
	public void setMapSize(int size) {
		map = new boolean[size][size];
		for(int i = 0 ; i < size ; ++i) {
			for(int j = 0 ; j < size ; ++j) {
				map[i][j] = false;
			}
		}
		this.size = size;
	}
}
