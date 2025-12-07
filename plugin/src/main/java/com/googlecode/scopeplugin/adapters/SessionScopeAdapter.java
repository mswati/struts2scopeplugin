package com.googlecode.scopeplugin.adapters;

import java.util.Map;

import com.googlecode.scopeplugin.ScopeType;
import org.apache.struts2.ActionContext;

public class SessionScopeAdapter extends AbstractScopeAdapter {
	final Map<String, Object> session;
	
	public SessionScopeAdapter(ActionContext context, String name) {
		super(context, ScopeType.SESSION, name);
		session = context.getSession();
	}

	public Object findObject() {
		return session.get(getName());
	}
	
	public void putObject(Object obj) {
		session.put(getName(), obj);
	}

	public Object removeObject() {
		return session.remove(getName());
	}
}
