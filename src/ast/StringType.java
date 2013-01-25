/**
 * 379930 Endel Guimaraes Silva
 * 400564 Felipe Augusto Rosa
 */
package ast;

public class StringType extends Type {
    
    public StringType() {
        super("String");
    }
   public String getCName() {
      return "char *";
   }
   public void genC(PW pw){
      
      pw.print(getCName());
    }
   
}