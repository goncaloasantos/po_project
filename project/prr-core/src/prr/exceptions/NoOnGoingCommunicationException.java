package prr.exceptions;

public class NoOnGoingCommunicationException extends NetworkExceptions{
    private final String _key;

    public NoOnGoingCommunicationException(String key) {
        _key = key;
    }

    public String getKey(){
        return _key;
    }
}
