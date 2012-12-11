package ast;

public class ParenthesisExpr extends Expr {
    
    public ParenthesisExpr( Expr expr ) {
        this.expr = expr;
    }
    
    public void genK( PW pw, boolean putParenthesis ) {
        pw.print("(");
        expr.genK(pw, false);
        pw.printIdent(")");
    }
    
    public Type getType() {
        return expr.getType();
    }
    
    private Expr expr;
}