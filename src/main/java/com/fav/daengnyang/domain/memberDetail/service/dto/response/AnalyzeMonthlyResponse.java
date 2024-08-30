package com.fav.daengnyang.domain.memberDetail.service.dto.response;

import com.fav.daengnyang.domain.memberDetail.entity.MemberDetail;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.math.RoundingMode;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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
    private int petPayCount;

    @Builder
    private AnalyzeMonthlyResponse(String mostUsed, int compareDiff, int pet, double petRate,
                                   int food, double foodRate, int shopping, double shoppingRate,
                                   int transportation, double transportationRate, int others,
                                   double othersRate, int total, int petPayCount) {
        this.mostUsed = mostUsed;
        this.compareDiff = compareDiff;
        this.pet = pet;
        this.petRate = petRate;
        this.food = food;
        this.foodRate = foodRate;
        this.shopping = shopping;
        this.shoppingRate = shoppingRate;
        this.transportation = transportation;
        this.transportationRate = transportationRate;
        this.others = others;
        this.othersRate = othersRate;
        this.total = total;
        this.petPayCount = petPayCount;
    }

    public static AnalyzeMonthlyResponse createAnalyzeMonthlyResponse(MemberDetail memberDetail) {
        // 값을 가져오기
        int pet = memberDetail.getPet();
        int food = memberDetail.getFood();
        int shopping = memberDetail.getShopping();
        int transportation = memberDetail.getTransportation();
        int others = memberDetail.getOthers();
        int total = memberDetail.getTotal();

        // 가장 큰 값을 찾기
        int maxAmount = Math.max(Math.max(pet, food), Math.max(shopping, Math.max(transportation, others)));

        // 가장 큰 값에 해당하는 문자열 설정
        String mostUsed;
        if (maxAmount == pet) {
            mostUsed = "반려동물";
        } else if (maxAmount == food) {
            mostUsed = "식비";
        } else if (maxAmount == shopping) {
            mostUsed = "쇼핑";
        } else if (maxAmount == transportation) {
            mostUsed = "교통";
        } else if (maxAmount == others) {
            mostUsed = "기타";
        } else {
            mostUsed = "없음"; // 기본값 (예외 처리)
        }

        // 비율 계산과 반올림 처리
        BigDecimal petRate = total > 0 ? BigDecimal.valueOf((double) pet / total * 100).setScale(1, RoundingMode.HALF_UP) : BigDecimal.ZERO;
        BigDecimal foodRate = total > 0 ? BigDecimal.valueOf((double) food / total * 100).setScale(1, RoundingMode.HALF_UP) : BigDecimal.ZERO;
        BigDecimal shoppingRate = total > 0 ? BigDecimal.valueOf((double) shopping / total * 100).setScale(1, RoundingMode.HALF_UP) : BigDecimal.ZERO;
        BigDecimal transportationRate = total > 0 ? BigDecimal.valueOf((double) transportation / total * 100).setScale(1, RoundingMode.HALF_UP) : BigDecimal.ZERO;
        BigDecimal othersRate = total > 0 ? BigDecimal.valueOf((double) others / total * 100).setScale(1, RoundingMode.HALF_UP) : BigDecimal.ZERO;

        // `AnalyzeMonthlyResponse` 객체 생성
        return AnalyzeMonthlyResponse.builder()
                .mostUsed(mostUsed)
                .pet(pet)
                .petRate(petRate.doubleValue())
                .food(food)
                .foodRate(foodRate.doubleValue())
                .shopping(shopping)
                .shoppingRate(shoppingRate.doubleValue())
                .transportation(transportation)
                .transportationRate(transportationRate.doubleValue())
                .others(others)
                .othersRate(othersRate.doubleValue())
                .total(total)
                .petPayCount(memberDetail.getPetPayCount()) // Assuming this method exists in MemberDetail
                .build();
    }


    public static AnalyzeMonthlyResponse createNoResponse(){
        return AnalyzeMonthlyResponse.builder().build();
    }

    public void updateAnlayze(int compareDiff){
        this.compareDiff = compareDiff;
    }
}
