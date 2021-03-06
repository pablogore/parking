package com.spothero.api;


import com.codahale.metrics.annotation.Timed;
import com.spothero.grpc.CreateResponse;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.Arrays;

/**
 * This class represent the api resources for calculate the parking rate
 */
@Path("/rate")
public class RateParkingResource {


    @GET
    @Path("{from}/{to}")
    @Produces({MediaType.APPLICATION_XML})
    @Timed
    public Rates xml(@PathParam("from") String from , @PathParam("to") String to) throws InterruptedException {
        return buildResponse(from,to);
    }


    @GET
    @Path("{from}/{to}")
    @Produces({MediaType.APPLICATION_JSON})
    @Timed
    public Rates json(@PathParam("from") String from ,@PathParam("to") String to) throws InterruptedException {
        return buildResponse(from,to);
    }

    private Rates buildResponse(String from , String to) throws InterruptedException {
        CreateResponse response = GrpcClient.Calculate(from,to);

        java.util.List<com.spothero.grpc.Rate> list = response.getRateList();

        Rates rates = new Rates();

        list.stream().forEach(r->{
            Rate rate = new Rate();
            rate.setDays(Arrays.asList(r.getDays().split(",")));
            rate.setPrice(r.getPrice());
            rate.setTimes(r.getTime());

            rates.add(rate);
        });

        return rates ;
    }
}
