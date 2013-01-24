/**
 * 379930 Endel Guimaraes Silva
 * 400564 Felipe Augusto Rosa
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
  @Override
  public void genC(PW pw) {
    pw.println("break;");
  }
}
