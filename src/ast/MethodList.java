/**
 * 379930 Endel Guimaraes Silva 400564 Felipe Augusto Rosa
 */
package ast;

import java.util.*;

public class MethodList extends MemberList {

  public MethodList() {
    methodList = new ArrayList<MethodDec>();
  }

  public void addElement(MethodDec method) {
    methodList.add(method);
  }

  public Iterator<MethodDec> elements() {
    return this.methodList.iterator();
  }

  public MethodDec get(int i) {
    return methodList.get(i);
  }

  public int getSize() {
    return methodList.size();
  }

  public void genK(PW pw) {
    if (methodList != null) {
      for (int i = 0; i < methodList.size(); i++) {
        methodList.get(i).genK(pw);
      }
    }
  }

  public void genC(PW pw) {
    if (methodList != null) {
      for (int i = 0; i < methodList.size(); i++) {
        methodList.get(i).genC(pw);
      }
    }
  }

  public void genCPrototype(PW pw) {
    if (methodList != null) {
      for (int i = 0; i < methodList.size(); i++) {
        methodList.get(i).genCPrototype(pw);
      }
    }
  }

  public void genCVT(PW pw, ClassDec classDec) {
    if (methodList != null) {
      pw.println("Func VTclass_" + classDec.getName() + "[] = {");
      recursiveCVT(pw, classDec, false);
      pw.println("};");
    }
  }

  public void recursiveCVT(PW pw, ClassDec classDec, boolean putComma) {
    if (classDec.getSuperclass() != null) {
      recursiveCVT(pw, classDec.getSuperclass(), true);
    }

    for (int i = 0; i < classDec.getPublicMethodList().getSize(); i++) {
      if (classDec.getPublicMethodList().get(i).isIsStatic()) {
        continue;
      }
      vt.add(classDec.getPublicMethodList().get(i).getCName());

      pw.printIdent("( void (*)() )" + classDec.getPublicMethodList().get(i).getCName());
      if (putComma || i < classDec.getPublicMethodList().getSize() - 1) {
        pw.print(",");
      }
      pw.println("");
    }
  }

  public int findCVTIndex(String methodName, boolean reverse) {
    if (reverse) {
      for (int i = vt.size() - 1; i >= 0; i--) {
        if (vt.get(i).equals(methodName)) {
          return i;
        }
      }
    } else {
      for (int i = 0; i < vt.size(); i++) {
        if (vt.get(i).equals(methodName)) {
          return i;
        }
      }
    }
    return -1;
  }
  /*
   * Metodo que BUSCA um metodo na lista
   */

  public int searchMethod(String name) {
    for (int i = 0; i < methodList.size(); i++) {
      if (methodList.get(i).getName().equals(name)) {
        return i;
      }
    }
    return -1;
  }
  private ArrayList<MethodDec> methodList;
  private ArrayList<String> vt = new ArrayList<String>();
}
