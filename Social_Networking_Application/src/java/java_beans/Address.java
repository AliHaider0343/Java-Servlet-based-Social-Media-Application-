/**
 *
 * Represents an address associated with a user.
 * This class provides methods to get and set various address properties.
 *
 * @author Jose Omar
 * @version 1.0
 * @since 2023-05-14
 */
package java_beans;

public class Address {

    private String user_email = "";
    private String street = "Not Specified";
    private String town = "Not Specified";
    private String state = "Not Specified";
    private String country = "Not Specified";

    /**
     * Constructs an empty Address object.
     */
    public Address() {
    }

    /**
     * Constructs an Address object with the specified properties.
     *
     * @param user_email the email of the user associated with the address
     * @param street the street name of the address
     * @param town the town name of the address
     * @param state the state name of the address
     * @param country the country name of the address
     */
    public Address(String user_email, String street, String town, String state, String country) {
        this.user_email = user_email;
        this.country = country;
        this.state = state;
        this.street = street;
        this.town = town;
    }

    /**
     * Returns the email of the user associated with the address.
     *
     * @return the email of the user
     */
    public String getUser_email() {
        return user_email;
    }

    /**
     * Sets the email of the user associated with the address.
     *
     * @param user_email the email of the user
     */
    public void setUser_email(String user_email) {
        this.user_email = user_email;
    }

    /**
     * Returns the street name of the address.
     *
     * @return the street name
     */
    public String getStreet() {
        return street;
    }

    /**
     * Sets the street name of the address.
     *
     * @param street the street name
     */
    public void setStreet(String street) {
        this.street = street;
    }

    /**
     * Returns the town name of the address.
     *
     * @return the town name
     */
    public String getTown() {
        return town;
    }

    /**
     * Sets the town name of the address.
     *
     * @param town the town name
     */
    public void setTown(String town) {
        this.town = town;
    }

    /**
     * Returns the state name of the address.
     *
     * @return the state name
     */
    public String getState() {
        return state;
    }

    /**
     * Sets the state name of the address.
     *
     * @param state the state name
     */
    public void setState(String state) {
        this.state = state;
    }

    /**
     * Returns the country name of the address.
     *
     * @return the country name
     */
    public String getCountry() {
        return country;
    }

    /**
     * Sets the country name of the address.
     *
     * @param country the country name
     */
    public void setCountry(String country) {
        this.country = country;
    }
}
