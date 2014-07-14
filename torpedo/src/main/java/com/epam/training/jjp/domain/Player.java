package com.epam.training.jjp.domain;

import java.util.List;

public class Player {
	protected static final float RANDOME_MULTIPLIER = 100f;
	protected List<Ship> ships;
	protected Byte[][] map;
	protected int sizeX = 30;
	protected int sizeY = 30;
	
	void setShips(List<Ship> ships) {
		this.ships = ships;
	}
	
	public Coordinate selectCoordinate() {
		return new Coordinate(0,0);
	}
	
	public void setMapSize(int sizeX, int sizeY) {
		map = new Byte[sizeX][sizeY];
		for(int x = 0 ; x < sizeX ; ++x) {
			for(int y = 0 ; y < sizeY ; ++y) {
				map[x][y] = 0;
			}
		}
		this.sizeX = sizeX;
		this.sizeY = sizeY;
	}
	
	public void Hit(Boolean isItAHit) {
		
	}
}
