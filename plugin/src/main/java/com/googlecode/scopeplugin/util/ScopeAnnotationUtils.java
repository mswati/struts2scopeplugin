// src/main/java/com/googlecode/scopeplugin/util/ScopeAnnotationUtils.java
package com.googlecode.scopeplugin.util;

import com.googlecode.scopeplugin.annotations.In;
import com.googlecode.scopeplugin.annotations.Out;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.Serial;
import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;

public abstract class ScopeAnnotationUtils implements Serializable {

    @Serial
    private static final long serialVersionUID = -4590322556118858869L;

    private static final Log LOG = LogFactory.getLog(ScopeAnnotationUtils.class);
    private static final Map<Class<?>, CachedMethods> cachedMethods = Collections
            .synchronizedMap(new HashMap<Class<?>, CachedMethods>());

    public static Method findAnnotatedMethod(Class<?> clazz, String methodName) {
        Method[] methods = clazz.getDeclaredMethods();
        for (Method method : methods) {
            if (method.getName().equals(methodName)
                    && method.getParameterTypes().length == 0) {
                return method;
            }
        }
        return null;
    }

    public static List<Field> findAnnotatedFields(Class<?> cls, boolean out) {
        List<Field> annotatedFields = new ArrayList<>();
        Field[] fields = cls.getDeclaredFields();
        for (Field field : fields) {
            if (out) {
                if (field.getAnnotation(Out.class) != null) {
                    annotatedFields.add(field);
                }
            } else {
                if (field.getAnnotation(In.class) != null) {
                    annotatedFields.add(field);
                }
            }
        }
        return annotatedFields;
    }

    public static List<Method> findAnnotatedMethods(Class<?> cls, boolean out) {
        CachedMethods cache = cachedMethods.computeIfAbsent(cls, k -> new CachedMethods());
        Collection<Method> methods = null;
        if (out) {
            methods = cache.getOutMethods();
            if (methods == null) {
                methods = getAnnotatedMethods(cls, Out.class);
                cache.setOutMethods(methods);
            }
        } else {
            methods = cache.getInMethods();
            if (methods == null) {
                methods = getAnnotatedMethods(cls, In.class);
                cache.setInMethods(methods);
            }
        }
        return new LinkedList<>(methods);
    }

    /**
     * Eigene Implementierung, um alle Methoden mit einer bestimmten Annotation
     * zu finden. Erfasst deklarierte Methoden der Klasse und ihrer Superklassen
     * sowie public Methoden aus Interfaces / geerbten Klassen. Duplikate werden
     * entfernt.
     */
    private static Collection<Method> getAnnotatedMethods(Class<?> clazz, Class<? extends Annotation> annotationClass) {
        Set<MethodSignature> methodSignatures = new HashSet<>();
        List<Method> result = new LinkedList<>();

        // überprüfe deklarierte Methoden in Klasse und Superklassen
        Class<?> current = clazz;
        while (current != null && current != Object.class) {
            for (Method method : current.getDeclaredMethods()) {
                if (method.getAnnotation(annotationClass) != null) {
                    MethodSignature methodSignature = new MethodSignature(method);
                    if (!methodSignatures.contains(methodSignature)) {
                        methodSignatures.add(methodSignature);
                        result.add(method);
                    }
                }
            }
            current = current.getSuperclass();
        }

        // überprüfe public geerbte Methoden (Interfaces / öffentliche Methoden)
        assert clazz != null;
        for (Method method : clazz.getMethods()) {
            if (method.getAnnotation(annotationClass) != null) {
                MethodSignature methodSignature = new MethodSignature(method);
                if (!methodSignatures.contains(methodSignature)) {
                    methodSignatures.add(methodSignature);
                    result.add(method);
                }
            }
        }

        return result;
    }

    private static class MethodSignature {
        private final String name;
        private final Class<?>[] params;

        MethodSignature(Method m) {
            this.name = m.getName();
            this.params = m.getParameterTypes();
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof MethodSignature)) return false;
            MethodSignature that = (MethodSignature) o;
            if (!name.equals(that.name)) return false;
            return Arrays.equals(params, that.params);
        }

        @Override
        public int hashCode() {
            int result = name.hashCode();
            result = 31 * result + Arrays.hashCode(params);
            return result;
        }
    }

    private static class CachedMethods {
        private List<Method> inMethods;
        private List<Method> outMethods;

        public List<Method> getInMethods() {
            return inMethods;
        }

        public void setInMethods(Collection<Method> inMethods) {
            this.inMethods = new LinkedList<Method>(inMethods);
        }

        public List<Method> getOutMethods() {
            return outMethods;
        }

        public void setOutMethods(Collection<Method> outMethods) {
            this.outMethods = new LinkedList<Method>(outMethods);
        }
    }
}