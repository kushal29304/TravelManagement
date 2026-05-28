package com.tms.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tms.dto.request.ItineraryItemRequest;
import com.tms.dto.response.ApiResponse;
import com.tms.dto.response.ItineraryItemResponse;
import com.tms.service.ItineraryService;

@RestController
@RequestMapping("/api/itinerary")
public class ItineraryController {

    private final ItineraryService itineraryService;

    public ItineraryController(ItineraryService itineraryService) {
        this.itineraryService = itineraryService;
    }

    @PostMapping("/{requestId}")
    public ResponseEntity<ApiResponse<ItineraryItemResponse>> addItem(
            @PathVariable Long requestId,
            @RequestBody ItineraryItemRequest dto) {
        return ResponseEntity.ok(ApiResponse.success(itineraryService.addItem(requestId, dto)));
    }

    @GetMapping("/{requestId}")
    public ResponseEntity<ApiResponse<List<ItineraryItemResponse>>> getItems(
            @PathVariable Long requestId) {
        return ResponseEntity.ok(ApiResponse.success(itineraryService.getByRequest(requestId)));
    }

    @DeleteMapping("/item/{itemId}")
    public ResponseEntity<ApiResponse<Void>> deleteItem(@PathVariable Long itemId) {
        itineraryService.deleteItem(itemId);
        return ResponseEntity.ok(ApiResponse.success(null, "Item deleted"));
    }
}