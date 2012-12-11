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
    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public Type getType() {
    throw new UnsupportedOperationException("Not supported yet.");
  }
  
}
