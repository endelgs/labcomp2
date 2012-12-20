/**
 * 379930 Endel Guimaraes Silva
 * 400564 Felipe Augusto Rosa
 */
package ast;

public class StringType extends Type {
    
    public StringType() {
        super("String");
    }
   public String getCname() {
      return "char *";
   }
   
}