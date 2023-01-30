package prr.app.terminal;

import prr.Network;
import prr.terminals.Terminal;
import prr.app.exceptions.UnknownTerminalKeyException;
import prr.exceptions.TerminalDoesntExistException;
import prr.exceptions.TerminalIsOffException;
import pt.tecnico.uilib.forms.Form;
import pt.tecnico.uilib.menus.CommandException;
//FIXME add more imports if needed

/**
 * Command for sending a text communication.
 */
class DoSendTextCommunication extends TerminalCommand {

        DoSendTextCommunication(Network context, Terminal terminal) {
                super(Label.SEND_TEXT_COMMUNICATION, context, terminal, receiver -> receiver.canStartCommunication());
        }

        @Override
        protected final void execute() throws CommandException {
                String terminalID = Form.requestString(Prompt.terminalKey());
                String message = Form.requestString(Prompt.textMessage());
                try{
                        _receiver.initiateTextCommunication(terminalID, message, _network);
                }
                catch(TerminalIsOffException e){
                        _display.popup(Message.destinationIsOff(e.getKey()));
                }
                catch(TerminalDoesntExistException e){
                        throw new UnknownTerminalKeyException(e.getKey());
                }
        }
} 
