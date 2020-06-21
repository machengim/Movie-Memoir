package m3;

import java.io.Serializable;

/**
 *
 * @author ma
 * Used only for task 4.b
 */
public class MonthCount implements Serializable {
    
    private static final long serialVersionUID = 1L;
    private String monthName;
    private int count;

    public MonthCount() {
    }

    
    
    public MonthCount(String monthName, int count) {
        this.monthName = monthName;
        this.count = count;
    }

    public String getMonthName() {
        return monthName;
    }

    public void setMonthName(String monthName) {
        this.monthName = monthName;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
    
    
    
}
