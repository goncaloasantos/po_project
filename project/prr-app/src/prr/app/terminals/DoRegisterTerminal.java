package prr.app.terminals;

import prr.Network;
import prr.app.exceptions.DuplicateTerminalKeyException;
import prr.app.exceptions.InvalidTerminalKeyException;
import prr.app.exceptions.UnknownClientKeyException;
import prr.app.exceptions.InvalidTerminalKeyException;
import prr.exceptions.ClientDoesntExistException;
import prr.exceptions.InvalidTerminalIDException;
import prr.exceptions.TerminalExistsException;
import prr.exceptions.UnrecognizedEntryException;
import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;
import pt.tecnico.uilib.forms.Form;
//FIXME add more imports if needed

/**
 * Register terminal.
 */
class DoRegisterTerminal extends Command<Network> {

	DoRegisterTerminal(Network receiver) {
		super(Label.REGISTER_TERMINAL, receiver);
	}

	@Override
	protected final void execute() throws CommandException {
        String terminalID = Form.requestString(Prompt.terminalKey());
		String fields[];
		String type = Form.requestOption(Prompt.terminalType(), "BASIC", "FANCY");
		Form f = new Form();
		String clientID = f.requestString(Prompt.clientKey());
		
		try{

			fields = new String[] {type, terminalID, clientID, "IDLE"};

			_receiver.registerTerminal(fields);
		}
		catch (TerminalExistsException e){
			throw new DuplicateTerminalKeyException(e.getKey());
		}	
		catch (InvalidTerminalIDException e) {
			throw new InvalidTerminalKeyException(e.getKey());
		}
		catch (ClientDoesntExistException e){
			throw new UnknownClientKeyException(clientID);
		}
		catch (UnrecognizedEntryException e) {
			e.printStackTrace();
		} 
	}
}
