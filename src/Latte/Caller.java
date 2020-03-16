package Latte;

import java.lang.reflect.Method;

public class Caller {
	private Class<?> callerClass;
	private Method callerMethod;
	private boolean onError=false;
	private String methodStr="";
	private Class<?>[] callerParams;
	
	protected Caller() {}
	
	public void getMethod(String classStr, String methodStr, Class<?>...params) {
		this.methodStr=methodStr;
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
		if(params.length==callerParams.length&&!onError) {
				try {
					callerMethod.invoke(null,params);
				} catch (Exception e) {
					System.out.println("Error function: "+methodStr +" in class: "+ callerClass.getName()+ " not found. Perhaps your missing parameters?");
					e.printStackTrace();
					onError=true;
				}
		}
	}
}