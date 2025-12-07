package com.googlecode.scopeplugin;

import com.googlecode.scopeplugin.annotations.In;
import com.googlecode.scopeplugin.annotations.Out;
import org.apache.struts2.action.Action;

public class FakeFieldAction implements Action {
	@In (scope=ScopeType.SESSION)
	@Out (scope=ScopeType.SESSION)
	private String testName;

	public String execute() {
		return SUCCESS;
	}
	
	public String getTestName() {
		return testName;
	}

	public void setTestName(String testName) {
		this.testName = testName;
	}
}
