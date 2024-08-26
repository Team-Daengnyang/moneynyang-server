package com.fav.daengnyang.domain.target.service.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TargetTransferRequest {

    @NotNull(message = "입금할 금액을 입력해 주세요.")
    @Min(value = 1, message = "입금할 금액은 1원 이상이어야 합니다.")
    private int amount;

}
