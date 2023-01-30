package prr.terminals;

public class TerminalBasic implements TerminalType{

    @Override
    public String toString(){return "BASIC";}

    @Override
    public boolean canInteract(){
        return false;
    }
}
