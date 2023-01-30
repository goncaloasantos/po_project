package prr.app.clients;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;


import prr.Network;
import prr.clients.Client;
import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;


/**
 * Show all clients.
 */
class DoShowAllClients extends Command<Network> {

	DoShowAllClients(Network receiver) {
		super(Label.SHOW_ALL_CLIENTS, receiver);
	}

	@Override
	protected final void execute() throws CommandException {
		_receiver.getAllClients()
				.stream()
				.map(c -> c.renderClients())
				.forEach(s -> _display.popup(s));
	}
}
