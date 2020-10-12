package Service;

public class ProxyRecommendationSystem implements StubSystem{

    private RealRecommendationSystem realRecommendationSystem;
    @Override
    public boolean connect(){
        if(realRecommendationSystem==null) {
            realRecommendationSystem = new RealRecommendationSystem();
            return realRecommendationSystem.connect();
        }
        return realRecommendationSystem.connect();
    }

    public boolean trainModel(){
        return realRecommendationSystem.trainModel();
    }
}
