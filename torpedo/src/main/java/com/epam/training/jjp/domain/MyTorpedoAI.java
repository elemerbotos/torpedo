package com.epam.training.jjp.domain;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class MyTorpedoAI extends Player implements TorpedoAI {

	private Boolean previousShootWasAHit = false;
	private Coordinate prevShoot;
	private Queue<Coordinate> prevShootsNeighbours;
	
	public MyTorpedoAI() {
		prevShootsNeighbours = new LinkedList<>();
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
		if(previousShootWasAHit) {
			addNeighboursToAQueu(prevShoot);
			map[prevShoot.getX()][prevShoot.getY()] = 1;
		}
		Coordinate choice;
		if(prevShootsNeighbours.isEmpty()) {
		
			boolean found = false;
			int x = 0;
			int y = 0;
			while(!found) {
				x = getIntFromRandome(sizeX);
				y = getIntFromRandome(sizeY);
				prevShoot = new Coordinate(x,y);
				if( map[x][y] == 0 ) {
					found = true;
					map[x][y] = -1;
				}
			}
			choice = new Coordinate(x,y);
		} else {
			choice = prevShootsNeighbours.poll();
		}
		return choice;	
	}
	
	
	
	private void addNeighboursToAQueu(Coordinate prevShoot2) {
		int x = prevShoot2.getX();
		int y = prevShoot2.getY();
		if(x+1 < sizeX && map[x+1][y] == 0) {
			prevShootsNeighbours.add(new Coordinate(x+1, y));
			map[x+1][y] = -1;
		}
		
		if(x-1 >= 0 && map[x-1][y] == 0) {
			prevShootsNeighbours.add(new Coordinate(x-1, y));
			map[x-1][y] = -1;
		}
		
		if(y+1 < sizeY && map[x][y+1] == 0) {
			prevShootsNeighbours.add(new Coordinate(x, y+1));
			map[x][y+1] = -1;
		}
		
		if(y-1 >= 0 && map[x][y-1] == 0) {
			prevShootsNeighbours.add(new Coordinate(x, y-1));
			map[x][y-1] = -1;
		}
		
	}

	public void Hit(Boolean isItAHit) {
		previousShootWasAHit = isItAHit;
	}

	private int getIntFromRandome(int modulo) {
		return (int)((Math.random() * RANDOME_MULTIPLIER * (float) modulo) % modulo);
	}

}
