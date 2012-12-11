package ast;

public class MessageSendToSuper extends MessageSend { 

  public MessageSendToSuper(Variable variable, MethodDec method, ExprList exprList,ClassDec superclass) {
    super(variable, method, exprList);
    this.superclass = superclass;
  }

    public Type getType() { 
        return null;
    }

    public void genK( PW pw, boolean putParenthesis ) {
      pw.print("super.");
      pw.print(getMethod().getName());
      pw.print("(");
      getExprList().genK(pw);
      pw.print(")");
    }
    private ClassDec superclass;
}