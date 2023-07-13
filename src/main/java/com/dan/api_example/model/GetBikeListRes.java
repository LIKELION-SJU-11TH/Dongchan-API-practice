package com.dan.api_example.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetBikeListRes {
    private int rackTotCnt;
    private String stationName;
    private int parkingBikeTotCnt;
    private int shared;
    private float stationLatitude;
    private float stationLongitude;
    private String stationId;

    public GetBikeListRes() {
    }

    public GetBikeListRes(int rackTotCnt, String stationName, int parkingBikeTotCnt, int shared, float stationLatitude, float stationLongitude, String stationId) {
        this.rackTotCnt = rackTotCnt;
        this.stationName = stationName;
        this.parkingBikeTotCnt = parkingBikeTotCnt;
        this.shared = shared;
        this.stationLatitude = stationLatitude;
        this.stationLongitude = stationLongitude;
        this.stationId = stationId;
    }
}
