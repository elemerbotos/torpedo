package com.epam.training.jjp.domain;

import java.util.List;

public interface TorpedoAI extends Runnable {
	List<Ship> getShips();
	
	void Hit(Boolean isItAHit);
}
