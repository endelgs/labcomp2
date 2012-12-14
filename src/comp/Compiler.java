package comp;

import ast.*;
import lexer.*;
import java.io.*;
import java.util.*;

public class Compiler {

  // compile must receive an input with an character less than
  // p_input.lenght
  public Program compile(char[] input, PrintWriter outError) {

    error = new CompilerError(new PrintWriter(outError));
    symbolTable = new SymbolTable();
    lexer = new Lexer(input, error);
    error.setLexer(lexer);


    Program program = null;
    //try {
      lexer.nextToken();
      if (lexer.token == Symbol.EOF) {
        error.show("Unexpected end of file");
      }
      program = program();
      if (lexer.token != Symbol.EOF) {
        program = null;
        error.show("End of file expected");
      }
    //} catch (Exception e) {
      // the below statement prints the stack of called methods.
      // of course, it should be removed if the compiler were
      // a production compiler.

      //e.printStackTrace();
      //program = null;
    //}

    return program;
  }
  // OK

  private Program program() {
    // Program ::=  ClassDec { ClassDec }
    ArrayList<ClassDec> classDecs = new ArrayList<ClassDec>();
    //classDecs.add(classDec());
    while (lexer.token == Symbol.CLASS) {
      // mutreta do zeh. nao sei se funfa por causa da passagem por referencia
      //currentClass = ;
      classDecs.add(classDec());
    }
    return new Program(classDecs);
  }
  // Constroi a AST
  // falta o tratamento de variaveis e metodos estaticos

  // OK
  private ClassDec classDec() { 
    // Note que os metodos desta classe nao correspondem exatamente as regras
    // da gramatica. Este metodo classDec, por exemplo, implementa
    // a producao ClassDec (veja abaixo) e partes de outras producoes.

    /* ClassDec ::=   ``class'' Id [ ``extends''  Id ]
     "{"   MemberList "}"
     MemberList ::= { Member }
     Member ::= InstVarDec | MethodDec
     InstVarDec ::= [ "static"  ] "private"  Type  IdList  ";"
     MethodDec ::= Qualifier ReturnType Id "("[ FormalParamDec ]  ")"
     "{"  StatementList "}"
     Qualifier} ::=  [ "static"  ] ( "private" |  "public" )
     */
    if (lexer.token != Symbol.CLASS) {
      error.show("'class' expected");
    }
    lexer.nextToken();
    if (lexer.token != Symbol.IDENT) {
      error.show(CompilerError.ident_expected);
    }
    String className = lexer.getStringValue();
    String superclassName = null;

    lexer.nextToken();
    if (lexer.token == Symbol.EXTENDS) {
      lexer.nextToken();
      if (lexer.token != Symbol.IDENT) {
        error.show(CompilerError.ident_expected);
      }
      superclassName = lexer.getStringValue();

      lexer.nextToken();
    }
    //lexer.nextToken();
    if (lexer.token != Symbol.LEFTCURBRACKET) {
      error.show("{ expected", true);
    }
    lexer.nextToken();
    ClassDec classDec = new ClassDec(className);
    currentClass = classDec;
    symbolTable.putInGlobal(className, classDec);
    
    if (superclassName != null) {
      classDec.setSuperclass(new ClassDec(superclassName));
    }
    // lendo os metodos e atributos da classe
    while (lexer.token == Symbol.PRIVATE
            || lexer.token == Symbol.PUBLIC
            || lexer.token == Symbol.STATIC) {

      Symbol qualifier;
      boolean isStatic = false;
      if(lexer.token == Symbol.STATIC){
          isStatic = true;
          lexer.nextToken();
      }
      
      switch (lexer.token) { 
        case PRIVATE:
          lexer.nextToken();
          qualifier = Symbol.PRIVATE;
          break;
        case PUBLIC:
          lexer.nextToken();
          qualifier = Symbol.PUBLIC;
          break;
        default:
          error.show("private, or public expected");
          qualifier = Symbol.PUBLIC;
      }
      Type t = type();
      if (lexer.token != Symbol.IDENT) {
        error.show("Identifier expected");
      }
      // Lendo o nome da variavel/metodo
      String name = lexer.getStringValue();
      lexer.nextToken();
      
      // Se for um "(", eh um metodo
      if (lexer.token == Symbol.LEFTPAR) {
        if (qualifier == Symbol.PUBLIC) {
          classDec.getPublicMethodList().addElement(methodDec(t, name, qualifier, isStatic));
          //lexer.nextToken();
        } else if(qualifier == Symbol.PRIVATE) {
          classDec.getPrivateMethodList().addElement(methodDec(t, name, qualifier, isStatic));
          //lexer.nextToken();
        }else{
          error.show("Invalid qualifier '"+lexer.getStringValue()+"'. public/private expected");
        }
      // Uma variavel de instancia nao pode ser publica
      } else if (qualifier != Symbol.PRIVATE) {
        error.show("Attempt to declare a public instance variable");
      } else { // faz a insercao direto na lista de variaveis 'por referencia'
        instanceVarDec(t, name, classDec.getInstanceVariableList(), isStatic);
      }
    }
    
    if (lexer.token != Symbol.RIGHTCURBRACKET) {
      error.show("public/private or \"}\" expected");
    }
    lexer.nextToken();
    return classDec;
  }
  // OK

