/**
 * 379930 Endel Guimaraes Silva
 * 400564 Felipe Augusto Rosa
 */
package ast;


public class MessageSendToSelf extends MessageSend {
    
  public MessageSendToSelf(Variable variable, MethodDec method, ExprList exprList) {
    super(variable, method, exprList);
  }  
  public Type getType() { 
        return getMethod().getType();
    }
    public void genK(PW pw){
      genK(pw,false);
    }
    public void genK( PW pw, boolean putParenthesis ) {
      pw.print("this.");
      pw.print(variable.getName()+".");
      pw.print(getMethod().getName());
      pw.print("(");
      if(exprList != null)
        getExprList().genK(pw);
      pw.print(")");
    }
    public void genC( PW pw, boolean putParenthesis ) {
      pw.print("this.");
      pw.print(getMethod().getName());
      pw.print("(");
      getExprList().genK(pw);
      pw.print(")");
    }
    
}