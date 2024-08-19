package com.example.producttestapi.mapper;

import com.example.producttestapi.dto.VoucherDto;
import com.example.producttestapi.entities.Voucher;

public class VoucherMapper {
    public static VoucherDto convertToVoucherDto(Voucher voucher) {
        return VoucherDto.builder()
                .code(voucher.getCode())
                .expiryDate(voucher.getExpireDate())
                .discount(voucher.getDiscount())
                .build();
    }
    public static Voucher convertToVoucher(VoucherDto voucherDto) {
        return Voucher.builder()
                .code(voucherDto.getCode())
                .expireDate(voucherDto.getExpiryDate())
                .discount(voucherDto.getDiscount())
                .build();
    }

}
