package com.jogrms.oxylab;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class OxyLab extends Game {
	private Color foregroundColor;
	private Color backgroundColor;
	
	public Color getForegroundColor() {
		return foregroundColor;
	}

	public Color getBackgroundColor() {
		return backgroundColor;
	}

	@Override
	public void create() {	

		backgroundColor = new Color(0x303030FF);
		foregroundColor = new Color(0xDB6E19FF);
		
		this.setScreen(new GameScreen(this));
	}
	
	@Override
	public void dispose() {
	}

	@Override
	public void render() {
		super.render();
	}
	
	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}
}