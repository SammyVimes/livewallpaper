package com.danilov.mylivewallpaper;

import java.util.Random;

import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.danilov.mylivewallpaper.services.MyWallpaperService.WorldBundle;

public class Cloud extends FlyingAnimatedObject {
	
	private Body mPhysicsBody;
	private static final float[] velocitiesNormal = {1.1f, 1.3f, 1.6f};
	private static Random random = new Random();
	private PhysicsWorld mPhysicsWorld;
	public static final float PIXEL_TO_METER_RATIO_DEFAULT = 32.0f;
	private float secondsElapsed = 0;
	
	public Cloud(float pX, float pY, ITiledTextureRegion pTiledTextureRegion,
			VertexBufferObjectManager pVertexBufferObjectManager, WorldBundle bundle) {
		super(pX, pY, pTiledTextureRegion, pVertexBufferObjectManager, bundle);
		int randomNum = random.nextInt(velocitiesNormal.length);
		setVelocity(velocitiesNormal[randomNum]);
	}
	
	@Override
	public void createUpdateHandler() {
		registerUpdateHandler(new UpdateHandler());
	}
	
	private class UpdateHandler implements IUpdateHandler {
		
		private float secondsElapsed = 0;

		@Override
		public void onUpdate(float pSecondsElapsed) {
			if(secondsElapsed > 2) {
				secondsElapsed = 0;
				int randomNum = random.nextInt(velocitiesNormal.length);
				setVelocity(velocitiesNormal[randomNum]);
			}else {
				secondsElapsed += pSecondsElapsed;
			}
			if(isOutOfScreen()) {
				resetObject();
			}
		}
	
		@Override
		public void reset() {
			
		}
		
	}
	
}