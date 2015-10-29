package util;

import java.util.HashMap;
import java.util.Map;

public class KeyCode {
	private Map<String,String> keycodes = new HashMap<String,String>();
	public KeyCode(){
		//数字键
		int zero = 48;
		for(int i = 0 ;i<=9;i++ ){
			keycodes.put(""+zero++, i+"");
		}
		//字母键
		keycodes.put("97", "a");
		keycodes.put("98", "b");
		keycodes.put("99", "c");
		keycodes.put("100", "d");
		keycodes.put("101", "e");
		keycodes.put("102", "f");
		keycodes.put("103", "g");
		keycodes.put("104", "h");
		keycodes.put("105", "i");
		keycodes.put("106", "j");
		keycodes.put("107", "k");
		keycodes.put("108", "l");
		keycodes.put("109", "m");
		keycodes.put("110", "n");
		keycodes.put("111", "o");
		keycodes.put("112", "p");
		keycodes.put("113", "q");
		keycodes.put("114", "r");
		keycodes.put("115", "s");
		keycodes.put("116", "t");
		keycodes.put("117", "u");
		keycodes.put("118", "v");
		keycodes.put("119", "w");
		keycodes.put("120", "x");
		keycodes.put("121", "y");
		keycodes.put("122", "z");
		
		keycodes.put("32", "space");
		keycodes.put("9", "tab");
		keycodes.put("13", "enter");
		keycodes.put("27", "esc");
	}
	
	public String getKeyName(int keycode){
		System.out.println(keycode);
		return keycodes.get(String.valueOf(keycode));
	}
}
