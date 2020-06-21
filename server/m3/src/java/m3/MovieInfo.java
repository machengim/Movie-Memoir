package m3;

import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author ma
 * Used only for task 4.c and 4.f
 */
public class MovieInfo implements Serializable{
    
    private static final long serialVersionUID = 1L;
    private String name;
    private float score;
    private Date releaseDate;

    public MovieInfo() {
    }

    public MovieInfo(String name, float score, Date releaseDate) {
        this.name = name;
        this.score = score;
        this.releaseDate = releaseDate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getScore() {
        return score;
    }

    public void setScore(float score) {
        this.score = score;
    }

    public Date getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(Date releaseDate) {
        this.releaseDate = releaseDate;
    }
    
    
    
}
