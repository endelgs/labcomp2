package ast;

import java.util.*;

public class InstanceVariableList extends MemberList{

    public InstanceVariableList() {
       instanceVariableList = new ArrayList<InstanceVariable>();
    }

    public void addElement(InstanceVariable instanceVariable) {
       instanceVariableList.add( instanceVariable );
    }

    public Iterator<InstanceVariable> elements() {
    	return this.instanceVariableList.iterator();
    }

    public int getSize() {
        return instanceVariableList.size();
    }
    public void genK(PW pw){
      if(instanceVariableList != null){
        for(int i = 0; i < instanceVariableList.size(); i++){
          instanceVariableList.get(i).genK(pw);
        }
      }
      
    }
    private ArrayList<InstanceVariable> instanceVariableList;

}
