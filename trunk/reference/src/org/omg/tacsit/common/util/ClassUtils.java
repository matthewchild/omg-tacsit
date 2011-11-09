/**
 * Organization:  SimVentions, Inc.
 * Creation Date: May 1, 2009
 */

package org.omg.tacsit.common.util;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;

/**
 * A collection of utilities for the core java Class object.
 * @author Matthew Child
 */
public abstract class ClassUtils
{
  /**
   * Gets a particular constructor for a given class, with a particular set of
   * parameters.  If a parameter is null, it may match multiple constructors, which
   * may lead to undesirable results.  For this reason, null arguments should
   * generally be avoided - especially if there is a difference between
   * the configuration object between the two constructors.
   *
   * Example:
   * <code>
   * class Foo
   * {
   *    public Foo(String str);
   *    public Foo(Foo foo);
   * }
   * </code>
   * In this example, invoking ClassUtils.getConstructorFor(Foo.class, null) could
   * mean either the constructor taking a <code>String</code>, or taking a <code>Foo</code>.
   * The constructor invoked depends on the order of declaration, and is thus
   * unreliable.
   *
   * @param <T> The type of object being constructed.
   * @param constructionClass The type of object to construct.
   * @param parameters The parameters that should be used in construction of the object
   * @return A constructor that can be invoked to create a new instance of the class
   * with the given parameters
   * @throws NoSuchMethodException If no constructor for constructionClass exists
   * with the given parameter list.
   */
  public static <T> Constructor<T> getConstructorFor(Class<T> constructionClass, Object... parameters) throws NoSuchMethodException
  {
    Constructor<T> validConstructor = null;
    for(Constructor constructor : constructionClass.getConstructors())
    {
      if(areParametersValid(constructor, parameters))
      {
        validConstructor = constructor;
        break;
      }
    }

    if(validConstructor == null)
    {
      StringBuilder buff = new StringBuilder("No constructor exists with the combination of parameters: {");
      buff.append(StringUtils.concatenateElements(Arrays.asList(parameters)));
      buff.append('}');
      throw new NoSuchMethodException(buff.toString());
    }

    return validConstructor;
  }

  /**
   * Checks to see if the given parameters are valid for a particular constructor.
   * @param constructor The constructor that will be used to create the object.
   * @param parameters The parameters that should be passed to the constructor.
   * @return true if the parameters successfully match the constructor's argument signature, or false otherwise.
   */
  public static boolean areParametersValid(Constructor constructor, Object... parameters)
  {
    boolean valid = false;
    Class[] parameterClasses = constructor.getParameterTypes();

    int parameterCount;
    if(parameters == null)
    {
      parameterCount = 0;
    }
    else
    {
      parameterCount = parameters.length;
    }

    // If the lengths differ, we know immediately that the parameters aren't valid.
    if(parameterClasses.length == parameterCount)
    {
      // If the lengths are the same, we start out with the supposition that it's correct.
      // This handles the case when we have a null parameter array, or zero length array.
      valid = true;
      for(int parameterIndex = 0; parameterIndex < parameterCount; parameterIndex++)
      {
        Object parameter = parameters[parameterIndex];
        // Null parameters can always be accepted as an object value.
        // TODO: What about primitive types?
        if((parameter != null) &&
           (!parameterClasses[parameterIndex].isInstance(parameter)))
        {
          valid = false;
          break;
        }
      }
    }

    return valid;
  }

  /**
   * Constructs a new instance of <code>constructionClass</code>.  The constructor will be chosen by suitability
   * of the parameters.
   * @param <T> The object type that will be constructed.
   * @param constructionClass The Class of object that should be constructed.
   * @param parameters - The parameters that should be used in the construction of the object.
   * @return A new instance of the object.
   * @throws NoSuchMethodException - If no constructor exists for the given parameter combination.
   * @throws InstantiationException - If the class that declares the underlying constructor represents an abstract class.
   * @throws IllegalAccessException - If this Constructor object enforces Java language access control and the underlying constructor is inaccessible.
   * @throws InvocationTargetException - If the underlying constructor throws an exception.
   * @throws IllegalArgumentException - If an unwrapping conversion for primitive arguments fails; or if, after possible unwrapping, a parameter value cannot be converted to the corresponding formal parameter type by a method invocation conversion; if this constructor pertains to an enum type.
   */
  public static <T> T constructObject(Class<? extends T> constructionClass, Object... parameters) throws NoSuchMethodException,
                                                                                                         InstantiationException,
                                                                                                         IllegalAccessException,
                                                                                                         InvocationTargetException
  {
    Constructor constructor = getConstructorFor(constructionClass, parameters);
    return (T)constructor.newInstance(parameters);
  }
}
