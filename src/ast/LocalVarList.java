package ast;

import java.util.*;

public class LocalVarList extends Statement{

    public LocalVarList() {
       localList = new ArrayList<Variable>();
    }

    public void addElement(Variable v) {
       localList.add(v);
    }

    public Iterator<Variable> elements() {
        return localList.iterator();
    }

    public int getSize() {
        return localList.size();
    }

    private ArrayList<Variable> localList;

  @Override
  public void genK(PW pw) {
    for(int i = 0; i < localList.size(); i++){
      localList.get(i);
    }
  }

}
