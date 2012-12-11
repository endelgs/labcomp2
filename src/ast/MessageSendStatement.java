package ast;

public class MessageSendStatement extends Statement { 
  public MessageSendStatement(MessageSend messageSend){
    this.messageSend = messageSend;
  }

   public void genK( PW pw ) {
      messageSend.genK(pw);
      pw.println(";");
   }

   private MessageSend  messageSend;

}


