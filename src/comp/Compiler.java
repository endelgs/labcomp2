/**
 * 379930 Endel Guimaraes Silva
 * 400564 Felipe Augusto Rosa
 */
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
    try {
    lexer.nextToken();
    if (lexer.token == Symbol.EOF) {
      error.show("Unexpected end of file");
    }
    program = program();
    if (lexer.token != Symbol.EOF) {
      program = null;
      error.show("End of file expected");
    }
    } catch (Exception e) {
    // the below statement prints the stack of called methods.
    // of course, it should be removed if the compiler were
    // a production compiler.

    //e.printStackTrace();
    program = null;
    }

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
    if (className.equals(superclassName)) {
      error.show("Class '" + className + "' cannot inherit from itself");
    }
    //lexer.nextToken();
    if (lexer.token != Symbol.LEFTCURBRACKET) {
      error.show("{ expected", true);
    }
    lexer.nextToken();
    ClassDec classDec = new ClassDec(className);
    ClassDec superClass = null;
    currentClass = classDec;
    symbolTable.putInGlobal(className, classDec);

    // Caso a classe atual seja herdada, faco a busca na tabela de simbolos, 
    // verifico se ela existe e seto ela como superclasse
    if (superclassName != null) {
      superClass = symbolTable.getInGlobal(superclassName);
      if (superClass == null) {
        error.show("Trying to extend undefined class '" + superclassName + "'");
      }
      classDec.setSuperclass(superClass);
    }
    // lendo os metodos e atributos da classe
    while (lexer.token == Symbol.PRIVATE
            || lexer.token == Symbol.PUBLIC
            || lexer.token == Symbol.STATIC) {

      Symbol qualifier;
      boolean isStatic = false;
      if (lexer.token == Symbol.STATIC) {
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
        error.show("Palavra reservada utilizada como variavel");
      }
      // Lendo o nome da variavel/metodo
      String name = lexer.getStringValue();
      lexer.nextToken();

      // Se for um "(", eh um metodo
      if (lexer.token == Symbol.LEFTPAR) {
        if (qualifier == Symbol.PUBLIC) {
          classDec.getPublicMethodList().addElement(methodDec(t, name, qualifier, isStatic,classDec));
        } else if (qualifier == Symbol.PRIVATE) {
          classDec.getPrivateMethodList().addElement(methodDec(t, name, qualifier, isStatic,classDec));
        } else {
          error.show("Invalid qualifier '" + lexer.getStringValue() + "'. public/private expected");
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

    String variableName = lexer.getStringValue();
    InstanceVariable iv = instanceVariableList.searchVariable(variableName);
    if (iv != null && (iv.getIsStatic() == isStatic)) {
      error.show("Redeclaration of instance variable '" + variableName + "'");
    }
    instanceVariableList.addElement(new InstanceVariable(variableName, type, isStatic));

    // Caso haja mais de uma declaracao
    while (lexer.token == Symbol.COMMA) {
      lexer.nextToken();
      if (lexer.token != Symbol.IDENT) {
        error.show("Identifier expected");
      }
      variableName = lexer.getStringValue();
      iv = instanceVariableList.searchVariable(variableName);

      error.show("Redeclaration of instance variable '" + variableName + "'");
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
  private MethodDec methodDec(Type type, String name, Symbol qualifier, boolean isStatic,ClassDec classDec) {
    /*   MethodDec ::= Qualifier ReturnType Id "("[ FormalParamDec ]  ")"
     "{"  StatementList "}"
     */
    // quando entra nesse metodo, ja ta no "("
    MethodDec methodDec = new MethodDec(name, type, qualifier, isStatic,classDec);
    MethodDec aMethod = null;
    currentMethod = methodDec;
    lexer.nextToken(); // "parametros..."
    if (lexer.token != Symbol.RIGHTPAR) {
      methodDec.setParamList(formalParamDec());
      //signatureCompare(methodDec,aMethod);
    }
    if (currentClass.getVariable(name) != null) {
      error.show("Redeclaration of member '" + name + "'");
    }
    // Verificando polimorfismo por sobreposicao
    aMethod = currentClass.checkOverrideMethod(name);
    if (aMethod != null) { // metodo existe
      
      // Evito comparacao de assinaturas caso algum dos metodos seja estatico
      if(!(aMethod.isIsStatic() && methodDec.isIsStatic())){
        if (!methodDec.getType().equals(aMethod.getType())) {
          error.show("Cannot override method. Superclass' method '" + aMethod.getName()
                  + "' has return type '" + aMethod.getType().getName()
                  + "' and redefined as '" + methodDec.getType().getName() + "'");
        }
        // Comparando as assinaturas dos metodos
        signatureCompare(aMethod, methodDec);
      }
    }
    aMethod = currentClass.getMethod(name, true, false,true);
    if (aMethod != null) {
      if(aMethod.isIsStatic() == methodDec.isIsStatic())
        error.show("Redeclaration of method '" + name + "'");
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

    // Verificando se metodos nao-void tem return
    if (!(type instanceof VoidType) && returnStatement == null) {
      error.show("Return statement missing");
    }
    if ((type instanceof VoidType) && returnStatement != null) {
      error.show("Return statement inside method without return type");
    }
    returnStatement = null;

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
    // Buscando na tabela local pra ver se existe uma variavel declarada com o mesmo nome
    Variable variable = symbolTable.getInLocal(lexer.getStringValue());
    if (variable != null) {
      error.show("Redeclaration of variable '" + lexer.getStringValue() + "'");
    }

    localVarList.addElement(new Variable(lexer.getStringValue(), type));
    symbolTable.putInLocal(lexer.getStringValue(), new Variable(lexer.getStringValue(), type));
    lexer.nextToken();
    while (lexer.token == Symbol.COMMA) {
      lexer.nextToken();
      if (lexer.token != Symbol.IDENT) {
        error.show("Identifier expected");
      }
      if (symbolTable.getInLocal(lexer.getStringValue()) != null) {
        error.show("Redeclaration of variable '" + lexer.getStringValue() + "'");
      }
      localVarList.addElement(new Variable(lexer.getStringValue(), type));
      symbolTable.putInLocal(lexer.getStringValue(), new Variable(lexer.getStringValue(), type));
      lexer.nextToken();
    }
    if (lexer.token != Symbol.SEMICOLON) {
      error.show("';' expected");
    }
//    lexer.nextToken();
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
          //error.show("Class " + lexer.getStringValue() + " doesn't exist");
          error.show("Tipo 'e uma variavel");
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
        //lexer.nextToken();
        statement = assignmentMessageSendLocalVarDecStatement();
        break;
      case INT:
        lexer.nextToken();
        statement = localDec(Type.intType);
        lexer.nextToken();
        break;
      case BOOLEAN:
        lexer.nextToken();
        statement = localDec(Type.booleanType);
        lexer.nextToken();
        break;
      case STRING:
        lexer.nextToken();
        statement = localDec(Type.stringType);
        lexer.nextToken();
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
    currentStatement = whileStatement;
    whileStack.push(whileStatement);

    lexer.nextToken();
    if (lexer.token != Symbol.LEFTPAR) {
      error.show("( expected");
    }
    lexer.nextToken();

    // Verifico se a expressao do while eh booleana
    Expr expr = expr();
    //if(!(expr instanceof BooleanExpr) || !(expr instanceof CompositeExpr))
    if (!(expr instanceof BooleanExpr || expr.getType() instanceof BooleanType)) {
      error.show("While expression must be boolean");
    }

    whileStatement.setExpr(expr);
    if (lexer.token != Symbol.RIGHTPAR) {
      error.show(") expected");
    }
    lexer.nextToken();
    whileStatement.setStatement(statement());
    currentStatement = null;
    whileStack.pop();
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
    this.returnStatement = returnStatement;
    lexer.nextToken();
    Expr expr = expr();
    // Verificando se o tipo de retorno do metodo bate com a expressao retornada
    // CASO 1: tipos primitivos
    if (expr.getType() instanceof IntType || expr.getType() instanceof BooleanType || expr.getType() instanceof StringType) {
      if (!expr.getType().equals(currentMethod.getType())) {
        error.show("Type mismatch. Expecting return type to be '" + currentMethod.getType().getName()
                + "' or one of its subclasses. '" + expr.getType().getName() + "' given");
      }
      // CASO 2: tipos complexos
    } else if (!((ClassDec) expr.getType()).isChildOf(currentMethod.getType().getName())) {
      error.show("Type mismatch. Expecting return type to be '" + currentMethod.getType().getName()
              + "' or one of its subclasses. '" + expr.getType().getName() + "' given");
    }
    returnStatement.setExpr(expr);
    if (lexer.token != Symbol.SEMICOLON) {
      error.show(CompilerError.semicolon_expected);
    }
    lexer.nextToken();
    return returnStatement;
  }
  // quaaase OK

  private ReadStatement readStatement() {
    ReadStatement readStatement = new ReadStatement();
    currentStatement = readStatement;
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
      Variable v = symbolTable.getInLocal(name);
      if (v == null) {
        v = currentClass.getVariable(name);
        if (v == null) {
          error.show("Undefined variable '" + name + "'");
        }
      }
      if (v.getType() instanceof ClassDec) {
        error.show("Cannot read to an object");
      }
      // Nao pode ler pra dentro de variaveis boolean
      if (v.getType() instanceof BooleanType) {
        error.show("Cannot read to a boolean variable");
      }

      readStatement.getVariableList().add(v); // precisa verificar esse intType depois

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
    currentStatement = null;
    return readStatement;
  }
  // OK

  private WriteStatement writeStatement() {
    WriteStatement writeStatement = new WriteStatement();
    currentStatement = writeStatement;
    lexer.nextToken();
    if (lexer.token != Symbol.LEFTPAR) {
      error.show("( expected");
    }
    lexer.nextToken();
    ExprList exprList = exprList();
    for (int i = 0; i < exprList.getSize(); i++) {
      if (exprList.getList().get(i).getType() instanceof BooleanType) {
        error.show("Cannot write boolean values");
      }
    }
    writeStatement.setExprList(exprList);
    if (lexer.token != Symbol.RIGHTPAR) {
      error.show(") expected");
    }
    lexer.nextToken();
    if (lexer.token != Symbol.SEMICOLON) {
      error.show(CompilerError.semicolon_expected);
    }
    lexer.nextToken();
    //writeStatement = null;
    currentStatement = null;
    return writeStatement;
  }
  // OK

  private BreakStatement breakStatement() {
    BreakStatement breakStatement = new BreakStatement();
    // Verificando se o break esta dentro de um while
    if (whileStack.empty()) {
      error.show("Break statement must be within a while statement");
    }
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
    Expr expr = expr();
    if (expr.getType() instanceof ClassDec) {
      if (currentStatement instanceof ReadStatement) {
        error.show("Cannot read to an object");
      }
      if (currentStatement instanceof WriteStatement) {
        error.show("Cannot write an object");
      }
    }

    anExprList.addElement(expr);
    while (lexer.token == Symbol.COMMA) {
      lexer.nextToken();
      expr = expr();
      if (expr.getType() instanceof ClassDec) {
        if (currentStatement instanceof ReadStatement) {
          error.show("Cannot read to an object");
        }
        if (currentStatement instanceof WriteStatement) {
          error.show("Cannot write to an object");
        }
      }
      anExprList.addElement(expr);
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
      if (right.getType() instanceof ClassDec || left.getType() instanceof ClassDec) {
        if (!((right.getType() instanceof ClassDec && left.getType() instanceof UndefinedType)
                || (left.getType() instanceof ClassDec && right.getType() instanceof UndefinedType))) {
          if(!(((VariableExpr) right).getV().isIsNull() ^ ((VariableExpr) right).getV().isIsNull()))
            error.show("Cannot apply operator '" + op + "' to pointers");
        }
      }

      left = new CompositeExpr(left, op, right);
    }
    op = lexer.token;
    if (op == Symbol.EQ || op == Symbol.NEQ
            || op == Symbol.LE || op == Symbol.LT
            || op == Symbol.GE || op == Symbol.GT) {
      error.show("Cannot use multiple comparison operators");
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
      if (op == Symbol.MINUS || op == Symbol.PLUS) {
        if (!(right.getType() instanceof IntType && left.getType() instanceof IntType)) {
          error.show("Operator '" + op + "' cannot be applied to given types");
        }
      }

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
      // Verificando se os operadores sao validos
      if (op != Symbol.AND && !(right.getType() instanceof IntType && left.getType() instanceof IntType)) {
        error.show("Operator '" + op + "' cannot be applied to given types");
      }
      left = new CompositeExpr(left, op, right);
    }
    return left;
  }
  // OK

  private Expr signalFactor() {
    Symbol op;
    if ((op = lexer.token) == Symbol.PLUS || op == Symbol.MINUS) {
      lexer.nextToken();
      Expr factor = factor();
      if (!(factor instanceof NumberExpr)
              && !(factor instanceof VariableExpr && factor.getType() instanceof IntType)) {
        error.show("Operator '" + op + "' cannot be applied to '" + factor.getType().getName() + "'");
      }
      return new SignalExpr(op, factor);
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
        if (!(e.getType() instanceof BooleanType)) {
          error.show("Operator '" + Symbol.NOT + "' cannot be applied to '" + e.getType().getName() + "'");
        }
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
            if (lexer.token == Symbol.SEMICOLON
                    || lexer.token == Symbol.COMMA
                    || lexer.token == Symbol.RIGHTPAR) {
              return new ThisExpr(currentClass);
            }

            if (lexer.token != Symbol.DOT) {
              // Verificando se o metodo nao eh estatico
              if (currentMethod.isIsStatic()) {
                error.show("Cannot use 'this' in static context");
              }
              // expression of the kind "this"
              //error.show(". expected");
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
                  aMethod = currentClass.getMethod(ident, true, true,true);
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
                  // Caso seja chamada pra metodo
                  if (lexer.token == Symbol.LEFTPAR) {
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
                        if (currentMethod.getName().equals(methodName)) {
                          aMethod = currentMethod;
                        } else {
                          error.show("Trying to access undefined method '" + methodName + "' in class '" + aClass.getName() + "'");
                        }
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
                      aMethod = aClass.getMethod(methodName, true, true,true);
                      if (aMethod.isIsStatic()) {
                        // Checando os parametros
                        paramCompare(aMethod, exprList);
                        
                      }
                      return new MessageSendStatic(aClass, aMethod, exprList);

                    }// if ((aVariable = symbolTable.getInLocal(variableName)) != null)
                  // Caso esteja acessando uma variavel estatica
                  } else {
                    ClassDec cd = symbolTable.getInGlobal(variableName);
                    if(cd == null)
                      error.show("Attempt to access property '"+methodName+"' in undefined class '"+variableName+"'");
                    Variable v = cd.getVariable(methodName);
                    return new StaticVarAccess(v, cd);
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

  private void signatureCompare(Method oldMethod, Method newMethod) {
    if (oldMethod.getParamList().getSize() != newMethod.getParamList().getSize()) {
      error.show("Cannot redefine method. Param count doesn't match with superclass");
    }
    for (int i = 0; i < oldMethod.getParamList().getSize(); i++) {
      if (!oldMethod.getParamList().get(i).getType().getName().equals(newMethod.getParamList().get(i).getType().getName())) {
        error.show("Cannot redefine method. Parameters don't match");
      }
    }
  }

  private void paramCompare(Method aMethod, ExprList exprList) {
    // Se o metodo nao tiver parametros E nao houver parametros a serem passados
    // simplesmente retorno
    if (aMethod.getParamList().getSize() == 0 && exprList == null) {
      return;
    }

    // SEM - comparando o NUMERO de parametros passados vs. parametros 
    // que o metodo espera
    if ((aMethod.getParamList().getSize() == 0 && exprList == null)
            || (aMethod.getParamList().getSize() != exprList.getSize())) {
      error.show("Param count mismatch");
    }

    // SEM - comparando o TIPO dos parametros
    for (int i = 0; i < exprList.getSize(); i++) {
      Variable expParam = aMethod.getParamList().get(i);
      Expr pasParam = exprList.getElement(i);
      // verifico se os parametros sao do mesmo tipo ou se tem a mesma heranca
      if ((pasParam.getType() instanceof ClassDec)
              && !((ClassDec) pasParam.getType()).isChildOf(expParam.getType().getName())) {
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
    if(lexer.getLineNumber() == 18){
      int i = 0;
    }
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
            currentAssignment = new AssignmentStatement(null, null);
            // this.id = expr
            lexer.nextToken();
            Expr anExpr = expr();

            // Verifico se a variavel pra quem to atribuindo existe, de fato
            InstanceVariable instanceVariable = currentClass.getVariable(ident);
            if (instanceVariable == null) {
              error.show("Attempted to assign value to undefined attribute '" + ident + "' in class '" + currentClass.getName() + "'");
            }
            // Verificando se nao estou usando this em um metodo estatico
            if (currentMethod.isIsStatic()) {
              error.show("Cannot use this in static context");
            }

            // Verificando se os tipos sao iguais
            if (!anExpr.getType().getName().equals(instanceVariable.getType().getName())) {
              error.show("Type mismatch: tried to assign '" + anExpr.getType().getName()
                      + "' to '" + instanceVariable.getType().getName() + "'");
            }

            result = new AssignmentStatement(instanceVariable, anExpr);
            currentAssignment = null;
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
            aMethod = aClass.getMethod(methodName, true, true,true);
            if (aMethod == null) {
              if (currentMethod.getName().equals(methodName)) {
                aMethod = currentMethod;
              } else {
                error.show("Trying to access undefined method '" + methodName + "' in class '" + currentClass.getName() + "'");
              }
            }
            result = new MessageSendStatement(new MessageSendToSelf(anInstanceVariable, aMethod, exprList));
            break;
          case LEFTPAR:
            // this.id()
            exprList = getRealParameters();
            // SEM - Verificando se o metodo existe
            aMethod = currentClass.getMethod(ident, true, true,true);
            if (aMethod == null) {
              error.show("Method " + ident + " doesn't exist");
            }
            if (currentMethod.isIsStatic()) {
              error.show("Cannot use this in static context");
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
          error.show("Class " + currentClass.getName() + " doesn't have a superclass");
        }
        aMethod = aClass.getMethod(methodName);
        if (aMethod == null) {
          error.show("Attempted to call undefined method " + aClass.getName() + "::" + methodName);
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
            currentAssignment = new AssignmentStatement(null, null);
            lexer.nextToken();
            Expr anExpr = expr();
            // verificando se a variavel pra quem estou atribuindo
            // realmente existe
            variable = symbolTable.getInLocal(variableName);
            if (variable == null) {
              error.show("Variable '" + variableName + "' doesn't exist in this context");
            }

            if (anExpr instanceof NumberExpr && !(variable.getType() instanceof IntType)) {
              error.show("Cannot assign value of primitive type '" + anExpr.getType().getName()
                      + "' to '" + variable.getType().getName() + "'");
            }
            // Verificando se nao eh expressao do tipo null
            if (!(anExpr.getType() instanceof UndefinedType)) {
              // Verificando tipos primitivos
              if (anExpr.getType() instanceof StringType) {
              } else if (anExpr.getType() instanceof VoidType) {
                if (currentAssignment != null) {
                  error.show("Cannot assign to '" + variableName + "': method returns void.");
                }
              } else if (anExpr.getType() instanceof BooleanType) {
                if (!(variable.getType() instanceof BooleanType)) {
                  error.show("Type mismatch: tried to assign '" + anExpr.getType().getName()
                          + "' to '" + variable.getType().getName() + "'");
                }
              } else if (!(anExpr.getType() instanceof IntType)
                      && !((ClassDec) anExpr.getType()).isChildOf(variable.getType().getName())) {
                error.show("Type mismatch: tried to assign '" + anExpr.getType().getName()
                        + "' to '" + variable.getType().getName() + "'");
              }
            } else if (!(variable.getType() instanceof ClassDec)) {
              error.show("Cannot assign null to '" + variable.getType().getName() + "'");
            }
            if(anExpr instanceof NullExpr)
              variable.setIsNull(true);
            else
              variable.setIsNull(false);
            result = new AssignmentStatement(variable, anExpr);
            currentAssignment = null;
            break;
          case IDENT:
            id = lexer.getStringValue();
            // id id;
            // variableName id
            // variable = symbolTable.getIn
            // verificando se o TIPO existe
            aClass = symbolTable.getInGlobal(variableName);
            if (aClass == null) {
              error.show("Class '" + variableName + "' doesn't exist");
            }
            //symbolTable.putInLocal(id, new Variable(id,aClass));
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
            if(lexer.token == Symbol.LEFTPAR){
              exprList = getRealParameters();
              
              // Verificando se o metodo eh estatico
              aClass = symbolTable.getInGlobal(variableName);
              if(aClass != null){
                aMethod = aClass.getMethod(methodName);
                if(aMethod == null){
                  error.show("Attempt to call to undefined method ");
                }
                result = new MessageSendStatement(new MessageSendStatic(aClass, aMethod, exprList));
                break;
              // Metodo nao-estatico
              }else{
                // verificando se a variavel existe
                variable = symbolTable.getInLocal(variableName);
                if (variable == null) {
                  error.show("Undefined variable '" + variableName + "'");
                }

                // verificando se o metodo existe na classe daquela variavel
                if (!(variable.getType() instanceof ClassDec)) {
                  error.show("Cannot call method '" + methodName + "' in a non-object");
                }
                aMethod = ((ClassDec) (variable.getType())).getMethod(methodName,false,true,false);
                if (aMethod == null) {
                  if (currentMethod.getName().equals(methodName)) {
                    aMethod = currentMethod;
                  } else {
                    error.show("Call to undefined method '" + methodName + "'");
                  }
                }
                if (aMethod != null && aMethod.isIsStatic()) {
                  error.show("Trying to call static method from non-static context");
                }
                if (!(aMethod.getType() instanceof VoidType) && currentAssignment == null) {
                  error.show("Return value is not assigned to any variable");
                }
                // comparando os parametros
                //paramCompare(aMethod, exprList);
                result = new MessageSendStatement(
                        new MessageSendToVariable(variable,
                        aMethod, exprList));
                break;
              }
            }else{ // Acessando variavel estatica
              ClassDec cd = symbolTable.getInGlobal(variableName);
              if(cd == null)
                error.show("Attempt to access property '"+methodName+"' in undefined class '"+variableName+"'");
              Variable v = cd.getVariable(methodName);
              if(lexer.token != Symbol.ASSIGN){
                error.show("Assignment expected");
              }
              lexer.nextToken();
              result = new StaticAssignmentStatement(cd,v,expr());
              break;
            }
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
    lexer.nextToken();
    return result;
  }

  private boolean hasMethod(String name) {
    return false;
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
  private ReturnStatement returnStatement = null;
  private Statement currentStatement = null;
  private AssignmentStatement currentAssignment = null;
  private Stack<WhileStatement> whileStack = new Stack<WhileStatement>();
}
