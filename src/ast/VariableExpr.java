/**
 * 379930 Endel Guimaraes Silva
 * 400564 Felipe Augusto Rosa
 */
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
  @Override
    public void genC( PW pw, boolean putParenthesis ) {
      if(v instanceof InstanceVariable)  
        pw.print("this->"+v.getCName());
      else
        pw.print( v.getCName() );
    }
    public Type getType() {
        return v.getType();
    }

  public Variable getV() {
    return v;
  }
    
    private Variable v;
}