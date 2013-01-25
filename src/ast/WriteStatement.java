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
    for(int i = 0; i < exprList.getSize(); i++){
      if(exprList.getElement(i).getType() instanceof StringType){
        pw.print("puts(");
        exprList.getElement(i).genC(pw,false);
        pw.println(");");
      }else{
        pw.print("printf(\"%d \", ");
        exprList.getElement(i).genC(pw,false);
        pw.println(");");
      }
    }
    
  }
  public ExprList getExprList() {
    return exprList;
  }

  public void setExprList(ExprList exprList) {
    this.exprList = exprList;
  }
  
  private ExprList exprList;
}
