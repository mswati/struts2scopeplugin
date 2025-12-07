package com.googlecode.scopeplugin.adapters;

import java.util.Map;

import com.googlecode.scopeplugin.ScopeType;
import org.apache.struts2.ActionContext;

public class ApplicationScopeAdapter extends AbstractScopeAdapter {
	final Map<String, Object> application;
	
	public ApplicationScopeAdapter(ActionContext context, String name) {
		super(context,ScopeType.APPLICATION, name);
		application = context.getApplication();
	}

	public Object findObject() {
		return application.get(getName());
	}
	
	public void putObject(Object obj) {
		application.put(getName(), obj);
	}

	public Object removeObject() {
		return application.remove(getName());
	}
}
