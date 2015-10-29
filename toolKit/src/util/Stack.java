package util;

public class Stack {   
	   private int maxSize;   
	   private char [] stackArray;   
	   private int top;   
	      
	   public Stack(int s){   
	        maxSize = s;   
	        stackArray = new char[maxSize];   
	        top = -1;   
	    }   
	       
//	      添加数据   
	    public void push(char j){   
	        stackArray[++top] = j;   
	    }   
	  
//	      删除数据   
	    public char pop(){   
	        return stackArray[top--];   
	    }   
	  
//	      获取数据   
	    public char peek(){   
	        return stackArray[top];   
	    }   
	       
//	      判断栈是否为空   
	    public boolean isEmpty(){   
	        return (top == -1);   
	    }   
	  
//	      判断栈是否已满   
	    public boolean isFull(){   
	        return (top ==  maxSize-1);   
	    }   
	       
 } 
