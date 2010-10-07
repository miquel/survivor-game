package com.studioa.survivorgame;

import org.anddev.andengine.entity.primitive.Rectangle;
import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.entity.shape.Shape;
import org.anddev.andengine.entity.sprite.Sprite;
import org.anddev.andengine.entity.util.FPSLogger; 
import org.anddev.andengine.extension.physics.box2d.PhysicsConnector;
import org.anddev.andengine.extension.physics.box2d.PhysicsFactory;
import org.anddev.andengine.extension.physics.box2d.PhysicsWorld;
import org.anddev.andengine.opengl.texture.Texture;
import org.anddev.andengine.opengl.texture.TextureOptions;
import org.anddev.andengine.opengl.texture.region.TextureRegion;
import org.anddev.andengine.opengl.texture.region.TextureRegionFactory;
import org.anddev.andengine.sensor.accelerometer.AccelerometerData;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;

import android.hardware.SensorManager;

/**
 * LevelAir GameMode implementation
 * 
 * This class is an implementation of a gamemode, in this case
 * an air level gamemode. See GameMode.java for a description of
 * the GameMode class.
 * 
 * This class contains all functions and routines for the air level
 * gamemode.
 *
 * @version 0.1alpha
 * @see     GameMode.java
 */
public class LevelAir extends GameMode 
{
	
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
		mBackgroundTexture 		 = new Texture( 1024, 512, TextureOptions.BILINEAR ); // Like always, texture dimensions should be the next power of 2
		mBackgroundTextureRegion = TextureRegionFactory.createFromAsset( mBackgroundTexture, this, "gfx/background_clouds.png", 0, 0 );
		this.mEngine.getTextureManager().loadTexture( mBackgroundTexture );
		
		Texture playerTexture 			  = new Texture( 64, 128, TextureOptions.BILINEAR );
		TextureRegion playerTextureRegion = TextureRegionFactory.createFromAsset( playerTexture, this, "gfx/cool_dude_sprite.png", 0, 0 );
		this.mEngine.getTextureManager().loadTexture( playerTexture );
		
		this.mPlayer = new PlayableCharacter( 50, 50, playerTexture, playerTextureRegion );
		
		// Enable the accelerometer
		this.enableAccelerometerSensor( this );
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
		
		this.mMainScene = new Scene( 3 );
		this.mMainScene.setBackgroundEnabled( false );
		this.mMainScene.getLayer( LAYER_BACKGROUND ).addEntity(
			new Sprite(
				0, 
				0, 
				this.mBackgroundTextureRegion
			)
		);
		
		// Create the physics world, vector indicates that no gravity should be set on the x-axis, only
		// on the y-axis, normal world behaviour
		this.mPhysicsWorld = new PhysicsWorld(
			new Vector2( 0, SensorManager.GRAVITY_EARTH ), 
			false 
		);
		
		// Create the shapes needed for our bounding box
		final Shape ground = new Rectangle( 0, CAMERA_HEIGHT - 2, CAMERA_WIDTH, 2 );
		final Shape roof   = new Rectangle( 0, 0, CAMERA_WIDTH, 2 );
		final Shape left   = new Rectangle( 0, 0, 2, CAMERA_HEIGHT );
		final Shape right  = new Rectangle( CAMERA_WIDTH - 2, 0, 2, CAMERA_HEIGHT );
		
		// Create fixtures and box bodies for our bounding box
		final FixtureDef wallFixtureDef = PhysicsFactory.createFixtureDef( 0, 0.5f, 0.5f );
		PhysicsFactory.createBoxBody( this.mPhysicsWorld, ground, BodyType.StaticBody, wallFixtureDef );
		PhysicsFactory.createBoxBody( this.mPhysicsWorld, roof,   BodyType.StaticBody, wallFixtureDef );
		PhysicsFactory.createBoxBody( this.mPhysicsWorld, left,   BodyType.StaticBody, wallFixtureDef );
		PhysicsFactory.createBoxBody( this.mPhysicsWorld, right,  BodyType.StaticBody, wallFixtureDef );

		// And bounding box to the world
		this.mMainScene.getBottomLayer().addEntity( ground );
		this.mMainScene.getBottomLayer().addEntity( roof );
		this.mMainScene.getBottomLayer().addEntity( left );
		this.mMainScene.getBottomLayer().addEntity( right );

		final FixtureDef objectFixtureDef = PhysicsFactory.createFixtureDef( 1, 0.5f, 0.5f );
		
		final Sprite face = new Sprite( mPlayer.getX(), mPlayer.getY(), mPlayer.getTextureRegion() );
		final Body 	 body = PhysicsFactory.createBoxBody( this.mPhysicsWorld, face, BodyType.DynamicBody, objectFixtureDef );
		
		// We'll use Box2D from here, so no internal physics please
		face.setUpdatePhysics( false );
		
		this.mMainScene.getLayer( LAYER_SPRITE ).addEntity( face );
		
		this.mPhysicsWorld.registerPhysicsConnector( 
			new PhysicsConnector( 
				face,  // Shape
				body,  // Body
				true,  // Updateposition
				true,  // Updaterotation
				false, // Update linear velocity
				false  // Update angular velocity
			) 
		);
		
		this.mMainScene.registerUpdateHandler( this.mPhysicsWorld );
		
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
	 * IAccelerometerListener method
	 * 
	 * Handler for changes in the Accelerometer sensor.
	 */
	@Override
	public void onAccelerometerChanged( AccelerometerData pAccelerometerData ) 
	{
		this.mPhysicsWorld.setGravity( new Vector2( pAccelerometerData.getY(), pAccelerometerData.getX() ) );
	}

}
