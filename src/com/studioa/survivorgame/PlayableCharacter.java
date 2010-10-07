package com.studioa.survivorgame;

import org.anddev.andengine.opengl.texture.Texture;
import org.anddev.andengine.opengl.texture.region.TextureRegion;

public class PlayableCharacter
{

	private int mX;
	private int mY;
	
	private TextureRegion mTextureRegion;
	private Texture 	  mTexture;
	
	public PlayableCharacter( int x, int y )
	{
		this.mX = x;
		this.mY = y;
	}
	
	public PlayableCharacter( int x, int y, Texture texture, TextureRegion textureRegion )
	{
		this.mX = x;
		this.mY = y;
		this.mTexture = texture;
		this.mTextureRegion = textureRegion;
	}
	
	public void setTextureRegion( TextureRegion textureRegion )
	{
		this.mTextureRegion = textureRegion;
	}
	
	public void setTexture( Texture texture )
	{
		this.mTexture = texture;
	}
	
	public void setX( int x )
	{
		this.mX = x;
	}
	
	public void setY( int y )
	{
		this.mY = y;
	}
	
	public Texture getTexture()
	{
		return mTexture;
	}
	
	public TextureRegion getTextureRegion()
	{
		return mTextureRegion;
	}
	
	public int getX()
	{
		return mX;
	}
	
	public int getY()
	{
		return mY;
	}
	
}
