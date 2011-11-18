package org.vaadin.hannu.animoo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.vaadin.hannu.animoo.FX.CssProperty;
import org.vaadin.hannu.animoo.client.ui.VAnimooProxy;

import com.vaadin.terminal.PaintException;
import com.vaadin.terminal.PaintTarget;
import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.Component;

@com.vaadin.ui.ClientWidget(org.vaadin.hannu.animoo.client.ui.VAnimooProxy.class)
public class AnimooProxy extends AbstractComponent {

	private static final long serialVersionUID = 1L;
	
	private Map<Integer, FX> fxMap;
	
	private List<FX> fxQueue;
	
	private List<FXEvent> eventsQueue;
	
	private Integer fxIdCounter = 1;
	
	private List<AnimooProxyListener> listeners;
	
	public AnimooProxy () {
		super();
		fxMap = new HashMap<Integer, FX>();
		fxQueue = new ArrayList<FX>();
		eventsQueue = new ArrayList<FXEvent>();
		listeners = new ArrayList<AnimooProxyListener>();
	}
	
	public void addListener (AnimooProxyListener l) {
		if (l != null && !listeners.contains(l)) {
			listeners.add(l);
		}
	}
	
	public void removeListener (AnimooProxyListener l) {
		if (l != null && listeners.contains(l)) {
			listeners.remove(l);
		}
	}
	
	private void fireFxOnCompleteEvent (FX fx, Component component) {
		for (AnimooProxyListener l : listeners) {
			l.fxOnComponentComplete(fx, component);
		}
	}
	
	public void register(FX fx) {
		fxQueue.add(fx);
		requestRepaint();
	}
	
	public void start (FX fx, Component target, boolean callback) {
		eventsQueue.add(new FXEvent(VAnimooProxy.EVENT_START, fx, target, callback));
		requestRepaint();
	}
	
	public void start (FX fx, List<Component> targets, boolean callback) {
		if (targets != null) {
			for (Component target : targets) {
				eventsQueue.add(new FXEvent(VAnimooProxy.EVENT_START, fx, target, callback));				
			}
		}
		requestRepaint();
	}


	public void pause (FX fx, Component target, boolean callback) {
		eventsQueue.add(new FXEvent(VAnimooProxy.EVENT_PAUSE, fx, target, callback));
		requestRepaint();
	}

	public void resume (FX fx, Component target, boolean callback) {
		eventsQueue.add(new FXEvent(VAnimooProxy.EVENT_RESUME, fx, target, callback));
		requestRepaint();
	}

	public void cancel (FX fx, Component target, boolean callback) {
		eventsQueue.add(new FXEvent(VAnimooProxy.EVENT_CANCEL, fx, target, callback));
		requestRepaint();
	}

	public void reverse (FX fx, Component target, boolean callback) {
		eventsQueue.add(new FXEvent(VAnimooProxy.EVENT_REVERSE, fx, target, callback));
		requestRepaint();
	}

	@Override
	public void paintContent(PaintTarget target) throws PaintException {
		super.paintContent(target);
		
		/* Register queued effects: */
		
		if (!fxQueue.isEmpty()) {
			target.startTag(VAnimooProxy.FX_QUEUE_TAG);
			for (FX fx : fxQueue) {
				int fxId = mapFx(fx);
				target.startTag(VAnimooProxy.FX_TAG);
				target.addAttribute(VAnimooProxy.FX_ID_ATTR, fxId); 
				target.addAttribute(VAnimooProxy.FX_FPS_ATTR, fx.getFps());
				target.addAttribute(VAnimooProxy.FX_UNIT_ATTR, fx.getUnit().getUidlString());
				target.addAttribute(VAnimooProxy.FX_LINK_ATTR, fx.getLink().getUidlString());
				target.addAttribute(VAnimooProxy.FX_DURATION_ATTR, fx.getDuration());
				target.addAttribute(VAnimooProxy.FX_TRANSITION_ATTR, fx.getTransition().getUidlString());
				target.addAttribute(VAnimooProxy.FX_EASE_ATTR, fx.getEase().getUidlString());
				
				Map<String, String> cssProperties = new HashMap<String, String>();
				for (CssProperty cssp : fx.getCssProperties()) {
					String key = cssp.property;
					String value = "";
					if (cssp.fromInt != null && cssp.toInt != null) {
						value = "[" + cssp.fromInt.toString() + "," + cssp.toInt.toString() + "]";
					}
					else {
						value = "['" + cssp.fromStr + "','" + cssp.toStr + "']";
					}
					cssProperties.put(key, value);
				}
				target.addAttribute(VAnimooProxy.FX_CSSPROP_ATTR, cssProperties);				
				target.endTag(VAnimooProxy.FX_TAG);
			}
			target.endTag(VAnimooProxy.FX_QUEUE_TAG);
			fxQueue.clear();			
		}
		
		/* Fire queued effect events: */
		
		if (!eventsQueue.isEmpty()) {
			target.startTag(VAnimooProxy.EVENTS_QUEUE_TAG);
			for (FXEvent ev : eventsQueue) {
				target.startTag(VAnimooProxy.EVENT_TAG);
				target.addAttribute(VAnimooProxy.EVENT_TYPE_ATTR, ev.type);
				target.addAttribute(VAnimooProxy.EVENT_FXID_ATTR, this.getFxId(ev.fx));
				target.addAttribute(VAnimooProxy.EVENT_TARGET_ATTR, ev.target);
				target.addAttribute(VAnimooProxy.EVENT_CALLBACK_ATTR, ev.callback);
				target.endTag(VAnimooProxy.EVENT_TAG);
			}
			target.endTag(VAnimooProxy.EVENTS_QUEUE_TAG);
			eventsQueue.clear();
		}
	}

	@Override
	public void changeVariables(Object source, Map<String, Object> variables) {
		super.changeVariables(source, variables);
		if (variables.containsKey(VAnimooProxy.FX_ON_COMPONENT_COMPLETE)) {
			final Object[] args = (Object[]) variables.get(VAnimooProxy.FX_ON_COMPONENT_COMPLETE);
			Integer fxId = (Integer)args[0];
			Component c = (Component)args[1];
			this.fireFxOnCompleteEvent(this.fxMap.get(fxId), c);
		}
		
	}
	
	private Integer getFxId (FX fx) {
		if (!fxMap.isEmpty() && fxMap.containsValue(fx)) {
			for (Integer id : fxMap.keySet()) {
				if (fxMap.get(id) == fx) {
					return id;
				}
			}
		}
		return -1;
	}
	
	private int mapFx (FX fx) {
		int result = -1;
		if (!fxMap.containsValue(fx)) {
			fxMap.put(fxIdCounter, fx);
			result = fxIdCounter;
			fxIdCounter++;
		}
		return result;
	}
	
	private class FXEvent {
		
		public String type;
		public FX fx;
		public Component target;
		public boolean callback;
		
		@SuppressWarnings("unused")
		public FXEvent () {			
		}
		
		public FXEvent (String type, FX fx, Component target, boolean callback) {
			this.type = type;
			this.fx = fx;
			this.target = target;
			this.callback = callback;
		}
	}
	
	public interface AnimooProxyListener {
		void fxOnComponentComplete (FX fx, Component target);
	}
}
