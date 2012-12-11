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

    public String getName() {
        return name;
    }
    public void genK(PW pw){
      pw.print(name+" ");
    }
    abstract public String getCname();

    private String name;
}
