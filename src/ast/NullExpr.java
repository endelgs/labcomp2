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