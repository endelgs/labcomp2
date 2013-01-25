/**
 * 379930 Endel Guimaraes Silva
 * 400564 Felipe Augusto Rosa
 */
package ast;

public class IntType extends Type {
    
    public IntType() {
        super("int");
    }
   public String getCName() {
      return "int";
   }
   public void genC(PW pw){
     pw.print(getCName());
   }
}