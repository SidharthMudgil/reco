package com.sidharth.reco.recommender;

import java.util.Comparator;

public class SongAttrComparator implements Comparator<SongAttrPair> {
    @Override
    public int compare(SongAttrPair song1, SongAttrPair song2) {
        return Double.compare(song2.getAttribute(), song1.getAttribute());
    }
}
