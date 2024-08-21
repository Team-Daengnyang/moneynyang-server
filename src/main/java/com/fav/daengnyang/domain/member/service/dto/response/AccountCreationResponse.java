package com.fav.daengnyang.domain.member.service.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@JsonIgnoreProperties(ignoreUnknown = true)
public class AccountCreationResponse {
    @JsonProperty("REC")
    private RecResponse rec;

    public String getAccountNo() {
        return this.rec.getAccountNo();
    }
}

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@JsonIgnoreProperties(ignoreUnknown = true)
class RecResponse{
    @JsonProperty("accountNo")
    private String accountNo;

}
