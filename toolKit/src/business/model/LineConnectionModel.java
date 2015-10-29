package business.model;
public class LineConnectionModel extends AbstractConnectionModel {
	private String display = "";//显示名称
	private String type = "0";//比较类型 0-空条件  1-判断条件 2-判断类 3-java表达式
	//条件判断
	private String paramName = "";//变量名
	private String opt = "0";//比较类型
	private String paramName2 = "";//被比较变量名
	private String valueType = "0";//值类型
	//类判断
	private String className = "";//判断类路径
	//java表达式
	private String javaEl = "";
	
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getParamName() {
		return paramName;
	}

	public void setParamName(String paramName) {
		this.paramName = paramName;
	}

	public String getOpt() {
		return opt;
	}

	public void setOpt(String opt) {
		this.opt = opt;
	}

	public String getParamName2() {
		return paramName2;
	}

	public void setParamName2(String paramName2) {
		this.paramName2 = paramName2;
	}

	public String getValueType() {
		if("".equals(valueType)){
			valueType = "0";
		}
		return valueType;
	}

	public void setValueType(String valueType) {
		this.valueType = valueType;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String getJavaEl() {
		return javaEl;
	}

	public void setJavaEl(String javaEl) {
		this.javaEl = javaEl;
	}

	public String getDisplay() {
		return display;
	}

	public void setDisplay(String display) {
		this.display = display;
		this.firePropertyChange("refreshAttr", null , null);
	}
	
}
