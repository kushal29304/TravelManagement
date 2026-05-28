package com.tms.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tms.dto.request.ItineraryItemRequest;
import com.tms.dto.response.ItineraryItemResponse;
import com.tms.exception.ResourceNotFoundException;
import com.tms.model.ItineraryItem;
import com.tms.model.TravelRequest;
import com.tms.repository.ItineraryItemRepository;
import com.tms.repository.TravelRequestRepository;

@Service
public class ItineraryService {

    private final ItineraryItemRepository itineraryItemRepository;
    private final TravelRequestRepository travelRequestRepository;

    private static final DateTimeFormatter FMT = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    public ItineraryService(ItineraryItemRepository itineraryItemRepository,
                             TravelRequestRepository travelRequestRepository) {
        this.itineraryItemRepository = itineraryItemRepository;
        this.travelRequestRepository = travelRequestRepository;
    }

    @Transactional
    public ItineraryItemResponse addItem(Long requestId, ItineraryItemRequest dto) {
        TravelRequest request = travelRequestRepository.findById(requestId)
                .orElseThrow(() -> new ResourceNotFoundException("Travel request not found"));

        ItineraryItem item = ItineraryItem.builder()
                .travelRequest(request)
                .type(dto.getType())
                .details(dto.getDetails())
                .startDt(dto.getStartDt() != null ? LocalDateTime.parse(dto.getStartDt(), FMT) : null)
                .endDt(dto.getEndDt() != null ? LocalDateTime.parse(dto.getEndDt(), FMT) : null)
                .location(dto.getLocation())
                .build();

        item = itineraryItemRepository.save(item);
        return toResponse(item);
    }

    public List<ItineraryItemResponse> getByRequest(Long requestId) {
        return itineraryItemRepository.findByTravelRequestIdOrderByStartDtAsc(requestId)
                .stream().map(this::toResponse).collect(Collectors.toList());
    }

    @Transactional
    public void deleteItem(Long itemId) {
        if (!itineraryItemRepository.existsById(itemId))
            throw new ResourceNotFoundException("Itinerary item not found");
        itineraryItemRepository.deleteById(itemId);
    }

    private ItineraryItemResponse toResponse(ItineraryItem i) {
        return ItineraryItemResponse.builder()
                .id(i.getId()).type(i.getType()).details(i.getDetails())
                .startDt(i.getStartDt()).endDt(i.getEndDt()).location(i.getLocation())
                .build();
    }
}