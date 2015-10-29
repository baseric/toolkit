package business.model.logic.dbs;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import business.AbstractModel;

@SuppressWarnings("serial")
public class DbsSelectModel extends AbstractModel{
	public String display = null;
	public DbsSelectModel() {
		this.setDisplay("DBS��ѯ");
		this.setIcon("database_search.png");
	}
	@Override
	public String toCode(Map<String, Object> root, int offset) {
		return "";
	}
	public String getDisplay() {
		return display;
	}
	public void setDisplay(String display) {
		this.display = display;
	}
	@Override
	public List<HashMap<String, String>> getParamList() {
		// TODO Auto-generated method stub
		return null;
	}

}
