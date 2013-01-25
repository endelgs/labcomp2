/**
 * 379930 Endel Guimaraes Silva
 * 400564 Felipe Augusto Rosa
 */
package ast;

public class Variable {

    public Variable( String name, Type type ) {
        this.name = name;
        this.type = type;
    }

    public String getName() { return name; }
    public String getCName() { 
      
      return "_"+name;//"_"+type.getName()+"_"+name; 
    }
    public Type getType() {
        return type;
    }
    public void genK(PW pw){
      pw.print(name);
    }
    public void genC(PW pw){
      pw.print(getCName());
    }

  public boolean isIsNull() {
    return isNull;
  }

  public void setIsNull(boolean isNull) {
    this.isNull = isNull;
  }
    
    private String name;
    private Type type;
    private boolean isNull;
}