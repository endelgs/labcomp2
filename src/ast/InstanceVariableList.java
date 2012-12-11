package ast;

import java.util.*;

public class InstanceVariableList {

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
        if(instanceVariableList.get(0).getIsStatic())
          pw.print("static ");
        pw.print("private ");
        
        for(int i = 0; i < instanceVariableList.size(); i++){
          pw.print(instanceVariableList.get(i).getName()+"");
        }
        
      }
      
    }
    private ArrayList<InstanceVariable> instanceVariableList;

}
