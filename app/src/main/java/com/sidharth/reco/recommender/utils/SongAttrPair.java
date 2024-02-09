package com.sidharth.reco.recommender.utils;

import com.sidharth.reco.recommender.model.SongFeatureModel;

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