  private void instanceVarDec(Type type, String name, InstanceVariableList instanceVariableList, boolean isStatic) {
    //   InstVarDec ::= [ "static"  ] "private"  Type  IdList  ";"
    
    // ao entrar nesse metodo, o programa ja analisou a primeira variavel da 
    // sequencia de declaracoes
    
    // Caso haja mais de uma declaracao
    while (lexer.token == Symbol.COMMA) {
      lexer.nextToken();
      if (lexer.token != Symbol.IDENT) {
        error.show("Identifier expected");
      }
      String variableName = lexer.getStringValue();
      instanceVariableList.addElement(new InstanceVariable(variableName, type, isStatic));
      lexer.nextToken();
    }
    // Termino das declaracoes
    if (lexer.token != Symbol.SEMICOLON) {
      error.show(CompilerError.semicolon_expected);
    }
    // Ao sair do metodo, o token jah esta DEPOIS das declaracoes
    lexer.nextToken();
  }

  // OK
  private MethodDec methodDec(Type type, String name, Symbol qualifier, boolean isStatic) {
    /*   MethodDec ::= Qualifier ReturnType Id "("[ FormalParamDec ]  ")"
     "{"  StatementList "}"
     */
    // quando entra nesse metodo, ja ta no "("
    MethodDec methodDec = new MethodDec(name, type, qualifier, isStatic);
    currentMethod = methodDec;
    lexer.nextToken(); // "parametros..."
    if (lexer.token != Symbol.RIGHTPAR) {
      methodDec.setParamList(formalParamDec());
    }
    // ")"
    if (lexer.token != Symbol.RIGHTPAR) {
      error.show(") expected");
    }
    
    lexer.nextToken(); // "{"
    if (lexer.token != Symbol.LEFTCURBRACKET) {
      error.show("{ expected");
    }

    lexer.nextToken(); // "statements..."
    methodDec.setStatementList(statementList());
    // "}"
    if (lexer.token != Symbol.RIGHTCURBRACKET) {
      error.show("} expected");
    }
    // Ao sair desse metodo, o compilador ja terminou de analisar a declaracao
    // do metodo e o cursor eh posicionada logo depois do ultimo "}"
    lexer.nextToken();
    symbolTable.removeLocalIdent();
    return methodDec;
  }
  // OK

  private LocalVarList localDec(Type type) {
    // LocalDec ::= Type IdList ";"
    LocalVarList localVarList = new LocalVarList();

    if (lexer.token != Symbol.IDENT) {
      error.show("Identifier expected");
    }
    localVarList.addElement(new Variable(lexer.getStringValue(), type));
    symbolTable.putInLocal(lexer.getStringValue(), new Variable(lexer.getStringValue(),type));
    lexer.nextToken();
    while (lexer.token == Symbol.COMMA) {
      lexer.nextToken();
      if (lexer.token != Symbol.IDENT) {
        error.show("Identifier expected");
      }
      localVarList.addElement(new Variable(lexer.getStringValue(), type));
      symbolTable.putInLocal(lexer.getStringValue(), new Variable(lexer.getStringValue(),type));
      lexer.nextToken();
    }
    return localVarList;
  }
  // OK

  private ParamList formalParamDec() {
    //  FormalParamDec ::= ParamDec { "," ParamDec }
    ParamList paramList = new ParamList();
    paramList.addElement(paramDec());
    while (lexer.token == Symbol.COMMA) {
      lexer.nextToken();
      paramList.addElement(paramDec());
    }
    return paramList;
  }

