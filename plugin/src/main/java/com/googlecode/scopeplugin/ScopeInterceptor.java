package com.googlecode.scopeplugin;

import com.googlecode.scopeplugin.annotations.Begin;
import com.googlecode.scopeplugin.annotations.End;
import com.googlecode.scopeplugin.annotations.In;
import com.googlecode.scopeplugin.annotations.Out;
import com.googlecode.scopeplugin.util.PropertyUtils;
import com.googlecode.scopeplugin.util.ScopeAnnotationUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.ActionContext;
import org.apache.struts2.ActionInvocation;
import org.apache.struts2.StrutsException;
import org.apache.struts2.interceptor.AbstractInterceptor;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ScopeInterceptor extends AbstractInterceptor {

    private static final long serialVersionUID = -4590322556118858869L;

    private static final Log LOG = LogFactory.getLog(ScopeInterceptor.class);

    /*
     * (non-Javadoc)
     *
     * @see com.opensymphony.xwork2.interceptor.Interceptor#intercept(org.apache.struts2.ActionInvocation)
     */
    public String intercept(ActionInvocation invocation) throws Exception {
        Object action = invocation.getAction();
        ActionContext ctx = invocation.getInvocationContext();
        String actionMethod = invocation.getProxy().getMethod();
        Class<? extends Annotation> cls = (Class<? extends Annotation>) action.getClass();

        Method method = processBegin(invocation, ctx, actionMethod);
        List<In> removableIns = new ArrayList<In>();
        boolean flashScopeUsed = processIns(ctx, action, cls, removableIns);
        if (flashScopeUsed) {
            ctx.getSession().remove(ScopeType.FLASH.toString());
        }

        String ret = invocation.invoke();

        processRemovableIns(ctx, removableIns);
        processOuts(ctx, action, cls);
        processEnd(ctx, method);

        return ret;
    }

    private Method processBegin(ActionInvocation invocation, ActionContext ctx,
                                String actionMethod) {
        Class<?> clazz = invocation.getAction().getClass();
        Method method = ScopeAnnotationUtils.findAnnotatedMethod(clazz, actionMethod);
        if (method != null) {
            Begin begin = method.getAnnotation(Begin.class);
            if (begin != null) {
                if (!ctx.getSession().containsKey(
                        ScopeType.CONVERSATION.toString())) {
                    ctx.getSession().put(ScopeType.CONVERSATION.toString(),
                            new HashMap<String, Object>());
                }
            }
        }
        return method;
    }

    private boolean processIns(ActionContext ctx, Object action, Class<? extends Annotation> cls,
                               List<In> removableIns)
            throws IllegalAccessException, InvocationTargetException {
        boolean flashScopeUsed = false;

        List<Field> inFields = ScopeAnnotationUtils.findAnnotatedFields(cls, false);
        for (Field f : inFields) {
            In in = f.getAnnotation(In.class);
            String propName = in.value();
            if (propName.length() == 0) {
                propName = f.getName();
            }

            if (in.remove()) {
                removableIns.add(in);
            }

            flashScopeUsed |= in.scope() == ScopeType.FLASH;

            ScopeAdapter adapter = ScopeAdapterFactory.newInstance(ctx, in.scope(), propName);
            Object obj = adapter.findObject();

            if (in.required() && obj == null) {
                throw new StrutsException("Scope object " + propName
                        + " cannot be found in scope " + in.scope());
            }
            PropertyUtils.setProperty(action, f, obj);
        }

        List<Method> inMethods = ScopeAnnotationUtils.findAnnotatedMethods(cls, false);
        for (Method m : inMethods) {
            In in = m.getAnnotation(In.class);
            String propName = in.value();
            if (propName.length() == 0) {
                propName = PropertyUtils.determinePropertyName(m.getName(), false);
            }

            if (in.remove()) {
                removableIns.add(in);
            }

            flashScopeUsed |= in.scope() == ScopeType.FLASH;

            ScopeAdapter adapter = ScopeAdapterFactory.newInstance(ctx, in.scope(), propName);
            Object obj = adapter.findObject();

            if (in.required() && obj == null) {
                throw new StrutsException("Scope object " + propName
                        + " cannot be found in scope " + in.scope());
            }
            PropertyUtils.setProperty(action, m, obj);
        }
        return flashScopeUsed;
    }

    private void processRemovableIns(ActionContext ctx, List<In> removableIns) {
        for (In in : removableIns) {
            ScopeAdapter adapter = ScopeAdapterFactory.newInstance(ctx, in.scope(), in.value());
            adapter.removeObject();
        }
    }

    private void processOuts(ActionContext ctx, Object action, Class<? extends Annotation> cls)
            throws IllegalAccessException, InvocationTargetException {
        List<Field> outFields = ScopeAnnotationUtils.findAnnotatedFields(cls, true);
        for (Field f : outFields) {
            Out out = f.getAnnotation(Out.class);
            String propName = out.value();
            if (propName.length() == 0) {
                propName = f.getName();
            }

            Object obj = PropertyUtils.getProperty(action, f);

            if (obj != null) {
                ScopeAdapter adapter = ScopeAdapterFactory.newInstance(ctx, out.scope(), propName);
                adapter.putObject(obj);
            }
        }

        List<Method> outMethods = ScopeAnnotationUtils.findAnnotatedMethods(cls, true);
        for (Method m : outMethods) {
            Out out = m.getAnnotation(Out.class);
            String propName = out.value();
            if (propName.length() == 0) {
                propName = PropertyUtils.determinePropertyName(m.getName(), true);
            }

            Object obj = PropertyUtils.getProperty(action, m);

            if (obj != null) {
                ScopeAdapter scopeObject = ScopeAdapterFactory.newInstance(ctx, out.scope(), propName);
                scopeObject.putObject(obj);
            }
        }
    }

    private void processEnd(ActionContext ctx, Method method) {
        if (method != null) {
            End end = method.getAnnotation(End.class);
            if (end != null) {
                ctx.getSession().remove(ScopeType.CONVERSATION.toString());
            }
        }
    }
}
