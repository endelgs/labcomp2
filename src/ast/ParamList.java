/**
 * 379930 Endel Guimaraes Silva
 * 400564 Felipe Augusto Rosa
 */
package ast;

import java.util.*;

public class ParamList {

    public ParamList() {
       paramList = new ArrayList<Parameter>();
    }

    public void addElement(Parameter v) {
       paramList.add(v);
    }

    public Iterator<Parameter> elements() {
        return paramList.iterator();
    }
    public Parameter get(int i){
      return paramList.get(i);
    }
    public int getSize() {
        return paramList.size();
    }
    public void genK(PW pw){
      for (int i = 0; i < paramList.size(); i++) {
        pw.print(paramList.get(i).getType().getName()+" ");
        pw.print(paramList.get(i).getName()+" ");
        if (i < paramList.size()-1) {
          pw.print(",");
        }
      }
    }
    public void genC(PW pw){
      for (int i = 0; i < paramList.size(); i++) {
        //if(paramList.get(i).getType() instanceof StringType)
          //pw.print("char * "+paramList.get(i).getName());
        //else
          //pw.print(paramList.get(i).getType().getName()+" "+paramList.get(i).getName());
        //pw.print(paramList.get(i).getType().getName()+" "+paramList.get(i).getName());
        paramList.get(i).genC(pw);
        if (i < paramList.size() -1) {
          pw.print(",");
        }
      }
    }
    private ArrayList<Parameter> paramList;

}
