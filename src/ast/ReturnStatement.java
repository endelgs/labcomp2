/**
 * 379930 Endel Guimaraes Silva
 * 400564 Felipe Augusto Rosa
 */
package ast;

/**
 *
 *  @author endel
 */
public class ReturnStatement extends Statement{

  @Override
  public void genK(PW pw) {
    pw.print("return ");
    expr.genK(pw,false);
  }

  public Expr getExpr() {
    return expr;
  }

  public void setExpr(Expr expr) {
    this.expr = expr;
  }
  
  private Expr expr;
}
