package com.danilov.mylivewallpaper.services;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Random;

import org.andengine.engine.LimitedFPSEngine;
import org.andengine.engine.camera.Camera;
import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.FillResolutionPolicy;
import org.andengine.entity.Entity;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.Background;
import org.andengine.entity.sprite.Sprite;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.extension.ui.livewallpaper.BaseLiveWallpaperService;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import android.content.Context;
import android.hardware.SensorManager;
import android.os.Looper;
import android.view.Display;
import android.view.WindowManager;

import com.badlogic.gdx.math.Vector2;
import com.danilov.mylivewallpaper.Cloud;
import com.danilov.mylivewallpaper.Star;
import com.danilov.mylivewallpaper.StarContainer;
import com.danilov.mylivewallpaper.util.MyEntity;
import com.danilov.mylivewallpaper.util.ResourceManager;


//TODO: STARS[v], MOON[v], SUN[x]

public class MyWallpaperService extends BaseLiveWallpaperService  {
	
	private Camera mCamera;
	private ResourceManager resourceManager;
	
    private Scene mScene;
    private PhysicsWorld mPhysicsWorld;
    private ScreenOrientation mScreenOrientation = ScreenOrientation.LANDSCAPE_FIXED;

    private MyEntity bottomLayer;
    private MyEntity topLayer;
    
    private Sprite astranomicalObject;
    
    private boolean showStars = false;
    private StarContainer starContainer;
    
    private String currentTime = "";
    
    public static int CAMERA_WIDTH;
    public static int CAMERA_HEIGHT;


