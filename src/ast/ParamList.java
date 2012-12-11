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

    public int getSize() {
        return paramList.size();
    }
    public void genK(PW pw){
      for (int i = 0; i < paramList.size(); i++) {
        paramList.get(i).genK(pw);
        if (i < paramList.size()) {
          pw.print(",");
        }
      }
    }

    private ArrayList<Parameter> paramList;

}
