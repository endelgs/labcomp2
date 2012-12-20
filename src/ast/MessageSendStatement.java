package ast;

public class MessageSendStatement extends Statement { 
  public MessageSendStatement(MessageSend messageSend){
    this.messageSend = messageSend;
  }

  @Override
   public void genK( PW pw ) {
     messageSend.genK(pw);
   }

   private MessageSend  messageSend;

}


