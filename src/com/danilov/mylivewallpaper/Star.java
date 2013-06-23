package com.danilov.mylivewallpaper;

import java.util.Random;

import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import com.danilov.mylivewallpaper.services.MyWallpaperService.WorldBundle;

public class Star extends AnimatedSprite {
	
	//TODO: couple of arrays, those stores info about frames OR random speed between 100 and 110

	public Star(float pX, float pY, ITiledTextureRegion pTiledTextureRegion,
			VertexBufferObjectManager manager, WorldBundle bundle) {
		super(pX, pY, pTiledTextureRegion, manager);
		bundle.getScene().attachChild(this);
		animate(85);
	}
	
	public void deleteStar() {
		detachSelf();
	}

}
