package com.kshrd.soccer_date.service;

import com.kshrd.soccer_date.model.SearchResponse;
import com.kshrd.soccer_date.model.Venue;
import com.kshrd.soccer_date.model.request.VenueRequest;

import java.util.List;

public interface VenueService {

    Venue searchLocationById(Integer locationId);
    List<Venue> searchLocationByName(String nameLocation,Integer pageSize,Integer pageNo);
    List<Venue> getAllLocation(Integer pageSize,Integer pageNo);
    List<SearchResponse> searchAllName(String name,Integer pageSize,Integer pageNo);
    Venue createVenue(VenueRequest venue);
    Venue deleteVenueById(Integer venueId);
    Venue updateVenueById(Integer venueId, VenueRequest venueRequest);


}
