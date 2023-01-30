package prr.clients;

import java.util.ArrayList;

import prr.Communications.Communication;
import prr.clients.Client.ClientType;

public class Platinum extends ClientType{
    
    public Platinum(Client c, long creditos){
        c.super(creditos);
    }

    public void upgrade(){}
    public boolean shouldDowngrade(){return false;}
    public boolean shouldUpgrade(){return false;}
    public boolean shouldUpgradePlat(){return false;}
    public boolean shouldUpgradeGold(){return false;}


    public boolean shouldDowngradeNormal(){
        setCreditos();
        long cred = getCreditos();
        return cred < 0;
    }

    public boolean shouldDowngradeGold(){
        int _cont = 0;
        ArrayList<Communication> _coms = getClient().getAllCommunications();
        for(int i = _coms.size()-1; i >= _coms.size()-2; i--){
            if(_coms.get(i).getText() != ""){
                _cont++;
            }
        }
        if (_cont == 2 && getCreditos() >= 0) {return true;}
        else return false;
    } 

    @Override
    public void downgrade(){
        if (shouldDowngradeNormal()){
            getClient().getClientType().setType(new Normal(this.getClient(), getCreditos()));
        }

        if (shouldDowngradeGold()){
            getClient().getClientType().setType(new Gold(this.getClient(), getCreditos()));
        }
    }

    @Override
    public long getCostText(int size){
        if (size < 50){ return 0;}
        if (size >= 50 || size < 100) {
            return 4;
        } else {
            return 4;
        }
    }

    @Override
    public long getCostVoice(int time){
        return time*10;
    }

    @Override
    public long getCostVideo(int time){
        return time*10;
    }

    @Override
    public String getTypeName(){
        return "PLATINUM";
    }

}
