/**
 * 379930 Endel Guimaraes Silva
 * 400564 Felipe Augusto Rosa
 */
package ast;

public class InstanceVariable extends Variable {

    public InstanceVariable( String name, Type type ,boolean isStatic) {
        super(name, type);
        this.isStatic = isStatic;
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
      pw.print(getType().getName()+" ");
      pw.println("_NOMEDACLASSE_"+getName()+";");
    }
    private boolean isStatic;
}