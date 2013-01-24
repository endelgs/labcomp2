/**
 * 379930 Endel Guimaraes Silva
 * 400564 Felipe Augusto Rosa
 */
package ast;

/**
 *
 * @author Endel
 */
public class ThisExpr extends Expr{
  public ThisExpr(ClassDec t){
    super();
    this.type = t;
  }
  @Override
  public void genK(PW pw, boolean putParenthesis) {
    pw.print("this");
  }
@Override
  public void genC(PW pw, boolean putParenthesis) {
    pw.print("this");
  }
  @Override
  public Type getType() {
    return type;
  }
  private ClassDec type;
}
