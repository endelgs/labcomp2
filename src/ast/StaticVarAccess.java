/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ast;

/**
 *
 * @author Endel
 */
public class StaticVarAccess extends MessageSend{
  public StaticVarAccess(Variable v,ClassDec c){
    super(v,null,null);
    classDec = c;
  }
  public void genK(PW pw){
    genK(pw,false);
  }
  @Override
  public void genK(PW pw, boolean putParenthesis) {
    pw.print(classDec.getName()+"."+variable.getName());
  }

  @Override
  public Type getType() {
    return variable.getType();
  }
  protected ClassDec classDec;
}
