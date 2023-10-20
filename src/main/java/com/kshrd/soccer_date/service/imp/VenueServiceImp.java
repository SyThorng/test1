package com.kshrd.soccer_date.service.imp;

import com.kshrd.soccer_date.exception.FieldNotFoundException;
import com.kshrd.soccer_date.exception.InvalidFieldException;
import com.kshrd.soccer_date.mapper.UserMapper;
import com.kshrd.soccer_date.model.SearchResponse;
import com.kshrd.soccer_date.model.Team;
import com.kshrd.soccer_date.model.UserApp;
import com.kshrd.soccer_date.model.Venue;
import com.kshrd.soccer_date.model.dto.UserAppDTO;
import com.kshrd.soccer_date.model.request.VenueRequest;
import com.kshrd.soccer_date.repository.TeamRepository;
import com.kshrd.soccer_date.repository.UserRepository;
import com.kshrd.soccer_date.repository.VenueRepository;
import com.kshrd.soccer_date.service.VenueService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class VenueServiceImp implements VenueService {

    private final VenueRepository venueRepository;
    private final TeamRepository teamRepository;
    private final UserRepository userRepository;


    @Value("${image.url}")
    private String imageUrl;

    public VenueServiceImp(VenueRepository venueRepository, TeamRepository teamRepository, UserRepository userRepository) {
        this.venueRepository = venueRepository;
        this.teamRepository = teamRepository;
        this.userRepository = userRepository;
    }


    @Override
    public Venue searchLocationById(Integer locationId) {
        Venue venue=venueRepository.searchVenueById(locationId);
        if (venue==null){
                throw new FieldNotFoundException(
                        "Venue Not Found",
                        "Venue id ["+locationId+"] is not found...!!"
                );
        }
        venue.setVenueLogo(imageUrl+venue.getVenueLogo());
        return venue;
    }

    @Override
    public List<Venue> searchLocationByName(String nameLocation,Integer pageSize,Integer pageNo) {
        List<Venue> venues=venueRepository.searchVenueByName(nameLocation,pageSize,pageNo);

        if (venues.size()==0){
            throw new FieldNotFoundException(
                    "Venue Not Found",
                    " venue name ["+nameLocation+"] don't have Venue "
            );
        }
        venues.forEach(venue -> venue.setVenueLogo(imageUrl+venue.getVenueLogo()));
        return venues;
    }

    @Override
    public List<Venue> getAllLocation(Integer pageSize,Integer pageNo) {
        List<Venue> venues=venueRepository.getAllLocation(pageSize,pageNo);
        if (venues.size()==0){
            throw new FieldNotFoundException(
                    "Venue Not Found",
                    "Don't have Venue "
            );
        }

        venues.forEach(venue -> venue.setVenueLogo(imageUrl+venue.getVenueLogo()));
        return venues;
    }

    @Override
    public List<SearchResponse> searchAllName(String name,Integer pageSize,Integer pageNo) {
        List<Team> teams=teamRepository.searchTeamByName(name,pageSize,pageNo);
        List<Venue> venues=venueRepository.searchVenueByName(name,pageSize,pageNo);
        List<UserAppDTO> userAppDTOS = userRepository.searchUserByName(name,pageSize,pageNo).stream().map(UserMapper.INSTANCE::toUserAppDTO).toList();


        for (Team team : teams) {
            team.setLogo(imageUrl+team.getLogo());
        }
        for (Venue venue : venues) {
            venue.setVenueLogo(imageUrl+venue.getVenueLogo());
        }
        for (UserAppDTO userAppDTO : userAppDTOS) {
            userAppDTO.setProfile(imageUrl+userAppDTO.getProfile());
        }


        List<SearchResponse> searchResponses=new ArrayList<>();
        searchResponses.add(new SearchResponse(teams,userAppDTOS,venues));

        return searchResponses;
    }

    @Override
    public Venue createVenue(VenueRequest venue) {
        if(venueRepository.searchVenueByNameForCreate(venue.getVenueName())!=null){
            throw new InvalidFieldException(
                    "Invalid venue",
                    "This Venue name already create..!!"
            );
        }
        Venue venue1=venueRepository.createVenue(venue);
        return venue1;
    }

    @Override
    public Venue deleteVenueById(Integer venueId) {
        if(venueRepository.searchVenueById(venueId)==null) {
            throw new FieldNotFoundException(
                    "Venue Not Found",
                    "Don't have Venue "
            );
        }
        Venue venue=venueRepository.deleteVenueById(venueId);
        return venue;
    }


    @Override
    public Venue updateVenueById(Integer venueId, VenueRequest venueRequest) {
        if(venueRepository.searchVenueById(venueId)==null) {
            throw new FieldNotFoundException(
                    "Venue Not Found",
                    "Don't have Venue "
            );
        }
        Venue venue=venueRepository.updateVenueById(venueId,venueRequest);
        return venue;
    }
}
