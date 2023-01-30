package prr.app.terminal;

import prr.Network;
import prr.exceptions.InvalidCommunicationException;
import prr.terminals.Terminal;
import pt.tecnico.uilib.menus.CommandException;
import pt.tecnico.uilib.forms.Form;
// Add more imports if needed

/**
 * Perform payment.
 */
class DoPerformPayment extends TerminalCommand {

	DoPerformPayment(Network context, Terminal terminal) {
		super(Label.PERFORM_PAYMENT, context, terminal);
		//FIXME add command fields
	}

	@Override
	protected final void execute() throws CommandException {
		try{
			int CommID = Form.requestInteger(Prompt.commKey());
			_receiver.payCommunication(CommID);
		}
		catch(InvalidCommunicationException e){
			_display.popup(Message.invalidCommunication());
		}
	}
}
