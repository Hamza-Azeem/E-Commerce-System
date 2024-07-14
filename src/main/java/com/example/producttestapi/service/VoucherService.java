package com.example.producttestapi.service;

import com.example.producttestapi.entities.Voucher;

import java.util.List;
import java.util.Optional;

public interface VoucherService {
    List<Voucher> findAllVouchers();
    Voucher findVoucherById(long id);
    Voucher createVoucher(Voucher voucher);
    Voucher findVoucherByCode(String code);
    void deleteVoucher(Long id);
}
