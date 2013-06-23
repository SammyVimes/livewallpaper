package com.danilov.mylivewallpaper;

import org.andengine.engine.LimitedFPSEngine;
import org.andengine.engine.camera.Camera;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.FillResolutionPolicy;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.Background;
import org.andengine.entity.sprite.Sprite;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.ui.IGameInterface.OnCreateResourcesCallback;
import org.andengine.ui.IGameInterface.OnCreateSceneCallback;
import org.andengine.ui.IGameInterface.OnPopulateSceneCallback;
import org.andengine.ui.activity.BaseGameActivity;

import android.content.Context;
import android.hardware.SensorManager;
import android.view.Display;
import android.view.WindowManager;

import com.badlogic.gdx.math.Vector2;
import com.danilov.mylivewallpaper.services.MyWallpaperService.WorldBundle;
import com.danilov.mylivewallpaper.util.MyEntity;

public class MainActivity extends BaseGameActivity  {

	private Camera mCamera;
    private BitmapTextureAtlas mBitmapTextureAtlas;
    private Scene mScene;
    private PhysicsWorld mPhysicsWorld;
    private ScreenOrientation mScreenOrientation = ScreenOrientation.LANDSCAPE_FIXED;
    private ITextureRegion cloudRegion;
    private ITextureRegion groundRegion;
    private MyEntity bottomLayer;
    private MyEntity topLayer;
    private int CAMERA_WIDTH;
    private int CAMERA_HEIGHT;
    
    private static final float ForceImpuse = -5;

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
		mPhysicsWorld = new PhysicsWorld(new Vector2(0, SensorManager.GRAVITY_EARTH), false);
		mScene.setBackground(new Background(0.09804f, 0.6274f, 0.8784f));
		mEngine.registerUpdateHandler(mPhysicsWorld);
		mEngine.setScene(mScene);
		bottomLayer = new MyEntity(mScene);
		topLayer = new MyEntity(mScene);
		mScene.attachChild(bottomLayer);
		mScene.attachChild(topLayer);
		/*TODO: NEXT IS FOR TEST - REMOVE IT LATER*/
		Sprite groundSprite = new Sprite(0, CAMERA_HEIGHT - 48, groundRegion, mEngine.getVertexBufferObjectManager());
		getScene().attachChild(groundSprite);
		testCreateClouds();
		pOnCreateSceneCallback.onCreateSceneFinished(mScene);
	}
	
	private void testCreateClouds(){
		VertexBufferObjectManager manager = mEngine.getVertexBufferObjectManager();
		Cloud cloud = new Cloud(30.0f, 90.0f, cloudRegion, manager, new WorldBundle(getPhysicsWorld(), topLayer));
		cloud.createUpdateHandler();
		cloud = new Cloud(50.0f, 30.0f, cloudRegion, manager, new WorldBundle(getPhysicsWorld(), topLayer));
		cloud.createUpdateHandler();
		cloud = new Cloud(10.0f, 60.0f, cloudRegion, manager, new WorldBundle(getPhysicsWorld(), topLayer));
		cloud.createUpdateHandler();
	}

	@Override
	public void onPopulateScene(Scene pScene,
			OnPopulateSceneCallback pOnPopulateSceneCallback) throws Exception {
		pOnPopulateSceneCallback.onPopulateSceneFinished();
	}
	
	private void loadGameGraphics() {
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");
		mBitmapTextureAtlas = new BitmapTextureAtlas(getTextureManager(), 512, 512, TextureOptions.BILINEAR);
		cloudRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(mBitmapTextureAtlas, this, "cloud_sprite.png", 0, 0);
		mBitmapTextureAtlas.load();
		BitmapTextureAtlas groundAtlas = new BitmapTextureAtlas(getTextureManager(), 700, 50, TextureOptions.BILINEAR);
		groundRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(groundAtlas, this, "ground_sprite.png", 0, 0);
		groundAtlas.load();
	}
	
	public Scene getScene() {
		return mScene;
	}
	
	public PhysicsWorld getPhysicsWorld() {
		return mPhysicsWorld;
	}


}
