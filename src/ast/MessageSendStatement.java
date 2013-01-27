/**
 * 379930 Endel Guimaraes Silva
 * 400564 Felipe Augusto Rosa
 */
package ast;

public class MessageSendStatement extends Statement {

  public MessageSendStatement(MessageSend messageSend) {
    this.messageSend = messageSend;
  }

  @Override
   public void genK( PW pw ) {
     messageSend.genK(pw);
   }

  @Override
  public void genC(PW pw) {
    messageSend.genC(pw);
    pw.println(";");
  }
  private MessageSend messageSend;
}
