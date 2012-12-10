package ast;

public class MessageSendToSuper extends MessageSend { 

  public MessageSendToSuper(Variable variable, MethodDec method, ExprList exprList,ClassDec superclass) {
    super(variable, method, exprList);
    this.superclass = superclass;
  }

    public Type getType() { 
        return null;
    }

    public void genC( PW pw, boolean putParenthesis ) {
        
    }
    private ClassDec superclass;
}