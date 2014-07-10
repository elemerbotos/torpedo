package com.epam.training.jjp.domain;

public class Ship {

	int healthPoints;
	int type;
	byte[][] shipMap = new byte[4][4];
	
	public Ship() {
		healthPoints = 0;
		type = 0;
	}
	
	public Ship(byte[][] shipMap, int healthPoints, int type ) {
		this.healthPoints = healthPoints;
		this.shipMap = shipMap;
		this.type = type;
	}

	public int getDefaultSize() {
		return 4;
	}

	public boolean isThereShip(int x, int y) {
		return shipMap[x][y] > 0;
	}

	public void sufferHit() {
		--healthPoints;
	}

	public int getType() {
		return type;
	}

	public int getHealthPoints() {
		return healthPoints;
	}

	public byte[][] getShipMap() {
		return shipMap;
	}

	public void setShipMap(byte[][] shipMap) {
		this.shipMap = shipMap;
	}

	public void setHealthPoints(int healthPoints) {
		this.healthPoints = healthPoints;
	}

	public void setType(int type) {
		this.type = type;
	}

}
