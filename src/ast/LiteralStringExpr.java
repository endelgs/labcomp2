package ast;

public class LiteralStringExpr extends Expr {
    
    public LiteralStringExpr( String literalString ) { 
        this.literalString = literalString;
    }
    
    public void genK( PW pw, boolean putParenthesis ) {
        pw.print("\""+literalString+"\"");
    }
    
    public Type getType() {
        return Type.stringType;
    }
    
    private String literalString;
}
