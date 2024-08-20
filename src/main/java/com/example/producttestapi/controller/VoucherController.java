package com.example.producttestapi.controller;

import com.example.producttestapi.dto.VoucherDto;
import com.example.producttestapi.entities.Voucher;
import com.example.producttestapi.service.VoucherService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
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
    @Operation(
            description = "GET endpoint to retrieve voucherDto list",
            responses = {
                    @ApiResponse(
                            description = "success",
                            responseCode = "200"
                    ),
                    @ApiResponse(
                            description = "unauthenticated",
                            responseCode = "401"
                    )
            }
    )
    @GetMapping
    public ResponseEntity<?> getAllVouchers(){
        return ResponseEntity.ok().body(voucherService.findAllVouchers());
    }

    @Operation(
            description = "POST endpoint to create a voucher by sending a voucherDto",
            responses = {
                    @ApiResponse(
                            description = "success",
                            responseCode = "201"
                    ),
                    @ApiResponse(
                            description = "unauthenticated",
                            responseCode = "401"
                    ),
                    @ApiResponse(
                            description = "unauthorized",
                            responseCode = "403"
                    )
            }
    )
    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody VoucherDto voucherDto) {
        voucherService.createVoucher(voucherDto);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @Operation(
            description = "GET endpoint to retrieve voucherDto by voucher code",
            responses = {
                    @ApiResponse(
                            description = "success",
                            responseCode = "200"
                    ),
                    @ApiResponse(
                            description = "unauthenticated",
                            responseCode = "401"
                    ),
                    @ApiResponse(
                            description = "not found",
                            responseCode = "404"
                    )
            }
    )
    @GetMapping("/{code}")
    public ResponseEntity<?> getVoucher(@PathVariable("code") String code) {
        return ResponseEntity.ok().body(voucherService.findVoucherDtoByCode(code));
    }

    @Operation(
            description = "DELETE endpoint to delete a voucher by voucher id",
            responses = {
                    @ApiResponse(
                            description = "success",
                            responseCode = "404"
                    ),
                    @ApiResponse(
                            description = "unauthenticated",
                            responseCode = "401"
                    ),
                    @ApiResponse(
                            description = "unauthorized",
                            responseCode = "403"
                    ),
                    @ApiResponse(
                            description = "not found",
                            responseCode = "404"
                    )
            }
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteVoucher(@PathVariable Long id) {
        voucherService.deleteVoucher(id);
        return ResponseEntity.notFound().build();
    }

}
