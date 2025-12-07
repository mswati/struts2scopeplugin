package com.googlecode.scopeplugin.adapters;

import org.apache.struts2.ActionContext;
import org.apache.struts2.StrutsException;

import com.googlecode.scopeplugin.ScopeAdapter;
import com.googlecode.scopeplugin.ScopeType;

public abstract class AbstractScopeAdapter implements ScopeAdapter {
	private final ActionContext context;
	private final ScopeType type;
	private final String name;
	
	/* (non-Javadoc)
	 * @see com.googlecode.scopeplugin.adapter.ScopeAdapter#putObject(java.lang.Object)
	 */
	public void putObject(Object obj) {
		throw new StrutsException("Scope object " + getName()
				+ " cannot be put in scope " + getType());
	}
	/* (non-Javadoc)
	 * @see com.googlecode.scopeplugin.adapter.ScopeAdapter#findObject()
	 */
	public abstract Object findObject();
	
	/* (non-Javadoc)
	 * @see com.googlecode.scopeplugin.adapter.ScopeAdapter#removeObject()
	 */
	public abstract Object removeObject();
	
	public AbstractScopeAdapter(ActionContext context, ScopeType type, String name) {
		super();
		this.context = context;
		this.type = type;
		this.name = name;
	}
	
	public ActionContext getContext() {
		return context;
	}
	public ScopeType getType() {
		return type;
	}
	public String getName() {
		return name;
	}
}
