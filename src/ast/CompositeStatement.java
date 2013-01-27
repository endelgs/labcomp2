/**
 * 379930 Endel Guimaraes Silva
 * 400564 Felipe Augusto Rosa
 */
package ast;

import java.util.ArrayList;

/**
 *
 *  @author endel
 */
public class CompositeStatement extends Statement{
  public CompositeStatement(ArrayList<Statement> statementList){
    this.statementList = statementList;
  }
  private ArrayList<Statement> statementList;

  @Override
  public void genK(PW pw) {
    pw.println("{");
    for(int i = 0; i < statementList.size(); i++){
      statementList.get(i).genK(pw);
      pw.println(";");
    }
    pw.println("}");
  }
  @Override
  public void genC(PW pw) {
    pw.println("{");
    for(int i = 0; i < statementList.size(); i++)
      statementList.get(i).genC(pw);
    pw.println("}");
  }
}