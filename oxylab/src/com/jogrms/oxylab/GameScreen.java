package com.jogrms.oxylab;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.TimeUtils;
import com.jogrms.oxylab.model.Factor;
import com.jogrms.oxylab.model.Lab;
import com.jogrms.oxylab.model.Factor.Kind;
import com.jogrms.oxylab.view.LabView;

public class GameScreen implements Screen {
	private OxyLab game;
	private Stage stage;
	private Table root;
	
	private Label fpsLabel, timeLabel;
	
	private Lab lab;
	private LabView labView;
	private long startTime;
	
	private Skin skin;
	
	private Label addLabel(Table root, String name) {
		Label l = new Label("", skin);
		root.add(new Label(name + ": ", skin)).align(Align.right);
		root.add(l).align(Align.left);
		root.row();
		return l;
	}
	
	public GameScreen(OxyLab game) {
		this.game = game;
		
		lab = new Lab();
		
		
		stage = new Stage(0, 0, false);
		Gdx.input.setInputProcessor(stage);
		
		skin = new Skin(Gdx.files.internal("uiskin.json"));
		
		Texture bgText = new Texture(Gdx.files.internal("background720ext.png"));
		bgText.setFilter(TextureFilter.Linear, TextureFilter.Nearest);
		TextureRegion bg = new TextureRegion(bgText, 1280, 720);
		
		root = new Table(skin);
		stage.addActor(root);
		root.setFillParent(true);
		root.setBackground(new TextureRegionDrawable(bg));
		root.debug();
		
		root.align(Align.left | Align.top);
		
		Table debug = new Table(skin);
		timeLabel = addLabel(debug, "Game time");
		fpsLabel = addLabel(debug, "FPS");
		root.add(debug).align(Align.left);
		root.row();
		
		labView = new LabView(lab, skin);
		root.add(labView).align(Align.left);
		root.row();
		
		TextButton tempUp, tempDown, humUp, humDown;
		
		root.add(tempUp = new TextButton("temp up", skin)).align(Align.left);
		root.row();
		root.add(tempDown = new TextButton("temp down", skin)).align(Align.left);
		root.row();
		root.add(humUp = new TextButton("humididty up", skin)).align(Align.left);
		root.row();
		root.add(humDown = new TextButton("humidity down", skin)).align(Align.left);
		
		tempUp.addListener(new ClickListener() {             
		    @Override
		    public void clicked(InputEvent event, float x, float y) {
		    	Factor f = lab.getFactor(Kind.TEMPERATURE);
		        f.setValue(f.getValue() + 1d);
		    };
		});
		
		tempDown.addListener(new ClickListener() {             
		    @Override
		    public void clicked(InputEvent event, float x, float y) {
		    	Factor f = lab.getFactor(Kind.TEMPERATURE);
		        f.setValue(f.getValue() - 1d);
		    };
		});
		
		humUp.addListener(new ClickListener() {             
		    @Override
		    public void clicked(InputEvent event, float x, float y) {
		    	Factor f = lab.getFactor(Kind.HUMIDITY);
		        f.setValue(f.getValue() + 1d);
		    };
		});
		
		humDown.addListener(new ClickListener() {             
		    @Override
		    public void clicked(InputEvent event, float x, float y) {
		    	Factor f = lab.getFactor(Kind.HUMIDITY);
		        f.setValue(f.getValue() - 1d);
		    };
		});
		
		startTime = TimeUtils.millis();
	}

	private static void showFloat(Label l, double f) {
		l.setText(Double.toString(f));
	}
	@Override
	public void render(float delta) {
		lab.update(delta);
		labView.update();
		
		Color bg = game.getBackgroundColor();
		Gdx.gl.glClearColor(bg.r, bg.g, bg.b, bg.a);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		
		showFloat(timeLabel, (double) (TimeUtils.millis() - startTime) / 1000d);
		if (delta > 0) {
			showFloat(fpsLabel, 1.0f / delta);
		}
		
		stage.act(Gdx.graphics.getDeltaTime());
		stage.draw();
		
		if (Gdx.input.isTouched()) {
			Table.drawDebug(stage);
		}
	}

	@Override
	public void resize(int width, int height) {
		stage.setViewport(640, 360, true);
	}

	@Override
	public void show() {
		// TODO Auto-generated method stub

	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub

	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub

	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub

	}

	@Override
	public void dispose() {
		stage.dispose();
	}

}
