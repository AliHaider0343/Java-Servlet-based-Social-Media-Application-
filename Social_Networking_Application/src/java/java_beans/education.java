/**

Represents the education details of a user.
This class provides methods to get and set various education properties.
@author Jose Omar
@version 1.0
@since 2023-05-14
*/
package java_beans;
public class education {private String user_email = "";
private String degree = "Not Specified";
private String school = "Not Specified";

/**
 * Constructs an Education object with the specified properties.
 *
 * @param user_email the email of the user associated with the education
 * @param degree     the degree obtained by the user
 * @param school     the name of the school attended by the user
 */
public education(String user_email, String degree, String school) {
    this.user_email = user_email;
    this.degree = degree;
    this.school = school;
}

/**
 * Constructs an empty Education object.
 */
public education() {
}

/**
 * Returns the email of the user associated with the education.
 *
 * @return the email of the user
 */
public String getUser_email() {
    return user_email;
}

/**
 * Sets the email of the user associated with the education.
 *
 * @param user_email the email of the user
 */
public void setUser_email(String user_email) {
    this.user_email = user_email;
}

/**
 * Returns the degree obtained by the user.
 *
 * @return the degree obtained
 */
public String getDegree() {
    return degree;
}

/**
 * Sets the degree obtained by the user.
 *
 * @param degree the degree obtained
 */
public void setDegree(String degree) {
    this.degree = degree;
}

/**
 * Returns the name of the school attended by the user.
 *
 * @return the name of the school
 */
public String getSchool() {
    return school;
}

/**
 * Sets the name of the school attended by the user.
 *
 * @param school the name of the school
 */
public void setSchool(String school) {
    this.school = school;
}
}