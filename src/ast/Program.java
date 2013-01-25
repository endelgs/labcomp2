/**
 * 379930 Endel Guimaraes Silva
 * 400564 Felipe Augusto Rosa
 */
package ast;

import java.util.*;

public class Program {

  public Program(ArrayList<ClassDec> classList) {
    this.classList = classList;
  }

  public void genK(PW pw) {
    for (int i = 0; i < classList.size(); i++) {
      classList.get(i).genK(pw);
    }
  }
  public void genC(PW pw){
    pw.println("// CABECALHOS DEFAULT");
    pw.println("");
    pw.println("#include <malloc.h>");
    pw.println("#include <stdlib.h>");
    pw.println("#include <stdio.h>");
    pw.println("typedef int boolean;");
    pw.println("#define true 1");
    pw.println("#define false 0");
    pw.println("typedef void (*Func)();");
    pw.println("");
    
    for (int i = 0; i < classList.size(); i++) {
      classList.get(i).genCStruct(pw);
    }
    
    // gerando os prototipos de todas as funcoes
    for (int i = 0; i < classList.size(); i++) {
      pw.println("// PROTOTIPOS dos metodos da classe "+classList.get(i).getName());
      pw.println("");
      classList.get(i).getPrivateMethodList().genCPrototype(pw);
      classList.get(i).getPublicMethodList().genCPrototype(pw);
    }
    for (int i = 0; i < classList.size(); i++) {
      classList.get(i).genC(pw);
    }
    pw.println("// Codigo para o MAIN");
    pw.printlnIdent("int main() {\n"
            + "_class_Program *program;\n"
            + "program = new_Program();\n"
            + "( ( void (*)(_class_Program *) ) program->vt[0] )(program);\n"
            + "return 0;\n"
            + "}\n");
  }
  private ArrayList<ClassDec> classList;
}