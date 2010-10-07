package com.studioa.survivorgame;

import org.anddev.andengine.engine.Engine;
import org.anddev.andengine.engine.camera.Camera;
import org.anddev.andengine.engine.options.EngineOptions;
import org.anddev.andengine.engine.options.EngineOptions.ScreenOrientation;
import org.anddev.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.entity.scene.menu.MenuScene;
import org.anddev.andengine.entity.scene.menu.MenuScene.IOnMenuItemClickListener;
import org.anddev.andengine.entity.scene.menu.item.IMenuItem;
import org.anddev.andengine.entity.scene.menu.item.SpriteMenuItem;
import org.anddev.andengine.entity.sprite.Sprite;
import org.anddev.andengine.entity.util.FPSLogger;
import org.anddev.andengine.entity.sprite.AnimatedSprite;
import org.anddev.andengine.opengl.texture.Texture;
import org.anddev.andengine.opengl.texture.TextureOptions;
import org.anddev.andengine.opengl.texture.region.TextureRegion;
import org.anddev.andengine.opengl.texture.region.TextureRegionFactory;
import org.anddev.andengine.opengl.texture.region.TiledTextureRegion;

import android.view.KeyEvent;

/**
 * SurvivorGame class
 * 
 * This class serves as the entry-point for the Survivor Game
 * game, the class currently launches a menu to serve as a switchboard
 * for all functions the game offers.
 * 
 * @version 0.1alpha
 */
public class SurvivorGame extends Base implements IOnMenuItemClickListener 
{

	// World layers
	private static final int LAYER_BACKGROUND = 0;
	private static final int LAYER_CHARACTER  = LAYER_BACKGROUND + 1;
	private static final int LAYER_MENU 	  = LAYER_BACKGROUND + 2;
	
	private static final int CAMERA_WIDTH  = 640;
	private static final int CAMERA_HEIGHT = 480;

	protected static final int MENU_START = 0;
	protected static final int MENU_LOAD  = MENU_START + 1;
	protected static final int MENU_QUIT  = MENU_START + 2;

	private Texture 	       mTexture;
	private Texture 	       mMenuTexture;
	private Texture			   mCharacterTexture;
	private Texture			   mBackgroundTexture;
	private TextureRegion      mLogoTextureRegion;
	private TextureRegion	   mBackgroundTextureRegion;
	private TiledTextureRegion mCharacterTextureRegion;

	protected MenuScene 	mMenuScene;
	protected Camera 		mCamera;
	protected Scene 		mMainScene;
	protected TextureRegion mMenuStartTextureRegion;
	protected TextureRegion mMenuLoadTextureRegion;
	protected TextureRegion mMenuQuitTextureRegion;

	/**
	 * Create menu scene
	 * 
	 * This method instantiates all instances needed by the menu.
	 */
	protected void createMenuScene() 
	{
		this.mMenuScene = new MenuScene( this.mCamera );

		this.mMenuScene.addMenuItem( new SpriteMenuItem( MENU_START, this.mMenuStartTextureRegion ) );
		this.mMenuScene.addMenuItem( new SpriteMenuItem( MENU_LOAD, this.mMenuLoadTextureRegion ) );
		this.mMenuScene.addMenuItem( new SpriteMenuItem( MENU_QUIT, this.mMenuQuitTextureRegion ) );
		this.mMenuScene.buildAnimations();

		this.mMenuScene.setBackgroundEnabled( false );

		this.mMenuScene.setOnMenuItemClickListener( this );
	}
	
	/**
	 * Overridden BaseGameActivity method
	 * 
	 * This method should return an instantiated Engine and thus
	 * initialize all instances needed by the Engine.
	 */
	@Override
	public Engine onLoadEngine() 
	{
		this.mCamera = new Camera( 0, 0, CAMERA_WIDTH, CAMERA_HEIGHT );
		
		return new Engine(
			new EngineOptions(
				true, 
				ScreenOrientation.LANDSCAPE, 
				new RatioResolutionPolicy(
					CAMERA_WIDTH, 
					CAMERA_HEIGHT
				), 
				this.mCamera
			)
		);
	}

