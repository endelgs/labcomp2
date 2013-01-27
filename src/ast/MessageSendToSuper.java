/**
 * 379930 Endel Guimaraes Silva 400564 Felipe Augusto Rosa
 */
package ast;

public class MessageSendToSuper extends MessageSend {

  public MessageSendToSuper(Variable variable, MethodDec method, ExprList exprList, ClassDec superclass) {
    super(variable, method, exprList);
    this.superclass = superclass;
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
    pw.print("super.");
    pw.print(getMethod().getName());
    pw.print("(");
    if (getExprList() != null) {
      getExprList().genK(pw);
    }
    pw.print(")");
  }

  @Override
  public void genC(PW pw, boolean putParenthesis) {
    
    // Faco a busca recursiva pelo metodo nas classes superiores
    ClassDec cd = superclass;
    while(cd.getMethod(getMethod().getName(),true,false,true) == null)
      cd = cd.getSuperclass();
    
    pw.print("_" + cd.getName() + "_" + getMethod().getName()
            + "( (_class_" + cd.getName() + " *) this");

    if (getExprList() != null) {
      if (getExprList().getSize() > 0) {
        pw.print(",");
      }

      getExprList().genC(pw);
    }
    pw.print(")");
  }
  private ClassDec superclass;
}