package com.jogrms.oxylab.view;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.jogrms.oxylab.model.Lab;
import com.jogrms.oxylab.model.Factor.Kind;

public class LabView extends Table implements View {
	private Lab lab;
	private Skin skin;

	List<LabelView> labels = new ArrayList<LabelView>();
	
	public LabView(Lab lab, Skin skin) {
		this.lab = lab;
		this.skin = skin;
		
		labels.add(new LabelView("temperature", lab.getFactor(Kind.TEMPERATURE), skin));
		labels.add(new LabelView("humidity", lab.getFactor(Kind.HUMIDITY), skin));
		labels.add(new LabelView("moss population", lab.getPopulations().get(0), skin));		
		
		for (LabelView l : labels) {
			add(l).align(Align.left);
			row();
		}
		
	}
	
	@Override
	public void update() {
		for (LabelView l : labels) {
			l.update();
		}
	}
	
}
