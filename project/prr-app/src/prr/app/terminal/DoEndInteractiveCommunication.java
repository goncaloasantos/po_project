package prr.app.terminal;

import prr.Network;
import prr.terminals.Terminal;
import pt.tecnico.uilib.forms.Form;
import pt.tecnico.uilib.menus.CommandException;
//FIXME add more imports if needed

/**
 * Command for ending communication.
 */
class DoEndInteractiveCommunication extends TerminalCommand {

	DoEndInteractiveCommunication(Network context, Terminal terminal) {
		super(Label.END_INTERACTIVE_COMMUNICATION, context, terminal, receiver -> receiver.canEndCurrentCommunication());
	}

	@Override
	protected final void execute() throws CommandException {
		int duration = Form.requestInteger(Prompt.duration());

		_display.popup(Message.communicationCost(_receiver.endInteractiveCommunication(duration)));
	}
}
