/**
 * 379930 Endel Guimaraes Silva
 * 400564 Felipe Augusto Rosa
 */
package ast;

public class BooleanExpr extends Expr {

    public BooleanExpr( boolean value ) {
        this.value = value;
    }

    @Override
	public void genK( PW pw, boolean putParenthesis ) {
       pw.print( value ? "1" : "0" );
    }

    @Override
	public Type getType() {
        return Type.booleanType;
    }

    public static BooleanExpr True  = new BooleanExpr(true);
    public static BooleanExpr False = new BooleanExpr(false);

    private boolean value;
}
