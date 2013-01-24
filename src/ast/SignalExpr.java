/**
 * 379930 Endel Guimaraes Silva
 * 400564 Felipe Augusto Rosa
 */
package ast;

import lexer.*;

public class SignalExpr extends Expr {

  public SignalExpr(Symbol oper, Expr expr) {
    this.oper = oper;
    this.expr = expr;
  }

  @Override
  public void genK(PW pw, boolean putParenthesis) {
    if (putParenthesis) {
      pw.print("(");
    }
    pw.print(oper == Symbol.PLUS ? "+" : "-");
    expr.genK(pw, true);
    if (putParenthesis) {
      pw.print(")");
    }
  }
  @Override
  public void genC(PW pw, boolean putParenthesis) {
    if (putParenthesis) {
      pw.print("(");
    }
    pw.print(oper == Symbol.PLUS ? "+" : "-");
    expr.genC(pw, true);
    if (putParenthesis) {
      pw.print(")");
    }
  }
  @Override
  public Type getType() {
    return expr.getType();
  }
  private Expr expr;
  private Symbol oper;
}
