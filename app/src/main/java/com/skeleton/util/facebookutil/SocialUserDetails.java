package com.skeleton.util.facebookutil;

import android.os.Parcel;
import android.os.Parcelable;


/**
 * Developer: Saurabh Verma
 * Dated: 09/11/16.
 */
public class SocialUserDetails implements Parcelable {


    /**
     * The constant CREATOR.
     */
    public static final Creator<SocialUserDetails> CREATOR = new Creator<SocialUserDetails>() {
        @Override
        public SocialUserDetails createFromParcel(final Parcel source) {
            return new SocialUserDetails(source);
        }

        @Override
        public SocialUserDetails[] newArray(final int size) {
            return new SocialUserDetails[size];
        }
    };

    private String id, firstName, lastName, email, gender, picture;

    /**
     * Instantiates a new Social user details.
     *
     * @param id        the id
     * @param firstName the first name
     * @param lastName  the last name
     * @param email     the email
     * @param gender    the gender
     * @param picture   the picture
     */
    public SocialUserDetails(final String id, final String firstName,
                             final String lastName, final String email,
                             final String gender, final String picture) {

        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.gender = gender;
        this.picture = picture;
    }

    /**
     * Instantiates a new Social user details.
     *
     * @param in the in
     */
    protected SocialUserDetails(final Parcel in) {
        this.id = in.readString();
        this.firstName = in.readString();
        this.lastName = in.readString();
        this.email = in.readString();
        this.gender = in.readString();
        this.picture = in.readString();
    }

    /**
     * Gets id.
     *
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * Gets first name.
     *
     * @return the first name
     */
    public String getFirst_name() {
        return firstName;
    }

    /**
     * Gets last name.
     *
     * @return the last name
     */
    public String getLast_name() {
        return lastName;
    }

    /**
     * Gets email.
     *
     * @return the email
     */
    public String getEmail() {
        return email;
    }

    /**
     * Gets gender.
     *
     * @return the gender
     */
    public String getGender() {
        return gender;
    }

    /**
     * Gets picture.
     *
     * @return the picture
     */
    public String getPicture() {
        return picture;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(final Parcel dest, final int flags) {
        dest.writeString(this.id);
        dest.writeString(this.firstName);
        dest.writeString(this.lastName);
        dest.writeString(this.email);
        dest.writeString(this.gender);
        dest.writeString(this.picture);
    }
}
