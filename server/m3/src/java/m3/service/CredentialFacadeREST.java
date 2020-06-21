/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package m3.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
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
import m3.Credential;
import m3.Person;

/**
 *
 * @author ma
 */
@Stateless
@Path("m3.credential")
public class CredentialFacadeREST extends AbstractFacade<Credential> {

    @PersistenceContext(unitName = "m3PU")
    private EntityManager em;

    public CredentialFacadeREST() {
        super(Credential.class);
    }

    @POST
    @Override
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public void create(Credential entity) {
        super.create(entity);
    }

    @PUT
    @Path("{id}")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public void edit(@PathParam("id") Integer id, Credential entity) {
        super.edit(entity);
    }

    @DELETE
    @Path("{id}")
    public void remove(@PathParam("id") Integer id) {
        super.remove(super.find(id));
    }

    @GET
    @Path("{id}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Credential find(@PathParam("id") Integer id) {
        return super.find(id);
    }

    @GET
    @Override
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Credential> findAll() {
        return super.findAll();
    }

    @GET
    @Path("{from}/{to}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Credential> findRange(@PathParam("from") Integer from, @PathParam("to") Integer to) {
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
    // Didn't implement findByHashedPassword(), since it doesn't make sense.
    @GET
    @Path("email/{email}")
    @Produces({"application/json"})
    public List<Credential> findByEmail(@PathParam("email") String email) {
        Query query = em.createNamedQuery("Credential.findByEmail");
        query.setParameter("email", email);
        return query.getResultList();   
    }
    
    @GET
    @Path("signupdate/{signupdate}")
    @Produces({"application/json"})
    public List<Credential> findBySignupdate(@PathParam("signupdate") String signupdate) throws ParseException{
        Query query = em.createNamedQuery("Credential.findBySignupdate");
        Date date = new SimpleDateFormat("yyyy-MM-dd").parse(signupdate);
        query.setParameter("signupdate", date);
        return query.getResultList();   
    }
    
   // Code below are used for phase 2.
    @POST
    @Path("login")
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces(MediaType.TEXT_PLAIN)
    public String login(String input) {
        java.io.StringReader in = new java.io.StringReader(input);
        JsonReader reader = Json.createReader(in);
        JsonObject jo;
        try {
            jo = reader.readObject();
        } catch (JsonParsingException e) {
            return "-1";
        }
        
        String email = jo.getString("email");
        String password = jo.getString("password");
        int id = getPersonLogin(email, password);
        System.out.println(id);
        return Integer.toString(id);
    }
    
    private int getPersonLogin(String email, String password) {
        String sql = "SELECT c FROM Credential c WHERE c.email=:email AND " +
                "c.hashedpw=:password";
        TypedQuery<Credential> query = em.createQuery(sql, Credential.class);
        query.setParameter("email", email);
        query.setParameter("password", password);
        
        List<Credential> cList = query.getResultList();
        
        if (cList == null || cList.size() == 0)
            return -1;
        
        Person p = cList.get(0).getPersonid();
        
        return p.getPersonid();
    }
    
    private boolean checkDuplicateEmail(String email) {
        String sql = "SELECT c FROM Credential c WHERE c.email=:email";
        TypedQuery<Credential> query = em.createQuery(sql, Credential.class);
        List cList = query.getResultList();
        if (cList.size() > 0) return true;
        else return false;
    }
    
    private int getMaxId() {
        String sql = "SELECT c FROM Credential c ORDER BY c.credentialid DESC";
        TypedQuery<Credential> query = em.createQuery(sql, Credential.class);
        List cList = query.setMaxResults(1).getResultList();
        
        if (cList == null || cList.size() == 0)
            return 0;
        
        return (int) cList.get(0);
    }
}
