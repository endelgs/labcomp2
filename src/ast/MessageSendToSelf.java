package ast;


public class MessageSendToSelf extends MessageSend {
    
  public MessageSendToSelf(Variable variable, MethodDec method, ExprList exprList) {
    super(variable, method, exprList);
  }  
  public Type getType() { 
        return null;
    }
    
    public void genK( PW pw, boolean putParenthesis ) {
      pw.print("this.");
      pw.print(getMethod().getName());
      pw.print("(");
      if(exprList != null)
        getExprList().genK(pw);
      pw.print(")");
    }
    
    
}