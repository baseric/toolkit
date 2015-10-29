package util;
public class StringBufferUtil {
	public int offset = 0;
	private StringBuffer str = new StringBuffer();
	public StringBufferUtil(){}
	public StringBufferUtil(StringBuffer str){
		this.str = str;
	}
	public StringBufferUtil(String str){
		this.str = new StringBuffer(str);
	}
	public void append(String str){
		this.str.append(getLvl()+str);
	}
	public void appendChildrenHtml(String str){
		if(str!=null&&str.length()>0){
			this.str.append(str);
		}
	}
	public String getLvl(){
	  	StringBuffer buffer = new StringBuffer("");
	  	for(int i = 0;i<=offset;i++){
	  		  buffer.append("\t");
	  	}
	  	return buffer.toString();
	}
	public String toString(){
		return str.toString();
	}
	public int length(){
		return str.length();
	}
}
