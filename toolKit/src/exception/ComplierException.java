package exception;

public class ComplierException extends Exception {
	private static final long serialVersionUID = -7797473518106840814L;
	private String errorMsg = "";
	public ComplierException(String msg){
		super(msg);
		this.errorMsg = msg;
	}
	public String getErrorMsg() {
		return errorMsg;
	}
	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}
	
}
