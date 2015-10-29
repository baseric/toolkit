package business.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import business.AbstractModel;

public class RemarkModel extends AbstractModel {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1212857075720287273L;

	public RemarkModel(){
		this.setDisplay("备注");
		this.setIcon("remark.png");
	}

	@Override
	public String toCode(Map<String, Object> root,int offset) {
		// TODO Auto-generated method stub
		return "";
	}

	@Override
	public List<HashMap<String, String>> getParamList() {
		// TODO Auto-generated method stub
		return null;
	}
}
