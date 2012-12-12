/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
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
    pw.println("new "+classDec.getName()+"()");
  }

  @Override
  public Type getType() {
    return classDec;
  }
}
