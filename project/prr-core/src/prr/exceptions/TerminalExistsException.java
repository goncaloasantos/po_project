package prr.exceptions;

public class TerminalExistsException extends NetworkExceptions {
    
    private final String _key;

    public TerminalExistsException(String key) {
        _key = key;
    }

    public String getKey(){
        return _key;
    }
}
