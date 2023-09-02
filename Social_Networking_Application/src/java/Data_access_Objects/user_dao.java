/**
 *
 * Data Access Object for user operations.
 * <p>
 * This class provides methods to perform various operations related to users in
 * the database, such as adding a user, adding a friend, removing a friend,
 * adding user address, adding user education, checking if a user exists,
 * hashing a password, getting all friends of a user, getting all users,
 * checking if a user is a friend, checking if a user matches a given location
 * or gender, authenticating a user, getting information of a single user,
 * getting addresses of a user, getting education of a user, deleting a user,
 * deleting user profile picture, and updating a user's information.
 * <p>
 * The user_dao class uses a connection from the ConnectionFactory class to
 * interact with the database.
 *
 * @author Jose Omar
 * @version 1.0
 * @since 2023-05-14
 */
package Data_access_Objects;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Base64;
import java_beans.Address;
import java_beans.education;
import java_beans.user;

public class user_dao {

    private Connection connection;

    /**
     * Constructs a new user_dao object.
     */
    public user_dao() {
        connection = ConnectionFactory.getConnection();
    }

    /**
     * Adds a user to the database.
     *
     * @param user the user to be added
     * @return the number of rows affected in the database
     * @throws IOException if an I/O exception occurs
     * @throws SQLException if an SQL exception occurs
     */
    public int addUser(user user) throws IOException, SQLException {
        int rowsAffected = 0;

        try ( PreparedStatement preparedStatement = connection.prepareStatement("insert into user(name,dob,gender,user_email,profile_pic,location,password,isadmin) values (?,?, ?, ?, ?, ?, ?, ? )")) {
            // Parameters start with 1
            preparedStatement.setString(1, user.getName());
            preparedStatement.setString(2, user.getDob());
            preparedStatement.setString(3, user.getGender());
            preparedStatement.setString(4, user.getUser_email());
            preparedStatement.setBlob(5, user.getProfile_pic());
            preparedStatement.setString(6, user.getLocation());
            String passwordHash = hashPassword(user.getPassword());
            preparedStatement.setString(7, passwordHash);
            preparedStatement.setInt(8, user.getIsAdmin());
            rowsAffected = preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println(e);

        }
        return rowsAffected;
    }

