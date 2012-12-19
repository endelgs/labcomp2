package ast;

import lexer.Symbol;

public class MethodDec extends Method {

    public MethodDec( String name, Type type ,Symbol qualifier, boolean isStatic) {
        super(name, type, qualifier, isStatic);
    }
    public void genK(PW pw){
      pw.printIdent("");
      // Imprimindo os qualificadores de escopo (static/private/public)
      if(isIsStatic()){
        pw.print("static ");
      }
      pw.print(getQualifier().toString()+" ");
      
      // Imprimindo o tipo de retorno
      pw.print(getType().getName()+" ");
      
      // Imprimindo o nome do metodo
      super.genK(pw);
      
      // Imprimindo a lista de parametros
      pw.print("(");
      if(getParamList() != null)
        getParamList().genK(pw);
      pw.println("){");
      if(getStatementList() != null){
        for(int i = 0; i< getStatementList().size(); i++){
         Statement statement = getStatementList().get(i);
         statement.genK(pw);
        }
      }
      pw.printIdent("}\n");
    }

}