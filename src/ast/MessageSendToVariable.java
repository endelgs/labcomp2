package ast;

public class MessageSendToVariable extends MessageSend {

  public MessageSendToVariable(Variable variable, MethodDec method, ExprList exprList) {
    super(variable, method, exprList);
    this.variable = variable;
  }

  public Type getType() {
    return null;
  }

  public void genC(PW pw, boolean putParenthesis) {
  }
  private Variable variable;
}