    /**
     * Adds a friend to a user's friend list.
     *
     * @param logged_user the email of the logged-in user
     * @param friend_email the email of the friend to be added
     * @return the number of rows affected in the database
     * @throws IOException if an I/O exception occurs
     * @throws SQLException if an SQL exception occurs
     */
    public int addFriend(String logged_user, String friend_email) throws IOException, SQLException {
        int rowsAffected = 0;

        try ( PreparedStatement preparedStatement = connection.prepareStatement("insert into user_friends(user_email,friend_email) values (?,?)")) {
            // Parameters start with 1
            preparedStatement.setString(1, logged_user);
            preparedStatement.setString(2, friend_email);

            rowsAffected = preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rowsAffected;
    }

    /**
     * Removes a friend from the user's friend list.
     *
     * @param logged_user the user's email
     * @param friend_email the friend's email
     * @return the number of rows affected in the database
     * @throws IOException if an I/O error occurs
     * @throws SQLException if a database access error occurs
     */
    public int RemoveFriend(String logged_user, String friend_email) throws IOException, SQLException {
        int rowsAffected = 0;

        try ( PreparedStatement preparedStatement = connection.prepareStatement("Delete from user_friends where user_email= ? and friend_email =? or user_email= ? and friend_email =?")) {
            preparedStatement.setString(1, logged_user);
            preparedStatement.setString(2, friend_email);
            preparedStatement.setString(3, friend_email);
            preparedStatement.setString(4, logged_user);
            rowsAffected = preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rowsAffected;
    }

    /**
     * Adds a user address to the database.
     *
     * @param user_email the email of the user
     * @param user_address the address object containing user's address details
     * @return the number of rows affected in the database
     * @throws IOException if an I/O error occurs while interacting with the
     * database
     * @throws SQLException if a database access error occurs
     */
    public int addUserAddress(String user_email, Address user_address) throws IOException, SQLException {
        int rowsAffected = 0;

        try ( PreparedStatement preparedStatement = connection.prepareStatement("insert into addresses(user_email ,street,town,state,country) values (?,?, ?, ?, ? ) ")) {
            // Parameters start with 1
            preparedStatement.setString(1, user_email);
            preparedStatement.setString(2, user_address.getStreet());
            preparedStatement.setString(3, user_address.getTown());
            preparedStatement.setString(4, user_address.getState());
            preparedStatement.setString(5, user_address.getCountry());
            rowsAffected = preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return rowsAffected;
    }

    /**
     * Adds user education information to the database.
     *
     * @param user_email the email of the user
     * @param user_education the education object containing user's education
     * details
     * @return the number of rows affected in the database
     * @throws IOException if an I/O error occurs while interacting with the
     * database
     * @throws SQLException if a database access error occurs
     */
    public int addUserEducation(String user_email, education user_education) throws IOException, SQLException {
        int rowsAffected = 0;

        try ( PreparedStatement preparedStatement = connection.prepareStatement("insert into education(user_email ,degree,school) values (?,?, ?) ")) {
            // Parameters start with 1
            preparedStatement.setString(1, user_email);
            preparedStatement.setString(2, user_education.getDegree());
            preparedStatement.setString(3, user_education.getSchool());

            rowsAffected = preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return rowsAffected;
    }

    /**
     * Checks if a user with the given email exists in the database.
     *
     * @param email the email of the user to check
     * @return true if the user exists, false otherwise
     * @throws SQLException if a database access error occurs
     */
    public boolean user_exists(String email) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT COUNT(*) FROM user WHERE user_email = ?");
        preparedStatement.setString(1, email);
        ResultSet result = preparedStatement.executeQuery();

        if (result.next() && result.getInt(1) > 0) {
            return true;
        }
        return false;
    }

    /**
     * Helper method to hash the password using SHA-256 algorithm.
     *
     * @param password the password to hash
     * @return the hashed password as a string
     */
    private String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hashedBytes = md.digest(password.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(hashedBytes);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     *
     * Retrieves all friends of a user based on the logged-in user's email.
     *
     * @param logged_user_email The email of the logged-in user.
     * @return An ArrayList of User objects representing the user's friends.
     * @throws SQLException If an SQL error occurs.
     * @throws IOException If an I/O error occurs.
     */
    public ArrayList<user> get_All_friends(String logged_user_email) throws SQLException, IOException {
        ArrayList<String> frineds_emails = new ArrayList<String>();

        try ( PreparedStatement preparedStatement = connection.prepareStatement("select * from user_friends where user_email=?")) {

            preparedStatement.setString(1, logged_user_email);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                frineds_emails.add(rs.getString("friend_email"));
            }
        }

        ArrayList<user> users = getAll_users();

        for (int i = 0; i < users.size(); i++) {
            for (int j = 0; j < frineds_emails.size(); j++) {
                if (!users.get(i).getUser_email().equals(frineds_emails.get(j))) {
                    users.remove(i);
                }
            }
        }
        return users;
    }

    /**
     * Retrieves all users from the database.
     *
     * @return An ArrayList of User objects representing all the users.
     * @throws SQLException If an SQL error occurs.
     * @throws IOException If an I/O error occurs.
     */
    public ArrayList<user> getAll_users() throws SQLException, IOException {

        ArrayList<user> users = new ArrayList<user>();
        user user_obj = null;

        try ( PreparedStatement preparedStatement = connection.prepareStatement("select * from user")) {

            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                user_obj = new user();
                user_obj.setName(rs.getString("name"));
                user_obj.setDob(rs.getString("dob"));
                user_obj.setGender(rs.getString("gender"));
                user_obj.setUser_email(rs.getString("user_email"));
                user_obj.setLocation(rs.getString("location"));
                user_obj.setIsAdmin(rs.getInt("isadmin"));
                user_obj.setPassword(rs.getString("password"));
                Blob blob = rs.getBlob("profile_pic");
                user_obj.setProfile_pic(blob);
                user_obj.setAddresses(get_Addresses(rs.getString("user_email")));
                user_obj.setEducations(get_Education(rs.getString("user_email")));
                users.add(user_obj);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return users;
    }

    /**
     * Checks if two users are friends.
     *
     * @param logged_user_email The email of the logged-in user.
     * @param other_user_email The email of the other user.
     * @return true if the users are friends, false otherwise.
     * @throws SQLException If an SQL error occurs.
     */
    public boolean isFriend(String logged_user_email, String other_user_email) throws SQLException {
        try ( PreparedStatement preparedStatement = connection.prepareStatement("select * from user_friends where ( user_email=? and friend_email=?) or (user_email=? and friend_email=?) ")) {
            preparedStatement.setString(1, logged_user_email);
            preparedStatement.setString(2, other_user_email);
            preparedStatement.setString(3, other_user_email);
            preparedStatement.setString(4, logged_user_email);
            ResultSet rs = preparedStatement.executeQuery();
            if (rs.next()) {

                return true;
            }
        }

        return false;
    }

    /**
     *
     * Checks if a user is from a given location or has a given gender.
     *
     * @param query The search query specifying the location or gender criteria.
     * @param email The email of the user.
     * @return true if the user matches the given criteria, false otherwise.
     * @throws SQLException If an SQL error occurs.
     */
    public boolean isfrom_given_location_or_gender(String query, String email) throws SQLException {

        try ( PreparedStatement preparedStatement = connection.prepareStatement("SELECT u.* FROM user u LEFT JOIN addresses a ON u.user_email=a.user_email WHERE (u.location LIKE ? OR u.gender=? OR a.street LIKE ? OR a.town LIKE ? OR a.state LIKE ? OR a.country LIKE ?  ) AND u.user_email=?")) {
            preparedStatement.setString(1, query);
            preparedStatement.setString(1, "%" + query + "%");
            preparedStatement.setString(2, query);
            preparedStatement.setString(3, "%" + query + "%");
            preparedStatement.setString(4, "%" + query + "%");
            preparedStatement.setString(5, "%" + query + "%");
            preparedStatement.setString(6, "%" + query + "%");
            preparedStatement.setString(7, email);
            ResultSet rs = preparedStatement.executeQuery();
            if (rs.next()) {

                return true;
            }
        }
        return false;

    }

    /**
     * Authenticates a user based on the provided email and password.
     *
     * @param email The email of the user.
     * @param password The password of the user.
     * @return A User object if authentication is successful, null otherwise.
     * @throws SQLException If an SQL error occurs.
     * @throws IOException If an I/O error occurs.
     */
    public user authenticateUser(String email, String password) throws SQLException, IOException {
        user user_obj = null;
        try ( PreparedStatement preparedStatement = connection.prepareStatement("select * from user where user_email=?")) {
            preparedStatement.setString(1, email);

            ResultSet rs = preparedStatement.executeQuery();
            if (rs.next()) {
                String storedPasswordHash = rs.getString("password");
                String inputHash = hashPassword(password);

                if (storedPasswordHash.equals(inputHash)) {
                    user_obj = new user();
                    user_obj.setName(rs.getString("name"));
                    user_obj.setDob(rs.getString("dob"));
                    user_obj.setGender(rs.getString("gender"));
                    user_obj.setUser_email(rs.getString("user_email"));
                    user_obj.setLocation(rs.getString("location"));
                    user_obj.setIsAdmin(rs.getInt("isadmin"));
                    user_obj.setPassword(rs.getString("password"));
                    Blob blob = rs.getBlob("profile_pic");
                    user_obj.setProfile_pic(blob);
                    user_obj.setAddresses(get_Addresses(email));
                    user_obj.setEducations(get_Education(email));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user_obj;

    }

    /**
     * Retrieves information about a single user based on the provided email.
     *
     * @param email The email of the user.
     * @return A User object containing the user's information.
     * @throws SQLException If an SQL error occurs.
     * @throws IOException If an I/O error occurs.
     */
    public user get_single_user_info(String email) throws SQLException, IOException {
        user user_obj = null;
        try ( PreparedStatement preparedStatement = connection.prepareStatement("select * from user where user_email=?")) {
            preparedStatement.setString(1, email);
            ResultSet rs = preparedStatement.executeQuery();
            if (rs.next()) {

                user_obj = new user();
                user_obj.setName(rs.getString("name"));
                user_obj.setDob(rs.getString("dob"));
                user_obj.setGender(rs.getString("gender"));
                user_obj.setUser_email(rs.getString("user_email"));
                user_obj.setLocation(rs.getString("location"));
                user_obj.setIsAdmin(rs.getInt("isadmin"));
                user_obj.setPassword(rs.getString("password"));
                Blob blob = rs.getBlob("profile_pic");
                user_obj.setProfile_pic(blob);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user_obj;
    }

    /**
     * Retrieves the addresses associated with a user based on the provided
     * email.
     *
     * @param email The email of the user.
     * @return An ArrayList of Address objects representing the user's
     * addresses.
     * @throws SQLException If an SQL error occurs.
     */
    public ArrayList<Address> get_Addresses(String email) throws SQLException {

        ArrayList<Address> addresses = new ArrayList<Address>();

        try ( PreparedStatement preparedStatement = connection.prepareStatement("select * from  addresses where user_email=?")) {
            preparedStatement.setString(1, email);

            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                Address user_address = new Address();
                user_address.setStreet(rs.getString("street"));
                user_address.setTown(rs.getString("town"));
                user_address.setState(rs.getString("state"));
                user_address.setCountry(rs.getString("country"));
                user_address.setUser_email(rs.getString("user_email"));
                addresses.add(user_address);

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (addresses.size() == 0) {
            addresses.add(new Address());
        }
        return addresses;
    }

    /**
     * Retrieves the education details associated with a user based on the
     * provided email.
     *
     * @param email The email of the user.
     * @return An ArrayList of Education objects representing the user's
     * education details.
     * @throws SQLException If an SQL error occurs.
     */
    public ArrayList<education> get_Education(String email) throws SQLException {
        ArrayList<education> user_educations = new ArrayList<education>();

        try ( PreparedStatement preparedStatement = connection.prepareStatement("select * from  education where user_email =?")) {
            preparedStatement.setString(1, email);

            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                education user_education = new education();
                user_education.setDegree(rs.getString("degree"));
                user_education.setSchool(rs.getString("school"));
                user_education.setUser_email(rs.getString("user_email"));
                user_educations.add(user_education);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (user_educations.size() == 0) {
            user_educations.add(new education());
        }
        return user_educations;
    }

    /**
     * Deletes a user and all associated data from the database based on the
     * provided user email.
     *
     * @param user_to_act_email The email of the user to be deleted.
     * @throws SQLException If an SQL error occurs.
     */
    public void delete_user(String user_to_act_email) throws SQLException {
        // Disable foreign key checks to allow deletion of dependent data
        Statement disableFK = connection.createStatement();
        disableFK.executeUpdate("SET foreign_key_checks = 0;");
        // Delete addresses associated with the user
        PreparedStatement deleteAddresses = connection.prepareStatement("DELETE FROM addresses WHERE user_email = ?");
        deleteAddresses.setString(1, user_to_act_email);
        deleteAddresses.executeUpdate();

        // Delete education associated with the user
        PreparedStatement deleteEducation = connection.prepareStatement("DELETE FROM education WHERE user_email = ? ");
        deleteEducation.setString(1, user_to_act_email);
        deleteEducation.executeUpdate();

        // Delete user_friends associated with the user
        PreparedStatement deleteFriends = connection.prepareStatement("DELETE FROM user_friends WHERE user_email = ? OR friend_email = ?;");
        deleteFriends.setString(1, user_to_act_email);
        deleteFriends.setString(2, user_to_act_email);
        deleteFriends.executeUpdate();

        // Delete the user
        PreparedStatement deleteUser = connection.prepareStatement("DELETE FROM user WHERE user_email = ?;");
        deleteUser.setString(1, user_to_act_email);
        deleteUser.executeUpdate();

        // Re-enable foreign key checks
        Statement enableFK = connection.createStatement();
        enableFK.executeUpdate("SET foreign_key_checks = 1;");

    }

    /**
     * Deletes the profile picture of a user based on the provided user email.
     *
     * @param user_to_act_email The email of the user.
     * @throws FileNotFoundException If the profile picture file is not found.
     * @throws IOException If an I/O error occurs.
     * @throws SQLException If an SQL error occurs.
     */
    public void delete_user_dp(String user_to_act_email) throws FileNotFoundException, IOException, SQLException {
        // Delete addresses associated with the user

        //C:\\Users\\Jose Omar\\Documents\\NetBeansProjects\\Social_Networking_Application\\src\\java\\com\\servelets\\deleted_dp.png
        File imageFile = new File("C:\\Users\\Ali Haider\\Documents\\NetBeansProjects\\Social_Networking_Application\\src\\java\\com\\servelets\\deleted_dp.png");
        FileInputStream imageFileInputStream = new FileInputStream(imageFile);
        ByteArrayOutputStream imageBytesOutputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[8192];
        int bytesRead;
        while ((bytesRead = imageFileInputStream.read(buffer)) != -1) {
            imageBytesOutputStream.write(buffer, 0, bytesRead);
        }
        byte[] imageBytes = imageBytesOutputStream.toByteArray();
        Blob profilePicBlob = new javax.sql.rowset.serial.SerialBlob(imageBytes);
        try {
            PreparedStatement deletedp = connection.prepareStatement("update user set profile_pic=? where user_email=?");
            deletedp.setBlob(1, profilePicBlob);
            deletedp.setString(2, user_to_act_email);

            deletedp.executeUpdate();
        } catch (SQLException e) {
            // Handle the exception here
            e.printStackTrace();
        }
    }

    /**
     * Updates a user's information in the database based on the provided user
     * object and user email.
     *
     * @param user The updated user object.
     * @param user_to_update_email The email of the user to be updated.
     * @return The number of rows affected in the database.
     */
    public int updateUser(user user, String user_to_update_email) {
        int rowsAffected = 0;
        try ( PreparedStatement preparedStatement = connection.prepareStatement("UPDATE user SET name=?, dob=?, gender=?, user_email=?, profile_pic=?, location=?, password=?, isadmin=? WHERE user_email=?")) {
            preparedStatement.setString(1, user.getName());
            preparedStatement.setString(2, user.getDob());
            preparedStatement.setString(3, user.getGender());
            preparedStatement.setString(4, user.getUser_email());
            preparedStatement.setBlob(5, user.getProfile_pic());
            preparedStatement.setString(6, user.getLocation());
            String passwordHash = hashPassword(user.getPassword());
            preparedStatement.setString(7, passwordHash);
            preparedStatement.setInt(8, user.getIsAdmin());
            preparedStatement.setString(9, user_to_update_email);
            rowsAffected = preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rowsAffected;
    }

}
