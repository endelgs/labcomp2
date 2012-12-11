package ast;

import lexer.Symbol;

public class MethodDec extends Method {

    public MethodDec( String name, Type type ,Symbol qualifier, boolean isStatic) {
        super(name, type, qualifier, isStatic);
    }
    public void genK(PW pw){
      // Imprimindo os qualificadores de escopo (static/private/public)
      if(isIsStatic()){
        pw.print("static ");
      }
      pw.print(getQualifier().toString()+" ");
      
      // Imprimindo o tipo de retorno
      getType().genK(pw);
      
      // Imprimindo o nome do metodo
      super.genK(pw);
      
      // Imprimindo a lista de parametros
      pw.print("(");
      if(getParamList() != null)
        getParamList().genK(pw);
      pw.print("){");
      if(getStatementList() != null){
        while(getStatementList().iterator().hasNext()){
         Statement statement = getStatementList().iterator().next();
         statement.genK(pw);
        }
      }
      pw.print("}");
    }

}