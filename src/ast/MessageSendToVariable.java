/**
 * 379930 Endel Guimaraes Silva 400564 Felipe Augusto Rosa
 */
package ast;

public class MessageSendToVariable extends MessageSend {

  public MessageSendToVariable(Variable variable, MethodDec method, ExprList exprList) {
    super(variable, method, exprList);
  }

  public Type getType() {
    return getMethod().getType();
  }

  public void genK(PW pw) {
    genK(pw, false);
  }

  public void genK(PW pw, boolean putParenthesis) {
    pw.print(variable.getName());
    pw.print(".");
    pw.print(getMethod().getName());
    pw.print("(");
    if (getExprList() != null) {
      getExprList().genK(pw);
    }
    pw.print(")");
  }

  public void genC(PW pw) {
    genC(pw, false);
  }

  @Override
  public void genC(PW pw, boolean putParenthesis) {
    ClassDec cd = (ClassDec)variable.getType();
    
    // Faco a busca recursiva pelo metodo nas classes superiores
    while(cd.getMethod(getMethod().getName(),true,false,true) == null)
      cd = cd.getSuperclass();
    
    int methodIndex = ((ClassDec) variable.getType()).getPublicMethodList().findCVTIndex(getMethod().getCName(),true);

    pw.print("( ("+getMethod().getType().getCName()+" (*)(" + variable.getType().getCName() + " *");
    for (int i = 0; i < getMethod().getParamList().getSize(); i++) {
      Parameter p = getMethod().getParamList().get(i);
      pw.print("," + p.getType().getCName());
    }
    pw.print(")) " + variable.getCName() + "->vt[" + methodIndex + "] )(" + variable.getCName());
    if(getExprList() != null){
      if(getExprList().getSize() > 0)
        pw.print(",");

      getExprList().genC(pw);
    }
    pw.print(")");
  }
  
}
