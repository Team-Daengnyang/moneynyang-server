package com.fav.daengnyang.domain.memberDetail.service.dto.response;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
public class AnalyzeMonthlyResponse {

    private String mostUsed;
    private int compareDiff;

    private int pet;
    private double petRate;

    private int food;
    private double foodRate;

    private int shopping;
    private double shoppingRate;

    private int transportation;
    private double transportationRate;

    private int others;
    private double othersRate;

    private int total;
    private int countPayment;

}
