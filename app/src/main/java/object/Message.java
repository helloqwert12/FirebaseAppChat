package object;

public class Message {
    private String sender;
    private String content;

    public Message(){

    }

    public String getContent(){
        return content;
    }
    public void setContent(String value){
        content = value;
    }

    public String getSender(){
        return sender;
    }
    public void setSender(String value){
        sender = value;
    }
}
