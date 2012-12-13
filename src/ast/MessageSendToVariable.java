package ast;

public class MessageSendToVariable extends MessageSend {

  public MessageSendToVariable(Variable variable, MethodDec method, ExprList exprList) {
    super(variable, method, exprList);
  }

  public Type getType() {
    return null;
  }

  public void genK(PW pw, boolean putParenthesis) {
      pw.print(variable.getName());
      pw.print(".");
      pw.print(getMethod().getName());
      pw.print("(");
      getExprList().genK(pw);
      pw.print(")");
  }
}
