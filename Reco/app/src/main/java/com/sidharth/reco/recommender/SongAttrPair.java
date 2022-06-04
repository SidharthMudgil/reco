package com.sidharth.reco.recommender;

public class SongAttrPair {
    private final double attribute;
    private final SongFeatureModel featureModel;

    public SongAttrPair(double distance, SongFeatureModel featureModel) {
        this.attribute = distance;
        this.featureModel = featureModel;
    }

    public double getAttribute() {
        return attribute;
    }

    public SongFeatureModel getFeatureModel() {
        return featureModel;
    }
}
