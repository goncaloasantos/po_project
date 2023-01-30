package prr.app.terminal;


import prr.Network;
import prr.app.exceptions.UnknownTerminalKeyException;
import prr.exceptions.TerminalDoesntExistException;
import prr.terminals.Terminal;
import pt.tecnico.uilib.menus.CommandException;
import pt.tecnico.uilib.forms.Form;

/**
 * Remove friend.
 */
class DoRemoveFriend extends TerminalCommand {

	DoRemoveFriend(Network context, Terminal terminal) {
		super(Label.REMOVE_FRIEND, context, terminal);
		//FIXME add command fields
	}

	@Override
	protected final void execute() throws CommandException {
		String terminalID = Form.requestString(Prompt.terminalKey());
		try{
			_receiver.removeFriend(terminalID, _network); 
		}
		catch(TerminalDoesntExistException e) {
			throw new UnknownTerminalKeyException(e.getKey());
		}
	}
}