  // OK
  private Parameter paramDec() {
    // ParamDec ::= Type Id

    Type type = type();
    if (lexer.token != Symbol.IDENT) {
      error.show("Identifier expected");
    }
    String name = lexer.getStringValue();
    lexer.nextToken();
    Parameter p = new Parameter(name, type);
    symbolTable.putInLocal(name, p);
    return p;
  }

  // OK
  private Type type() {
    // Type ::= BasicType | Id
    Type result;

    switch (lexer.token) {
      case VOID:
        result = Type.voidType;
        break;
      case INT:
        result = Type.intType;
        break;
      case BOOLEAN:
        result = Type.booleanType;
        break;
      case STRING:
        result = Type.stringType;
        break;
      case IDENT:
        //# corrija: faca uma busca na TS para buscar a classe
        // corrigido!
        result = symbolTable.getInGlobal(lexer.getStringValue());
        if (result == null) {
          error.show("Class " + lexer.getStringValue() + " doesn't exist");
        }
        break;
      default:
        error.show("Type expected");
        result = Type.undefinedType;
    }
    lexer.nextToken();
    return result;
  }
  // OK

  private CompositeStatement compositeStatement() {
    // Quando entra nesse metodo, o token eh "{"
    lexer.nextToken();
    CompositeStatement compositeStatement = new CompositeStatement(statementList());
    if (lexer.token != Symbol.RIGHTCURBRACKET) {
      error.show("} expected");
    } else {
      // Ao sair do metodo, o token esta em "}" e deve avancar
      lexer.nextToken();
    }
    return compositeStatement;
  }
  // OK

  private ArrayList<Statement> statementList() {
    // CompStatement ::= "{" { Statement } "}"
    Symbol tk;
    ArrayList<Statement> statementList = new ArrayList<Statement>();
    // statements always begin with an identifier, if, read, write, ...
    while ((tk = lexer.token) != Symbol.RIGHTCURBRACKET
            && tk != Symbol.ELSE) {
      statementList.add(statement());
    }
//    lexer.nextToken();
    return statementList;
  }
  // OK

  private Statement statement() {
    /*
     Statement ::= Assignment ``;'' | IfStat |WhileStat
     |  MessageSend ``;''  | ReturnStat ``;''
     |  ReadStat ``;'' | WriteStat ``;'' | ``break'' ``;''
     | ``;'' | CompStatement | LocalDec
     */
    Statement statement = null;
    switch (lexer.token) {
      case THIS:
      case IDENT:
      case SUPER:
        statement = assignmentMessageSendLocalVarDecStatement();
        //lexer.nextToken();
        break;
      case INT:
        lexer.nextToken();
        statement = localDec(Type.intType);
        break;
      case BOOLEAN:
        lexer.nextToken();
        statement = localDec(Type.booleanType);
        break;
      case STRING:
        lexer.nextToken();
        statement = localDec(Type.stringType);
        break;
      case RETURN:
        statement = returnStatement();
        break;
      case READ:
        statement = readStatement();
        break;
      case WRITE:
        statement = writeStatement();
        break;
      case IF:
        statement = ifStatement();
        break;
      case BREAK:
        statement = breakStatement();
        break;
      case WHILE:
        statement = whileStatement();
        break;
      case SEMICOLON:
        statement = nullStatement();
        break;
      case LEFTCURBRACKET:
        statement = compositeStatement();
        break;
      default:
        error.show("Statement expected");
    }
    return statement;
  }
  // OK

  private ExprList getRealParameters() {
    ExprList anExprList = null;

    if (lexer.token != Symbol.LEFTPAR) {
      error.show("( expected");
    }
    lexer.nextToken();
    if (startExpr(lexer.token)) {
      anExprList = exprList();
    }
    if (lexer.token != Symbol.RIGHTPAR) {
      error.show(") expected");
    }
    lexer.nextToken();
    return anExprList;
  }
  // OK

  private WhileStatement whileStatement() {

    WhileStatement whileStatement = new WhileStatement();

    lexer.nextToken();
    if (lexer.token != Symbol.LEFTPAR) {
      error.show("( expected");
    }
    lexer.nextToken();
    whileStatement.setExpr(expr());
    if (lexer.token != Symbol.RIGHTPAR) {
      error.show(") expected");
    }
    lexer.nextToken();
    whileStatement.setStatement(statement());
    return whileStatement;
  }
  // OK

