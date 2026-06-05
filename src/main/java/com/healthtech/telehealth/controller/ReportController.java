package com.healthtech.telehealth.controller;

import com.healthtech.telehealth.dto.ReportDTO;
import com.healthtech.telehealth.service.ReportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/reports")
@Tag(name = "Reports", description = "Sistem raporlama ve istatistik endpoint'leri")
public class ReportController {

    private final ReportService reportService;

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @Operation(summary = "Sistem raporu", description = "Kullanıcı, randevu, reçete ve tıbbi kayıt istatistiklerini döner")
    @ApiResponse(responseCode = "200", description = "Rapor başarıyla oluşturuldu")
    @GetMapping("/dashboard")
    public ResponseEntity<ReportDTO> getDashboardReport() {
        return ResponseEntity.ok(reportService.getSystemReport());
    }
}
