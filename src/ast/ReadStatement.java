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
  @Override
  public void genC(PW pw) {
    for(int i = 0; i< variableList.size(); i++){
      if(variableList.get(i).getType() instanceof IntType){
        pw.print("{\n"
                + "char __s[512];\n"
                + "gets(__s);\n"
                + "sscanf(__s, \"%d\", &");
        if(variableList.get(i) instanceof InstanceVariable)
          pw.print("this->");
        pw.print(variableList.get(i).getCName()
                + ");\n"
                + "}");
      }else{
        pw.print("{char __s[512];gets(__s);");
        variableList.get(i).genC(pw);
        pw.print("= malloc(strlen(__s) + 1);strcpy(");
        variableList.get(i).genC(pw);
        pw.println(", __s);}");
      }
      
      if(i < variableList.size()-1)
        pw.print(",");
    }
  }

  public ArrayList<Variable> getVariableList() {
    return variableList;
  }

  public void setVariableList(ArrayList<Variable> variableList) {
    this.variableList = variableList;
  }
  
  private ArrayList<Variable> variableList;
}
