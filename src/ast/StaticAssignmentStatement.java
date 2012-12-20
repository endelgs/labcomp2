/**
 * 379930 Endel Guimaraes Silva
 * 400564 Felipe Augusto Rosa
 */
package ast;

/**
 *
 * @author Endel
 */
public class StaticAssignmentStatement extends Statement{
  public StaticAssignmentStatement(ClassDec c, Variable v, Expr data){
    this.classDec = c;
    this.variable = v;
    this.data = data;
  }

  @Override
  public void genK(PW pw) {
    pw.print(classDec.getName()+".");
    pw.print(variable.getName()+" = ");
    data.genK(pw, true);
  }
  private ClassDec classDec;
  private Expr data;
  private Variable variable;
}
