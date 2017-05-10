
package com.skeleton.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Model class for user
 */
public class UserDetails implements Serializable {

    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("createdAt")
    @Expose
    private String createdAt;
    @SerializedName("updatedAt")
    @Expose
    private String updatedAt;
    @SerializedName("dob")
    @Expose
    private String dob;
    @SerializedName("countryCode")
    @Expose
    private String countryCode;
    @SerializedName("phoneNo")
    @Expose
    private String phoneNo;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("orientation")
    @Expose
    private String orientation;
    @SerializedName("newNumber")
    @Expose
    private String newNumber;
    @SerializedName("userImages")
    @Expose
    private List<UserImage> userImages = null;
    @SerializedName("admin_deactivateAccount")
    @Expose
    private Boolean adminDeactivateAccount;
    @SerializedName("timeOffset")
    @Expose
    private Integer timeOffset;
    @SerializedName("gender")
    @Expose
    private String gender;
    @SerializedName("aboutMe")
    @Expose
    private String aboutMe;
    @SerializedName("step2CompleteOrSkip")
    @Expose
    private Boolean step2CompleteOrSkip;
    @SerializedName("step1CompleteOrSkip")
    @Expose
    private Boolean step1CompleteOrSkip;
    @SerializedName("interestCategories")
    @Expose
    private List<Object> interestCategories = null;
    @SerializedName("profilePicURL")
    @Expose
    private ProfilePicURL profilePicURL;
    @SerializedName("defaultAddressId")
    @Expose
    private Object defaultAddressId;
    @SerializedName("currentLocation")
    @Expose
    private CurrentLocation currentLocation;
    @SerializedName("phoneVerified")
    @Expose
    private Boolean phoneVerified;
    @SerializedName("emailVerified")
    @Expose
    private Boolean emailVerified;
    @SerializedName("drinking")
    @Expose
    private Object drinking;
    @SerializedName("smoking")
    @Expose
    private Object smoking;
    @SerializedName("bodyType")
    @Expose
    private Object bodyType;
    @SerializedName("height")
    @Expose
    private String height;
    @SerializedName("religion")
    @Expose
    private Object religion;
    @SerializedName("ethnicity")
    @Expose
    private Object ethnicity;
    @SerializedName("relationshipHistory")
    @Expose
    private Object relationshipHistory;
    @SerializedName("notificationEnable")
    @Expose
    private Boolean notificationEnable;
    @SerializedName("directDateRequestEnable")
    @Expose
    private Boolean directDateRequestEnable;
    @SerializedName("privacy")
    @Expose
    private Boolean privacy;
    @SerializedName("isDisable")
    @Expose
    private Boolean isDisable;
    @SerializedName("language")
    @Expose
    private String language;
    @SerializedName("firstTimeLogin")
    @Expose
    private Boolean firstTimeLogin;
    @SerializedName("lastName")
    @Expose
    private String lastName;
    @SerializedName("firstName")
    @Expose
    private String firstName;

    /**
     * getter
     *
     * @return : id of user
     */
    public String getId() {
        return id;
    }

    /**
     * Setter method
     *
     * @param id : id of user
     */
    public void setId(final String id) {
        this.id = id;
    }

    /**
     * Getter method
     *
     * @return :  date of account creation
     */
    public String getCreatedAt() {
        return createdAt;
    }

    /**
     * Setter method
     *
     * @param createdAt : date of account creation
     */
    public void setCreatedAt(final String createdAt) {
        this.createdAt = createdAt;
    }

    /**
     * getter method
     *
     * @return : last updated info
     */
    public String getUpdatedAt() {
        return updatedAt;
    }

    /**
     * Setter method
     *
     * @param updatedAt : last update d ifo
     */
    public void setUpdatedAt(final String updatedAt) {
        this.updatedAt = updatedAt;
    }

    /**
     * dob
     *
     * @return : dob
     */
    public String getDob() {
        return dob;
    }

    /**
     * Setter method
     *
     * @param dob : dob
     */
    public void setDob(final String dob) {
        this.dob = dob;
    }

    /**
     * Getter
     *
     * @return : countryCode
     */
    public String getCountryCode() {
        return countryCode;
    }

    /**
     * Setter method
     *
     * @param countryCode : countryCode
     */
    public void setCountryCode(final String countryCode) {
        this.countryCode = countryCode;
    }

    /**
     * Getter method
     *
     * @return : phone
     */
    public String getPhoneNo() {
        return phoneNo;
    }

    /**
     * phone
     *
     * @param phoneNo : phone
     */
    public void setPhoneNo(final String phoneNo) {
        this.phoneNo = phoneNo;
    }

    /**
     * getter method
     *
     * @return : email
     */
    public String getEmail() {
        return email;
    }

    /**
     * email of user
     *
     * @param email : email
     */
    public void setEmail(final String email) {
        this.email = email;
    }

    /**
     * Getter method
     *
     * @return : orientation
     */
    public String getOrientation() {
        return orientation;
    }

