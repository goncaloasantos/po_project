package prr.app.terminal;

import prr.Network;
import prr.app.exceptions.UnknownTerminalKeyException;
import prr.exceptions.TerminalDoesntExistException;
import prr.terminals.Terminal;
import pt.tecnico.uilib.menus.CommandException;
import pt.tecnico.uilib.forms.Form;
//FIXME add more imports if needed

/**
 * Add a friend.
 */
class DoAddFriend extends TerminalCommand {

	DoAddFriend(Network context, Terminal terminal) {
		super(Label.ADD_FRIEND, context, terminal);
	}

	@Override
	protected final void execute() throws CommandException {
		String terminalID = Form.requestString(Prompt.terminalKey());
		try{
			_receiver.addFriendByID(terminalID, _network); 
		}
		catch(TerminalDoesntExistException e) {
			throw new UnknownTerminalKeyException(e.getKey());
		}   
	}
}
