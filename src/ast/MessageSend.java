package ast;


abstract class MessageSend  extends Expr  {

  public MessageSend(Variable variable, MethodDec method, ExprList exprList ) {
    this.exprList = exprList;
    this.variable = variable;
    this.method = method;
  }
  public abstract void genK(PW pw);
  public ExprList getExprList() {
    return exprList;
  }

  public void setExprList(ExprList exprList) {
    this.exprList = exprList;
  }

  public Variable getVariable() {
    return variable;
  }

  public void setVariable(Variable variable) {
    this.variable = variable;
  }

  public MethodDec getMethod() {
    return method;
  }

  public void setMethod(MethodDec method) {
    this.method = method;
  }

  
  protected ExprList exprList;
  protected Variable variable;
  protected MethodDec method;
}

