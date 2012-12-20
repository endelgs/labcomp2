/**
 * 379930 Endel Guimaraes Silva
 * 400564 Felipe Augusto Rosa
 */
package ast;

public class ParenthesisExpr extends Expr {

  public ParenthesisExpr(Expr expr) {
    this.expr = expr;
  }

  public void genK(PW pw, boolean putParenthesis) {
    pw.print("(");
    expr.genK(pw, false);
    pw.printIdent(")");
  }

  public Type getType() {
    return expr.getType();
  }
  private Expr expr;
}