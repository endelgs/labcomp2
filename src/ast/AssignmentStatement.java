/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ast;

/**
 *
 * @author endel
 */
public class AssignmentStatement extends Statement{
  public AssignmentStatement(Variable variable, Expr data){
    this.variable = variable;
    this.data = data;
  }
  @Override
  public void genK(PW pw) {
    if(variable instanceof InstanceVariable && ((InstanceVariable) variable).getIsStatic())
      pw.print(variable.getType().getName()+".");
    pw.print(variable.getName()+" = ");
    data.genK(pw,false);
  }
  private Variable variable;
  private Expr data;
}
