package com.googlecode.scopeplugin.adapters;

import java.util.HashMap;
import java.util.Map;

import com.googlecode.scopeplugin.ScopeType;
import org.apache.struts2.ActionContext;

public class FlashScopeAdapter extends AbstractScopeAdapter {
	final Map<String, Object> session;

	public FlashScopeAdapter(ActionContext context, String name) {
		super(context, ScopeType.FLASH, name);
		session = context.getSession();
	}

	public Object findObject() {
		Object obj = null;
		if (session != null) {
			Map<String, Object> flash = (Map<String, Object>) session
					.get(ScopeType.FLASH.toString());
			if (flash != null) {
				obj = flash.get(getName());
			}
		}
		return obj;
	}

	public void putObject(Object obj) {
		Map<String, Object> flash = (Map<String, Object>) session
				.get(ScopeType.FLASH.toString());
		if (flash == null) {
			flash = new HashMap<String, Object>();
		}
		flash.put(getName(), obj);
		session.put(ScopeType.FLASH.toString(), flash);
	}

	public Object removeObject() {
		Object obj = null;
		if (session != null) {
			Map<String, Object> flash = (Map<String, Object>) session
					.get(ScopeType.FLASH.toString());
			if (flash != null) {
				obj = flash.remove(getName());
			}
		}
		return obj;
	}
}