    /**
     * Setter method
     *
     * @param orientation : orientation
     */
    public void setOrientation(final String orientation) {
        this.orientation = orientation;
    }

    /**
     * newNumber
     *
     * @return : newNumber
     */
    public String getNewNumber() {
        return newNumber;
    }

    /**
     * Setter method
     * newNumber
     *
     * @param newNumber :
     */
    public void setNewNumber(final String newNumber) {
        this.newNumber = newNumber;
    }

    /**
     * getter method
     *
     * @return : list of userimages
     */
    public List<UserImage> getUserImages() {
        return userImages;
    }

    /**
     * Set user imaged
     *
     * @param userImages : userImages
     */
    public void setUserImages(final List<UserImage> userImages) {
        this.userImages = userImages;
    }

    /**
     * Getter method
     *
     * @return : admin_deactivateAccount
     */
    public Boolean getAdminDeactivateAccount() {
        return adminDeactivateAccount;
    }

    /**
     * Setter method
     *
     * @param adminDeactivateAccount : admin_deactivateAccount
     */
    public void setAdminDeactivateAccount(final Boolean adminDeactivateAccount) {
        this.adminDeactivateAccount = adminDeactivateAccount;
    }

    /**
     * Getter method
     *
     * @return : timeOffset
     */
    public Integer getTimeOffset() {
        return timeOffset;
    }

    /**
     * timeOffset
     *
     * @param timeOffset : timeOffset
     */
    public void setTimeOffset(final Integer timeOffset) {
        this.timeOffset = timeOffset;
    }

    /**
     * Getter method
     *
     * @return : gender of user
     */
    public String getGender() {
        return gender;
    }

    /**
     * Setter method
     * gender of user
     *
     * @param gender : gender of user
     */
    public void setGender(final String gender) {
        this.gender = gender;
    }

    /**
     * Getter method
     *
     * @return : user info
     */
    public String getAboutMe() {
        return aboutMe;
    }

    /**
     * user's about
     *
     * @param aboutMe : aboutMe
     */
    public void setAboutMe(final String aboutMe) {
        this.aboutMe = aboutMe;
    }

    /**
     * Getter method
     *
     * @return : step2CompleteOrSkip
     */
    public Boolean getStep2CompleteOrSkip() {
        return step2CompleteOrSkip;
    }

    /**
     * Setter method
     *
     * @param step2CompleteOrSkip : step2CompleteOrSkip
     */
    public void setStep2CompleteOrSkip(final Boolean step2CompleteOrSkip) {
        this.step2CompleteOrSkip = step2CompleteOrSkip;
    }

    /**
     * Getter method
     *
     * @return : step1CompleteOrSkip
     */
    public Boolean getStep1CompleteOrSkip() {
        return step1CompleteOrSkip;
    }

    /**
     * Setter method
     *
     * @param step1CompleteOrSkip : step1CompleteOrSkip
     */
    public void setStep1CompleteOrSkip(final Boolean step1CompleteOrSkip) {
        this.step1CompleteOrSkip = step1CompleteOrSkip;
    }

    /**
     * Getter method
     *
     * @return : getInterestCategories
     */
    public List<Object> getInterestCategories() {
        return interestCategories;
    }

    /**
     * Setter method
     *
     * @param interestCategories : interestCategories
     */
    public void setInterestCategories(final List<Object> interestCategories) {
        this.interestCategories = interestCategories;
    }

    /**
     * Getter method
     *
     * @return : profilePicURL
     */
    public ProfilePicURL getProfilePicURL() {
        return profilePicURL;
    }

    /**
     * Setter method
     *
     * @param profilePicURL : photo url of user
     */
    public void setProfilePicURL(final ProfilePicURL profilePicURL) {
        this.profilePicURL = profilePicURL;
    }

    /**
     * getter method Default address of user
     *
     * @return : defaultAddressId
     */
    public Object getDefaultAddressId() {
        return defaultAddressId;
    }

    /**
     * default addresss of user
     *
     * @param defaultAddressId : defaultAddressId
     */
    public void setDefaultAddressId(final Object defaultAddressId) {
        this.defaultAddressId = defaultAddressId;
    }

    /**
     * Getter method
     *
     * @return : current location of user
     */
    public CurrentLocation getCurrentLocation() {
        return currentLocation;
    }

    /**
     * Setter method
     *
     * @param currentLocation : current location of user
     */
    public void setCurrentLocation(final CurrentLocation currentLocation) {
        this.currentLocation = currentLocation;
    }

    /**
     * Getter method
     *
     * @return : getPhoneVerified
     */
    public Boolean getPhoneVerified() {
        return phoneVerified;
    }

    /**
     * phoneVerified
     *
     * @param phoneVerified : phoneVerified
     */
    public void setPhoneVerified(final Boolean phoneVerified) {
        this.phoneVerified = phoneVerified;
    }

