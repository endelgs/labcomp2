/**
 * 379930 Endel Guimaraes Silva
 * 400564 Felipe Augusto Rosa
 */
package ast;

/**
 *
 * @author endel
 */
public class AssignmentStatement extends Statement{
  public AssignmentStatement(Variable variable, Expr data){
    this.variable = variable;
    if(data != null)
      variable.setCurrentType(data.getType());
    this.data = data;
  }
  @Override
  public void genK(PW pw) {
    if(variable instanceof InstanceVariable && ((InstanceVariable) variable).getIsStatic())
      pw.print(variable.getType().getName()+".");
    pw.print(variable.getName()+" = ");
    data.genK(pw,false);
  }
  @Override
  public void genC(PW pw) {
    if(variable instanceof InstanceVariable)
      pw.print("this->");
    pw.print(variable.getCName()+" = ");
    if(!data.getType().equals(variable.getType()))
      pw.print("("+variable.getType().getCName()+" *)");
    data.genC(pw,false);
    pw.println(";");
  }
  private Variable variable;
  private Expr data;
}
