package prr.clients;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import java.io.Serializable;

import prr.Communications.Communication;
import prr.Notifications.Notifications;
import prr.balance.Balance;
import prr.deliveryMethod.DeliveryMethod;
import prr.exceptions.NotificationsAlreadyDisabled;
import prr.exceptions.NotificationsAlreadyEnabled;
import prr.terminals.Terminal;

public class Client implements Serializable{

    private String _id;
    private String _nome;
    private int _nif;
    private ClientType _type;
    private boolean _notif = true;
    private Map<String, Terminal> _terminals = new TreeMap<String, Terminal>();
    private ArrayList<Terminal> _terminalsToNotify = new ArrayList<Terminal>();
    private Balance _balance;
    private ArrayList <Notifications> _myNotifications = new ArrayList<Notifications>();
    private SortedMap<Integer, Communication> _receivedCommunications = new TreeMap<Integer, Communication>();
    private ArrayList<Communication> _doneCommunications = new ArrayList<Communication>();
    private DeliveryMethod deliveryMethod = new DeliverInApp();


    public Client(String id, String nome, int nif){
        this._id = id;
        this._nome = nome;
        this._nif = nif;
        this._type = new Normal(this, 0);
        this._balance = new Balance();
    }

    public ClientType getClientType(){return _type;}

    public void addTerminal(String id, Terminal t){
        _terminals.put(id, t);
    }

    public Client getClient(){
        return this;
    }

    public String getChave(){
        return _id;
    }

    public Collection<Terminal> getAllTerminals(){
        return _terminals.values();
    }

    public void newDoneCommunication(Communication c){
        _doneCommunications.add(c);
    }

    public void newReceivedCommunication(Communication c){
        _receivedCommunications.put(c.getcomID(), c);
    }

    public String getNome(){
        return _nome;
    }

    public int getNif(){
        return _nif;
    }

    public long getDebt(){
        _balance.setDebtTo0();
        _terminals.values().stream().forEach(t -> _balance.addDebts(t.getTerminalBalance().getDebt()));
        return _balance.getDebt();
    }

    public long getPayment(){
        _balance.setPaymentsTo0();
        _terminals.values().stream().forEach(t -> _balance.addPaid(t.getTerminalBalance().getPaid()));
        return _balance.getPaid();
    }

    public int getNumberOfTerminals(){
        return _terminals.size();
    }

    public boolean getNotifState(){
        return _notif;
    }

    public void setNotifStateTrue() throws NotificationsAlreadyEnabled{
        if(_notif == true){ throw new NotificationsAlreadyEnabled();}
        else{_notif = true;}
    }

    public void setNotifStateFalse() throws NotificationsAlreadyDisabled{
        if(_notif == false){ throw new NotificationsAlreadyDisabled();}
        _notif = false;
    }

    public void toNotify(Terminal t){
        _terminalsToNotify.add(t);
    }

    public Collection<Communication> getAllToCommunications(){
        return this._receivedCommunications.values();
    }

    public ArrayList<Communication> getAllCommunications(){
		return this._doneCommunications;
	}

    public void addNotifications(Notifications n){
        if(_myNotifications.isEmpty()){
            _myNotifications.add(n);
        }
        else{
            for(int i = 0; i < _myNotifications.size(); i++){
                if(!n.getTerminal().getTerminalID().equals(_myNotifications.get(i).getTerminal().getTerminalID())){
                    _myNotifications.add(n);
                } 
            }
        }
            
    }

	public double getAveragePayments(){
		return getPayment()/_terminals.size();
	}

    public String renderClients(){   
        return "CLIENT|" + getChave() + "|" + getNome()
             + "|" + getNif() + "|" + _type.getTypeName() + "|" + (getNotifState() ? "YES" : "NO") + "|"
             + getNumberOfTerminals() + "|" + Math.round(getPayment()) + "|" + Math.round(getDebt());
    }

    public String renderClientsWNotifications(){
        String s = renderClients();
        if(_myNotifications.isEmpty())
            return s;
        s += "\n";
        s += deliveryMethod.deliver(_myNotifications);
        _myNotifications = new ArrayList<Notifications>();
        return s;
    }

    public class DeliverInApp implements DeliveryMethod{
        public String deliver(ArrayList<Notifications> n){
            String s = "";
            for(int i=0; i< n.size(); ++i){
                s += n.get(i);
                if(i < n.size()-1)
                    s += "\n";
            }
            return s;
        }
    }


    public abstract class ClientType implements Serializable{
        private long _creditos;
        
        public ClientType(long cred){
            _creditos = cred;
        }
    
        public long getCreditos(){
            return this._creditos;
        }
        
        public ClientType getType(){
            return Client.this._type;
        }

        public Client getClient(){
            return Client.this;
        }

        public void setType(ClientType cltype){
            Client.this._type = cltype;
        }

        public void setCreditos(){
            _creditos = Client.this.getPayment() - Client.this.getDebt();
        }

        public abstract String getTypeName();

        public abstract long getCostText(int size);
    
        public abstract long getCostVoice(int time);

        public abstract long getCostVideo(int time);

        public abstract boolean shouldUpgradeGold();

        public abstract boolean shouldUpgradePlat();

        public abstract void upgrade();

        public abstract boolean shouldDowngrade();
        
        public abstract void downgrade();
    }

}




