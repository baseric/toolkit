package business.codesource;

import java.io.BufferedInputStream; 
import java.io.FileInputStream; 
import org.eclipse.jdt.core.dom.AST; 
import org.eclipse.jdt.core.dom.ASTParser; 
import org.eclipse.jdt.core.dom.CompilationUnit; 
/** 
* java源文件解析操作 
*/ public class JdtAst { 
 
   private static ASTParser astParser = ASTParser.newParser(AST.JLS3); // 非常慢 
   /** 
    * 获得java源文件的结构CompilationUnit 
    */
   public CompilationUnit getCompilationUnit(String javaFilePath) 
           throws Exception { 
 
       BufferedInputStream bufferedInputStream = new BufferedInputStream( 
               new FileInputStream(javaFilePath)); 
       byte[] input = new byte[bufferedInputStream.available()]; 
       bufferedInputStream.read(input); 
       bufferedInputStream.close(); 
       JdtAst.astParser.setSource(new String(input).toCharArray()); 
       /**/
       CompilationUnit result = (CompilationUnit) (JdtAst.astParser.createAST(null)); // 很慢 
       return result; 
 
   } 
} 
