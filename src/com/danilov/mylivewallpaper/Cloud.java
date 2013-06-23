package com.danilov.mylivewallpaper;

import java.util.Random;

import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.entity.sprite.Sprite;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.danilov.mylivewallpaper.services.MyWallpaperService;
import com.danilov.mylivewallpaper.services.MyWallpaperService.WorldBundle;

public class Cloud extends Sprite {
	
	private Body mPhysicsBody;
	private static final float[] velocitiesNormal = {1.1f, 1.3f, 1.6f};
	private static Random random = new Random();
	private PhysicsWorld mPhysicsWorld;
	private static final FixtureDef FIXTURE_DEF = PhysicsFactory.createFixtureDef(0, 0f, 0f, false);
	public static final float PIXEL_TO_METER_RATIO_DEFAULT = 32.0f;
	private float secondsElapsed = 0;
	
	public Cloud(float pX, float pY, ITextureRegion pTiledTextureRegion,
			VertexBufferObjectManager pVertexBufferObjectManager, WorldBundle bundle) {
		super(pX, pY, pTiledTextureRegion, pVertexBufferObjectManager);
		this.mPhysicsWorld = bundle.getPhysicsWorld();
		mPhysicsBody = PhysicsFactory.createBoxBody(mPhysicsWorld, this, BodyType.KinematicBody, FIXTURE_DEF);
		mPhysicsBody.setUserData(new BodyUserData(this));
		bundle.getScene().attachChild(this);
		mPhysicsWorld.registerPhysicsConnector(new PhysicsConnector(this, mPhysicsBody, true, true));
		int randomNum = random.nextInt(velocitiesNormal.length);
		setVelocity(velocitiesNormal[randomNum]);
	}
	
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
				getCloud().setVelocity(velocitiesNormal[randomNum]);
			}else {
				secondsElapsed += pSecondsElapsed;
			}
			if(isOutOfScreen()) {
				resetCloud();
			}
		}
	
		@Override
		public void reset() {
			
		}
		
	}

	/*If cloud is out of screen [right] than put it out of screen [left] - simple loop*/
	private void resetCloud() {
		float desiredX = -this.getWidth();
		float desiredY = mPhysicsBody.getPosition().y;
		final Vector2 desiredPosition = new Vector2(desiredX / PIXEL_TO_METER_RATIO_DEFAULT, desiredY);
		mPhysicsBody.setTransform(desiredPosition, mPhysicsBody.getAngle());
	}
	
	private boolean isOutOfScreen() {
		boolean result = false;
		float x = this.getX();
		if(x > MyWallpaperService.CAMERA_WIDTH) {
			result = true;
		}
		return result;
	}
	
	private void setVelocity(float value){
		mPhysicsBody.setLinearVelocity(new Vector2(value, 0));
	}
	
	public Cloud getCloud() {
		return this;
	}
	
	public Body getBody() {
		return mPhysicsBody;
	}
	
	private class BodyUserData {
		
	    Sprite sprite;
	    
	    BodyUserData(final Sprite sprite) {
	            this.sprite = sprite;
	    }
	    
	}


	
}