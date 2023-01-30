package prr.app.lookups;

import java.util.Collections;

import prr.Network;
import prr.app.exceptions.UnknownClientKeyException;
import prr.exceptions.ClientDoesntExistException;
import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;
import pt.tecnico.uilib.forms.Form;

//FIXME add more imports if needed

/**
 * Show communications to a client.
 */
class DoShowCommunicationsToClient extends Command<Network> {

	DoShowCommunicationsToClient(Network receiver) {
		super(Label.SHOW_COMMUNICATIONS_TO_CLIENT, receiver);
		//FIXME add command fields
	}

	@Override
	protected final void execute() throws CommandException {
		String clientID = Form.requestString(Prompt.clientKey());	
		try{
			_receiver.getClient(clientID).getAllToCommunications()
						.stream()
						.map(c -> c.renderCommunications())
						.forEach(s -> _display.popup(s));
		}
		catch(ClientDoesntExistException e){
			throw new UnknownClientKeyException(e.getKey());
		}
	}
}
