package Service;

public class ProxyIsraeliTaxLawsSystem implements StubSystem {

    private static RealIsraeliTaxLawsSystem realIsraeliTaxLawsSystem;

    @Override
    public boolean connect() {
        if(realIsraeliTaxLawsSystem==null) {
            realIsraeliTaxLawsSystem = new RealIsraeliTaxLawsSystem();
            return realIsraeliTaxLawsSystem.connect();
        }
        return realIsraeliTaxLawsSystem.connect();
    }

    public double revenueAmount(double getTaxRate) {
        return realIsraeliTaxLawsSystem.revenueAmount(getTaxRate);
    }


}
