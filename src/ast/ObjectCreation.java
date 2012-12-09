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
  public void genC(PW pw, boolean putParenthesis) {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public Type getType() {
    throw new UnsupportedOperationException("Not supported yet.");
  }
}
