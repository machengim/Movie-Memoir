package m3;

import java.io.Serializable;

/**
 *
 * @author ma
 * Used for task 4.d and 4.e
 */
public class MovieRelease implements Serializable {
    
    private static final long serialVersionUID = 1L;
    private String name;
    private String releaseYear;

    public MovieRelease() {
    }

    public MovieRelease(String name, String releaseYear) {
        this.name = name;
        this.releaseYear = releaseYear;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getReleaseYear() {
        return releaseYear;
    }

    public void setReleaseYear(String releaseYear) {
        this.releaseYear = releaseYear;
    }

}
