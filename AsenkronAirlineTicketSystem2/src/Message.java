import java.io.Serializable;

public class Message implements Serializable {
    private static final long serialVersionUID = 1L;
    private int senderPortNumber;
    private int senderID;
    private Object content;

    public enum Type {
        SERVICE_MAKE_REZERVATION,
        SERVICE_CANCEL_REZERVATION,
        SERVICE_READ_REZERVATION_LIST,
        SERVICE_RESPONSE_SUCCESSFUL,
        SERVICE_RESPONSE_UNSUCCESSFUL
    }

    private Type type;

    public int getSenderPortNumber() {
        return senderPortNumber;
    }

    public void setSenderPortNumber(int senderPortNumber) {
        this.senderPortNumber = senderPortNumber;
    }

    public int getSenderID() {
        return senderID;
    }

    public void setSenderID(int senderID) {
        this.senderID = senderID;
    }

    public Object getContent() {
        return content;
    }

    public void setContent(Object content) {
        this.content = content;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }


}