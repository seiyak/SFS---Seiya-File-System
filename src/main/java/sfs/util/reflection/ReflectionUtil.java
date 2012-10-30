package sfs.util.reflection;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

public class ReflectionUtil {

	private static final String STRING = "String";
	private static final String WRAPPER_SHORT = "Short";
	private static final String WRAPPER_INT = "Integer";
	private static final String WRAPPER_LONG = "Long";
	private static final String WRAPPER_FLOAT = "Float";
	private static final String WRAPPER_DOUBLE = "Double";
	private static final String WRAPPER_BOOLEAN = "Boolean";
	private static Logger log = Logger.getLogger( ReflectionUtil.class );

	/**
	 * Gets the corresponding primitive class to the specified class name.
	 * 
	 * @param wrapperType
	 *            Wrapper class name.
	 * @return Corresponding primitive type as Class.
	 */
	public static Class getPrimitiveType(String wrapperType) {

		if ( wrapperType.equals( WRAPPER_SHORT ) ) {
			return Short.TYPE;
		}
		else if ( wrapperType.equals( WRAPPER_INT ) ) {
			return Integer.TYPE;
		}
		else if ( wrapperType.equals( WRAPPER_LONG ) ) {
			return Long.TYPE;
		}
		else if ( wrapperType.equals( WRAPPER_FLOAT ) ) {
			return Float.TYPE;
		}
		else if ( wrapperType.equals( WRAPPER_DOUBLE ) ) {
			return Double.TYPE;
		}
		else if ( wrapperType.equals( WRAPPER_BOOLEAN ) ) {
			return Boolean.TYPE;
		}
		else if ( wrapperType.equals( STRING ) ) {
			return String.class;
		}

		throw new IllegalArgumentException( "could not find the specified wrapper type, " + wrapperType );
	}

	/**
	 * Instantiates the specified class.
	 * 
	 * @param cls
	 *            Class to be instantiated.
	 * @return Object instantiated by this method.
	 */
	public static <T> T getInstance(Class<T> cls) {

		try {
			return cls.newInstance();
		}
		catch ( InstantiationException ex ) {
			log.error( ex );
		}
		catch ( IllegalAccessException ex ) {
			log.error( ex );
		}

		return null;
	}

	/**
	 * Gets methods as Map.
	 * 
	 * @param cls
	 *            Method retrieval is taken place.
	 * @return Map holding the methods on the class.
	 */
	public static <T> Map<String, Method> getMethodsAsMap(Class<T> cls) {

		Map<String, Method> map = new HashMap<String, Method>();
		for ( Method method : cls.getDeclaredMethods() ) {
			map.put( method.getName(), method );
		}

		return Collections.unmodifiableMap( map );
	}
	
	/**
	 * Gets methods as Map.
	 * 
	 * @param methodPrefix
	 *            Method prefix that is expected for the methods to have.
	 * @param cls
	 *            Method retrieval is taken place.
	 * @return Map holding the methods on the class.
	 */
	public static <T> Map<String, Method> getMethodsAsMap(MethodPrefix methodPrefix, Class<T> cls) {

		Map<String, Method> map = new HashMap<String, Method>();
		for ( Method method : cls.getDeclaredMethods() ) {
			if ( method.getName().startsWith( methodPrefix.toString() ) ) {
				map.put( method.getName(), method );
			}
		}

		return Collections.unmodifiableMap( map );
	}

	/**
	 * Invokes the specified method on object with argument.
	 * 
	 * @param obj
	 *            Where Method is called
	 * @param methodName
	 *            Method to be invoked.
	 * @param argument
	 *            Used as a part of the method invocation.
	 * @return Object set by the argument.
	 */
	public static <T> T invokeSetter(T obj, String methodName, Object argument) {

		Method method = null;
		for ( Method m : obj.getClass().getDeclaredMethods() ) {
			if ( m.getName().equals( methodName ) ) {
				method = m;
				break;
			}
		}

		if ( method != null ) {
			try {
				method.invoke( obj, argument );
			}
			catch ( Exception ex ) {
				log.error( ex );
			}
		}

		return obj;
	}

	/**
	 * Invokes the specified method on object with argument.
	 * 
	 * @param obj
	 *            Where Method is called
	 * @param method
	 *            Method to be invoked.
	 * @param argument
	 *            Used as a part of the method invocation.
	 * @return Object set by the argument.
	 */
	public static <T> T invokeSetter(T obj, Method method, Object argument) {
		try {
			if ( method != null ) {
				method.invoke( obj, argument );
			}
		}
		catch ( Exception ex ) {
			log.error( ex );
		}

		return obj;
	}

	/**
	 * Gets Class type from the specified array.
	 * 
	 * @param array
	 *            Array where the class type is taken.
	 * @return Class type of the array,
	 */
	public static <T> Class<T> getTypeFromArray(T[] array) {

		return (Class<T>) array.getClass().getComponentType();
	}
}
