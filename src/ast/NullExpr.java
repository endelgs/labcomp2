package ast;

public class NullExpr extends Expr {
    
   public void genK( PW pw, boolean putParenthesis ) {
      pw.printIdent("NULL");
   }
   
   public Type getType() {
      //# corrija
      return null;
   }
}