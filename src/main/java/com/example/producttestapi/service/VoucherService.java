package com.example.producttestapi.service;

import com.example.producttestapi.dto.VoucherDto;
import com.example.producttestapi.entities.Product;
import com.example.producttestapi.entities.Voucher;

import java.util.List;


public interface VoucherService {
    List<VoucherDto> findAllVouchers();
    VoucherDto findVoucherById(long id);
    void createVoucher(VoucherDto voucherDto);
    Voucher findVoucherByCode(String code);
    VoucherDto findVoucherDtoByCode(String code);
    void deleteVoucher(Long id);
    void applyVoucherOnProduct(Product product);
}
