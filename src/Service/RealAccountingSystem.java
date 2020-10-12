package Service;

public class RealAccountingSystem implements StubSystem {

    @Override
    public boolean connect() {
        return true;
    }

    public boolean addPayment(String teamName, String date, double amount){
        return true;
    }

}
