package com.epam.training.jjp.domain;

import java.util.ArrayList;
import java.util.List;

public class MyTorpedoAI extends Player implements TorpedoAI {
	private List<Coordinate> prevShoots;
	private Coordinate prevShoot;
	
	public MyTorpedoAI() {
		prevShoots = new ArrayList<Coordinate>();
	}
	
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
		if(previousTryIsAHit) {
			prevShoots.add(prevShoot);
		}
		
		if(prevShoots.size()>0) {
			return smartSelectCoordinate();
		} else {
			return simpleSelectCoordinate();
		}
	}

	private Coordinate simpleSelectCoordinate() {
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
		prevShoot = new Coordinate(x,y);
		return prevShoot;
	}

	private Coordinate smartSelectCoordinate() {
		Coordinate coord = prevShoots.remove(0);
		
	}
	
	@Override
	public void setPreviousTryIsAHit(boolean itIs) {
		previousTryIsAHit = itIs;
	}

	private int getIntFromRandome(int modulo) {
		return (int)((Math.random() * RANDOME_MULTIPLIER * (float) modulo) % modulo);
	}

}
