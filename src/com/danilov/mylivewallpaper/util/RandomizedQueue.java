package com.danilov.mylivewallpaper.util;

import java.util.ArrayList;
import java.util.Random;

public class RandomizedQueue<T> {
	
	private ArrayList<T> list;
	private int current = 0;
	private int listSize;
	
	@SuppressWarnings("unchecked")
	public RandomizedQueue(ArrayList<T> list) {
		this.list = (ArrayList<T>) list.clone();
		listSize = list.size();
		randomize();
	}
	
	private void randomize() {
		Random random = new Random();
		for(int i = 0; i < listSize; i++) {
			int randomIndex = random.nextInt(listSize);
			while(i == randomIndex) {
				randomIndex = random.nextInt(listSize);
			}
			T temporary = list.get(randomIndex);
			list.set(randomIndex, list.get(i));
			list.set(i, temporary);
		}
	}
	
	public T getNext() {
		if(current > (listSize - 1)) {
			current = 0;
			randomize();
		}
		T cur = list.get(current);
		current++;
		return cur;
	}
	
	public ArrayList<T> getList() {
		return list;
	}
	
}
