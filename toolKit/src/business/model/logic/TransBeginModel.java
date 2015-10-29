package business.model.logic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import business.AbstractModel;

public class TransBeginModel extends AbstractModel {
	private static final long serialVersionUID = 6639562615129394786L;
	public TransBeginModel(){
		this.setDisplay("获取连接");
		this.setIcon("transactionBegin.png");
	}
	@Override
	public String toCode(Map<String, Object> root, int offset) {
		HashMap<String,Object> obj = new HashMap<String,Object>();
		computeNextStep(obj);
		return getCodeByTemplate(obj);
	}
	@Override
	public List<HashMap<String, String>> getParamList() {
		List<HashMap<String,String>> paramList = new ArrayList<HashMap<String,String>>();
		HashMap<String,String> row = new HashMap<String,String>();
		row.put("paramName", "_connection");
		row.put("paramType", "java.sql.Connection");
		paramList.add(row);
		return paramList;
	}

}
