package com.example.producttestapi.service;

import com.example.producttestapi.entities.Product;
import com.example.producttestapi.entities.Voucher;
import com.example.producttestapi.exception.ResourceNotFoundException;
import com.example.producttestapi.repos.VoucherRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class VoucherServiceImpl implements VoucherService {
    private final VoucherRepo voucherRepo;

    @Autowired
    public VoucherServiceImpl(VoucherRepo voucherRepo) {
        this.voucherRepo = voucherRepo;
    }

    @Override
    public List<Voucher> findAllVouchers() {
        return voucherRepo.findAll();
    }

    @Override
    public Voucher findVoucherById(long id) {
        Optional<Voucher> voucherOptional = voucherRepo.findById(id);
        if(voucherOptional.isEmpty()){
            throw new ResourceNotFoundException(String.format("Voucher not found with this id: %s", id));
        }
        return voucherOptional.get();
    }

    @Override
    public Voucher createVoucher(Voucher voucher) {
        return voucherRepo.save(voucher);
    }

    @Override
    public Voucher findVoucherByCode(String code) {
        Optional<Voucher> optionalVoucher = voucherRepo.findByCode(code);
        if (optionalVoucher.isEmpty()) {
            throw new ResourceNotFoundException("Voucher not found with this code : " + code);
        }
        return optionalVoucher.get();
    }

    @Override
    public void deleteVoucher(Long id) {
        if (!voucherRepo.existsById(id)) {
            throw new ResourceNotFoundException("Voucher not found with id: " + id);
        }
        voucherRepo.deleteById(id);
    }

    @Override
    public void applyVoucherOnProduct(Product product) {
        Voucher voucher = product.getVoucherCode();
        if(voucher == null){
            return;
        }
        if(!voucherRepo.existsById(voucher.getId())) {
            throw new ResourceNotFoundException("Voucher not found with this code : " + voucher.getId());
        }else if(voucher.getExpireDate().isBefore(LocalDate.now())){
            deleteVoucher(voucher.getId());
            return;
        }
        BigDecimal discount = voucher.getDiscount();
        BigDecimal productPrice = BigDecimal.valueOf(product.getPrice());
        // price - (price * (discount/100))
        BigDecimal discountedPrice = productPrice.subtract(productPrice.multiply(discount.divide(BigDecimal.valueOf(100))));
        product.setPrice(discountedPrice.doubleValue());
    }
}

