/**
 * 379930 Endel Guimaraes Silva
 * 400564 Felipe Augusto Rosa
 */
package ast;

public class LiteralStringExpr extends Expr {
    
    public LiteralStringExpr( String literalString ) { 
        this.literalString = literalString;
    }
    
    public void genK( PW pw, boolean putParenthesis ) {
        pw.print("\""+literalString+"\"");
    }
    public void genC( PW pw, boolean putParenthesis ) {
        pw.print("\""+literalString+"\"");
    }
    public Type getType() {
        return Type.stringType;
    }
    
    private String literalString;
}
