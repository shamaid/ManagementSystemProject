package Service;

public class RealIsraeliTaxLawsSystem implements StubSystem {
    @Override
    public boolean connect() {
        return false;
    }

    public double revenueAmount(double getTaxRate) {
        return getTaxRate;
    }
}
