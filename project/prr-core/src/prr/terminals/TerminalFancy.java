package prr.terminals;

public class TerminalFancy implements TerminalType{

    @Override
    public String toString(){return "FANCY";}

    @Override
    public boolean canInteract(){
        return true;
    }
}
