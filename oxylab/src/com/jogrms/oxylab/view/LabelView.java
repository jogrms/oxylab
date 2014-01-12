package com.jogrms.oxylab.view;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.jogrms.oxylab.model.ValueModel;

public class LabelView extends Label implements View {
	private ValueModel model;
	private String name;
	
	public LabelView(String name, ValueModel model, Skin skin) {
		super(name, skin);
		this.name = name;
		this.model = model;
	}
	
	@Override
	public void update() {
		setText(name + ": " + Double.toString(model.getValue()));
	}
}
