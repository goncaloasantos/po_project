package prr.terminals;

public class TerminalStateSilence extends TerminalState{
    
    public TerminalStateSilence(Terminal t, TerminalState previousState) {
        super(t, true, true, false, previousState);
    }

    @Override
    public void setToSilence(){}

    @Override
    public void setToOff(){
        getTerminal().setState(new TerminalStateOff(getTerminal(), this));
    }

    @Override
    public void setToIdle(){
        getTerminal().setState(new TerminalStateIdle(getTerminal(), this));
    }
    
    @Override
    public void setToBusy(){
        getTerminal().setState(new TerminalStateBusy(getTerminal(), this));
    }
    
    @Override
    public String toString(){return "SILENCE";}
}
