/**
 * 379930 Endel Guimaraes Silva
 * 400564 Felipe Augusto Rosa
 */
package ast;

/**
 *
 * @author endel
 */
public class MessageSendStatic extends MessageSend{
  public MessageSendStatic(ClassDec c, MethodDec method, ExprList exprList) {
    super(new Variable(c.getName(), c), method, exprList);
    classDec = c;
  }
  public void genK(PW pw){
    genK(pw,false);
  }
  @Override
  public void genK(PW pw, boolean putParenthesis) {
    pw.print(classDec.getName());
    pw.print(".");
    pw.print(getMethod().getName());
    pw.print("(");
    if(getExprList() != null)
      getExprList().genK(pw);
    pw.print(")");
  }

  @Override
  public Type getType() {
    return getMethod().getType();
  }
  protected ClassDec classDec;
}
