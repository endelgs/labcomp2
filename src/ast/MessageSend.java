package ast;


abstract class MessageSend  extends Expr  {

  public MessageSend(Variable variable, MethodDec method, ExprList exprList ) {
    this.exprList = exprList;
    this.variable = variable;
    this.method = method;
  }
  public void genK(PW pw){
    variable.genK(pw);
    pw.print(".");
    pw.print(method.getName());
    pw.print("(");
    if(exprList != null)
      exprList.genK(pw);
    pw.println(")");
  }
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

