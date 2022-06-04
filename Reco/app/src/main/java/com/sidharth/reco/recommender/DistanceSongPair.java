package com.sidharth.reco.recommender;

public class DistanceSongPair {
    private final double distance;
    private final SongFeatureModel featureModel;

    public DistanceSongPair(double distance, SongFeatureModel featureModel) {
        this.distance = distance;
        this.featureModel = featureModel;
    }

    public double getDistance() {
        return distance;
    }

    public SongFeatureModel getFeatureModel() {
        return featureModel;
    }
}
