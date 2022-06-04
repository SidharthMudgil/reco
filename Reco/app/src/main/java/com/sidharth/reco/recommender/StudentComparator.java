package com.sidharth.reco.recommender;

import java.util.Comparator;

public class StudentComparator implements Comparator<DistanceSongPair> {
    @Override
    public int compare(DistanceSongPair d1, DistanceSongPair d2) {
        return Double.compare(d2.getDistance(), d1.getDistance());
    }
}