  private IfStatement ifStatement() {
    IfStatement ifStatement = new IfStatement();
    lexer.nextToken();
    if (lexer.token != Symbol.LEFTPAR) {
      error.show("( expected");
    }
    lexer.nextToken();
    ifStatement.setExpr(expr());
    if (lexer.token != Symbol.RIGHTPAR) {
      error.show(") expected");
    }
    lexer.nextToken();
    ifStatement.setIfStatements(statement());
    if (lexer.token == Symbol.ELSE) {
      lexer.nextToken();
      ifStatement.setElseStatements(statement());
    }
    return ifStatement;
  }
  // OK

  private ReturnStatement returnStatement() {
    ReturnStatement returnStatement = new ReturnStatement();
    lexer.nextToken();
    returnStatement.setExpr(expr());
    if (lexer.token != Symbol.SEMICOLON) {
      error.show(CompilerError.semicolon_expected);
    }
    lexer.nextToken();
    return returnStatement;
  }
  // quaaase OK

  private ReadStatement readStatement() {
    ReadStatement readStatement = new ReadStatement();
    lexer.nextToken();
    if (lexer.token != Symbol.LEFTPAR) {
      error.show("( expected");
    }
    lexer.nextToken();
    while (true) {
      if (lexer.token == Symbol.THIS) {
        lexer.nextToken();
        if (lexer.token != Symbol.DOT) {
          error.show(". expected");
        }
        lexer.nextToken();
      }
      if (lexer.token != Symbol.IDENT) {
        error.show(CompilerError.ident_expected);
      }

      String name = (String) lexer.getStringValue();
      readStatement.getVariableList().add(new Variable(name, Type.intType)); // precisa verificar esse intType depois

      lexer.nextToken();
      if (lexer.token == Symbol.COMMA) {
        lexer.nextToken();
      } else {
        break;
      }
    }

    if (lexer.token != Symbol.RIGHTPAR) {
      error.show(") expected");
    }
    lexer.nextToken();
    if (lexer.token != Symbol.SEMICOLON) {
      error.show(CompilerError.semicolon_expected);
    }
    lexer.nextToken();
    return readStatement;
  }
  // OK

  private WriteStatement writeStatement() {
    WriteStatement writeStatement = new WriteStatement();

    lexer.nextToken();
    if (lexer.token != Symbol.LEFTPAR) {
      error.show("( expected");
    }
    lexer.nextToken();
    writeStatement.setExprList(exprList());
    if (lexer.token != Symbol.RIGHTPAR) {
      error.show(") expected");
    }
    lexer.nextToken();
    if (lexer.token != Symbol.SEMICOLON) {
      error.show(CompilerError.semicolon_expected);
    }
    lexer.nextToken();
    return writeStatement;
  }
  // OK

  private BreakStatement breakStatement() {
    BreakStatement breakStatement = new BreakStatement();
    lexer.nextToken();
    if (lexer.token != Symbol.SEMICOLON) {
      error.show(CompilerError.semicolon_expected);
    }
    lexer.nextToken();
    return breakStatement;
  }
  // OK

  private NullStatement nullStatement() {
    lexer.nextToken();
    return new NullStatement();
  }
  // OK

  private ExprList exprList() {
    // ExpressionList ::= Expression { "," Expression }


    ExprList anExprList = new ExprList();
    anExprList.addElement(expr());
    while (lexer.token == Symbol.COMMA) {
      lexer.nextToken();
      anExprList.addElement(expr());
    }
    return anExprList;
  }
  // OK

  private Expr expr() {

    Expr left = simpleExpr();
    Symbol op = lexer.token;
    if (op == Symbol.EQ || op == Symbol.NEQ
            || op == Symbol.LE || op == Symbol.LT
            || op == Symbol.GE || op == Symbol.GT) {
      lexer.nextToken();
      Expr right = simpleExpr();
      left = new CompositeExpr(left, op, right);
    }
    return left;
  }
  // OK

  private Expr simpleExpr() {
    Symbol op;

    Expr left = term();
    while ((op = lexer.token) == Symbol.MINUS || op == Symbol.PLUS
            || op == Symbol.OR) {
      lexer.nextToken();
      Expr right = term();
      left = new CompositeExpr(left, op, right);
    }
    return left;
  }
  // OK

  private Expr term() {
    Symbol op;

    Expr left = signalFactor();
    while ((op = lexer.token) == Symbol.DIV || op == Symbol.MULT
            || op == Symbol.AND) {
      lexer.nextToken();
      Expr right = signalFactor();
      left = new CompositeExpr(left, op, right);
    }
    return left;
  }
  // OK

