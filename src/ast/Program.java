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
    pw.println("#include <malloc.h>");
    pw.println("#include <stdlib.h>");
    pw.println("#include <stdio.h>");
    pw.println("typedef int boolean;");
    pw.println("#define true 1");
    pw.println("#define false 0");
    pw.println("typedef void (*Func)();");
    for (int i = 0; i < classList.size(); i++) {
      classList.get(i).genCStruct(pw);
    }
    // gerando os prototipos de todas as funcoes
    for (int i = 0; i < classList.size(); i++) {
      classList.get(i).getPrivateMethodList().genCPrototype(pw);
      classList.get(i).getPublicMethodList().genCPrototype(pw);
    }
    for (int i = 0; i < classList.size(); i++) {
      classList.get(i).genC(pw);
    }
    
  }
  private ArrayList<ClassDec> classList;
}