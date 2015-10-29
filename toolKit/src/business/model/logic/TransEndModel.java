package business.model.logic;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import business.AbstractModel;

public class TransEndModel extends AbstractModel {
	private static final long serialVersionUID = -6917163791154526150L;
	public TransEndModel(){
		this.setDisplay("提交事务");
		this.setIcon("transactionEnd.png");
	}
	@Override
	public String toCode(Map<String, Object> root, int offset) {
		HashMap<String,Object> obj = new HashMap<String,Object>();
		computeNextStep(obj);
		return getCodeByTemplate(obj);
	}
	@Override
	public List<HashMap<String, String>> getParamList() {
		// TODO Auto-generated method stub
		return null;
	}

}