  private Expr signalFactor() {
    Symbol op;
    if ((op = lexer.token) == Symbol.PLUS || op == Symbol.MINUS) {
      lexer.nextToken();
      return new SignalExpr(op, factor());
    } else {
      return factor();
    }
  }

  // OK
  private Expr factor() {
    /*
     Factor ::= BasicValue | RightValue | MessageSend  | "(" Expression ")"
     | "!" Factor | "null" | ObjectCreation
     BasicValue ::= IntValue | BooleanValue | StringValue
     BooleanValue ::= "true" | "false"
     RightValue ::= "this" [ "." Id ] | Id
     MessageSend ::= ReceiverMessage "." Id "("  [ ExpressionList ] ")"
     ReceiverMessage ::=  "super" | Id | "this" | "this" "."  Id
     ObjectCreation ::= ``new" Id ``("  ``)"
     */

    Expr e;
    Variable aVariable;
    ClassDec aClass;
    MethodDec aMethod;
    InstanceVariable anInstanceVariable;

    switch (lexer.token) {
      case NOT:
        lexer.nextToken();
        e = expr();
        return new UnaryExpr(e, Symbol.NOT);
      case LITERALSTRING:
        String literalString = lexer.getLiteralStringValue();
        lexer.nextToken();
        return new LiteralStringExpr(literalString);
      case TRUE:
        lexer.nextToken();
        return BooleanExpr.True;
      case FALSE:
        lexer.nextToken();
        return BooleanExpr.False;
      case LEFTPAR:
        lexer.nextToken();
        e = expr();
        if (lexer.token != Symbol.RIGHTPAR) {
          error.show(") expected");
        }
        lexer.nextToken();
        return new ParenthesisExpr(e);
      case NULL:
        lexer.nextToken();
        return new NullExpr();
      case NUMBER:
        return number();
      // se o operador for NEW, estou enviando um novo objeto
      case NEW:
        lexer.nextToken();
        if (lexer.token != Symbol.IDENT) {
          error.show("Identifier expected");
        }

        String className = lexer.getStringValue();

        aClass = symbolTable.getInGlobal(className);
        // Mostra erro se tentar dar New em uma classe que não existe
        if (aClass == null) {
          error.show("Class " + className + " doesn't exist");
        }

        lexer.nextToken();
        if (lexer.token != Symbol.LEFTPAR) {
          error.show("( expected");
        }
        lexer.nextToken();
        if (lexer.token != Symbol.RIGHTPAR) {
          error.show(") expected");
        }
        lexer.nextToken();

        return new ObjectCreation(aClass);
      default:
        String variableName,
         methodName;
        ExprList exprList;
        /* there are seven cases to consider :
         super.m()
         this.x
         this
         this.m()
         this.x.m();
         x
         x.m()

         in which x is either a variable or
         an instance variable and m is a method
         */
        switch (lexer.token) {
          // OK
          case SUPER:
            className = currentClass.getName();

            // expression of the kind "super.m()"
            lexer.nextToken();
            if (lexer.token != Symbol.DOT) {
              error.show(". expected");
            }
            lexer.nextToken();
            if (lexer.token != Symbol.IDENT) {
              error.show(CompilerError.ident_expected);
            }
            methodName = lexer.getStringValue();
            lexer.nextToken();
            exprList = getRealParameters();
            aClass = currentClass.getSuperclass();
            if (aClass == null) {
              error.show("Class " + className + " doesn't have a superclass");
            }
            aMethod = aClass.getMethod(methodName);
            if (aMethod == null) {
              error.show("Class " + className + " doesn't have a method called " + methodName);
            }

            return new MessageSendToSuper(new Variable("super", aClass), aMethod, exprList, aClass);
          // OK
          case THIS:
            lexer.nextToken();
            if (lexer.token != Symbol.DOT) {
              // expression of the kind "this"
              error.show(". expected");
              // Verificando se o metodo nao eh estatico
              if (currentMethod.isIsStatic()) {
                error.show("Cannot use 'this' in static context");
              }

            } else {
              lexer.nextToken();
              if (lexer.token != Symbol.IDENT) {
                error.show(CompilerError.ident_expected);
              }
              // it may be method name or an instance variable
              String ident = lexer.getStringValue();
              lexer.nextToken();
              switch (lexer.token) {
                case LEFTPAR:
                  // expression of the kind "this.m()"

                  exprList = getRealParameters();

                  // SEM - Verificando se o metodo existe
                  aMethod = currentClass.getMethod(ident);
                  if (aMethod == null) {
                    error.show("Method " + ident + " doesn't exist");
                  }
                  // Checando os parametros
                  paramCompare(aMethod, exprList);
                  return new MessageSendToSelf(new Variable("this", currentClass), aMethod, exprList);

                // OK
                case DOT:
                  // expression of the kind "this.x.m()"
                  lexer.nextToken();
                  if (lexer.token != Symbol.IDENT) {
                    error.show(CompilerError.ident_expected);
                  }
                  methodName = lexer.getStringValue();
                  lexer.nextToken();
                  exprList = getRealParameters();
                  // verificando se o atributo existe
                  anInstanceVariable = currentClass.getVariable(ident);
                  if (anInstanceVariable == null) {
                    error.show("Trying to access undefined attribute '" + ident + "' in class '" + currentClass.getName() + "'");
                  }

                  // verifico se a variavel que estou tentando acessar eh um objeto, de fato
                  if (!(anInstanceVariable.getType() instanceof ClassDec)) {
                    error.show("Trying to access attribute '" + ident + "' in a non-object");
                  }

                  // verificando se a classe do objeto tem um metodo com ese nome
                  aClass = (ClassDec) anInstanceVariable.getType();
                  aMethod = aClass.getMethod(methodName);
                  if (aMethod == null) {
                    error.show("Trying to access undefined method '" + methodName + "' in class '" + currentClass.getName() + "'");
                  }

                  return new MessageSendToVariable(anInstanceVariable, aMethod, exprList);

                // OK
                default:
                  // expression of the kind "this.x"
                  // Verificando se existe o atributo x na classe atual
                  anInstanceVariable = currentClass.getVariable(ident);
                  if (anInstanceVariable == null) {
                    error.show("Trying to access undefined attribute '" + ident + "' in class '" + currentClass.getName() + "'");
                  }
                  return new VariableExpr(anInstanceVariable);
              }

            }
            break;
          case IDENT:
            variableName = lexer.getStringValue();
            lexer.nextToken();
            if (lexer.token != Symbol.DOT) {
              // expression of the kind "x"
              if ((aVariable = symbolTable.getInLocal(variableName)) == null) {
                error.show("Trying to access undefined attribute '" + variableName + "' in class '" + currentClass.getName() + "'");
              }
              return new VariableExpr(aVariable);

            } else {
              // expression of the kind "x.m()"
              lexer.nextToken();  // eat the dot
              switch (lexer.token) {
                case IDENT:
                  methodName = lexer.getStringValue();
                  lexer.nextToken();
                  exprList = getRealParameters();
                  if ((aVariable = symbolTable.getInLocal(variableName)) != null) {
                    // x is a variable
                    Type t = aVariable.getType();
                    if (!(t instanceof ClassDec)) {
                      error.show("Trying to access attribute '" + variableName + "' in a non-object");
                    }
                    aClass = (ClassDec) t;

                    // Verificando se o metodo existe
                    aMethod = aClass.getMethod(methodName);
                    if (aMethod == null) {
                      error.show("Trying to access undefined method '" + methodName + "' in class '" + aClass.getName() + "'");
                    }
                    // Checando os parametros
                    paramCompare(aMethod, exprList);

                    return new MessageSendToVariable(aVariable, aMethod, exprList);

                  } else {
                    // em "x.m()", x is not a variable. Should be a class name
                    if ((aClass = symbolTable.getInGlobal(variableName)) == null) {
                      error.show("Trying to access undefined attribute '" + variableName + "' in class '" + aClass.getName() + "'");
                    }

                    // nesta linha, verifique se methodName e um metodo estatico da
                    // classe aClass que pode aceitar como parmetros as expressoes 
                    // de exprList.Algo como (apenas o inicio):
                    aMethod = aClass.getMethod(methodName);
                    if (aMethod.isIsStatic()) {
                      // Checando os parametros
                      paramCompare(aMethod, exprList);
                    }
                    return new MessageSendStatic(new Variable("", aClass), aMethod, exprList);
                  }
                default:
                  error.show(CompilerError.ident_expected);
              }
            }
            break;
          default:
            error.show(CompilerError.ident_expected);
        }
        return null;
    }

  }

