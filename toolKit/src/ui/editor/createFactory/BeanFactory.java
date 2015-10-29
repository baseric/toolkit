package ui.editor.createFactory;

import java.lang.reflect.Constructor;

import org.eclipse.gef.requests.CreationFactory;

public class BeanFactory implements CreationFactory {

	  private Class type;
	  private String modelName = "";
	  public BeanFactory(Class aClass,String modelName)
	  {
	    this.type = aClass;
	    this.modelName = modelName;
	  }

	  @SuppressWarnings("unchecked")
	  public Object getNewObject() {
	    try {
	      Constructor constructor = type.getConstructor(String.class);
	      return constructor.newInstance(modelName);
	    } catch (Exception localException) {
	      return null;
	    }
	  }

	  public Object getObjectType() {
	    return this.type;
	  }

}
