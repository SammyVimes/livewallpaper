package com.danilov.mylivewallpaper.util;

import org.andengine.entity.Entity;
import org.andengine.entity.scene.Scene;

public class MyEntity extends Entity {

	private Scene scene;

	public MyEntity(Scene scene){
		super();
		this.scene = scene;
	}

	public Scene getScene(){
		return scene;
	}
}