package prr.terminals;

public class TerminalStateIdle extends TerminalState{

    public TerminalStateIdle(Terminal t, TerminalState previousState) {
        super(t, true, true, true, previousState);
    }

    @Override
    public void setToSilence(){
        getTerminal().setState(new TerminalStateSilence(getTerminal(), this));
    }

    @Override
    public void setToOff(){
        getTerminal().setState(new TerminalStateOff(getTerminal(),this));
    }

    @Override
    public void setToIdle(){}
    
    @Override
    public void setToBusy(){
        getTerminal().setState(new TerminalStateBusy(getTerminal(), this));
    }
    
    @Override
    public String toString(){return "IDLE";}

}
