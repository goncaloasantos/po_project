package prr.terminals;

public class TerminalStateBusy extends TerminalState {


    public TerminalStateBusy(Terminal t, TerminalState previousState) {
        super(t, false, true, false, previousState);
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
    public String toString(){return "BUSY";}
}
