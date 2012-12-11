/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ast;

/**
 *
 *  @author endel
 */
public class BreakStatement extends Statement{

  @Override
  public void genK(PW pw) {
    pw.println("break;");
  }
}
