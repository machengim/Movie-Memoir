package m3;

import java.io.Serializable;

/**
 *
 * @author ma
 * Used only for task 4.a
 */
public class CinemaCount implements Serializable {

    private static final long serialVersionUID = 1L;
    private String postcode;
    private int count;

    
    public CinemaCount() {
    }

    public CinemaCount(String postcode, int count) {
        this.postcode = postcode;
        this.count = count;
    }



    public String getPostcode() {
        return postcode;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

}
