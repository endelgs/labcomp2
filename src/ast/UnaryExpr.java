/**
 * 379930 Endel Guimaraes Silva
 * 400564 Felipe Augusto Rosa
 */
package ast;

import lexer.*;

public class UnaryExpr extends Expr {

  public UnaryExpr(Expr expr, Symbol op) {
    this.expr = expr;
    this.op = op;
  }

  @Override
  public void genK(PW pw, boolean putParenthesis) {
    switch (op) {
      case PLUS:
        pw.print("+");
        break;
      case MINUS:
        pw.print("-");
        break;
      case NOT:
        pw.print("!");
        break;
    }
    expr.genK(pw, false);
  }
  @Override
  public void genC(PW pw, boolean putParenthesis) {
    switch (op) {
      case PLUS:
        pw.print("+");
        break;
      case MINUS:
        pw.print("-");
        break;
      case NOT:
        pw.print("!");
        break;
    }
    expr.genC(pw, false);
  }
  @Override
  public Type getType() {
    return expr.getType();
  }
  private Expr expr;
  private Symbol op;
}
