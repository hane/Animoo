package org.vaadin.hannu.animoo;

import java.util.ArrayList;

import org.vaadin.hannu.animoo.client.ui.VFX;

public class FX {
	
	private int fps = 50;
	
	private VFX.Unit unit = VFX.Unit.PX;
	
	private VFX.Link link = VFX.Link.CANCEL;
	
	private int duration = 500;
	
	private VFX.Transition transition = VFX.Transition.SINE;
	
	private VFX.Ease ease = VFX.Ease.IN_OUT;
	
	private final ArrayList<CssProperty> cssProperties = new ArrayList<FX.CssProperty>();
	
	public FX () {
		
	}

	public FX (int fps, VFX.Unit unit, VFX.Link link, int duration, VFX.Transition transition, VFX.Ease ease) {
		this.fps = fps;
		this.unit = unit;
		this.link = link;
		this.duration = duration;
		this.transition = transition;
		this.ease = ease;		
	}

	public int getFps() {
		return fps;
	}

	public void setFps(int fps) {
		this.fps = fps;
	}

	public VFX.Unit getUnit() {
		return unit;
	}

	public void setUnit(VFX.Unit unit) {
		this.unit = unit;
	}

	public VFX.Link getLink() {
		return link;
	}

	public void setLink(VFX.Link link) {
		this.link = link;
	}

	public int getDuration() {
		return duration;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}
	
	public void setDuration (VFX.Duration duration) {
		if (duration == VFX.Duration.LONG) this.duration = 1000;
		if (duration == VFX.Duration.SHORT) this.duration = 250;
		else this.duration = 500;
	}

	public VFX.Transition getTransition() {
		return transition;
	}

	public void setTransition(VFX.Transition transition) {
		this.transition = transition;
	}

	public VFX.Ease getEase() {
		return ease;
	}

	public void setEase(VFX.Ease ease) {
		this.ease = ease;
	}
	
	public void addCssProperty(String property, String from, String to) {
		CssProperty cssp = new CssProperty(property, from, to);
		this.cssProperties.add(cssp);
	}
	
	public void addCssProperty(String property, int from, int to) {
		this.cssProperties.add(new CssProperty(property, from, to));
	}

	public void addCssProperty(CssProperty cssProperty) {
		this.cssProperties.add(cssProperty);
	}
	
	public ArrayList<CssProperty> getCssProperties () {
		return this.cssProperties;
	}
	
	public class CssProperty {
		public String property;
		public Integer fromInt;
		public Integer toInt;
		public String fromStr;
		public String toStr;
		
		public CssProperty(String property, String from, String to) {
			this.property = property;
			this.fromStr = from;
			this.toStr = to;
		}
		public CssProperty(String property, int from, int to) {
			this.property = property;
			this.fromInt = from;
			this.toInt = to;
		}
	}
	
}