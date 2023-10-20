package com.kshrd.soccer_date.controller;

import com.kshrd.soccer_date.model.Venue;
import com.kshrd.soccer_date.model.request.UpdateCommentRequest;
import com.kshrd.soccer_date.model.request.VenueRequest;
import com.kshrd.soccer_date.model.response.Response;
import com.kshrd.soccer_date.service.VenueService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/venue")
@SecurityRequirement(name="auth")
public class VenueController {
    private final VenueService venueService;
    public VenueController(VenueService venueService) {
        this.venueService = venueService;
    }
    @GetMapping("/{venueId}")
    public ResponseEntity<Response<Venue>> searchLocationById(
            @PathVariable Integer venueId
    ) {
        Response<Venue> response = Response.<Venue>builder()
                .message("get venue by id success..!!")
                .payload(venueService.searchLocationById(venueId))
                .status(200)
                .build();
        return ResponseEntity.ok().body(response);
    }
    @PostMapping("")
    public ResponseEntity<Response<Venue>>  createVenue(
            @RequestBody VenueRequest venue
    ) {
        Response<Venue> response = Response.<Venue>builder()
                .message("create venue success..!!")
                .payload(venueService.createVenue(venue))
                .status(200)
                .build();
        return ResponseEntity.ok().body(response);
    }


    @GetMapping ("")
    public ResponseEntity<Response<List<Venue>>> getAllVenue(
            @RequestParam(defaultValue = "3") Integer pageSize,
            @RequestParam(defaultValue = "0") Integer pageNo
    ) {
        Response<List<Venue>> response = Response.<List<Venue>>builder()
                .message("get all venue success..!!")
                .payload(venueService.getAllLocation(pageSize,pageNo))
                .status(200)
                .build();
        return ResponseEntity.ok().body(response);
    }

    @GetMapping ("/venue-name")
    @Operation(summary = "search-venue-by-name")
    public ResponseEntity<Response<List<Venue>>> searchLocationByName(
            @RequestParam String venueName,
            @RequestParam(defaultValue = "3") Integer pageSize,
            @RequestParam(defaultValue = "0") Integer pageNo
    ) {
        Response<List<Venue>> response = Response.<List<Venue>>builder()
                .message("get venue by name  success..!!")
                .payload(venueService.searchLocationByName(venueName,pageSize,pageNo))
                .status(200)
                .build();
        return ResponseEntity.ok().body(response);
    }


    @GetMapping ("/all-name")
    @Operation(summary = "search-venue-user-location-by-name")
    public ResponseEntity<Response<?>> searchAllName(
            @RequestParam String name,
            @RequestParam(defaultValue = "3") Integer pageSize,
            @RequestParam(defaultValue = "0") Integer pageNo
    ) {
        Response response = Response.builder()
                .message("get all name success..!!")
                .payload(venueService.searchAllName(name,pageSize,pageNo))
                .status(200)
                .build();
        return ResponseEntity.ok().body(response);
    }



    @PutMapping("/{venueId}")
    public ResponseEntity<Response<Venue>>  updateVenueById(
            @PathVariable Integer venueId,
            @RequestBody VenueRequest venueRequest
    ) {
        Response<Venue> response = Response.<Venue>builder()
                .message("create venue success..!!")
                .payload(venueService.updateVenueById(venueId,venueRequest))
                .status(200)
                .build();
        return ResponseEntity.ok().body(response);
    }
    @DeleteMapping("/{venueId}")
    public ResponseEntity<Response<Venue>>  deleteVenueById(
            @PathVariable Integer venueId
    ) {
        Response<Venue> response = Response.<Venue>builder()
                .message("Delete venue success..!!")
                .payload(venueService.deleteVenueById(venueId))
                .status(200)
                .build();
        return ResponseEntity.ok().body(response);
    }
}
