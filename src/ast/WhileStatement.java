/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ast;

import java.util.ArrayList;

/**
 *
 *  @author endel
 */
public class WhileStatement extends Statement {

  @Override
  public void genK(PW pw) {
    
    pw.print("while(");
    expr.genK(pw, false);
    pw.println(")");
    statement.genK(pw);
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
