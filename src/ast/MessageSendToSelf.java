/**
 * 379930 Endel Guimaraes Silva 400564 Felipe Augusto Rosa
 */
package ast;

public class MessageSendToSelf extends MessageSend {

  public MessageSendToSelf(Variable variable, MethodDec method, ExprList exprList) {
    super(variable, method, exprList);
  }

  public Type getType() {
    return getMethod().getType();
  }

  public void genK(PW pw) {
    genK(pw, false);
  }

  public void genC(PW pw) {
    genC(pw, false);
  }

  public void genK(PW pw, boolean putParenthesis) {
    pw.print("this.");
    pw.print(variable.getName() + ".");
    pw.print(getMethod().getName());
    pw.print("(");
    if (exprList != null) {
      getExprList().genK(pw);
    }
    pw.print(")");
  }

  public void genC(PW pw, boolean putParenthesis) {
    
     pw.print("_"+getMethod().getClassDec().getName()+"_"+getMethod().getName()+
              "( (_class_"+getMethod().getClassDec().getName()+" *) this");
    
    if(getExprList() != null){
      if(getExprList().getSize() > 0)
        pw.print(",");
      getExprList().genC(pw);
    }
    pw.print(")");
  }
}