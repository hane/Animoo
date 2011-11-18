package org.vaadin.hannu.animoo;

import java.util.ArrayList;
import java.util.List;

import org.vaadin.hannu.animoo.AnimooProxy.AnimooProxyListener;
import org.vaadin.hannu.animoo.client.ui.VFX;

import com.vaadin.Application;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.ui.*;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.themes.Reindeer;

public class AnimooApplication extends Application implements AnimooProxyListener {
	
	private static final long serialVersionUID = 1L;
	private AnimooProxy aproxy;
	private FX colorFx;
	private FX scaleFx;
	
	@Override
	public void init() {
		setTheme("animootheme");
		Window mainWindow = new Window("Animoo Demo");
		setMainWindow(mainWindow);

		aproxy = new AnimooProxy();
		aproxy.addListener(this);
		mainWindow.addComponent(aproxy);

		scaleFx = new FX();
		scaleFx.setTransition(VFX.Transition.SINE);
		scaleFx.setDuration(5000);
		scaleFx.addCssProperty("font-size", 12, 32);
		scaleFx.addCssProperty("line-height", 18, 64);		
		aproxy.register(scaleFx);
		
		colorFx = new FX();
		colorFx.setTransition(VFX.Transition.BOUNCE);
		colorFx.setDuration(1000);
		colorFx.addCssProperty("color", "#f5f5f5", "#0000ff");
		aproxy.register(colorFx);
		
		// Title label:
		Label title = new Label("Animoo Demo");
		title.setStyleName(Reindeer.LABEL_H1);
		mainWindow.getContent().setSizeFull();
		mainWindow.addComponent(title);
		
		// Test labels for some chain action:
		GridLayout gl = new GridLayout(3,3);
		final List<Component> targets = new ArrayList<Component>();
		for (int i=0; i<9; i++) {
			Label l = new Label("Animoo Test Label #" + Integer.toString(i+1));
			l.setStyleName("fadein");
			targets.add(l);
			gl.addComponent(l);
		}
		for (int i=0; i<8; i++) ((Label)targets.get(i)).setData(targets.get(i+1));
		
		gl.setSizeFull();
		mainWindow.addComponent(gl);				
		((VerticalLayout)mainWindow.getContent()).setExpandRatio(gl, 1);

		// Animate font size for all the targets:
		Button b1 = new Button("Font size", new ClickListener() {
			public void buttonClick(ClickEvent event) {
				aproxy.start(scaleFx, targets, false);
			}
		});
		mainWindow.addComponent(b1);
		
		// Animate font size starting from first:
		Button b2 = new Button("Color bounce", new ClickListener() {
			public void buttonClick(ClickEvent event) {
				aproxy.start(colorFx, targets.get(0), true);
			}
		});
		mainWindow.addComponent(b2);

		
	}
	
	public void fxOnComponentComplete(FX fx, Component target) {
		if (target instanceof Label) {
			System.out.println("Done " + target.toString()); //DEBUG
			Label nextTarget = (Label)((Label)target).getData();
			if (nextTarget != null) {
				aproxy.start(colorFx, nextTarget, true);
			}
		}
	}
}
