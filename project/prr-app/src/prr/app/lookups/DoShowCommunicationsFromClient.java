package prr.app.lookups;

import prr.Network;
import prr.app.exceptions.UnknownClientKeyException;
import prr.exceptions.ClientDoesntExistException;
import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;
import pt.tecnico.uilib.forms.Form;
//FIXME add more imports if needed

/**
 * Show communications from a client.
 */
class DoShowCommunicationsFromClient extends Command<Network> {

	DoShowCommunicationsFromClient(Network receiver) {
		super(Label.SHOW_COMMUNICATIONS_FROM_CLIENT, receiver);
		//FIXME add command fields
	}

	@Override
	protected final void execute() throws CommandException {
		String clientID = Form.requestString(Prompt.clientKey());
		try{
			_receiver.getAllCommunicationsFromClient(clientID).stream()
																.map(c -> c.renderCommunications())
																.forEach(s -> _display.popup(s));	
		}
		catch(ClientDoesntExistException e){
			throw new UnknownClientKeyException(e.getKey());
		}
	}
}
