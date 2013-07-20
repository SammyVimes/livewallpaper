package com.danilov.mylivewallpaper;

import java.util.Random;

import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.entity.sprite.Sprite;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.danilov.mylivewallpaper.services.MyWallpaperService;
import com.danilov.mylivewallpaper.services.MyWallpaperService.WorldBundle;

public class FlyingAnimatedObject extends AnimatedSprite {
	
	private Body mPhysicsBody;
	private static Random random = new Random();
	private PhysicsWorld mPhysicsWorld;
	protected static final FixtureDef FIXTURE_DEF = PhysicsFactory.createFixtureDef(0, 0f, 0f, false);
	public static final float PIXEL_TO_METER_RATIO_DEFAULT = 32.0f;
	private float secondsElapsed = 0;
	
	public FlyingAnimatedObject(float pX, float pY, ITiledTextureRegion pTiledTextureRegion,
			VertexBufferObjectManager pVertexBufferObjectManager, WorldBundle bundle) {
		super(pX, pY, pTiledTextureRegion, pVertexBufferObjectManager);
		this.mPhysicsWorld = bundle.getPhysicsWorld();
		mPhysicsBody = PhysicsFactory.createBoxBody(mPhysicsWorld, this, BodyType.KinematicBody, FIXTURE_DEF);
		mPhysicsBody.setUserData(new BodyUserData(this));
		bundle.getScene().attachChild(this);
		mPhysicsWorld.registerPhysicsConnector(new PhysicsConnector(this, mPhysicsBody, true, true));
	}
	
	public void createUpdateHandler() {
	}


	/*If cloud is out of screen [right] than put it out of screen [left] - simple loop*/
	void resetObject() {
		float desiredX = -this.getWidth();
		float desiredY = mPhysicsBody.getPosition().y;
		final Vector2 desiredPosition = new Vector2(desiredX / PIXEL_TO_METER_RATIO_DEFAULT, desiredY);
		mPhysicsBody.setTransform(desiredPosition, mPhysicsBody.getAngle());
	}
	
	private void flyBack() {
		float velocityX = -mPhysicsBody.getLinearVelocity().x;
		setVelocity(velocityX);
	}
	
	boolean isOutOfScreen() {
		boolean result = false;
		float x = this.getX();
		if(x > MyWallpaperService.CAMERA_WIDTH) {
			result = true;
		}
		return result;
	}
	
	protected void setVelocity(float value){
		mPhysicsBody.setLinearVelocity(new Vector2(value, 0));
	}
	
	public Body getBody() {
		return mPhysicsBody;
	}
	
	class BodyUserData {
		
	    AnimatedSprite sprite;
	    
	    BodyUserData(final AnimatedSprite sprite) {
	            this.sprite = sprite;
	    }
	    
	}
}
