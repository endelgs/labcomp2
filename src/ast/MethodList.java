/**
 * 379930 Endel Guimaraes Silva
 * 400564 Felipe Augusto Rosa
 */
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
    public MethodDec get(int i){
      return methodList.get(i);
    }
    public int getSize() {
        return methodList.size();
    }
    public void genK(PW pw){
      if(methodList != null){
        for(int i = 0; i< methodList.size(); i++)
          methodList.get(i).genK(pw);
      }
    }
    public void genC(PW pw){
      if(methodList != null){
        for(int i = 0; i< methodList.size(); i++)
          methodList.get(i).genC(pw);
      }
    }
    public void genCPrototype(PW pw){
      if(methodList != null){
        for(int i = 0; i< methodList.size(); i++)
          methodList.get(i).genCPrototype(pw);
      }
    }
    public void genCVT(PW pw,ClassDec classDec){
      if(methodList != null){
        pw.println("Func VTclass_"+classDec.getName()+"[] = {");
        for(int i = 0; i< methodList.size(); i++){
          pw.printIdent("( void (*)() )"+methodList.get(i).getCName());
          if(i < methodList.size()-1)
            pw.print(",");
          pw.println("");
          }
        pw.println("};");
      }
    }
    
    /*
     * Metodo que BUSCA um metodo na lista
     */
    public int searchMethod(String name){
      for(int i = 0; i< methodList.size();i++)
        if(methodList.get(i).getName().equals(name))
          return i;
      return -1;
    }
    private ArrayList<MethodDec> methodList;

}
