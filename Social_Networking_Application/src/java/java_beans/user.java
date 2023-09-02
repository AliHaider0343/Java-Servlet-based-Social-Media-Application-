/**

The {@code User} class represents a user in the system.
It stores various information about the user such as their name, date of birth, gender, email, profile picture, location, password, and admin status.
It also contains lists of addresses and educations associated with the user.
* @author Jose Omar
 * @version 1.0
 * @since 2023-05-14
*/

package java_beans;

import java.sql.Blob;
import java.util.ArrayList;

public class user {private String name;
private String dob;
private String gender;
private String user_email;
private Blob profile_pic;
private String location;
private String password;
private int isAdmin;
public ArrayList<Address> addresses = null;
public ArrayList<education> educations = null;

/**
 * Retrieves the addresses associated with the user.
 *
 * @return a string representation of the user's addresses.
 */
public String getAddresses() {
    String addresses = "";
    for (int i = 0; i < this.addresses.size(); i++) {
        addresses += "Lives at " + this.addresses.get(i).getStreet() + ", " +
                this.addresses.get(i).getTown() + ", " +
                this.addresses.get(i).getState() + ", " +
                this.addresses.get(i).getCountry() + "\n";
    }
    return addresses;
}

/**
 * Sets the addresses for the user.
 *
 * @param addresses the addresses to be associated with the user.
 */
public void setAddresses(ArrayList<Address> addresses) {
    this.addresses = addresses;
}

/**
 * Retrieves the educations associated with the user.
 *
 * @return a string representation of the user's educations.
 */
public String getEducations() {
    String educations = "";
    for (int i = 0; i < this.educations.size(); i++) {
        educations += "Studied " + this.educations.get(i).getDegree() + " at " +
                this.educations.get(i).getSchool() + "\n";
    }
    return educations;
}

/**
 * Sets the educations for the user.
 *
 * @param educations the educations to be associated with the user.
 */
public void setEducations(ArrayList<education> educations) {
    this.educations = educations;
}

/**
 * Constructs an empty user object.
 */
public user() {

}

/**
 * Constructs a user object with the specified information.
 *
 * @param name         the name of the user.
 * @param dob          the date of birth of the user.
 * @param gender       the gender of the user.
 * @param user_email   the email of the user.
 * @param profile_pic  the profile picture of the user.
 * @param location     the location of the user.
 * @param password     the password of the user.
 * @param isAdmin      the admin status of the user.
 */
public user(String name, String dob, String gender, String user_email, Blob profile_pic, String location, String password, int isAdmin) {
    this.name = name;
    this.dob = dob;
    this.gender = gender;
    this.user_email = user_email;
    this.profile_pic = profile_pic;
    this.location = location;
    this.password = password;
    this.isAdmin = isAdmin;
}

/**
 * Retrieves the admin status of the user.
 *
 * @return the admin status of the user.
 */
public int getIsAdmin() {
return isAdmin;
}
/**
 * Sets the admin status for the user.
 *
 * @param isAdmin the admin status to be set for the user.
 */
public void setIsAdmin(int isAdmin) {
    this.isAdmin = isAdmin;
}

/**
 * Retrieves the name of the user.
 *
 * @return the name of the user.
 */
public String getName() {
    return name;
}

/**
 * Sets the name for the user.
 *
 * @param name the name to be set for the user.
 */
public void setName(String name) {
    this.name = name;
}

/**
 * Retrieves the password of the user.
 *
 * @return the password of the user.
 */
public String getPassword() {
    return password;
}

/**
 * Sets the password for the user.
 *
 * @param password the password to be set for the user.
 */
public void setPassword(String password) {
    this.password = password;
}

/**
 * Retrieves the date of birth of the user.
 *
 * @return the date of birth of the user.
 */
public String getDob() {
    return dob;
}

/**
 * Sets the date of birth for the user.
 *
 * @param dob the date of birth to be set for the user.
 */
public void setDob(String dob) {
    this.dob = dob;
}

/**
 * Retrieves the gender of the user.
 *
 * @return the gender of the user.
 */
public String getGender() {
    return gender;
}

/**
 * Sets the gender for the user.
 *
 * @param gender the gender to be set for the user.
 */
public void setGender(String gender) {
    this.gender = gender;
}

/**
 * Retrieves the email of the user.
 *
 * @return the email of the user.
 */
public String getUser_email() {
    return user_email;
}

/**
 * Sets the email for the user.
 *
 * @param user_email the email to be set for the user.
 */
public void setUser_email(String user_email) {
    this.user_email = user_email;
}

/**
 * Retrieves the profile picture of the user.
 *
 * @return the profile picture of the user.
 */
public Blob getProfile_pic() {
    return profile_pic;
}

/**
 * Sets the profile picture for the user.
 *
 * @param profile_pic the profile picture to be set for the user.
 */
public void setProfile_pic(Blob profile_pic) {
    this.profile_pic = profile_pic;
}

/**
 * Retrieves the location of the user.
 *
 * @return the location of the user.
 */
public String getLocation() {
    return location;
}

/**
 * Sets the location for the user.
 *
 * @param location the location to be set for the user.
 */
public void setLocation(String location) {
    this.location = location;
}
}