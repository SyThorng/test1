package com.kshrd.soccer_date.repository;


import com.kshrd.soccer_date.model.Venue;
import com.kshrd.soccer_date.model.request.VenueRequest;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface VenueRepository {


    @Select("""
            SELECT * FROM venue  where id=#{locationId}
            """)
    @ResultMap("venue")
    Venue searchVenueById(Integer locationId);

    @Select("""
            SELECT * FROM venue limit #{pageSize} offset #{pageNo}
            """)
    @Results(id = "venue",value = {
            @Result(property = "venueId",column = "id"),
            @Result(property = "venueName",column = "location_name"),
            @Result(property = "venueLogo",column = "image")
    })
    List<Venue> getAllLocation(Integer pageSize,Integer pageNo);

    @Select("""
            SELECT * FROM venue where location_name ilike (concat('%',#{nameLocation},'%')) 
            limit #{pageSize} offset #{pageNo}
            """)
    @ResultMap("venue")
    List<Venue> searchVenueByName(String nameLocation,Integer pageSize,Integer pageNo);

    @Select("""
            SELECT image FROM venue where id=#{venueId}
            """)
    String getVenueLogoById(Integer venueId);

    @Select("""
            INSERT INTO venue (location_name,image) values (#{venue.venueName},#{venue.venueLogo})
            returning *
            """)
    @ResultMap("venue")
    Venue createVenue(@Param("venue") VenueRequest venue);

    @Select("""
            SELECT * FROM venue where location_name=#{venueName}
            """)
    @ResultMap("venue")
    Venue searchVenueByNameForCreate(String venueName);


    @Select("""
            update venue set location_name=#{venue.venueName},
                             image=#{venue.venueLogo}
                             where id=#{venueId}
                             returning *
            """)
    @ResultMap("venue")
    Venue updateVenueById(Integer venueId,@Param("venue") VenueRequest venueRequest);

    @Select("""
            DELETE FROM venue where id=#{venueId} returning *
            """)
    @ResultMap("venue")
    Venue deleteVenueById(Integer venueId);
}


