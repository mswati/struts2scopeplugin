package com.googlecode.scopeplugin.adapters;

import com.googlecode.scopeplugin.ScopeType;
import org.apache.struts2.ActionContext;

public class UnspecifiedScopeAdapter extends AbstractScopeAdapter {
	private RequestScopeAdapter request;
	private FlashScopeAdapter flash;
	private ConversationScopeAdapter conversation;
	private SessionScopeAdapter session;
	private CookieScopeAdapter cookie;
	private ApplicationScopeAdapter application;
	
	public UnspecifiedScopeAdapter(ActionContext context, String name) {
		super(context, ScopeType.REQUEST, name);
		this.request = new RequestScopeAdapter(context, name);
		this.flash = new FlashScopeAdapter(context, name);
		this.conversation = new ConversationScopeAdapter(context, name);
		this.session = new SessionScopeAdapter(context, name);
		this.cookie = new CookieScopeAdapter(context, name);
		this.application = new ApplicationScopeAdapter(context, name);
	}

	public Object findObject() {
		Object obj = null;
		if (obj == null) {
			obj = request.findObject();
		}
		if (obj == null) {
			obj = flash.findObject();
		}
		if (obj == null) {
			obj = conversation.findObject();
		}
		if (obj == null) {
			obj = session.findObject();
		}
		if (obj == null) {
			obj = cookie.findObject();
		}
		if (obj == null) {
			obj = application.findObject();
		}
		return obj;
	}

	public Object removeObject() {
		Object obj = null;
		if (obj == null) {
			obj = request.removeObject();
		}
		if (obj == null) {
			obj = flash.removeObject();
		}
		if (obj == null) {
			obj = conversation.removeObject();
		}
		if (obj == null) {
			obj = session.removeObject();
		}
		if (obj == null) {
			obj = cookie.removeObject();
		}
		if (obj == null) {
			obj = application.removeObject();
		}
		return obj;
	}
}
