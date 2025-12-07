package com.googlecode.scopeplugin;

import com.googlecode.scopeplugin.annotations.In;
import com.googlecode.scopeplugin.annotations.Out;
import org.apache.struts2.action.Action;

public class FakeMethodAction implements Action {
	private String testName;

	public String execute() {
		return SUCCESS;
	}

	@Out (required=false, scope=ScopeType.SESSION)
	public String getTestName() {
		return testName;
	}

	@In (required=false, scope=ScopeType.SESSION)
	public void setTestName(String testName) {
		this.testName = testName;
	}
}
