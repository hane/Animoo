package org.vaadin.hannu.animoo.client.ui;

public class VFX {

	public enum Unit {
		PX("px"),
		EM("em"),
		PERCENT("%");
		
		private String uidlStr;
		
		Unit(String str) {
			this.uidlStr = str;
		}
		
		public String getUidlString() {
			return this.uidlStr;
		}
		
		public static Unit fromUidlString(String str) {
			for (Unit u : Unit.values()) {
				if (u.uidlStr.equalsIgnoreCase(str)) {
					return u;
				}
			}
			return null;
		}
	}
	
	public enum Link {
		IGNORE("ig", "Ignore"),
		CANCEL("ca", "Cancel"),
		CHAIN("ch", "Chain");

		private String uidlStr;
		private String fullStr;
		
		Link(String uidlStr, String fullStr) {
			this.uidlStr = uidlStr;
			this.fullStr = fullStr;
		}		
		public String getUidlString() {
			return this.uidlStr;
		}
		public String getFullName() {
			return this.fullStr;
		}		
		public static Link fromUidlString(String str) {
			for (Link l : Link.values()) {
				if (l.uidlStr.equalsIgnoreCase(str)) {
					return l;
				}
			}
			return null;
		}		
	}
	
	public enum Duration {
		SHORT(250),
		NORMAL(500),
		LONG(1000);
		
		private int ms;
		
		Duration(int duration) {
			this.ms = duration;
		}
		public int inMilliseconds() {
			return this.ms;
		}		
	}
	
	public enum Transition {
		LINEAR("li", "Linear"),
		QUAD("q1", "Quad"),
		CUBIC("cu", "Cubic"),
		QUART("q2", "Quart"),
		QUINT("q3", "Quint"),
		EXPO("ex", "Expo"),
		CIRC("ci", "Circ"),
		SINE("si", "Sine"),
		BACK("ba", "Back"),
		BOUNCE("bo", "Bounce"),
		ELASTIC("el", "Elastic");
		
		private String uidlStr;
		private String fullStr;
		
		Transition(String uidlStr, String fullStr) {
			this.uidlStr = uidlStr;
			this.fullStr = fullStr;
		}		
		public String getUidlString() {
			return this.uidlStr;
		}
		public String getFullName() {
			return this.fullStr;
		}		
		public static Transition fromUidlString(String str) {
			for (Transition t : Transition.values()) {
				if (t.uidlStr.equalsIgnoreCase(str)) {
					return t;
				}
			}
			return null;
		}		
	}
	
	public enum Ease {
		IN("i", "easeIn"),
		OUT("o", "easeOut"),
		IN_OUT("io", "easeInOut");
		
		private String uidlStr;
		private String fullStr;
		
		Ease(String uidlStr, String fullStr) {
			this.uidlStr = uidlStr;
			this.fullStr = fullStr;
		}		
		public String getUidlString() {
			return this.uidlStr;
		}
		public String getFullString() {
			return this.fullStr;
		}		
		public static Ease fromUidlString(String str) {
			for (Ease e : Ease.values()) {
				if (e.uidlStr.equalsIgnoreCase(str)) {
					return e;
				}
			}
			return null;
		}		
	}
	
	private int fps = 50;
	
	private Unit unit = Unit.PX;
	
	private Link link = Link.CANCEL;
	
	private int duration = 500;
	
	private Transition transition = Transition.SINE;
	
	private Ease ease = Ease.IN_OUT;
	
	private String cssProperties;
	
	public VFX () {
		
	}

	public VFX (int fps, VFX.Unit unit, VFX.Link link, int duration, VFX.Transition transition, VFX.Ease ease) {
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
		this.duration = duration.inMilliseconds();
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
	
	public String getCssProperties() {
		return this.cssProperties;
	}
	
	public void setCssProperties (String cssProperties) {
		this.cssProperties = cssProperties;
	}
}
