package util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.Map;
/**
 * 描述：常用文件工具类
 * 作者：tianming
 * 日期：2013-3-2
 */
public class FileUtil {
/**
 * 描述：打印路径下所有的文件和文件夹
 * @param f
 * 作者：tianming
 * 日期：2013-3-2
 */
 public static void printAllFile(File f) {
	  // 是否是文件夹
	  if (f.isDirectory()) {
		   // 获得该文件夹下所有子文件和子文件夹
		   File[] f1 = f.listFiles();
		   // 循环处理每个对象
		   int len = f1.length;
		   for (int i = 0; i < len; i++) {
		    // 递归调用，处理每个文件对象
		    printAllFile(f1[i]);
		   }
	  }
 }
 /**
  * 
  * 描述：通用写文件方法
  * @param content 要写入文件的内容
  * @param desFilePath 输入文件路径
  * @param append 是否在原文件追加
  * 作者：tianming
  * 日期：2013-3-30 
  */
 public static String writerFile(String content,String desFilePath,Boolean append){
	 File f = new File(desFilePath);
	 FileOutputStream out = null;
	 String success = "0000";
	try {
		out = new FileOutputStream(f,append);
		out.write(content.getBytes("GB18030"));
	} catch (FileNotFoundException e) {
		e.printStackTrace();
		success = "f001";//文件不存在
	}catch (IOException e) {
		e.printStackTrace();
		success = "f002";//写文件失败
	}finally{
		if(out!=null){
			try {
				out.flush();
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
				success = "f003";//关闭文件失败
			}
		}
		f = null;
	}
	return success;
 }
 /**
  * 
  * 描述：读文件
  * @param desFilePath
  * @return
  * 作者：tianming
  * 日期：2013-3-30
  */
 public static String readFile(String desFilePath){
	File f = new File(desFilePath);
	@SuppressWarnings("unused")
	String success = "0000";
	FileInputStream in = null;
	StringBuffer content = new StringBuffer();
	byte[] b = null;
	try {
		in = new FileInputStream(f);
		b = new byte[in.available()];
        in.read(b);
		content.append(new String(b,"GB18030"));
	} catch (FileNotFoundException e) {
		e.printStackTrace();
		success = "f001";//文件不存在
	}catch (IOException e) {
		e.printStackTrace();
		success = "f002";//写文件失败
	}finally{
		if(in!=null){
			try {
				in.close();
			} catch (IOException e) {
				e.printStackTrace();
				success = "f003";//关闭文件失败
			}
		}
	}
	f = null;
	b = null;
	return content.toString();
 }
/**
 * 描述：删除路径下所有的文件和文件夹
 * @param f
 * 作者：tianming
 * 日期：2013-3-2
 */
 public static void deleteAll(File f) throws IOException{
	  // 文件
	  if (f.isFile()) {
	       f.delete();
	  } else { // 文件夹
		   // 获得当前文件夹下的所有子文件和子文件夹
		   File[] f1 = f.listFiles();
		   // 循环处理每个对象
		   int len = f1.length;
		   for (int i = 0; i < len; i++) {
		      // 递归调用，处理每个文件对象
		      deleteAll(f1[i]);
		   }
		   // 删除当前文件夹
		   f.delete();
	  }
 }
 /**
  * 
  * 描述：文件的复制
  * @param sourceFile
  * @param targetFile
  * @throws IOException
  * 作者：tianming
  * 日期：2013-3-2
  */
 public static void copyFile(File sourceFile, File targetFile) throws IOException {
     BufferedInputStream inBuff = null;
     BufferedOutputStream outBuff = null;
     byte[] b = null;
     try {
    	 if(!targetFile.exists()){
    		 targetFile.createNewFile();
    	 }
         // 新建文件输入流并对它进行缓冲
         inBuff = new BufferedInputStream(new FileInputStream(sourceFile));

         // 新建文件输出流并对它进行缓冲
         outBuff = new BufferedOutputStream(new FileOutputStream(targetFile));

         // 缓冲数组
         b = new byte[1024 * 5];
         int len;
         while ((len = inBuff.read(b)) != -1) {
             outBuff.write(b, 0, len);
         }
         // 刷新此缓冲的输出流
         outBuff.flush();
     } finally {
         // 关闭流
         if (inBuff != null)
             inBuff.close();
         if (outBuff != null)
             outBuff.close();
         b = null;
     }
 }
 /**
  * 
  * 描述：文件的复制
  * @param sourceFile
  * @param targetFile
  * @throws IOException
  * 作者：tianming
  * 日期：2013-3-2
  */
 public static void copyFile(File sourceFile, File targetFolder,String fileName) throws IOException {
     BufferedInputStream inBuff = null;
     BufferedOutputStream outBuff = null;
     byte[] b = null;
     File targetFile = new File(targetFolder.getAbsolutePath()+"/"+fileName);
     try {
    	 if(!targetFile.exists()){
    		 targetFile.createNewFile();
    	 }
         // 新建文件输入流并对它进行缓冲
         inBuff = new BufferedInputStream(new FileInputStream(sourceFile));

         // 新建文件输出流并对它进行缓冲
         outBuff = new BufferedOutputStream(new FileOutputStream(targetFile));

         // 缓冲数组
         b = new byte[1024 * 5];
         int len;
         while ((len = inBuff.read(b)) != -1) {
             outBuff.write(b, 0, len);
         }
         // 刷新此缓冲的输出流
         outBuff.flush();
     } finally {
         // 关闭流
         if (inBuff != null)
             inBuff.close();
         if (outBuff != null)
             outBuff.close();
         b = null;
     }
 }
 /**
  * 
  * 描述：文件的复制
  * @param sourceFile
  * @param targetFile
  * @throws IOException
  * 作者：tianming
  * 日期：2013-3-2
  */
 public static void copyFile(File srcFileName, File destFileName, String srcCoding, String destCoding) throws IOException {// 把文件转换为GBK文件
	 BufferedReader br = null;
     BufferedWriter bw = null;
     char[] cbuf = null;
     try {
         br = new BufferedReader(new InputStreamReader(new FileInputStream(srcFileName), srcCoding));
         bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(destFileName), destCoding));
         cbuf = new char[1024 * 5];
         int len = cbuf.length;
         int off = 0;
         int ret = 0;
         while ((ret = br.read(cbuf, off, len)) > 0) {
             off += ret;
             len -= ret;
         }
         bw.write(cbuf, 0, off);
         bw.flush();
     } finally {
         if (br != null)
             br.close();
         if (bw != null)
             bw.close();
         cbuf = null;
     }
 }
 /**
  * 
  * 描述：复制文件夹
  * @param sourceDir
  * @param targetDir
  * @throws IOException
  * 作者：tianming
  * 日期：2013-3-2
  */
 public static void copyDirectiory(String sourceDir, String targetDir) throws IOException {
     // 新建目标目录
	 File target  = new File(targetDir);
	 if(target.exists()){
		 deleteAll(target);
		 target.delete();
		 target.mkdirs();
	 }else{
		 target.mkdirs();
	 }
     // 获取源文件夹当前下的文件或目录
     File[] file = (new File(sourceDir)).listFiles();
     for (int i = 0; i < file.length; i++) {
         if (file[i].isFile()) {
             // 源文件
             File sourceFile = file[i];
             // 目标文件
             File targetFile = new File(new File(targetDir).getAbsolutePath() + File.separator + file[i].getName());
             copyFile(sourceFile, targetFile);
             targetFile = null;
         }
         if (file[i].isDirectory()) {
             // 准备复制的源文件夹
             String dir1 = sourceDir + "/" + file[i].getName();
             // 准备复制的目标文件夹
             String dir2 = targetDir + "/" + file[i].getName();
             copyDirectiory(dir1, dir2);
         }
     }
     target = null;
 }
   
 /**
  * 以行为单位读取文件，常用于读面向行的格式化文件
  */
 public static String readFileByLines(String fileName) {
     File file = new File(fileName);
     BufferedReader reader = null;
     StringBuffer buff = new StringBuffer();
     try {
         System.out.println("以行为单位读取文件内容，一次读一整行：");
         reader = new BufferedReader(new FileReader(file));
         String tempString = null;
         int line = 1;
         // 一次读入一行，直到读入null为文件结束
         while ((tempString = reader.readLine()) != null) {
             // 显示行号
        	 buff.append(tempString);
             line++;
         }
         reader.close();
     } catch (IOException e) {
         e.printStackTrace();
     } finally {
         if (reader != null) {
             try {
                 reader.close();
             } catch (IOException e1) {
             }
         }
         file = null;
     }
     return buff.toString();
 }
 public static String getExtensionName(String filename) {   
	         if ((filename != null) && (filename.length() > 0)) {   
	             int dot = filename.lastIndexOf('.');   
	             if ((dot >-1) && (dot < (filename.length() - 1))) {   
	                return filename.substring(dot + 1);   
	           }   
	        }   
	        return filename;   
 } 
 /**
  * 
  * 描述：判断文件类型是否是图片
  * @param filename
  * @return
  * 作者：tianming
  * 日期：2013-3-8
  */
 public static boolean isImage(String filename){
	 String extension = FileUtil.getExtensionName(filename);
	 Map<String,String> images = new HashMap<String,String>();
	 images.put("jpg", "Y");
	 images.put("bmp", "Y");
	 images.put("gif", "Y");
	 images.put("png", "Y");
	 return images.containsKey(extension.toLowerCase());	 
 }
 public static void main(String[] args){
	try {
		String sss = "";
		FileUtil.writerFile("描述：设置附件编号，将附件从临时路径移动到正式路径", "D:/x.txt", false);
		sss = FileUtil.readFile("D:/x.txt"); 
		System.out.println(sss);
	} catch (Exception e) {
		e.printStackTrace();
	}
 }
 
 public static String joinArray(String[] arr,String sp){
	 String start = arr[0];
	 for(int i = 1;i<arr.length;i++){
		 start +=sp+arr[i];
	 }
	 return start;
 }
 public static String joinArray(String[] arr,String sp,int start,int end){
	 String str = "";
	 for(int i = start;i<end;i++){
		 if(i==end-1){
			 str +=arr[i];
		 }else{
			 str +=arr[i]+sp;
		 }
	 }
	 return str;
 }
}
