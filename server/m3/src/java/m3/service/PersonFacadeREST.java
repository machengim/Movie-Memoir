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
@Path("m3.person")
public class PersonFacadeREST extends AbstractFacade<Person> {

    @PersistenceContext(unitName = "m3PU")
    private EntityManager em;

    public PersonFacadeREST() {
        super(Person.class);
    }

    @POST
    @Override
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public void create(Person entity) {
        super.create(entity);
    }

    @PUT
    @Path("{id}")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public void edit(@PathParam("id") Integer id, Person entity) {
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
    public Person find(@PathParam("id") Integer id) {
        return super.find(id);
    }

    @GET
    @Override
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Person> findAll() {
        return super.findAll();
    }

    @GET
    @Path("{from}/{to}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Person> findRange(@PathParam("from") Integer from, @PathParam("to") Integer to) {
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
    @Path("fname/{fname}")
    @Produces({"application/json"})
    public List<Person> findByFname(@PathParam("fname") String fname) {
        Query query = em.createNamedQuery("Person.findByFname");
        query.setParameter("fname", fname);
        return query.getResultList();   
    }
    
    @GET
    @Path("lname/{lname}")
    @Produces({"application/json"})
    public List<Person> findByLname(@PathParam("lname") String lname) {
        Query query = em.createNamedQuery("Person.findByLname");
        query.setParameter("lname", lname);
        return query.getResultList();  
    }
    
    @GET
    @Path("gender/{gender}")
    @Produces({"application/json"})
    public List<Person> findByGender(@PathParam("gender") Integer gender) {
        Query query = em.createNamedQuery("Person.findByGender");
        query.setParameter("gender", gender);
        return query.getResultList();  
    }
    
    @GET
    @Path("dob/{dob}")
    @Produces({"application/json"})
    public List<Person> findByDob(@PathParam("dob") String dob) throws ParseException{
        Query query = em.createNamedQuery("Person.findByDob");
        Date date = new SimpleDateFormat("yyyy-MM-dd").parse(dob);
        query.setParameter("dob", date);
        return query.getResultList();  
    }
    
    @GET
    @Path("address/{address}")
    @Produces({"application/json"})
    public List<Person> findByAddress(@PathParam("address") String address) {
        Query query = em.createNamedQuery("Person.findByAddress");
        query.setParameter("address", address);
        return query.getResultList();  
    }
    
    @GET
    @Path("stat/{stat}")
    @Produces({"application/json"})
    public List<Person> findByStat(@PathParam("stat") String stat) {
        Query query = em.createNamedQuery("Person.findByStat");
        query.setParameter("stat", stat);
        return query.getResultList();  
    }
    
    @GET
    @Path("postcode/{postcode}")
    @Produces({"application/json"})
    public List<Person> findByPostcode(@PathParam("postcode") String postcode) {
        Query query = em.createNamedQuery("Person.findByPostcode");
        query.setParameter("postcode", postcode);
        return query.getResultList();  
    }
    
    // Task 3.b: Dynamic query for combination of attributes in Person.
    // Fname, lname and postcode are chosen for this task.
    @GET
    @Path("namepostcode/{fname}/{lname}/{postcode}")
    @Produces({"application/json"})
    public List<Person> findByFnameLnamePostcode(
        @PathParam("fname") String fname,
        @PathParam("lname") String lname,
        @PathParam("postcode") String postcode) {
        String sql = "SELECT p FROM Person p WHERE p.fname = :fname AND"
                + " p.lname = :lname AND p.postcode = :postcode";
        TypedQuery<Person> query = em.createQuery(sql, Person.class);
        query.setParameter("fname", fname);
        query.setParameter("lname", lname);
        query.setParameter("postcode", postcode);
        return query.getResultList();
    }
    
    // Code below are used for phase 2.
    @POST
    @Path("signup")
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces(MediaType.TEXT_PLAIN)
    public String signup(String input) throws ParseException{
        Credential c = getCredentialFromInput(input);
        if (c == null) return "Error: email existed";
        Person p = getPersonFromInput(input);
        System.out.println("address" + p.getAddress());
        c.setPersonid(p);
        
        super.create(p);
        insertCredential(c);
        //CredentialFacadeREST cRest = new CredentialFacadeREST();
        //cRest.create(c);
        
        return "OK";
    }
    
    private int getMaxId() {
        String sql = "SELECT p.personid FROM Person p ORDER BY p.personid DESC";
        TypedQuery<Person> query = em.createQuery(sql, Person.class);
        List cList = query.setMaxResults(1).getResultList();
        
        if (cList == null || cList.size() == 0)
            return 0;
        
        return (int) cList.get(0);
    }
    
    private Credential getCredentialFromInput(String input) {
        java.io.StringReader in = new java.io.StringReader(input);
        JsonReader reader = Json.createReader(in);
        JsonObject jo;
        try {
            jo = reader.readObject();
        } catch (JsonParsingException e) {
            return null;
        }
        
        String email = jo.getString("email");
        String pw = jo.getString("password");
        String signup = jo.getString("signupDate");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        try {
            date = sdf.parse(signup); 
        } catch(ParseException e) {
            e.printStackTrace();
        }
        
        if (checkDuplicateEmail(email)) return null;
        int maxId = getMaxCredentialId();
        Credential c = new Credential(maxId + 1, email, pw, date);
        // Now credential still needs a personid.
        return c;
    }
    
    private Person getPersonFromInput(String input) throws ParseException{
        java.io.StringReader in = new java.io.StringReader(input);
        JsonReader reader = Json.createReader(in);
        JsonObject jo;
        try {
            jo = reader.readObject();
        } catch (JsonParsingException e) {
            return null;
        }
        
        String fname = jo.getString("fname");
        String lname = jo.getString("lname");
        int gender = jo.getInt("gender");
        String dob = jo.getString("dob");
        String address = (jo.getString("address").length() > 0)?
                jo.getString("address"): "unknown";
        String stat = (jo.getString("stat").length() > 0)?
                jo.getString("stat"): "unknown";
        String postcode = (jo.getString("postcode").length() > 0)?
                jo.getString("postcode"): "0000";
             
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date birthday = new Date();
        try {
            birthday = sdf.parse(dob); 
        } catch (ParseException e) {
            System.out.println("no birthday input");
        }
        
        int id = getMaxId();
        Person p = new Person(id+1, fname, lname, gender, birthday,
                address, stat, postcode);
        
        return p;
    }
    
    
    private boolean checkDuplicateEmail(String email) {
        String sql = "SELECT c FROM Credential c WHERE c.email=:email";
        TypedQuery<Credential> query = em.createQuery(sql, Credential.class);
        query.setParameter("email", email);
        List cList = query.getResultList();
        if (cList.size() > 0) return true;
        else return false;
    }

    private int getMaxCredentialId() {
        String sql = "SELECT c.credentialid FROM Credential c ORDER BY c.credentialid DESC";
        TypedQuery<Credential> query = em.createQuery(sql, Credential.class);
        List cList = query.setMaxResults(1).getResultList();
        
        if (cList == null || cList.size() == 0)
            return 0;
        
        return (int) cList.get(0);
    }
    
    private void insertCredential(Credential c) {
        String sql = "INSERT INTO Credential VALUES (?,?,?,?,?)";
        em.createNativeQuery(sql)
                .setParameter(1, c.getCredentialid())
                .setParameter(2, c.getEmail())
                .setParameter(3, c.getHashedpw())
                .setParameter(4, c.getSignupdate())
                .setParameter(5, c.getPersonid().getPersonid())
                .executeUpdate();
    }
    
}
