package ast;

import java.util.*;

public class MethodList extends MemberList{

    public MethodList() {
       methodList = new ArrayList<MethodDec>();
    }

    public void addElement(MethodDec method) {
       methodList.add( method );
    }

    public Iterator<MethodDec> elements() {
    	return this.methodList.iterator();
    }

    public int getSize() {
        return methodList.size();
    }
    public void genK(PW pw){
      if(methodList != null){
        while(methodList.iterator().hasNext())
          methodList.iterator().next().genK(pw);
      }
    }
    private ArrayList<MethodDec> methodList;

}
