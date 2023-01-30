package prr.exceptions;

public class InvalidCommunicationException extends NetworkExceptions{
    private final int _key;

    public InvalidCommunicationException(int key) {
        _key = key;
    }

    public int getKey(){
        return _key;
    }
}
