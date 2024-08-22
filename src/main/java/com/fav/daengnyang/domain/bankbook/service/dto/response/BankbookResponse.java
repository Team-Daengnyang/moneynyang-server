package com.fav.daengnyang.domain.bankbook.service.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class BankbookResponse {
    private String bankbookTitle;
    private String bankbookNumber;
    private String bankbookImage;
    private String bankbookColor;
}
