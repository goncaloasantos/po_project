package prr.exceptions;

public class InvalidTerminalIDException extends NetworkExceptions{
    
    private final String _key;

    public InvalidTerminalIDException(String key) {
        _key = key;
    }

    public String getKey(){
        return _key;
    }
}
