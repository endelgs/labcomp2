/**
 * 379930 Endel Guimaraes Silva
 * 400564 Felipe Augusto Rosa
 */
package ast;

public class VoidType extends Type {
    
    public VoidType() {
        super("void");
    }
    
   public String getCname() {
      return "void";
   }

}