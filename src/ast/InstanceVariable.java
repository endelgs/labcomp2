/**
 * 379930 Endel Guimaraes Silva
 * 400564 Felipe Augusto Rosa
 */
package ast;

public class InstanceVariable extends Variable {

    public InstanceVariable( String name, Type type ,boolean isStatic,ClassDec classDec) {
        super(name, type);
        this.isStatic = isStatic;
        this.classDec = classDec;
    }
    public boolean getIsStatic(){
      return isStatic;
    }
    public void genK(PW pw){
      if(isStatic)
        pw.print("static ");
      pw.print("private ");
      pw.print(getType().getName()+" ");
      pw.println(getName()+";");
    }
    public void genC(PW pw){
      pw.print(getType().getCName(true)+" ");
      
      pw.println(getCName()+";");
    }
    public String getCName(){
      return "_"+classDec.getName()+super.getCName();
    }
    private boolean isStatic;
    private ClassDec classDec;
}