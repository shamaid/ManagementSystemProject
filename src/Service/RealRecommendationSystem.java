package Service;

public class RealRecommendationSystem implements StubSystem {
    @Override
    public boolean connect() {
        return true;
    }

    public boolean trainModel(){
        return true;
    }
}
