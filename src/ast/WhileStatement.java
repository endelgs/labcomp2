/**
 * 379930 Endel Guimaraes Silva
 * 400564 Felipe Augusto Rosa
 */
package ast;

import java.util.ArrayList;

/**
 *
 * @author endel
 */
public class WhileStatement extends Statement {

  @Override
  public void genK(PW pw) {

    pw.print("while(");
    expr.genK(pw, false);
    pw.print(")");
    statement.genK(pw);
  }

  @Override
  public void genC(PW pw) {

    pw.print("while(");
    if (expr.getType() instanceof BooleanType) {
      expr.genC(pw, true);
      pw.print(" != false");
    } else {
      expr.genC(pw, false);
    }
    pw.println(")");
    statement.genC(pw);
  }

  public Expr getExpr() {
    return expr;
  }

  public void setExpr(Expr expr) {
    this.expr = expr;
  }

  public Statement getStatement() {
    return statement;
  }

  public void setStatement(Statement statement) {
    this.statement = statement;
  }
  private Expr expr;
  private Statement statement;
}
