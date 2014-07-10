package com.epam.training.jjp.domain;

import java.util.List;

public class MyTorpedoAI extends Player implements TorpedoAI {

	@Override
	public void run() {
		// TODO for the future
	}

	@Override
	public List<Ship> getShips() {
		return ships;
	}
	
	@Override
	public Coordinate selectCoordinate() {
		boolean found = false;
		int x = 0;
		int y = 0;
		while(!found) {
			x = getIntFromRandome(size);
			y = getIntFromRandome(size);
			if( !map[x][y] ) {
				found = true;
				map[x][y] = true;
			}
		}
		return new Coordinate(x,y);
		
		
	}

	private int getIntFromRandome(int modulo) {
		return (int)((Math.random() * RANDOME_MULTIPLIER * (float) modulo) % modulo);
	}

}
