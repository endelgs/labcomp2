/**
 * 379930 Endel Guimaraes Silva
 * 400564 Felipe Augusto Rosa
 */
package ast;

import java.util.*;

public class InstanceVariableList extends MemberList{

    public InstanceVariableList() {
       instanceVariableList = new ArrayList<InstanceVariable>();
    }
    public InstanceVariable get(int i){
      return instanceVariableList.get(i);
    }
    public void addElement(InstanceVariable instanceVariable) {
       instanceVariableList.add( instanceVariable );
    }
    public InstanceVariable searchVariable(String name){
      InstanceVariable v = null;
      for(int i = 0; i< instanceVariableList.size(); i++){
        if(instanceVariableList.get(i).getName().equals(name))
          return instanceVariableList.get(i);
      }
      return v;
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
    public void genC(PW pw){
      if(instanceVariableList != null){
        for(int i = 0; i < instanceVariableList.size(); i++){
          if(!instanceVariableList.get(i).getIsStatic())
            instanceVariableList.get(i).genC(pw);
        }
      }
      
    }
    private ArrayList<InstanceVariable> instanceVariableList;

}
