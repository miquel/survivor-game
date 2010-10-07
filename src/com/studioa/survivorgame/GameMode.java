package com.studioa.survivorgame;

import org.anddev.andengine.engine.Engine;
import org.anddev.andengine.engine.camera.Camera;
import org.anddev.andengine.engine.options.EngineOptions;
import org.anddev.andengine.engine.options.EngineOptions.ScreenOrientation;
import org.anddev.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.extension.physics.box2d.PhysicsWorld;
import org.anddev.andengine.opengl.texture.Texture;
import org.anddev.andengine.opengl.texture.region.TextureRegion;
import org.anddev.andengine.sensor.accelerometer.IAccelerometerListener;

/**
 * GameMode base class
 * 
 * This class serves as a base structure for gamemode implementations,
 * the class implements all necessary sensor listeners and functions
 * to be implemented in the gamemode implementations.
 * 
 * The idea behind this class and it's implementations is to be run as 
 * an activity in Android.
 * 
 * @author Marlon Etheredge <marlon.etheredge@gmail.com>
 * @version 0.1alpha
 */
public abstract class GameMode extends Base implements IAccelerometerListener
{
	
	// World layers
	protected static final int LAYER_BACKGROUND = 0;
	protected static final int LAYER_SPRITE	  	= LAYER_BACKGROUND + 1;
	protected static final int LAYER_FOREGROUND = LAYER_BACKGROUND + 2;
	
	// Camera dimensions
	protected static final int CAMERA_WIDTH  = 640;
	protected static final int CAMERA_HEIGHT = 480;
	
	private Camera  			mCamera;
	
	protected Scene 			mMainScene;
	protected PlayableCharacter mPlayer;
	protected PhysicsWorld	    mPhysicsWorld;
	
	protected TextureRegion 	mBackgroundTextureRegion;
	protected Texture			mBackgroundTexture;
	
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
				true, 						 // Fullscreen
				ScreenOrientation.LANDSCAPE, // Run in landscape mode
				new RatioResolutionPolicy(	 // Use camera ratio
					CAMERA_WIDTH, 
					CAMERA_HEIGHT
				), 
				this.mCamera
			)
		);
	}
	
}
