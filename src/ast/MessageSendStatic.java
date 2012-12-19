/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ast;

/**
 *
 * @author endel
 */
public class MessageSendStatic extends MessageSend{
  public MessageSendStatic(Variable variable, MethodDec method, ExprList exprList) {
    super(variable, method, exprList);
  }
  @Override
  public void genK(PW pw, boolean putParenthesis) {
    pw.print(getVariable().getType().getName());
    pw.print(".");
    pw.print(getMethod().getName());
    pw.print("(");
    getExprList().genK(pw);
    pw.print(")");
  }

  @Override
  public Type getType() {
    return getMethod().getType();
  }
  
}
