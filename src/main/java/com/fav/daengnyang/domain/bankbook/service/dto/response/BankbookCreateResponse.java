package com.fav.daengnyang.domain.bankbook.service.dto.response;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;

@Data
@Getter
@Builder
public class BankbookCreateResponse {
    private String bankbookNumber;
    private String bankbookTitle;
    private String bankbookImage;
    private String bankbookColor;
}