	/**
	 * Overridden BaseGameAcivity method
	 * 
	 * All resources should be loaded in this function,
	 * the reason for not using constructors to initialize
	 * is that there are loads of references to this class that
	 * are needed to initialize other classes, whenever we initialize
	 * with this class we'll be presented with a NullPointerException.
	 * 
	 * Initialize your resources here.
	 */
	@Override
	public void onLoadResources() 
	{
		this.mTexture = new Texture( 256, 64, TextureOptions.BILINEAR );
		this.mLogoTextureRegion = TextureRegionFactory.createFromAsset( this.mTexture, this, "gfx/logo.png", 0, 0 );
		this.mEngine.getTextureManager().loadTexture( this.mTexture );
		
		this.mMenuTexture = new Texture( 256, 256, TextureOptions.BILINEAR );
		this.mMenuStartTextureRegion = TextureRegionFactory.createFromAsset( this.mMenuTexture, this, "gfx/menu_start.png", 0, 0 );
		this.mMenuLoadTextureRegion = TextureRegionFactory.createFromAsset( this.mMenuTexture, this, "gfx/menu_load.png", 0, 50 );
		this.mMenuQuitTextureRegion = TextureRegionFactory.createFromAsset( this.mMenuTexture, this, "gfx/menu_quit.png", 0, 100 );
		this.mEngine.getTextureManager().loadTexture( this.mMenuTexture );
		
		this.mCharacterTexture = new Texture( 512, 256, TextureOptions.BILINEAR );
		this.mCharacterTextureRegion = TextureRegionFactory.createTiledFromAsset( this.mCharacterTexture, this, "gfx/character_animation.png", 132, 180, 2, 1 );
		this.mEngine.getTextureManager().loadTexture( this.mCharacterTexture );
		
		this.mBackgroundTexture = new Texture( 1024, 512, TextureOptions.DEFAULT );
		this.mBackgroundTextureRegion = TextureRegionFactory.createFromAsset( this.mBackgroundTexture, this, "gfx/jungle_background.png", 0, 0 );
		this.mEngine.getTextureManager().loadTexture( this.mBackgroundTexture );
	}

	/**
	 * Overridden BaseGameActivity method
	 * 
	 * All scene-related instances should be instantiated here,
	 * make sure to instantiate all in-scene instances here, otherwise
	 * they will not be rendered.
	 */
	@Override
	public Scene onLoadScene() 
	{
		this.mEngine.registerUpdateHandler( new FPSLogger() );

		this.createMenuScene();
		
		this.mMainScene = new Scene( 3 );
		this.mMainScene.setBackgroundEnabled(false);
		this.mMainScene.setChildScene( this.mMenuScene, false, true, true );
		this.mMainScene.getLayer( LAYER_BACKGROUND ).addEntity(
			new Sprite(
				0, 
				0, 
				this.mBackgroundTextureRegion
			)
		);
		
		final AnimatedSprite animatedFace = new AnimatedSprite( (CAMERA_WIDTH - 256) * 0.1f, (CAMERA_HEIGHT - 64) * 0.9f, this.mCharacterTextureRegion );
		animatedFace.animate( 100 );
		this.mMainScene.getLayer( LAYER_CHARACTER ).addEntity( animatedFace );

		final Sprite face = new Sprite( (CAMERA_WIDTH - 256) * 0.5f, (CAMERA_HEIGHT - 64) * 0.1f, this.mLogoTextureRegion );
		this.mMainScene.getLayer( LAYER_MENU ).addEntity( face );

		return this.mMainScene;
	}

	/**
	 * Overridden BaseGameActivity method
	 * 
	 * This function will be called after OnLoadScene.
	 * 
	 * @todo Not yet implemented
	 */
	@Override
	public void onLoadComplete() 
	{

	}

	/**
	 * Key down handler
	 */
	@Override
	public boolean onKeyDown( final int pKeyCode, final KeyEvent pEvent ) 
	{
		/*if( pKeyCode == KeyEvent.KEYCODE_MENU && pEvent.getAction() == KeyEvent.ACTION_DOWN ) 
		{
			if( this.mMainScene.hasChildScene() ) 
			{
				this.mMenuScene.back();
			} 
			else 
			{
				this.mMainScene.setChildScene( this.mMenuScene, false, true, true );
			}
			
			return true;
		} 
		else 
		{
			return super.onKeyDown(pKeyCode, pEvent);
		}*/
		
		return super.onKeyDown( pKeyCode, pEvent );
	}

	/**
	 * Menu item handler
	 */
	@Override
	public boolean onMenuItemClicked( final MenuScene pMenuScene, final IMenuItem pMenuItem, final float pMenuItemLocalX, final float pMenuItemLocalY ) 
	{
		switch( pMenuItem.getID() ) 
		{
			case MENU_LOAD:
				/* Do stuff */
				
				return true;
			case MENU_START:
				/*this.mMainScene.reset();

				this.mMainScene.clearChildScene();
				this.mMenuScene.reset();*/
				
				return true;
			case MENU_QUIT:
				this.finish();
				
				return true;
			default:
				return false;
		}
	}
	
}