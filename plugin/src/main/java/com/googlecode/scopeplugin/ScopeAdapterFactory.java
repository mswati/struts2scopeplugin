package com.googlecode.scopeplugin;

import com.googlecode.scopeplugin.adapters.ApplicationScopeAdapter;
import com.googlecode.scopeplugin.adapters.ConversationScopeAdapter;
import com.googlecode.scopeplugin.adapters.CookieScopeAdapter;
import com.googlecode.scopeplugin.adapters.FlashScopeAdapter;
import com.googlecode.scopeplugin.adapters.RequestScopeAdapter;
import com.googlecode.scopeplugin.adapters.SessionScopeAdapter;
import com.googlecode.scopeplugin.adapters.UnspecifiedScopeAdapter;
import org.apache.struts2.ActionContext;

public abstract class ScopeAdapterFactory {
	public static ScopeAdapter newInstance(ActionContext context, ScopeType type, String name) {
		switch (type) {
		case REQUEST:
			return new RequestScopeAdapter(context, name);
		case FLASH:
			return new FlashScopeAdapter(context, name);
		case CONVERSATION:
			return new ConversationScopeAdapter(context, name);
		case SESSION:
			return new SessionScopeAdapter(context, name);
		case COOKIE:
			return new CookieScopeAdapter(context, name);
		case APPLICATION:
			return new ApplicationScopeAdapter(context, name);
		case UNSPECIFIED:
			return new UnspecifiedScopeAdapter(context, name);
		default:
			return null;
		}
	}
}