  private void paramCompare(Method aMethod, ExprList exprList) {
    // SEM - comparando o NUMERO de parametros passados vs. parametros 
    // que o metodo espera
    if (aMethod.getParamList().getSize() != exprList.getSize()) {
      error.show("Param count mismatch");
    }

    // SEM - comparando o TIPO dos parametros
    for (int i = 0; i< exprList.getSize(); i++) {
      Variable expParam = aMethod.getParamList().get(i);
      Expr pasParam = exprList.getElement(i);
      // verifico se o nome dos tipos eh igual
      if (!expParam.getType().getName().equals(pasParam.getType().getName())) {
        error.show("Param type mismatch: '" + expParam.getType().getName() + "' expected. '" + pasParam.getType().getName() + "' given.");
      }
    }
  }

  private NumberExpr number() {

    NumberExpr e = null;

    // the number value is stored in lexer.getToken().value as an object of Integer.
    // Method intValue returns that value as an value of type int.
    int value = lexer.getNumberValue();
    lexer.nextToken();
    return new NumberExpr(value);
  }

  private Statement assignmentMessageSendLocalVarDecStatement() {
    /*
     Assignment ::= LeftValue "=" Expression
     LeftValue} ::= [ "this" "." ] Id
     MessageSend ::= ReceiverMessage "." Id "("  [ ExpressionList ] ")"
     ReceiverMessage ::=  "super" | Id | "this" | "this" "."  Id
     LocalDec ::= Type IdList ";"
     */

    // an assignment, a message send or a local variable declaration

    String methodName, variableName;
    ExprList exprList;
    Statement result = null;
    MethodDec aMethod;
    ClassDec aClass;
    InstanceVariable anInstanceVariable;
    Variable variable;
    String id;
    /*
     there are eight possibilities:
     this.id()
     this.id = expr
     this.id.id()
     super.id()
     id = expr
     id.id()

     Id IdList  // Id is a type
     */

    switch (lexer.token) {
      case THIS:
        lexer.nextToken();
        if (lexer.token != Symbol.DOT) {
          error.show(". expected");
          // Verificando se o metodo nao eh estatico
          if (currentMethod.isIsStatic()) {
            error.show("Cannot use 'this' in static context");
          }
        }
        lexer.nextToken();
        if (lexer.token != Symbol.IDENT) {
          error.show(CompilerError.ident_expected);
        }
        String ident = lexer.getStringValue();
        lexer.nextToken();
        switch (lexer.token) {
          // OK
          case ASSIGN:
            // this.id = expr
            lexer.nextToken();
            Expr anExpr = expr();

            // Verifico se a variavel pra quem to atribuindo existe, de fato
            InstanceVariable instanceVariable = currentClass.getVariable(ident);
            if (instanceVariable == null) {
              error.show("Attempted to assign value to undefined attribute '" + ident + "' in class '" + currentClass.getName() + "'");
            }
            // Verificando se os tipos sao iguais
            if(!anExpr.getType().getName().equals(instanceVariable.getType().getName())){
              error.show("Type mismatch: tried to assign '"+anExpr.getType().getName()
                      +"' to '"+instanceVariable.getType().getName()+"'");
            }

            result = new AssignmentStatement(instanceVariable, anExpr);
            break;
          // OK
          case DOT:
            // this.id.id()
            lexer.nextToken();
            if (lexer.token != Symbol.IDENT) {
              error.show(CompilerError.ident_expected);
            }
            methodName = lexer.getStringValue();
            lexer.nextToken();
            exprList = getRealParameters();
            
            // verificando se o atributo existe
            anInstanceVariable = currentClass.getVariable(ident);
            if (anInstanceVariable == null) {
              error.show("Trying to access undefined attribute '" + ident + "' in class '" + currentClass.getName() + "'");
            }

            // verifico se a variavel que estou tentando acessar eh um objeto, de fato
            if (!(anInstanceVariable.getType() instanceof ClassDec)) {
              error.show("Trying to access attribute '" + ident + "' in a non-object");
            }

            // verificando se a classe do objeto tem um metodo com ese nome
            aClass = (ClassDec) anInstanceVariable.getType();
            aMethod = aClass.getMethod(methodName);
            if (aMethod == null) {
              error.show("Trying to access undefined method '" + methodName + "' in class '" + currentClass.getName() + "'");
            }
            result = new MessageSendStatement(new MessageSendToVariable(anInstanceVariable, aMethod, exprList));
            break;
          case LEFTPAR:
            // this.id()
            exprList = getRealParameters();
            // SEM - Verificando se o metodo existe
            aMethod = currentClass.getMethod(ident);
            if (aMethod == null) {
              error.show("Method " + ident + " doesn't exist");
            }
            // Checando os parametros
            paramCompare(aMethod, exprList);
            result = new MessageSendStatement(new MessageSendToSelf(new Variable("this", currentClass), aMethod, exprList));
            break;
          default:
            error.show(CompilerError.ident_expected);
        }
        break;
      // OK
      case SUPER:
        // super.id()
        lexer.nextToken();
        if (lexer.token != Symbol.DOT) {
          error.show(". expected");
        }
        lexer.nextToken();
        if (lexer.token != Symbol.IDENT) {
          error.show(CompilerError.ident_expected);
        }
        methodName = lexer.getStringValue();
        lexer.nextToken();
        exprList = getRealParameters();
        aClass = currentClass.getSuperclass();
        if (aClass == null) {
          error.show("Class " + aClass.getName() + " doesn't have a superclass");
        }
        aMethod = aClass.getMethod(methodName);
        if (aMethod == null) {
          error.show("Class " + aClass.getName() + " doesn't have a method called " + methodName);
        }
        paramCompare(aMethod, exprList);
        result = new MessageSendStatement(new MessageSendToSuper(new Variable("super", aClass), aMethod, exprList, aClass));

        break;
      // se encontrar um identificador, pode ser uma atribuicao ou declaracao de
      // variavel (com tipo definido pelo usuario)
      case IDENT:
        variableName = lexer.getStringValue();
        lexer.nextToken();
        switch (lexer.token) {
          // OK
          case ASSIGN:
            // id = expr
            lexer.nextToken();
            Expr anExpr = expr();
            // verificando se a variavel pra quem estou atribuindo
            // realmente existe
            variable = symbolTable.getInLocal(variableName);
            if(variable == null)
              error.show("Variable '"+variableName+"' doesn't exist in this context");
            if(!variable.getType().getName().equals(anExpr.getType().getName())){
              error.show("Type mismatch: tried to assign '"+anExpr.getType().getName()
                      +"' to '"+variable.getType().getName()+"'");
            }
            
            result = new AssignmentStatement(variable,anExpr);
            break;
          case IDENT:
           id = lexer.getStringValue();
            // id id;
            // variableName id
            // variable = symbolTable.getIn
            // verificando se o TIPO existe
            aClass = symbolTable.getInGlobal(variableName);
            if(aClass == null){
              error.show("Class '"+variableName+"' doesn't exist");
            }
            variable = symbolTable.getInLocal(id);
            if(variable != null){
              error.show("Redeclaration of variable '"+id+"'");
            }
            symbolTable.putInLocal(id, new Variable(id,aClass));
            // variableName must be the name of a class
            // replace null in the statement below by
            // a point to the class named variableName.
            // A search in the symbol table is necessary.
            result = localDec(aClass);
            //lexer.nextToken();
            break;
          case DOT:
            // id.id()
            lexer.nextToken();
            methodName = lexer.getStringValue();
            lexer.nextToken();
            exprList = getRealParameters();
            //lexer.nextToken();
            // verificando se a variavel existe
            variable = symbolTable.getInLocal(variableName);
            if(variable == null)
              error.show("Undefined variable '"+variableName+"'");
            
            // verificando se o metodo existe na classe daquela variavel
            aMethod = ((ClassDec)(variable.getType())).getMethod(methodName);
            if(aMethod == null)
              error.show("Call to undefined method '"+methodName+"'");
            
            // comparando os parametros
            //paramCompare(aMethod, exprList);
            result = new MessageSendStatement(
             new MessageSendToVariable( variable,
             aMethod, exprList ) );
            break;
          default:
            error.show(". or = expected");
        }
        break;
      default:
        error.show("'this', 'super', basic type or identifier expected");
    }

    if (lexer.token != Symbol.SEMICOLON) {
      error.show(CompilerError.semicolon_expected);
    }
    //lexer.nextToken();
    return result;
  }

  private boolean startExpr(Symbol token) {

    return lexer.token == Symbol.FALSE
            || lexer.token == Symbol.TRUE
            || lexer.token == Symbol.NOT
            || lexer.token == Symbol.THIS
            || lexer.token == Symbol.NUMBER
            || lexer.token == Symbol.SUPER
            || lexer.token == Symbol.LEFTPAR
            || lexer.token == Symbol.NULL
            || lexer.token == Symbol.IDENT
            || lexer.token == Symbol.LITERALSTRING;

  }
  private SymbolTable symbolTable;
  private Lexer lexer;
  private CompilerError error;
  private ClassDec currentClass;
  private MethodDec currentMethod;
}
