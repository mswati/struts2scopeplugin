package com.googlecode.scopeplugin;

import com.googlecode.scopeplugin.annotations.Begin;
import com.googlecode.scopeplugin.annotations.End;
import com.googlecode.scopeplugin.annotations.In;
import com.googlecode.scopeplugin.annotations.Out;
import org.apache.struts2.action.Action;

import java.util.List;

public class FakeConversationAction implements Action {
    @In(scope = ScopeType.CONVERSATION)
    @Out(scope = ScopeType.CONVERSATION)
    private List list;

    public String execute() {
        return SUCCESS;
    }

    @Begin
    public String firstPage() {
        return SUCCESS;
    }

    public String secondPage() {
        return SUCCESS;
    }

    @End
    public String thirdPage() {
        return SUCCESS;
    }

    public List getList() {
        return list;
    }

    public void setList(List list) {
        this.list = list;
    }
}
