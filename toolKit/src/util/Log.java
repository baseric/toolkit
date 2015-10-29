package util;

import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.eclipse.core.runtime.ILog;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.ui.console.ConsolePlugin;
import org.eclipse.ui.console.IConsole;
import org.eclipse.ui.console.MessageConsole;
import org.eclipse.ui.console.MessageConsoleStream;

import toolkit.Activator;

public class Log {
	public static void write(String message){
		Date date = new Date();
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd HH:mm:ss");
		printConsole(format.format(date)+"["+message+"]",null);
		//System.out.println(format.format(date)+"["+message+"]");
	}
	public static void write(String message,Exception e){
		//System.out.println(message);
		printConsole(message,e);
		e.printStackTrace(System.out);
	 	ILog log = Activator.getDefault().getLog();
		log.log(new Status(IStatus.OK, Activator.PLUGIN_ID,
				0,message, e));
	}
	public static void printConsole(String message,Exception e){
		 // 首先新建一个MessageConsole
		  MessageConsole console = null;
		  IConsole[] consoles = ConsolePlugin.getDefault().getConsoleManager().getConsoles();
		  for(int i = 0;i<consoles.length;i++){
			if("mytools-console".equals(consoles[i].getName())){
				console =(MessageConsole) consoles[i];
				break;
			}
		  }
		  if(console==null){
			  console = new MessageConsole("mytools-console", null);
			  // 通过ConsolePlugin得到ConsoleManager，并把新建立的console 添加进去
			  ConsolePlugin.getDefault().getConsoleManager().addConsoles(
					  new IConsole[]{console});
		  }
		  
		  // 新建一个MessageConsoleStream，用于接收需要显示的信息
		  MessageConsoleStream consoleStream = console.newMessageStream();
		 
		  // 打开Console视图
		  ConsolePlugin.getDefault().getConsoleManager().showConsoleView(console);
		  // 使用MessageConsoleStream来打印你想要显示的信息到Console视图，这样一切就OK了，简单吧：）
		  if(message!=null&&message.length()>0)
			  consoleStream.println(message);
		  if(e!=null){
			  try{
				  e.printStackTrace(new PrintStream(console.newOutputStream()));
			  }catch(Exception ex){
				  ex.printStackTrace();
			  }
		  } 
	}
}
