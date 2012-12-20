/**
 * 379930 Endel Guimaraes Silva
 * 400564 Felipe Augusto Rosa
 */
package ast;

public class NullExpr extends Expr {
    
   public void genK( PW pw, boolean putParenthesis ) {
      pw.printIdent("null");
   }
   
   public Type getType() {
      //# corrija
      return new UndefinedType();
   }
}