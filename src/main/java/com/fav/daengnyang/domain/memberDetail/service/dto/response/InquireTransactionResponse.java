package com.fav.daengnyang.domain.memberDetail.service.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@JsonIgnoreProperties(ignoreUnknown = true)
@ToString
public class InquireTransactionResponse {
    @JsonProperty("REC")
    private RecResponse rec;

    public List<TransResponse> getTransactionList() {
        return this.rec.getList();
    }

}

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@JsonIgnoreProperties(ignoreUnknown = true)
class RecResponse{
    @JsonProperty("totalCount")
    private String totalCount;
    private List<TransResponse> list;
}

