/**
 * 379930 Endel Guimaraes Silva
 * 400564 Felipe Augusto Rosa
 */
package ast;

abstract public class Type {

    public Type( String name ) {
        this.name = name;
    }

    public static Type booleanType = new BooleanType();
    public static Type intType = new IntType();
    public static Type stringType = new StringType();
    public static Type voidType = new VoidType();
    public static Type undefinedType = new UndefinedType();

    public String getCName(){
      
      return "_class_"+name;
    }
    public String getName() {

      return name;
    }
    public void genK(PW pw){
      pw.print(name+" ");
    }
    public void genC(PW pw){
      if(this instanceof StringType)
        pw.print("char * ");
      else
        pw.print(name+" ");
    }
//    abstract public String getCname();

    private String name;
}
