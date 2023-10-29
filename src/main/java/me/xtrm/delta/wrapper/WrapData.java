package me.xtrm.delta.wrapper;

import java.lang.reflect.Method;

public class WrapData {

	private String id;
	private Class<?> parent;
	private Method method;
	
	public WrapData(String id, Class<?> parent, Method method) {
		this.id = id;
		this.parent = parent;
		this.method = method;
	}

	public String getId() {
		return id;
	}

	public Class<?> getParent() {
		return parent;
	}

	public Method getMethod() {
		return method;
	}

}
