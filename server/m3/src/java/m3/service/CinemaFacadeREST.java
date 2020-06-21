/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package m3.service;

import java.util.List;
import javax.ejb.Stateless;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.stream.JsonParsingException;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import m3.Cinema;

/**
 *
 * @author ma
 */
@Stateless
@Path("m3.cinema")
public class CinemaFacadeREST extends AbstractFacade<Cinema> {

    @PersistenceContext(unitName = "m3PU")
    private EntityManager em;

    public CinemaFacadeREST() {
        super(Cinema.class);
    }

    @POST
    @Override
    @Consumes({MediaType.APPLICATION_JSON})
    public void create(Cinema entity) {
        super.create(entity);
    }

    @PUT
    @Path("{id}")
    @Consumes({MediaType.APPLICATION_JSON})
    public void edit(@PathParam("id") Integer id, Cinema entity) {
        super.edit(entity);
    }

    @DELETE
    @Path("{id}")
    public void remove(@PathParam("id") Integer id) {
        super.remove(super.find(id));
    }

    @GET
    @Path("{id}")
    @Produces({MediaType.APPLICATION_JSON})
    public Cinema find(@PathParam("id") Integer id) {
        return super.find(id);
    }

    @GET
    @Override
    @Produces({MediaType.APPLICATION_JSON})
    public List<Cinema> findAll() {
        return super.findAll();
    }

    @GET
    @Path("{from}/{to}")
    @Produces({MediaType.APPLICATION_JSON})
    public List<Cinema> findRange(@PathParam("from") Integer from, @PathParam("to") Integer to) {
        return super.findRange(new int[]{from, to});
    }

    @GET
    @Path("count")
    @Produces(MediaType.TEXT_PLAIN)
    public String countREST() {
        return String.valueOf(super.count());
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
    
    // Codes below are required by the assessment.
    // Task 3.a: Querying each attribute in the table.
    @GET
    @Path("name/{name}")
    @Produces({"application/json"})
    public List<Cinema> findByName(@PathParam("name") String name) {
        Query query = em.createNamedQuery("Cinema.findByName");
        query.setParameter("name", name);
        return query.getResultList();   
    }
    
    @GET
    @Path("postcode/{postcode}")
    @Produces({"application/json"})
    public List<Cinema> findByPostcode(@PathParam("postcode") String postcode) {
        Query query = em.createNamedQuery("Cinema.findByPostcode");
        query.setParameter("postcode", postcode);
        return query.getResultList();   
    }
    
    // Code below are used for phase 2.
    // Accept a cinema name and postcode json as input,
    // check whether it exists in the database and insert into database.
    @POST
    @Path("newcinema")
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces(MediaType.TEXT_PLAIN)
    public String insertNewCinema(String input) {
        java.io.StringReader in = new java.io.StringReader(input);
        JsonReader reader = Json.createReader(in);
        JsonObject jo;
        try {
            jo = reader.readObject();
        } catch (JsonParsingException e) {
            return "Error: wrong format.";
        }
        
        String name = jo.getString("name");
        String postcode = jo.getString("postcode");
        
        if (name == null || postcode == null)
            return "Error: wrong format.";
        if (checkCinemaExist(name, postcode))   
            return "Error: already exist.";
        
        int maxId = getMaxCinemaId();
        Cinema cinema = new Cinema(maxId + 1, name, postcode);
        create(cinema);
        return "Success.";
    }
    
    private boolean checkCinemaExist(String name, String postcode) {
        String sql = "SELECT c FROM Cinema c WHERE c.name = :name AND c.postcode"
                + "= :postcode";
        TypedQuery<Cinema> query = em.createQuery(sql, Cinema.class);
        query.setParameter("name", name);
        query.setParameter("postcode", postcode);
        List<Cinema> cList = query.getResultList();
        
        if (cList != null && cList.size() > 0) 
            return true;
        else 
            return false;
    }
    
    private int getMaxCinemaId() {
        String sql = "SELECT c.cinemaid FROM Cinema c ORDER BY c.cinemaid DESC";
        TypedQuery<Cinema> query = em.createQuery(sql, Cinema.class);
        List cList = query.setMaxResults(1).getResultList();
        
        if (cList == null || cList.size() == 0)
            return 0;
        
        return (int) cList.get(0);
    }
    
}
