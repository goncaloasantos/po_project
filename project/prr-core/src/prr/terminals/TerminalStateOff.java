package prr.terminals;

public class TerminalStateOff extends TerminalState{

    public TerminalStateOff(Terminal t, TerminalState previousState) {
        super(t, false, false, false, previousState);
    }
    
    @Override
    public void setToSilence(){
        getTerminal().setState(new TerminalStateSilence(getTerminal(), this));
    }

    @Override
    public void setToOff(){}

    @Override
    public void setToIdle(){
        getTerminal().setState(new TerminalStateIdle(getTerminal(), this));
    }
    
    @Override
    public void setToBusy(){}
    
    @Override
    public String toString(){return "OFF";}
}
