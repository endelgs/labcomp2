package ast;

abstract public class Expr {
    abstract public void genK( PW pw, boolean putParenthesis );
      // new method: the type of the expression
    abstract public Type getType();
}