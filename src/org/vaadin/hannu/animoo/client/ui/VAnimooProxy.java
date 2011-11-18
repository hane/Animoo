package org.vaadin.hannu.animoo.client.ui;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.vaadin.terminal.gwt.client.ApplicationConnection;
import com.vaadin.terminal.gwt.client.Paintable;
import com.vaadin.terminal.gwt.client.UIDL;
import com.vaadin.terminal.gwt.client.ValueMap;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.SimplePanel;

public class VAnimooProxy extends SimplePanel implements Paintable {

	protected String paintableId;
	protected ApplicationConnection client;
	public Map<Integer, VFX> effectsMap;
	
	public static final String CLASSNAME = "v-animooproxy";
	
	public static final String FX_QUEUE_TAG = "fq";
	
	public static final String FX_TAG = "f";
	
	public static final String FX_ID_ATTR = "id";
	
	public static final String FX_FPS_ATTR = "fps";
	
	public static final String FX_UNIT_ATTR = "uni";
	
	public static final String FX_DURATION_ATTR = "dur";
	
	public static final String FX_LINK_ATTR = "lnk";
	
	public static final String FX_TRANSITION_ATTR = "tra";
	
	public static final String FX_EASE_ATTR = "eas";
	
	public static final String FX_CSSPROP_ATTR = "css";
	
	public static final String EVENTS_QUEUE_TAG = "eq";
	
	public static final String EVENT_TAG = "e";
	
	public static final String EVENT_FXID_ATTR = "fx";
	
	public static final String EVENT_CALLBACK_ATTR = "cb";

	public static final String EVENT_TARGET_ATTR = "tar";
	
	public static final String EVENT_TYPE_ATTR = "typ";
	
	public static final String EVENT_START = "sta";
	
	public static final String EVENT_PAUSE = "pau";
	
	public static final String EVENT_RESUME = "res";
	
	public static final String EVENT_CANCEL = "can";
	
	public static final String EVENT_REVERSE = "rev";
	
	public static final String FX_ON_COMPONENT_COMPLETE = "fxc";
	
	public VAnimooProxy() {
		super();
		this.effectsMap = new HashMap<Integer, VFX>();
		//setElement(Document.get().createDivElement());
		setStyleName(CLASSNAME);
	}
	
	private VFX buildFxFromUidl (UIDL fxUidl) {
		VFX fx = new VFX();
		for (String attrName : fxUidl.getAttributeNames()) {
			if (attrName.equals(FX_FPS_ATTR)) fx.setFps(fxUidl.getIntAttribute(attrName));
			else if (attrName.equals(FX_UNIT_ATTR)) fx.setUnit(VFX.Unit.fromUidlString(fxUidl.getStringAttribute(attrName)));
			else if (attrName.equals(FX_LINK_ATTR)) fx.setLink(VFX.Link.fromUidlString((fxUidl.getStringAttribute(attrName))));
			else if (attrName.equals(FX_DURATION_ATTR)) fx.setDuration(fxUidl.getIntAttribute(attrName));
			else if (attrName.equals(FX_TRANSITION_ATTR)) fx.setTransition(VFX.Transition.fromUidlString(fxUidl.getStringAttribute(attrName)));
			else if (attrName.equals(FX_EASE_ATTR)) fx.setEase(VFX.Ease.fromUidlString(fxUidl.getStringAttribute(attrName)));
			else if (attrName.equals(FX_CSSPROP_ATTR)) {
				ValueMap cssPropertiesMap = fxUidl.getMapAttribute(attrName);
				String cssProperties = "({";
				for (String cssProperty : cssPropertiesMap.getKeySet()) {
					cssProperties += "'" + cssProperty + "': " + cssPropertiesMap.getString(cssProperty) + ",";
				}
				cssProperties += "})";
				fx.setCssProperties(cssProperties);
			}			
		}		
		return fx;
	}

	public void updateFromUIDL(UIDL uidl, ApplicationConnection client) {
		if (client.updateComponent(this, uidl, true)) return;
		this.client = client;
		paintableId = uidl.getId();
		
		/* If UIDL defines new effects, build and store them: */
		
		UIDL fxsUidl = uidl.getChildByTagName(FX_QUEUE_TAG);
		if (fxsUidl != null) {
			Iterator<Object> it = fxsUidl.getChildIterator();
			while (it.hasNext()) {
				UIDL child = (UIDL) it.next();
				if (child.getTag().equals(FX_TAG)) {
					Integer fxId = Integer.valueOf(child.getIntAttribute(FX_ID_ATTR));
					VFX fx = buildFxFromUidl(child);
					effectsMap.put(fxId, fx);
				}
			}			
		}
		
		/* If UIDL has effect events, act upon them: */
		
		UIDL evtsUidl = uidl.getChildByTagName(EVENTS_QUEUE_TAG);
		if (evtsUidl != null) {
			Iterator<Object> it = evtsUidl.getChildIterator();
			while (it.hasNext()) {
				UIDL child = (UIDL) it.next();
				if (child.getTag().equals(EVENT_TAG)) {
					
					/* Get the FX for this event: */
					final Integer fxId = Integer.valueOf(child.getIntAttribute(EVENT_FXID_ATTR));
					VFX fx = effectsMap.get(fxId);
					
					/* Get the main element of the target paintable: */				
					Paintable p = child.getPaintableAttribute(EVENT_TARGET_ATTR, client);
					final String pid = client.getPid(p);
					Element el = client.getElementByPid(pid);
					
					/* Run the event: */
					String event = child.getStringAttribute(EVENT_TYPE_ATTR);
					final Boolean callback = child.getBooleanAttribute(EVENT_CALLBACK_ATTR);
					
					if (event.equals(EVENT_START)) {
						startFx(
								this,
								fxId,
								pid,
								callback,
								el,
								fx.getFps(),
								fx.getUnit().getUidlString(),
								fx.getLink().getFullName(),
								fx.getDuration(),
								fx.getTransition().getFullName(),
								fx.getEase().getFullString(),
								fx.getCssProperties()
								);
					}
					else if (event.equals(EVENT_PAUSE)) {
						// Not yet implemented
					}
					else if (event.equals(EVENT_RESUME)) {
						// Not yet implemented
					}
					else if (event.equals(EVENT_CANCEL)) {
						// Not yet implemented
					}
					else if (event.equals(EVENT_REVERSE)) {
						// Not yet implemented
					}
				}
			}
		}
	}
	
	void fxComplete (Integer fxId, String pid, Boolean callback) {
		if (callback) {
			Paintable paintable = this.client.getPaintable(pid);
			Object[] args = {fxId, paintable};
			this.client.updateVariable(this.paintableId, FX_ON_COMPONENT_COMPLETE, args, true);			
		}
	}
	
	private native void startFx (
			VAnimooProxy animoo,
			Integer fxId,
			String pid,
			Boolean callback,
			Element el,
			int fxFps,
			String fxUnit,
			String fxLink,
			int fxDuration,
			String fxTransition,
			String fxEase,
			String cssProperties) /*-{
		var fx = new $wnd.Fx.Morph(el, {
			fps: fxFps,
			unit: fxUnit,
			link: fxLink,
			duration: fxDuration,
			transition: $wnd.Fx.Transitions[fxTransition][fxEase]
		});
		fx.start(eval(cssProperties)).chain(function(){
			animoo.@org.vaadin.hannu.animoo.client.ui.VAnimooProxy::fxComplete(Ljava/lang/Integer;Ljava/lang/String;Ljava/lang/Boolean;)(fxId,pid,callback);
		});
	}-*/;
}