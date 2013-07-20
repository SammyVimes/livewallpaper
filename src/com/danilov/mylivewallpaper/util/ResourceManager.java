package com.danilov.mylivewallpaper.util;

import java.util.HashMap;
import java.util.Map;

import org.andengine.opengl.texture.TextureManager;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.texture.region.TiledTextureRegion;

import android.content.Context;

public class ResourceManager {
	
	private TextureManager manager;
	private Context context;
	
	private static final String STAR_ATLAS = "STAR_ATLAS";
	private static final String GROUND_ATLAS = "GROUND_ATLAS";
	private static final String ASTRO_ATLAS = "ASTRO_ATLAS";
	private static final String CLOUD_ATLAS= "CLOUD_ATLAS";
	
	public static final int ASTRO_SUN = 1;
	public static final int ASTRO_MOON = 2;
	
	private Map<String, Boolean> atlasStates = new HashMap<String, Boolean>();
	private Map<String, BitmapTextureAtlas> atlasMap = new HashMap<String, BitmapTextureAtlas>();
	
	
	public ResourceManager(TextureManager manager, Context context) {
		this.manager = manager;
		this.context = context;
		init();
	}
	
	private void init() {
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");
		BitmapTextureAtlas atlas = new BitmapTextureAtlas(manager, 512, 512, TextureOptions.BILINEAR);
		atlasMap.put(CLOUD_ATLAS, atlas);
		atlasStates.put(CLOUD_ATLAS, false);
		atlas = new BitmapTextureAtlas(manager, 120, 60);;
		atlasMap.put(ASTRO_ATLAS, atlas);
		atlasStates.put(ASTRO_ATLAS, false);
		atlas = new BitmapTextureAtlas(manager, 700, 50, TextureOptions.BILINEAR);
		atlasMap.put(GROUND_ATLAS, atlas);
		atlasStates.put(GROUND_ATLAS, false);
		atlas = new BitmapTextureAtlas(manager, 120, 20);
		atlasMap.put(STAR_ATLAS, atlas);
		atlasStates.put(STAR_ATLAS, false);
		
	}
	
	private BitmapTextureAtlas loadAtlas(String whatAtlas) {
		BitmapTextureAtlas atlas = atlasMap.get(whatAtlas);
		if (!atlasStates.get(whatAtlas)) {
			atlas.load();
		}
		return atlas;
	}
	
	public void unloadAtlas(String whatAtlas) {
		BitmapTextureAtlas atlas = atlasMap.get(whatAtlas);
		if (atlasStates.get(whatAtlas)) {
			atlas.unload();
		}
	}
	
	public ITiledTextureRegion getCloudTexture() {
		ITiledTextureRegion cloudTexture = null;
		BitmapTextureAtlas atlas = loadAtlas(CLOUD_ATLAS);
		cloudTexture = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(atlas, context, "cloud_sprite.png", 0, 0, 1, 1);
		return cloudTexture;
	}
	
	public ITextureRegion getGroundTexture() {
		ITextureRegion groundTexture = null;
		BitmapTextureAtlas atlas = loadAtlas(GROUND_ATLAS);
		groundTexture = BitmapTextureAtlasTextureRegionFactory.createFromAsset(atlas, context, "ground_sprite.png", 0, 0);
		return groundTexture;
	}
	
	public TiledTextureRegion getStarTexture() {
		TiledTextureRegion starTexture = null;
		BitmapTextureAtlas atlas = loadAtlas(STAR_ATLAS);
		starTexture = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(atlas, context, "star_sprite.png", 0, 0, 6, 1);
		return starTexture;
	}
	
	public ITextureRegion getAstroTexture(int whatAstro) {
		ITextureRegion astroTexture = null;
		BitmapTextureAtlas atlas = loadAtlas(ASTRO_ATLAS);
		switch(whatAstro) {
		case ASTRO_SUN:
			astroTexture = BitmapTextureAtlasTextureRegionFactory.createFromAsset(atlas, context, "sun_sprite.png", 60, 0);
			break;
		case ASTRO_MOON:
			astroTexture = BitmapTextureAtlasTextureRegionFactory.createFromAsset(atlas, context, "moon_sprite.png", 0, 0);
			break;
		}
		return astroTexture;
	}
	
}
