package ast;


public class MessageSendToSelf extends MessageSend {
    
  public MessageSendToSelf(Variable variable, MethodDec method, ExprList exprList) {
    super(variable, method, exprList);
  }  
  public Type getType() { 
        return null;
    }
    
    public void genC( PW pw, boolean putParenthesis ) {
    }
    
    
}