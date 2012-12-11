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
  private ArrayList<ClassDec> classList;
}