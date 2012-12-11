/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
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
    for(int i = 0; i < statementList.size(); i++)
      statementList.get(i).genK(pw);
    pw.println("}");
  }
}