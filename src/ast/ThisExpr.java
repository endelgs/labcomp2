/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ast;

/**
 *
 * @author Endel
 */
public class ThisExpr extends Expr{
  public ThisExpr(ClassDec t){
    super();
    this.type = t;
  }
  @Override
  public void genK(PW pw, boolean putParenthesis) {
    pw.print("this");
  }

  @Override
  public Type getType() {
    return type;
  }
  private ClassDec type;
}
