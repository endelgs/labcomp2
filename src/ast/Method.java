/**
 * 379930 Endel Guimaraes Silva
 * 400564 Felipe Augusto Rosa
 */
package ast;

import java.util.ArrayList;
import lexer.Symbol;

public class Method {

    public Method( String name, Type type , Symbol qualifier, boolean isStatic) {
        this.name = name;
        this.type = type;
        this.qualifier = qualifier;
        this.isStatic = isStatic;
        this.statementList = new StatementList();
        this.paramList = new ParamList();
    }
    public void genK(PW pw){
      pw.print(name);
    }
    public String getName() { return name; }

    public Type getType() {
        return type;
    }

  public Symbol getQualifier() {
    return qualifier;
  }

  public void setQualifier(Symbol qualifier) {
    this.qualifier = qualifier;
  }

  public boolean isIsStatic() {
    return isStatic;
  }

  public void setIsStatic(boolean isStatic) {
    this.isStatic = isStatic;
  }

  public ArrayList<Statement> getStatementList() {
    return statementList.getStatementList();
  }

  public void setStatementList(ArrayList<Statement> statementList) {
    this.statementList.setStatementList(statementList);
  }

  public ParamList getParamList() {
    return paramList;
  }

  public void setParamList(ParamList paramList) {
    this.paramList = paramList;
  }

    private String name;
    private Type type;
    private Symbol qualifier;
    private boolean isStatic = false;
    private StatementList statementList;
    private ParamList paramList;
}