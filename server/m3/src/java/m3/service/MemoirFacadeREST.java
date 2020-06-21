/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package m3.service;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Year;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
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
import m3.CinemaCount;
import m3.Memoir;
import m3.MonthCount;
import m3.MovieInfo;
import m3.MovieRelease;
import m3.Person;

/**
 *
 * @author ma
 */
@Stateless
@Path("m3.memoir")
public class MemoirFacadeREST extends AbstractFacade<Memoir> {

    @PersistenceContext(unitName = "m3PU")
    private EntityManager em;

    public MemoirFacadeREST() {
        super(Memoir.class);
    }

    @POST
    @Override
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public void create(Memoir entity) {
        super.create(entity);
    }

    @PUT
    @Path("{id}")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public void edit(@PathParam("id") Integer id, Memoir entity) {
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
    public Memoir find(@PathParam("id") Integer id) {
        return super.find(id);
    }

    @GET
    @Override
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Memoir> findAll() {
        return super.findAll();
    }

    @GET
    @Path("{from}/{to}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Memoir> findRange(@PathParam("from") Integer from, @PathParam("to") Integer to) {
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
    @Path("moviename/{moviename}")
    @Produces({"application/json"})
    public List<Memoir> findByMoviename(@PathParam("moviename") String moviename) {
        Query query = em.createNamedQuery("Memoir.findByMoviename");
        query.setParameter("moviename", moviename);
        return query.getResultList();   
    }
    
    @GET
    @Path("moviereleasedate/{moviereleasedate}")
    @Produces({"application/json"})
    public List<Memoir> findByMoviereleasedate(@PathParam("moviereleasedate") String moviereleasedate) throws ParseException{
        Query query = em.createNamedQuery("Memoir.findByMoviereleasedate");
        Date date = new SimpleDateFormat("yyyy-MM-dd").parse(moviereleasedate);
        query.setParameter("moviereleasedate", date);
        return query.getResultList();   
    }
    
    @GET
    @Path("watchtime/{watchtime}")
    @Produces({"application/json"})
    public List<Memoir> findByMoviewatchtime(@PathParam("watchtime") String watchtime) throws ParseException{
        Query query = em.createNamedQuery("Memoir.findByMoviewatchtime");
        Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(watchtime);
        query.setParameter("watchtime", date);
        return query.getResultList();   
    }
    
    @GET
    @Path("comment/{comment}")
    @Produces({"application/json"})
    public List<Memoir> findByComment(@PathParam("comment") String comment) {
        Query query = em.createNamedQuery("Memoir.findByComment");
        query.setParameter("comment", comment);
        return query.getResultList();   
    }
    
    @GET
    @Path("score/{score}")
    @Produces({"application/json"})
    public List<Memoir> findByScore(@PathParam("score") Float score) {
        Query query = em.createNamedQuery("Memoir.findByScore");
        query.setParameter("score", score);
        return query.getResultList();   
    }
    
    // Task 3.c: Dynamic query memoir and cinema with implicit join.
    // Cinema name from cinema table and movie name from memoir table are used in this task.
    @GET
    @Path("moviecinemanames/{moviename}/{cinemaname}")
    @Produces({"application/json"})
    public List<Memoir> findByMovieAndCinema(
        @PathParam("moviename") String moviename,
        @PathParam("cinemaname") String cinemaname) {
        String sql = "SELECT m FROM Memoir m WHERE m.moviename=:moviename AND "
                    + "m.cinemaid.name = :cinemaname";
        TypedQuery<Memoir> query = em.createQuery(sql, Memoir.class);
        query.setParameter("moviename", moviename);
        query.setParameter("cinemaname", cinemaname);
        return query.getResultList();
    }
    
    // Task 3.d: Static query memoir and cinema with implicit join.
    // Cinema postcode from cinema table and movie name from memoir table are used in this task.
    @GET
    @Path("moviecinemapostcode/{moviename}/{postcode}")
    @Produces({"application/json"})
    public List<Memoir> findByMovieAndCinemaPostcode (
        @PathParam("moviename") String moviename,
        @PathParam("postcode") String postcode) {
        Query query = em.createNamedQuery("Memoir.findByMovieAndCinamePostcode");
        query.setParameter("moviename", moviename);
        query.setParameter("postcode", postcode);
        return query.getResultList();
    }

    // Task 4.a: input person id, start date and ending date,
    // Output a list contains cinema's postcodes and the count of movies watched.
    @GET
    @Path("personAndDate/{personid}/{start}/{end}")
    @Produces({"application/json"})
    public List<CinemaCount> findByPersonAndDate (
        @PathParam("personid") Integer personid,
        @PathParam("start") String start,
        @PathParam("end") String end
    ) throws ParseException {
        String sql = "SELECT m.cinemaid.postcode, COUNT(m) FROM Memoir m WHERE m.personid.personid = :personid AND "
                + " m.watchtime > :startDate AND m.watchtime < :endDate GROUP BY m.cinemaid.postcode";
        Query query = em.createQuery(sql);
        Date startDate = new SimpleDateFormat("yyyy-MM-dd").parse(start);
        Date endDate = new SimpleDateFormat("yyyy-MM-dd").parse(end);
        query.setParameter("personid", personid);
        query.setParameter("startDate", startDate);
        query.setParameter("endDate", endDate);
        List memoirList = query.getResultList();
        List results = new ArrayList();
        Iterator it = memoirList.iterator();
        while(it.hasNext()) {
            Object[] obj = (Object[]) it.next();
            String postcode = String.valueOf(obj[0]);
            Integer count = Integer.parseInt(String.valueOf(obj[1]));
            System.out.println(postcode + " and " + count);
            results.add(new CinemaCount(postcode, count));
        }
        
        return results;
    }
    
    //Task 4.b, Input a person id and a year,
    //return the month names and the movies watched in that month.
    @GET
    @Path("personAndYear/{personid}/{year}")
    @Produces({"application/json"})
    public List<MonthCount> findByPersonidAndyear(
        @PathParam("personid") Integer personid,
        @PathParam("year") Integer year) {
        String sql = "SELECT EXTRACT(MONTH FROM m.watchtime), COUNT(m) FROM Memoir m WHERE m.personid.personid = :personid AND "
                + " EXTRACT(YEAR FROM m.watchtime) = :year GROUP BY EXTRACT(MONTH FROM m.watchtime)";
        Query query = em.createQuery(sql);
        query.setParameter("personid", personid);
        query.setParameter("year", year);
        List memoirList = query.getResultList();
        
        // Use an array to store the counts for all months.
        int[] counts = new int[12];
        Arrays.fill(counts, 0);
        Iterator it = memoirList.iterator();
        while(it.hasNext()) {
            Object[] obj = (Object[]) it.next();
            Integer monthIndex = Integer.parseInt(String.valueOf(obj[0]));
            Integer count = Integer.parseInt(String.valueOf(obj[1]));
            counts[monthIndex - 1] = count;
        }
        
        // Use an arraylist to store the MonthCount Objects to return.
        List<MonthCount> results = new ArrayList();
        String[] months = new DateFormatSymbols().getMonths();
        for(int i = 0; i< 12; i++) {
            results.add(new MonthCount(months[i], counts[i]));
        }
        
        return results;
    }
    
    //Task 4.c, input a person id and return the movie(s) info with highest score.
    // TODO: person id!
    @GET
    @Path("topScore/{personid}")
    @Produces({"application/json"})
    public List<MovieInfo> findFavoriteByPerson(
        @PathParam("personid") Integer personid ) {
        String sql = "SELECT m FROM Memoir m WHERE m.personid.personid = :personid AND"
                + " m.score=(SELECT MAX(m2.score) FROM Memoir m2 WHERE m2.personid.personid = :personid)";
        TypedQuery query = em.createQuery(sql, Memoir.class);
        query.setParameter("personid", personid);
        List<Memoir> memoirList = query.getResultList();
        List<MovieInfo> results = new ArrayList();
        for (Memoir m: memoirList) {
            results.add(new MovieInfo(m.getMoviename(), m.getScore(), m.getMoviereleasedate()));
        }
        
        return results;
    }
    
    // Task 4.d, input a person id and return a movie list
    // where its release year == watch year.
    @GET
    @Path("sameYearWatch/{personid}")
    @Produces({"application/json"})
    public List<MovieRelease> findSameYearWatchByPerson(
        @PathParam("personid") Integer personid) {
        String sql = "SELECT m FROM Memoir m WHERE m.personid.personid = :personid"
                + " AND EXTRACT(YEAR FROM m.watchtime) = EXTRACT(YEAR FROM m.moviereleasedate)";
        TypedQuery<Memoir> query = em.createQuery(sql, Memoir.class);
        query.setParameter("personid", personid);
        List<Memoir> memoirList = query.getResultList();
        
        List<MovieRelease> results = new ArrayList();
        for (Memoir m: memoirList) {
            String year = new SimpleDateFormat("yyyy").format(m.getMoviereleasedate());
            results.add(new MovieRelease(m.getMoviename(), year));
        }
        
        return results;
    }
    
    // Task 4.e, input a person id and return a movie list where this user has
    // watched its remake version.
    @GET
    @Path("watchRemake/{personid}")
    @Produces({"application/json"})
    public List<MovieRelease> findWatchedRemake(
        @PathParam("personid") Integer personid) {
        String sql = "SELECT m.moviename, m.moviereleasedate FROM Memoir m WHERE m.personid.personid = :personid"
                + " GROUP BY m.moviename, m.moviereleasedate";
        Query query = em.createQuery(sql);
        query.setParameter("personid", personid);
        List mList = query.getResultList();
        
        // Use list temp to hold all query results, and list results to hold the movies with remake versions to return.
        List<MovieRelease> temp = new ArrayList();
        List<MovieRelease> results = new ArrayList();

        Iterator it = mList.iterator();
        while(it.hasNext()) {
            Object[] obj = (Object[]) it.next();
            String name = String.valueOf(obj[0]);
            String release = new SimpleDateFormat("yyyy").format(obj[1]);
            temp.add(new MovieRelease(name, release));
        }
        
        temp.sort(Comparator.comparing(a -> a.getName()));
        int size = temp.size();
        if (size < 2) return null;
        for(int i = 0; i < size; i++) {
            String name = temp.get(i).getName();
            if (i == 0 && name.equals(temp.get(i + 1).getName())) {
                results.add(temp.get(i));
            }
            else if (i == size - 1 && name.equals(temp.get(i - 1).getName())) {
                results.add(temp.get(i));
            }
            else if (i != 0 && i != size - 1 && (name.equals(temp.get(i + 1).getName()) || name.equals(temp.get(i - 1).getName()))){
                results.add(temp.get(i));
            }
        }
        
        return results;
    }

    // Task 4.f, input a person id and return a 5 movies in recent year
    // and have the highest rating score.
    @GET
    @Path("topFive/{personid}")
    @Produces({"application/json"})
    public List<MovieInfo> findTopFive(
        @PathParam("personid") Integer personid) {
        String sql = "SELECT m FROM Memoir m WHERE m.personid.personid = :personid "
                + "AND EXTRACT(YEAR FROM m.watchtime) = 2020 ORDER BY m.score DESC";
        TypedQuery<Memoir> query = em.createQuery(sql, Memoir.class);
        query.setParameter("personid", personid);
        
        List<Memoir> mList = query.setMaxResults(5).getResultList();
        List<MovieInfo> results = new ArrayList();
        for (Memoir m: mList) {
            results.add(new MovieInfo(m.getMoviename(), m.getScore(), m.getMoviereleasedate()));
        }
        
        return results;
    }
    
    // Code below are used for phase 2.
    // Accept a cinema name and postcode json as input,
    // check whether it exists in the database and insert into database.
    @POST
    @Path("new")
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces(MediaType.TEXT_PLAIN)
    public String insertNewCinema(String input) throws ParseException {
        java.io.StringReader in = new java.io.StringReader(input);
        JsonReader reader = Json.createReader(in);
        JsonObject jo;
        try {
            jo = reader.readObject();
        } catch (JsonParsingException e) {
            return "Error: wrong format.";
        }
        
        String name = jo.getString("moviename");
        String release = jo.getString("release");
        String watch = jo.getString("watch");
        String comment = jo.getString("comment");
        int uid = jo.getInt("uid");
        int cid = jo.getInt("cid");
        int mid = jo.getInt("mid");
        float rating = Float.valueOf(jo.getString("rating"));
        int max = getMaxMemoirId();
        
        Date releaseDate = new Date(), watchTime = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            releaseDate = sdf.parse(release);
        } catch (ParseException e) {
            return "Error: release date format error.";
        }
        
        sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm");
        try {
            watchTime = sdf.parse(watch);
        } catch(ParseException e) {
            return "Error: watch time format error.";
        }
        
        Cinema c = getCinemaById(cid);
        Person p = getPersonById(uid);
        
        Memoir m = new Memoir(max + 1, name, releaseDate, watchTime, comment,
            rating, c, p, mid);
        create(m);
        
        return "Success";
    }
    
    private int getMaxMemoirId() {
        String sql = "SELECT c.memoirid FROM Memoir c ORDER BY c.memoirid DESC";
        TypedQuery<Memoir> query = em.createQuery(sql, Memoir.class);
        List cList = query.setMaxResults(1).getResultList();
        
        if (cList == null || cList.size() == 0)
            return 0;
        
        return (int) cList.get(0);
    }
    
    private Cinema getCinemaById(int id) {
        String sql = "SELECT c FROM Cinema c WHERE c.cinemaid = :id";
        TypedQuery<Cinema> query = em.createQuery(sql, Cinema.class);
        query.setParameter("id", id);
        List<Cinema> cList = query.getResultList();
        
        if (cList == null || cList.size() == 0)
            return null;
        
        System.out.println(cList.get(0).getName());
        return cList.get(0);
    }
    
    private Person getPersonById(int id) {
        String sql = "SELECT p FROM Person p WHERE p.personid = :id";
        TypedQuery<Person> query = em.createQuery(sql, Person.class);
        query.setParameter("id", id);
        List<Person> cList = query.getResultList();
        
        if (cList == null || cList.size() == 0)
            return null;
        
        return cList.get(0);
    }
    
    // Retrive memoir list by person's id.
    @GET
    @Path("person/{pid}")
    @Produces({"application/json"})
    public List<Memoir> findByPersonId(
        @PathParam("pid") Integer pid) {
        String sql = "SELECT m FROM Memoir m WHERE m.personid.personid =:pid";
        TypedQuery<Memoir> query = em.createQuery(sql, Memoir.class);
        query.setParameter("pid", pid);
        return query.getResultList();
    }
}