	@Override
	public EngineOptions onCreateEngineOptions() {
		WindowManager window = (WindowManager) this.getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
        Display display = window.getDefaultDisplay();
        CAMERA_WIDTH = display.getWidth();
        CAMERA_HEIGHT = display.getHeight();
		mCamera = new Camera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);
		return new EngineOptions(true, mScreenOrientation, new FillResolutionPolicy(), mCamera);
	}
	
	@Override
	public LimitedFPSEngine onCreateEngine(EngineOptions pEngineOptions) {
	    return new LimitedFPSEngine(pEngineOptions, 60);
	}

	@Override
	public void onCreateResources(
			OnCreateResourcesCallback pOnCreateResourcesCallback)
			throws Exception {
		loadGameGraphics();
		pOnCreateResourcesCallback.onCreateResourcesFinished();
	}

	@Override
	public void onCreateScene(OnCreateSceneCallback pOnCreateSceneCallback)
			throws Exception {
		mScene = new Scene();
		Looper.prepare();
		mPhysicsWorld = new PhysicsWorld(new Vector2(0, SensorManager.GRAVITY_EARTH), false);
		mEngine.registerUpdateHandler(mPhysicsWorld);
		mEngine.setScene(mScene);
		bottomLayer = new MyEntity(mScene);
		topLayer = new MyEntity(mScene);
		mScene.attachChild(bottomLayer);
		mScene.attachChild(topLayer);
		/*TODO: NEXT IS FOR TEST - REMOVE IT LATER*/
		createGround();
		testCreateClouds();
		setSceneUpdateHandler();
		handleTimeChanges();
		pOnCreateSceneCallback.onCreateSceneFinished(mScene);
	}
	
	private void testCreateClouds(){
	    ITiledTextureRegion cloudRegion = resourceManager.getCloudTexture();
		VertexBufferObjectManager manager = mEngine.getVertexBufferObjectManager();
		Cloud cloud = new Cloud(10.0f, 90.0f, cloudRegion, manager, new WorldBundle(getPhysicsWorld(), topLayer));
		cloud.createUpdateHandler();
		cloud = new Cloud(50.0f, 30.0f, cloudRegion, manager, new WorldBundle(getPhysicsWorld(), topLayer));
		cloud.createUpdateHandler();
		cloud = new Cloud(90.0f, 60.0f, cloudRegion, manager, new WorldBundle(getPhysicsWorld(), topLayer));
		cloud.createUpdateHandler();
	}
	
	private void createGround() {
		ITextureRegion groundRegion = resourceManager.getGroundTexture();
		Sprite groundSprite = new Sprite(0, CAMERA_HEIGHT - 48, groundRegion, mEngine.getVertexBufferObjectManager());
		getScene().attachChild(groundSprite);
		while ((groundSprite.getX() + groundSprite.getWidth()) < CAMERA_WIDTH) {
			groundSprite = new Sprite(groundSprite.getWidth(), CAMERA_HEIGHT - 48, groundRegion, mEngine.getVertexBufferObjectManager()); 
			getScene().attachChild(groundSprite);
		}	
	}

	@Override
	public void onPopulateScene(Scene pScene,
			OnPopulateSceneCallback pOnPopulateSceneCallback) throws Exception {
		pOnPopulateSceneCallback.onPopulateSceneFinished();
	}
	
	private void loadGameGraphics() {
		resourceManager = new ResourceManager(getTextureManager(), this);
	}
	
	public Scene getScene() {
		return mScene;
	}
	
	public PhysicsWorld getPhysicsWorld() {
		return mPhysicsWorld;
	}
	
	public static class WorldBundle {
		
		private PhysicsWorld mPhysicsWorld;
		private Entity mScene;
		
		public WorldBundle(PhysicsWorld mPhysicsWorld, Entity mScene) {
			this.mPhysicsWorld = mPhysicsWorld;
			this.mScene = mScene;
		}
		
		public Entity getScene() {
			return mScene;
		}
		
		public PhysicsWorld getPhysicsWorld() {
			return mPhysicsWorld;
		}
	}
	
	private void setEvening() {
		String time = "evening";
		if(currentTime.equals(time)) {
			return;
		}
		currentTime = time;
		mScene.setBackground(new Background(0f, 0f, 0.5576f));
		showStars();
		hideAstronomicalObject();
	}
	
	private void setNight() {
		String time = "night";
		if(currentTime.equals(time)) {
			return;
		}
		currentTime = time;
		mScene.setBackground(new Background(0f, 0f, 0.2576f));
		showStars();
		showMoon();
	}
	
	private void setDay() {
		String time = "day";
		if(currentTime.equals(time)) {
			return;
		}
		currentTime = time;
		mScene.setBackground(new Background(0.09804f, 0.6274f, 0.8784f));
		deleteStars();
		showSun();
	}
	
	private void setSceneUpdateHandler() {
		mScene.registerUpdateHandler(new IUpdateHandler(){
			
			private float secondsElapsed = 0;
			
			@Override
			public void onUpdate(float pSecondsElapsed) {
				secondsElapsed += pSecondsElapsed;
				if(secondsElapsed > 5){
					handleTimeChanges();
					secondsElapsed = 0;
				}
				if(showStars) {
					starContainer.updateStars(pSecondsElapsed);
				}
			}

			@Override
			public void reset() {
				// TODO Auto-generated method stub
				
			}
			
		});
	}
	
	private void handleTimeChanges() {
		Calendar calendar = Calendar.getInstance();
		int hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);
		if(hourOfDay >= 23 || hourOfDay <= 5){
			setNight();
		}else if(hourOfDay >= 17) {
			setEvening();
		}else {
			setDay();
		}
	}
	
	private void showMoon() {
		if(astranomicalObject != null) {
			astranomicalObject.detachSelf();
		}
	    ITextureRegion moonRegion = resourceManager.getAstroTexture(ResourceManager.ASTRO_MOON);
		VertexBufferObjectManager manager = mEngine.getVertexBufferObjectManager();
		astranomicalObject = new Sprite(CAMERA_WIDTH - 60, 50, moonRegion, manager);
		bottomLayer.attachChild(astranomicalObject);
	}
	
	private void showSun() {
		if(astranomicalObject != null) {
			astranomicalObject.detachSelf();
		}
	}
	
	private void hideAstronomicalObject() {
		if(astranomicalObject != null) {
			astranomicalObject.detachSelf();
		}
	}
	
	private void showStars() {
		if(starContainer != null) {
			starContainer.deleteStars();
		}
		ArrayList<Star> starsList = new ArrayList<Star>();
		int starsInRow = CAMERA_WIDTH / 80;
		if(starsInRow >= 6) {
			starsInRow = 6;
		}
		int starsInColumn = 3;
		int starSpaceRow = CAMERA_WIDTH / starsInRow;
		int starSpaceColumn = CAMERA_HEIGHT / (3 * starsInColumn);
		Random random = new Random();
		VertexBufferObjectManager manager = mEngine.getVertexBufferObjectManager();
		TiledTextureRegion starRegion = resourceManager.getStarTexture();
		for(int j = 0; j < starsInColumn; j++) {
			for(int i = 0; i < starsInRow; i++){
				Star star = new Star((float)i * starSpaceRow + random.nextInt(starSpaceRow - 10), (float)j * starSpaceColumn + random.nextInt(starSpaceColumn - 10), starRegion, manager, new WorldBundle(getPhysicsWorld(), bottomLayer));
				starsList.add(star);
			}
		}
		starContainer = new StarContainer(starsList);
		showStars = true;
	}
	
	private void deleteStars() {
		if (starContainer != null) {
			starContainer.deleteStars();
		}
		showStars = false;
	}
	
	

}
