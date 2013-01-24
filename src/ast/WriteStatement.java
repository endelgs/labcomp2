/**
 * 379930 Endel Guimaraes Silva
 * 400564 Felipe Augusto Rosa
 */
package ast;

/**
 *
 *  @author endel
 */
public class WriteStatement extends Statement{

  @Override
  public void genK(PW pw) {
    pw.print("write(");
    exprList.genK(pw);
    pw.print(")");
  }
  public void genC(PW pw) {
    pw.print("puts(");
    exprList.genC(pw);
    pw.println(");");
  }
  public ExprList getExprList() {
    return exprList;
  }

  public void setExprList(ExprList exprList) {
    this.exprList = exprList;
  }
  
  private ExprList exprList;
}
