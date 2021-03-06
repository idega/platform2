package com.idega.repository.data;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import com.idega.util.ArrayUtil;
import com.idega.util.datastructures.HashMatrix;

/**
 * <p>Title: idegaWeb</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: idega Software</p>
 * @author <a href="thomas@idega.is">Thomas Hilbig</a>
 * @version 1.0
 * Created on Jun 14, 2004
 */
public class ImplementorRepository implements Singleton {
	
	private static Instantiator instantiator = new Instantiator() { public Object getInstance() { return new ImplementorRepository();}};
	
	private static final String GENERAL = "general";
	
	static public ImplementorRepository getInstance() {
		return (ImplementorRepository) SingletonRepository.getRepository().getInstance(ImplementorRepository.class, instantiator);
	  }
	
	private HashMatrix interfaceCallerImplementor = null;
	
	private ImplementorRepository(){
		// should not be initialized by constructor
	} 
	
	public void  addImplementorForCaller(Class interfaceClass, Class callerClass, Class implementorClass) {
		if (this.interfaceCallerImplementor == null) {
			this.interfaceCallerImplementor = new HashMatrix();
		}
		String interfaceClassName = interfaceClass.getName();
		String callerClassName = (callerClass == null) ? GENERAL : callerClass.getName();
		String implementorClassName = implementorClass.getName();
		if (this.interfaceCallerImplementor.containsKey(interfaceClassName, callerClassName)) {
			List implementorNames = (List) this.interfaceCallerImplementor.get(interfaceClassName, callerClassName);
			if (implementorNames.contains(implementorClassName)) {
				// already added
				return;
			}
			// add the name to the existing list
			implementorNames.add(implementorClassName);
		}
		else{
			// new entry
			List implementorNames = new ArrayList(1);
			implementorNames.add(implementorClassName);
			this.interfaceCallerImplementor.put(interfaceClassName, callerClassName, implementorNames);
		}
	}
	
	public void addImplementor(Class interfaceClass, Class implementationClass) {
		addImplementorForCaller(interfaceClass, null, implementationClass);
	}
	
	public Object newInstance(Class interfaceClass, Class callerClass) throws ClassNotFoundException, InstantiationException, IllegalAccessException {
		List implementors = getValidImplementorClasses(interfaceClass, callerClass);
		// get the first one
		if (implementors == null) {
			throw new ClassNotFoundException("[ImplementorRepository] ImImplementor for interface " + interfaceClass.getName() + "could not be found");
		}
		Class implementorClass = (Class) implementors.get(0);
		return implementorClass.newInstance();
	}
	
	public Object newInstanceOrNull(Class interfaceClass, Class callerClass) {
		try {
			return newInstance(interfaceClass, callerClass);
		} 
		catch (ClassNotFoundException e) {
			return null;
		}
		catch (InstantiationException e) {
			return null;
		}
		catch (IllegalAccessException e) {
			return null;
		}
	}
	
	public List  newInstances(Class interfaceClass, Class callerClass) {
		List implementors = getValidImplementorClasses(interfaceClass, callerClass);
		List instances = null;
		if (implementors == null) {
			// return empty list
			return new ArrayList(0);
		}
		instances = new ArrayList(implementors.size());
		Iterator iterator = implementors.iterator();
		while (iterator.hasNext()) {
			Class aClass = (Class) iterator.next();
			try {
				Object object = aClass.newInstance();
				instances.add(object);
			}
			catch (InstantiationException e) {
				// ignore
			}
			catch (IllegalAccessException e) {
				// ignore
			}
		}
		return instances;
	}
	
	public Class getAnyClassImpl(Class interfaceClass, Class callerClass) {
		List validClasses = getValidImplementorClasses(interfaceClass, callerClass);
		if (validClasses == null || validClasses.isEmpty()) {
			return null;
		}
		return (Class) validClasses.get(0);
	}
		
	
	
	/**
	 * 
	 * @param interfaceClass
	 * @param callerClass
	 * @return null or a non-empty list
	 */
	private List getImplementorNames(Class interfaceClass, Class callerClass) {
		if (this.interfaceCallerImplementor == null) {
			return null;
		}
		String interfaceClassName = interfaceClass.getName();
		String callerClassName = callerClass.getName();
		if (! this.interfaceCallerImplementor.containsKey(interfaceClassName, callerClassName)) {
			callerClassName = GENERAL;
		}
		List implementors = (List) this.interfaceCallerImplementor.get(interfaceClassName, callerClassName);
		if (implementors == null || implementors.isEmpty()) {
			return null;
		}
		return implementors;
	}
	
	private List getValidImplementorClasses(Class interfaceClass, Class callerClass) {
		List names = getImplementorNames(interfaceClass, callerClass);
		if (names == null) {
			return null;
		}
		List classes = new ArrayList(names.size());
		Iterator iterator = names.iterator();
		while (iterator.hasNext()) {
			String name = (String) iterator.next();
			try {
				Class implementorClass = RefactorClassRegistry.forName(name);
				classes.add(implementorClass);
			}
			catch (ClassNotFoundException e) {
				// do nothing, not very likely that a class is registered but doesn't exist
				e.printStackTrace();
			}
		}
		if (classes.isEmpty()) {
			return null;
		}
		return classes;
	}
	

	/** 
	 * Checks if the caller class is considered to be of the specified type.
	 * Use that method only for interfaces that are used as flags like Clonable.
	 * If that method returns true it doesn't mean that you can cast an instance of
	 * the callerClass to the specified class.
	 * If you are going to perform a cast use the instanceOf check. 
	 * @param interfaceClass
	 * @param callerClass
	 * @return true if the callerClass is considered to be of the specified type. 
	 */
	public boolean isTypeOf(Class interfaceClass, Class callerClass) {
		// first part: 
		// does the same like "instanceOf"
		//
		// get all super classes and check for the interface
		List classes = new ArrayList(); 
		Class superClass = callerClass;
		while (superClass != null) {
			Class[] interfaces = superClass.getInterfaces();
			if (ArrayUtil.contains(interfaces,interfaceClass)) {
				// if the method is left at the place a cast could be done
				return true;
			}
			classes.add(superClass);
			superClass = superClass.getSuperclass();
		}
		// second part:
		// not found? check if there are some registered implementors that are considered as interface implementors
		// Important note: You can't cast the caller to that type! You can only check if that class is of that type.
		List implementorClasses = getValidImplementorClasses(interfaceClass, callerClass);
		if (implementorClasses == null) {
			return false;
		}
		Iterator iterator = implementorClasses.iterator();
		while (iterator.hasNext()) {
			Class implementor = (Class) iterator.next();
			if (classes.contains(implementor)) {
				return true;
			}
		}
		return false;		
	}
	

}