    /**
     * Getter method
     *
     * @return : emailVerified
     */
    public Boolean getEmailVerified() {
        return emailVerified;
    }

    /**
     * set Email Verified
     *
     * @param emailVerified : emailVerified
     */
    public void setEmailVerified(final Boolean emailVerified) {
        this.emailVerified = emailVerified;
    }

    /**
     * Getter method
     *
     * @return : drinking of user
     */
    public Object getDrinking() {
        return drinking;
    }

    /**
     * user drinking
     *
     * @param drinking : drinking
     */
    public void setDrinking(final Object drinking) {
        this.drinking = drinking;
    }

    /**
     * Getter method
     *
     * @return : smoking
     */
    public Object getSmoking() {
        return smoking;
    }

    /**
     * Setter method
     *
     * @param smoking : smoking
     */
    public void setSmoking(final Object smoking) {
        this.smoking = smoking;
    }

    /**
     * Getter method
     *
     * @return : bodyType
     */
    public Object getBodyType() {
        return bodyType;
    }

    /**
     * Setter method
     *
     * @param bodyType : bodyType
     */
    public void setBodyType(final Object bodyType) {
        this.bodyType = bodyType;
    }

    /**
     * Getter method
     *
     * @return : height of user
     */
    public String getHeight() {
        return height;
    }

    /**
     * Setter method height of user
     *
     * @param height : height of user
     */
    public void setHeight(final String height) {
        this.height = height;
    }

    /**
     * Getter method
     *
     * @return : get religion of user
     */
    public Object getReligion() {
        return religion;
    }

    /**
     * Setter method
     *
     * @param religion : religion of user
     */
    public void setReligion(final Object religion) {
        this.religion = religion;
    }

    /**
     * Getter method
     *
     * @return : ethnicity
     */
    public Object getEthnicity() {
        return ethnicity;
    }

    /**
     * Setter method
     *
     * @param ethnicity : ethnicity
     */
    public void setEthnicity(final Object ethnicity) {
        this.ethnicity = ethnicity;
    }

    /**
     * Getter method
     *
     * @return : relationshipHistory
     */
    public Object getRelationshipHistory() {
        return relationshipHistory;
    }

    /**
     * Setter method
     *
     * @param relationshipHistory : relationshipHistory
     */
    public void setRelationshipHistory(final Object relationshipHistory) {
        this.relationshipHistory = relationshipHistory;
    }

    /**
     * Getter method
     *
     * @return : getNotificationEnable
     */
    public Boolean getNotificationEnable() {
        return notificationEnable;
    }

    /**
     * setNotificationEnable
     *
     * @param notificationEnable : setNotificationEnable
     */
    public void setNotificationEnable(final Boolean notificationEnable) {
        this.notificationEnable = notificationEnable;
    }

    /**
     * Getter method
     *
     * @return : getDirectDateRequestEnable
     */
    public Boolean getDirectDateRequestEnable() {
        return directDateRequestEnable;
    }

    /**
     * Setter method
     *
     * @param directDateRequestEnable : setDirectDateRequestEnable
     */
    public void setDirectDateRequestEnable(final Boolean directDateRequestEnable) {
        this.directDateRequestEnable = directDateRequestEnable;
    }

    /**
     * get privacy settings
     *
     * @return : true if private
     */
    public Boolean getPrivacy() {
        return privacy;
    }

    /**
     * is data private
     *
     * @param privacy : true if private, false if public
     */
    public void setPrivacy(final Boolean privacy) {
        this.privacy = privacy;
    }

    /**
     * Getter method
     *
     * @return : boolean vlaue of isDisable
     */
    public Boolean getIsDisable() {
        return isDisable;
    }

    /**
     * isDisable
     *
     * @param isDisable : boolean value of isDisable
     */
    public void setIsDisable(final Boolean isDisable) {
        this.isDisable = isDisable;
    }

    /**
     * getter method
     *
     * @return : get language preferences
     */
    public String getLanguage() {
        return language;
    }

    /**
     * Setter method
     *
     * @param language : language preferences
     */
    public void setLanguage(final String language) {
        this.language = language;
    }

    /**
     * Getter method
     *
     * @return : firstTimeLogin
     */
    public Boolean getFirstTimeLogin() {
        return firstTimeLogin;
    }

    /**
     * Setter method
     *
     * @param firstTimeLogin : firstTimeLogin
     */
    public void setFirstTimeLogin(final Boolean firstTimeLogin) {
        this.firstTimeLogin = firstTimeLogin;
    }

    /**
     * Getter method
     *
     * @return : lname of user
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Setter method
     *
     * @param lastName : lname of the user
     */
    public void setLastName(final String lastName) {
        this.lastName = lastName;
    }

    /**
     * Getter method firstName
     *
     * @return : fname of user
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Setter method
     *
     * @param firstName : fname of the user
     */
    public void setFirstName(final String firstName) {
        this.firstName = firstName;
    }

}