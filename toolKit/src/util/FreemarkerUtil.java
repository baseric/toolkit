package util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Map;

import org.eclipse.core.resources.IFile;

import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;

public class FreemarkerUtil {
	/**
	 * 取得生成java文件的存放路径
	 * @param file
	 * @return
	 * @throws Exception
	 * 2015-6-22
	 * @tianming
	 */
	public File getCreateCodePath(IFile file) throws Exception{
		String fileName = file.getName();
		String filePath = file.getLocation().toFile().getAbsolutePath();//配置文件路径
		String projectPath = file.getProject().getLocation().toFile().getAbsolutePath();//工程路径
		filePath = filePath.replace(projectPath+"\\", "").replace("\\"+fileName,"");
		
		//获取类名
		fileName = fileName.replace(".action", ".java").replace(".logic",".java");
		fileName = fileName.toUpperCase().substring(0, 1)+fileName.substring(1);
		
		File outputFile = new File(projectPath+"/src/"+filePath+"/"+fileName);//生成代码的目录
		Log.write("生成源文件路径："+outputFile.getAbsolutePath());
		if(!outputFile.exists()){
			File dir = new File(projectPath+"/src/"+filePath+"/");
			if(!dir.exists()){
				dir.mkdirs();
			}
			outputFile.createNewFile();
		}
		return outputFile;
	}
	/**
	 * 生成action用到的
	 * @param ftl_path
	 * @param root
	 * @param templateFile
	 * @param outputFile
	 * @throws Exception
	 * 2015-6-22
	 * @tianming
	 */
	public void createCode(String ftl_path,Map<String, Object> root,String templateFile,File outputFile) throws Exception{
		Configuration cfg = new Configuration();
		cfg.setDefaultEncoding("UTF-8");
		/**查找配置文件**/
		/**freemark加载配置文件**/
		cfg.setDirectoryForTemplateLoading(new File(ftl_path+"/"));
		cfg.setObjectWrapper(new DefaultObjectWrapper()); 
		Template temp = cfg.getTemplate(templateFile,"UTF-8"); //加载代码模版文件
		temp.setEncoding("UTF-8");
		FileOutputStream fileout = new FileOutputStream(outputFile);
		Writer out = new OutputStreamWriter(fileout,"UTF-8");
	    temp.process(root, out); //将数据输出到模版生成代码文件
	    out.flush();
	    out.close();
	    out = null;
	    temp = null;
	}
	/**
	 * 根据模版文件生成代码的字符串
	 * @param ftl_path
	 * @param root
	 * @param templateFile
	 * @return
	 * 2015-6-22
	 * @tianming
	 */
	public String getStringByFtl(String ftl_path,Map<String, Object> root,String templateFile){
		Configuration cfg = new Configuration();
		StringWriter sw = new StringWriter();
		try {
			cfg.setDirectoryForTemplateLoading(new File(ftl_path+"/"));
			cfg.setObjectWrapper(new DefaultObjectWrapper()); 
			Template temp = cfg.getTemplate(templateFile,"UTF-8"); //加载代码模版文件
			temp.setEncoding("UTF-8");
			temp.process(root, sw);
		} catch (Exception e) {
			Log.write("",e);
		}
		return sw.toString();

	}
}
