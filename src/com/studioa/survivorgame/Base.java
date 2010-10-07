package com.studioa.survivorgame;

import org.anddev.andengine.ui.activity.BaseGameActivity;

import android.view.Menu;
import android.view.MenuItem;

/**
 * Core base activity class
 * 
 * All classes that should run as separate activities should
 * implement this class, not the default BaseGameActivity or
 * Activity class. This class offers all general functionality
 * for implementations and should be implemented for the purpose
 * of consistency.
 * 
 * @version 0.1alpha
 * @todo	Add generic methods
 */
public abstract class Base extends BaseGameActivity 
{

	@Override
	public boolean onCreateOptionsMenu( final Menu pMenu ) 
	{
		return super.onCreateOptionsMenu( pMenu );
	}

	@Override
	public boolean onPrepareOptionsMenu( final Menu pMenu ) 
	{
		return super.onPrepareOptionsMenu( pMenu );
	}

	@Override
	public boolean onMenuItemSelected( final int pFeatureId, final MenuItem pItem ) 
	{
		return super.onMenuItemSelected( pFeatureId, pItem );
	}

}
