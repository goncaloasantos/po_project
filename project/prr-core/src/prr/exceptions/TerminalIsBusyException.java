package prr.exceptions;

public class TerminalIsBusyException extends NetworkExceptions {
    private final String _key;


    public TerminalIsBusyException(String key) {
        _key = key;
    }

    public String getKey(){
        return _key;
    }
}
