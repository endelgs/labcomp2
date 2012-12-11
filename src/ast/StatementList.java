/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ast;

import java.util.ArrayList;

/**
 *
 * @author endel
 */
public class StatementList {
  private ArrayList<Statement> statementList;
  
  public void setStatementList(ArrayList<Statement> statementList){
    this.statementList = statementList;
  }  

  public ArrayList<Statement> getStatementList() {
    return statementList;
  }
  
  public void genK(PW pw){
    
  }
}
