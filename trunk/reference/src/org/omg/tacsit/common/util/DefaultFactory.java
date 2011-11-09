/**
 * Organization:  SimVentions, Inc.
 * Creation Date: Jun 16, 2011
 */
package org.omg.tacsit.common.util;

import java.lang.reflect.Constructor;
import java.util.Arrays;

/**
 * A simple factory that creates new data objects via reflection.
 * @param <T> The type of object the Factory creates.
 * @author Matthew Child
 */
public class DefaultFactory<T> implements Factory<T>
{

  private Constructor<? extends T> constructor;
  private Object[] constructorArguments;

  /**
   * Creates a new factory.  A constructor will have to be set before the createObject() method is called.
   */
  public DefaultFactory()
  {
  }

  /**
   * Creates a factory that will construct new instances of a class with a specified set of parameters.
   * @param constructionClass The class to create new instances of.
   * @param constructorArguments The arguments that should be passed to the constructor for the class.  An empty 
   * argument list signifies that the empty constructor should be used.
   * @throws NoSuchMethodException Thrown if no constructor exists that takes types which match the constructor arguments (in
   * the order they are specified).
   */
  public DefaultFactory(Class<? extends T> constructionClass, Object... constructorArguments) throws NoSuchMethodException
  {
    setConstructionMethod(constructionClass, constructorArguments);
  }  
  
  /**
   * Creates a factory that will construct new objects using the associated constructor.  The constructor will
   * be invoked using the associated argument list.
   * @param constructor The constructor that should be used to create new objects.
   * @param constructorArguments The arguments that should be passed to the constructor.  An empty argument list signifies
   * that the empty constructor should be used.
   * @throws IllegalArgumentException Thrown if the associated arguments are not a valid match to the 
   * <code>constrctor</code>.
   */
  public DefaultFactory(Constructor<? extends T> constructor, Object... constructorArguments) throws IllegalArgumentException
  {
    setConstructionMethod(constructor, constructorArguments);
  }
  
  private void setConstructorArguments(Object[] arguments)
  {
    if (arguments != null)
    {
      this.constructorArguments = arguments;
    }
    else
    {
      this.constructorArguments = new Object[0];
    }
  }

  /**
   * Sets the construction method used for the factory.  The factory will create new instances of the 
   * <code>constructionClass</code>, passing the <code>constructorArguments</code> to the relevant constructor.
   * @param constructionClass The class to create new instances of.
   * @param constructorArguments The arguments that should be passed to the constructor for the class.  An empty 
   * argument list signifies that the empty constructor should be used.
   * @throws NoSuchMethodException If no constructor exists that takes types which match the constructor arguments (in
   * the order they are specified).
   */
  public final void setConstructionMethod(Class<? extends T> constructionClass, Object... constructorArguments) throws
      NoSuchMethodException
  {
    this.constructor = ClassUtils.getConstructorFor(constructionClass, constructorArguments);
    setConstructorArguments(constructorArguments);
  }

  /**
   * Sets the construction method used for the factory.  The factory will invoke the constructor to create new
   * objects, and use the associated parameters
   * @param constructor The constructor that should be used to create new objects.
   * @param constructorArguments The arguments that should be passed to the constructor.  An empty argument list signifies
   * that the empty constructor should be used.
   * @throws IllegalArgumentException Thrown if no constructor exists that takes types which match the constructor arguments (in
   * the order they are specified).
   */
  public final void setConstructionMethod(Constructor<? extends T> constructor, Object... constructorArguments) throws
      IllegalArgumentException
  {
    if (!ClassUtils.areParametersValid(constructor, constructorArguments))
    {
      throw new IllegalArgumentException("Constructor arguments aren't valid for the given constructor.");
    }
    this.constructor = constructor;
    setConstructorArguments(constructorArguments);
  }
  
  /**
   * Gets the class of object that will be created by this factory.
   * @return The class of new objects.
   */
  public Class<? extends T> getNewObjectClass()
  {
    Class<? extends T> newObjectClass = null;
    if(this.constructor != null)
    {
      newObjectClass = this.constructor.getDeclaringClass();
    }
    return newObjectClass;
  }
  
  /**
   * Gets the constructor used for creating new objects.
   * @return The constructor that instantiates new objects.
   */
  public Constructor<? extends T> getConstructor()
  {
    return this.constructor;
  }
  
  /**
   * Gets the arguments that will be used for the constructor.  This will never be null.
   * @return An array of objects that will be used to construct new objects.
   */
  public Object[] getConstructorArguments()
  {
    return Arrays.copyOf(constructorArguments, constructorArguments.length);
  }

  @Override
  public T createObject()
  {
    if (constructor == null)
    {
      throw new UnsupportedOperationException("No objects may be created if an associated "
                                              + "constructor or construction class has been set.");
    }
    try
    {
      T newObject = constructor.newInstance(constructorArguments);
      return newObject;
    }
    catch (Exception ex)
    {
      throw new UnsupportedOperationException("Couldn't create a new object.", ex);
    }
  }

  @Override
  public String toString()
  {
    StringBuilder builder = new StringBuilder("DefaultFactory{");
    Class newObjectClass = getNewObjectClass();
    if(newObjectClass != null)
    {
      builder.append("newObjectClass=");
      builder.append(newObjectClass.getName());
    }
    for (int argIndex = 0; argIndex < constructorArguments.length; argIndex++)
    {
      Object argument = constructorArguments[argIndex];      
      builder.append(',');
      builder.append("arg[");
      builder.append(argIndex);
      builder.append("]=");
      builder.append(argument);
    }
    builder.append('}');
    return builder.toString();
  }
}
