package prr.app.clients;

import java.io.IOException;

import prr.Network;
import prr.app.exceptions.DuplicateClientKeyException;
import prr.exceptions.ClientExistsException;
import prr.exceptions.UnrecognizedEntryException;
import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;
import pt.tecnico.uilib.forms.Form;

/**
 * Register new client.
 */
class DoRegisterClient extends Command<Network> {

	DoRegisterClient(Network receiver) {
		super(Label.REGISTER_CLIENT, receiver);
	}

	@Override
	protected final void execute() throws CommandException {
		String clientID = Form.requestString(Prompt.key());
		String name = Form.requestString(Prompt.name());
		String taxId = Form.requestString(Prompt.taxId());
		try {
			_receiver.registerClient(clientID, name, Integer.parseInt(taxId));
		} 
		catch (ClientExistsException e) {
			throw new DuplicateClientKeyException(clientID);
		}
		catch (UnrecognizedEntryException e) {
			e.printStackTrace();
		}
	}

}
