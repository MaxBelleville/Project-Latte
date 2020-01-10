package Latte;

import java.lang.reflect.Method;

public class Caller {
	private Class<?> callerClass;
	private Method callerMethod;
	private Class<?>[] callerParams;
	
	protected Caller() {}
	
	public void getMethod(String classStr, String methodStr, Class<?>...params) {
		try {
			callerClass = Class.forName(classStr);
			callerParams=params;
			callerMethod = callerClass.getMethod(methodStr, params);
		} catch (Exception e) {}
	}
	
	public boolean isEmpty() { 
		return callerMethod==null; 
	}
	
	public void call(Object...params) {
		if(params.length==callerParams.length) {
			try {
				callerMethod.invoke(callerClass.newInstance(),params);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
