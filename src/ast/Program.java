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
    pw.println("#include <string.h>");
    pw.println("typedef int boolean;");
    pw.println("#define true 1");
    pw.println("#define false 0");
    pw.println("typedef void (*Func)();");
    pw.println("");
    
    pw.println("// Gerando todas as STRUCTS");
    for (int i = 0; i < classList.size(); i++) {
      classList.get(i).genCStruct(pw);
    }
    pw.println("// Gerando os PROTOTIPOS de todas as funcoes");
    for (int i = 0; i < classList.size(); i++) {
      classList.get(i).genConstructorPrototype(pw);
      classList.get(i).getPrivateMethodList().genCPrototype(pw);
      classList.get(i).getPublicMethodList().genCPrototype(pw);
    }
    pw.println("// Gerando os VETORES de METODOS");
    for (int i = 0; i < classList.size(); i++) {
      if (classList.get(i).getPublicMethodList() != null) {
        classList.get(i).getPublicMethodList().genCVT(pw,classList.get(i));
      }
    }
    pw.println("// Gerando a IMPLEMENTACAO das classes");
    for (int i = 0; i < classList.size(); i++) {
      classList.get(i).genConstructor(pw);
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