package prr.terminals;

abstract public class TerminalState {
    private Terminal _terminal;
    private boolean _canCommunicate;
    private boolean _canReciveMessages;
    private boolean _canReciveNonMessages;
    private TerminalState _previousState;

    
    public TerminalState(Terminal t, boolean canCommunicate, boolean canReciveMessages,
                boolean canReciveNonMessages, TerminalState previousState){
        _terminal = t;
        _canCommunicate = canCommunicate;
        _canReciveMessages = canReciveMessages;
        _canReciveNonMessages = canReciveNonMessages;
        _previousState = previousState;
    }

    public TerminalState getPreviousState(){
        return _previousState;
    }

    public Terminal getTerminal(){
        return _terminal;
    }

    public boolean isOff(){
        return !_canCommunicate && !_canReciveMessages && !_canReciveNonMessages;
    }

    public boolean isIdle(){
        return _canCommunicate && _canReciveMessages && _canReciveNonMessages;
    }

    public boolean isSilence(){
        return _canCommunicate && _canReciveMessages && !_canReciveNonMessages;
    }

    public boolean isBusy(){
        return !_canCommunicate && _canReciveMessages && !_canReciveNonMessages;
    }
    public boolean canCommunicate(){
        return _canCommunicate;
    }

    public boolean canReciveMessages(){
        return _canReciveMessages;
    }

    public boolean canReciveNonMessages(){
        return _canReciveNonMessages;
    }

    public abstract void setToSilence();

    public abstract void setToOff();

    public abstract void setToIdle();
    
    public abstract void setToBusy();

    abstract public String toString();
}
