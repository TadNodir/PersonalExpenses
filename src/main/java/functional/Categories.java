package functional;

import java.sql.Date;

/**
 * Class of categories of expenses, which are the main component of the program
 *
 * @author Nodirjon Tadjiev
 * @version 1.0
 */

public class Categories {
    // the following are the attributes, which represent the categories
    private double id;
    private double rent;
    private double food;
    private double entertainment;
    private double clothes;
    private double bills;
    private double hobby;
    private Date date;

    public Categories() {
    }

    /*
    Prints out personal expenses in a following form
     */
    @Override
    public String toString() {
        return "Personal expenses: " + "date=" + date +
                ", rent=" + rent +
                ", food=" + food +
                ", entertainment=" + entertainment +
                ", clothes=" + clothes +
                ", bills=" + bills +
                ", hobby=" + hobby;
    }

    // sets the Id category
    public void setId(double id) {
        this.id = id;
    }

    // sets the rent category
    public void setRent(double rent) {
        this.rent = rent;
    }

    // sets the food category
    public void setFood(double food) {
        this.food = food;
    }

    // sets the entertainment category
    public void setEntertainment(double entertainment) {
        this.entertainment = entertainment;
    }

    // sets the clothes category
    public void setClothes(double clothes) {
        this.clothes = clothes;
    }

    // sets the bills category
    public void setBills(double bills) {
        this.bills = bills;
    }

    // sets the hobby category
    public void setHobby(double hobby) {
        this.hobby = hobby;
    }

    // sets the date category
    public void setDate(Date date) {
        this.date = date;
    }
}
