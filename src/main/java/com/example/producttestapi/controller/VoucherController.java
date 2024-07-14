package com.example.producttestapi.controller;

import com.example.producttestapi.entities.Voucher;
import com.example.producttestapi.service.VoucherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/vouchers")
public class VoucherController {
    private final VoucherService voucherService;

    @Autowired
    public VoucherController(VoucherService voucherService) {
        this.voucherService = voucherService;
    }
    @GetMapping
    public ResponseEntity<?> getAllVouchers(){
        return ResponseEntity.ok().body(voucherService.findAllVouchers());
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody Voucher voucher) {
        voucherService.createVoucher(voucher);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("/{code}")
    public ResponseEntity<?> getVoucher(@PathVariable("code") String code) {
        return ResponseEntity.ok().body(voucherService.findVoucherByCode(code));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteVoucher(@PathVariable Long id) {
        voucherService.deleteVoucher(id);
        return ResponseEntity.notFound().build();
    }

}
