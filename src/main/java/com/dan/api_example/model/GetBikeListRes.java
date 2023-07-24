package com.dan.api_example.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

@Getter
@Setter
public class GetBikeListRes {
    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class GetBikeResList {
        private List<RentBikeStatus> rentBikeStatus;
        private List<GetBikeRes> row;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class RentBikeStatus {
        private int totalCount;
        private List<ResResult> result;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class ResResult {
        private String code;
        private String resMessage;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class GetBikeRes {
        private int rackTotCnt;
        private String stationName;
        private int parkingBikeTotCnt;
        private int shared;
        private float stationLatitude;
        private float stationLongitude;
        private String stationId;
    }


}
