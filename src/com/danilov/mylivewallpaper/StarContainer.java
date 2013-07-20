package com.danilov.mylivewallpaper;

import java.util.ArrayList;

import com.danilov.mylivewallpaper.util.RandomizedQueue;

public class StarContainer {
	
	private static final int updateDelay = 2;
	
	private float elapsedTime = 0;
	
	RandomizedQueue<Star> starsQueue;
	
	public StarContainer(ArrayList<Star> star) {
		starsQueue = new RandomizedQueue<Star>(star);
	}
	
	public void updateStars(float elapsedTime) {
		this.elapsedTime += elapsedTime;
		if(this.elapsedTime > updateDelay) {
			blink();
			this.elapsedTime = 0;
		}
	}
	
	private void blink() {
		starsQueue.getNext().blink();
	}
	
	public void deleteStars() {
		ArrayList<Star> list = starsQueue.getList();
		for(Star star : list) {
			star.deleteStar();
		}
	}
}
