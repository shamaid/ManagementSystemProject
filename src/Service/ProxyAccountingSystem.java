package Service;

public class ProxyAccountingSystem implements StubSystem {

    private static RealAccountingSystem realAccountingSystem;

    public boolean addPayment(String teamName, String date, double amount){
        return realAccountingSystem.addPayment(teamName, date, amount);
    }

    @Override
    public boolean connect() {
        if(realAccountingSystem==null) {
            realAccountingSystem = new RealAccountingSystem();
            return realAccountingSystem.connect();
        }
        return realAccountingSystem.connect();
    }
}
