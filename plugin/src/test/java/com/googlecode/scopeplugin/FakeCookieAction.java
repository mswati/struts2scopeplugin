package com.googlecode.scopeplugin;

import com.googlecode.scopeplugin.annotations.In;
import com.googlecode.scopeplugin.annotations.Out;
import org.apache.struts2.action.Action;

public class FakeCookieAction implements Action {
	private String cookieIn;
	private String cookieOut;
	
	public String execute() {
		return SUCCESS;
	}

	public String getCookieIn() {
		return cookieIn;
	}

	@In(remove=true, required=false, scope=ScopeType.COOKIE)
	public void setCookieIn(String testName) {
		this.cookieIn = testName;
	}

	@Out(scope=ScopeType.COOKIE)	
	public String getCookieOut() {
		return cookieOut;
	}

	public void setCookieOut(String cookieOut) {
		this.cookieOut = cookieOut;
	}
}
