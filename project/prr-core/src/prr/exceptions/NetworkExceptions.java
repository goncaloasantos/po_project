package prr.exceptions;

public abstract class NetworkExceptions extends Exception{
    public NetworkExceptions() {
        super("empty message");
    }
    public NetworkExceptions(String message) {
        super(message);
    }
}
