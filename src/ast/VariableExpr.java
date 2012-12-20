package ast;

public class VariableExpr extends Expr {
    
    public VariableExpr( Variable v ) {
        this.v = v;
    }
    
    public void genK( PW pw, boolean putParenthesis ) {
      if(v instanceof InstanceVariable)
        pw.print("this.");
      pw.print( v.getName() );
    }
    
    public Type getType() {
        return v.getType();
    }

  public Variable getV() {
    return v;
  }
    
    private Variable v;
}