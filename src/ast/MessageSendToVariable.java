/**
 * 379930 Endel Guimaraes Silva
 * 400564 Felipe Augusto Rosa
 */
package ast;

public class MessageSendToVariable extends MessageSend {

  public MessageSendToVariable(Variable variable, MethodDec method, ExprList exprList) {
    super(variable, method, exprList);
  }

  public Type getType() {
    return getMethod().getType();
  }
  public void genK(PW pw){
    genK(pw,false);
  }
  public void genK(PW pw, boolean putParenthesis) {
      pw.print(variable.getName());
      pw.print(".");
      pw.print(getMethod().getName());
      pw.print("(");
      if(getExprList() != null)
        getExprList().genK(pw);
      pw.print(")");
  }
}
