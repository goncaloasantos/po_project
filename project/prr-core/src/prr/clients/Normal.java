package prr.clients;

public class Normal extends Client.ClientType{
    
    public Normal(Client c, long creditos){
        c.super(creditos);
    }

    @Override
    public boolean shouldUpgradeGold(){
        setCreditos();
        long cred = getCreditos();
        return cred > 500;
    }

    @Override
    public void upgrade(){
        if (shouldUpgradeGold()){
            getClient().getClientType().setType(new Gold(this.getClient(), getCreditos()));
        }
    }

    @Override
    public void downgrade(){}

    @Override
    public boolean shouldDowngrade(){return false;}

    @Override
    public  boolean shouldUpgradePlat(){return false;}


    @Override
    public long getCostText(int size){
        if (size < 50){ return 10;}
        if (size >= 50 && size < 100) {
            return 16;
        } else {
            return 2*size;
        }
    }

    @Override
    public long getCostVoice(int time){
        return time*20;
    }

    @Override
    public long getCostVideo(int time){
        return time*30;
    }

    @Override
    public String getTypeName(){
        return "NORMAL";
    }
}
