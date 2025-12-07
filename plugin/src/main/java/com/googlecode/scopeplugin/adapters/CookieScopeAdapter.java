package com.googlecode.scopeplugin.adapters;


import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.struts2.ActionContext;
import org.apache.struts2.ServletActionContext;

import com.googlecode.scopeplugin.ScopeType;

public class CookieScopeAdapter extends AbstractScopeAdapter {
	private HttpServletRequest request;
	private HttpServletResponse response;

	public CookieScopeAdapter(ActionContext context, String name) {
		super(context, ScopeType.COOKIE, name);
		request = ServletActionContext.getRequest();
		response = ServletActionContext.getResponse();
	}

	public Object findObject() {
		Cookie[] cookies = request.getCookies();
		for (Cookie cookie : cookies) {
			if (getName().equals(cookie.getName())) {
				return cookie.getValue();
			}
		}
		return null;
	}

	public void putObject(Object obj) {
		response.addCookie(new Cookie(getName(), obj.toString()));
	}

	public Object removeObject() {
		Object obj = findObject();
		if (obj != null) {
			Cookie cookie = new Cookie(getName(), "");
			cookie.setMaxAge(0);
			response.addCookie(cookie);
		}
		return obj;
	}
}
