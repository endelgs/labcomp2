/**
 * 379930 Endel Guimaraes Silva
 * 400564 Felipe Augusto Rosa
 */
package ast;

public class NumberExpr extends Expr {
    
    public NumberExpr( int value ) { 
        this.value = value;
    }
    
    public int getValue() {
        return value;
    }
    public void genK( PW pw, boolean putParenthesis ) {
        pw.printIdent(value + "");
    }
    
    public Type getType() {
        return Type.intType;
    }
    
    private int value;
}
