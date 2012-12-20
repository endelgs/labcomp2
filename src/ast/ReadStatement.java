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
public class ReadStatement extends Statement{
  public ReadStatement(){
    variableList = new ArrayList<Variable>();
  }
  @Override
  public void genK(PW pw) {
    pw.print("read (");
    for(int i = 0; i< variableList.size(); i++){
      pw.print(variableList.get(i).getName());
      if(i < variableList.size()-1)
        pw.print(",");
    }
    pw.print(")");
  }

  public ArrayList<Variable> getVariableList() {
    return variableList;
  }

  public void setVariableList(ArrayList<Variable> variableList) {
    this.variableList = variableList;
  }
  
  private ArrayList<Variable> variableList;
}
