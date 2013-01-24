/**
 * 379930 Endel Guimaraes Silva
 * 400564 Felipe Augusto Rosa
 */
package ast;

/**
 *
 *  @author endel
 */
public class ObjectCreation extends Expr{

  public ObjectCreation(ClassDec classDec) {
    this.classDec = classDec;
  }

  public ClassDec getClassDec() {
    return classDec;
  }

  public void setClassDec(ClassDec classDec) {
    this.classDec = classDec;
  }
  
  private ClassDec classDec;

  @Override
  public void genK(PW pw, boolean putParenthesis) {
    pw.print("new "+classDec.getName()+"()");
  }
  @Override
  public void genC(PW pw, boolean putParenthesis) {
    pw.println("new_"+classDec.getName()+"();");
  }
  @Override
  public Type getType() {
    return classDec;
  }
}
