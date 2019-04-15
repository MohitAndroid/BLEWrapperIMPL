package org.beaconwrapper;

import com.google.gson.annotations.SerializedName;

import org.beaconwrapper.beacon.BeaconKey;

import java.io.Serializable;
import java.util.List;

public class CouponEntity implements Serializable {

    @SerializedName("id")
    private long couponId;

    @SerializedName("brandName")
    private String couponName;

    @SerializedName("longDescription")
    private String couponLongDescription;

    @SerializedName("shortDescription")
    private String couponShortDescription;

    @SerializedName("requirementDescription")
    private String couponRequirementDescription;

    @BeaconKey
    @SerializedName("categories")
    private List<String> couponCategories;

    @SerializedName("expirationDate")
    private String couponExpirationDate;

    @SerializedName("lastRedemptionDate")
    private String couponLastRedemptionDate;

    @SerializedName("displayStartDate")
    private String couponDisplayStartDate;

    @SerializedName("imageUrl")
    private String couponImageUrl;

    @SerializedName("krogerCouponNumber")
    private String couponKrogerCouponNumber;

    @SerializedName("addedToCard")
    private boolean couponAddedToCard;

    @SerializedName("canBeAddedToCard")
    private boolean couponCanBeAddedToCard;

    @SerializedName("canBeRemoved")
    private boolean couponCanBeRemoved;

    @SerializedName("filterTags")
    private List<String> couponFilterTags;

    @SerializedName("title")
    private String couponTitle;

    @SerializedName("displayDescription")
    private String couponDisplayDescription;

    @SerializedName("redemptionsAllowed")
    private int couponRedemptionsAllowed;

    @SerializedName("value")
    private double couponValue;

    private boolean isFav;

    private boolean isHeader;

    private int couponCount;

    private String couponHeaderTitle;

    public static final int HEADER_TYPE = 0;
    public static final int COUPON_TYPE = 1;


    public String getCouponHeaderTitle() {
        return couponHeaderTitle;
    }

    public void setCouponHeaderTitle(String couponHeaderTitle) {
        this.couponHeaderTitle = couponHeaderTitle;
    }

    public boolean isHeader() {
        return isHeader;
    }

    public void setHeader(boolean header) {
        isHeader = header;
    }

    public int getCouponCount() {
        return couponCount;
    }

    public void setCouponCount(int couponCount) {
        this.couponCount = couponCount;
    }

    public boolean isFav() {
        return isFav;
    }

    public void setFav(boolean fav) {
        isFav = fav;
    }

    public long getCouponId() {
        return couponId;
    }

    public void setCouponId(long couponId) {
        this.couponId = couponId;
    }

    public String getCouponName() {
        return couponName;
    }

    public void setCouponName(String couponName) {
        this.couponName = couponName;
    }

    public String getCouponLongDescription() {
        return couponLongDescription;
    }

    public void setCouponLongDescription(String couponLongDescription) {
        this.couponLongDescription = couponLongDescription;
    }

    public String getCouponShortDescription() {
        return couponShortDescription;
    }

    public void setCouponShortDescription(String couponShortDescription) {
        this.couponShortDescription = couponShortDescription;
    }

    public String getCouponRequirementDescription() {
        return couponRequirementDescription;
    }

    public void setCouponRequirementDescription(String couponRequirementDescription) {
        this.couponRequirementDescription = couponRequirementDescription;
    }

    public List<String> getCouponCategories() {
        return couponCategories;
    }

    public void setCouponCategories(List<String> couponCategories) {
        this.couponCategories = couponCategories;
    }

    public String getCouponExpirationDate() {
        return couponExpirationDate;
    }

    public void setCouponExpirationDate(String couponExpirationDate) {
        this.couponExpirationDate = couponExpirationDate;
    }

    public String getCouponLastRedemptionDate() {
        return couponLastRedemptionDate;
    }

    public void setCouponLastRedemptionDate(String couponLastRedemptionDate) {
        this.couponLastRedemptionDate = couponLastRedemptionDate;
    }

    public String getCouponDisplayStartDate() {
        return couponDisplayStartDate;
    }

    public void setCouponDisplayStartDate(String couponDisplayStartDate) {
        this.couponDisplayStartDate = couponDisplayStartDate;
    }

    public String getCouponImageUrl() {
        return couponImageUrl;
    }

    public void setCouponImageUrl(String couponImageUrl) {
        this.couponImageUrl = couponImageUrl;
    }

    public String getCouponKrogerCouponNumber() {
        return couponKrogerCouponNumber;
    }

    public void setCouponKrogerCouponNumber(String couponKrogerCouponNumber) {
        this.couponKrogerCouponNumber = couponKrogerCouponNumber;
    }

    public boolean isCouponAddedToCard() {
        return couponAddedToCard;
    }

    public void setCouponAddedToCard(boolean couponAddedToCard) {
        this.couponAddedToCard = couponAddedToCard;
    }

    public boolean isCouponCanBeAddedToCard() {
        return couponCanBeAddedToCard;
    }

    public void setCouponCanBeAddedToCard(boolean couponCanBeAddedToCard) {
        this.couponCanBeAddedToCard = couponCanBeAddedToCard;
    }

    public boolean isCouponCanBeRemoved() {
        return couponCanBeRemoved;
    }

    public void setCouponCanBeRemoved(boolean couponCanBeRemoved) {
        this.couponCanBeRemoved = couponCanBeRemoved;
    }

    public List<String> getCouponFilterTags() {
        return couponFilterTags;
    }

    public void setCouponFilterTags(List<String> couponFilterTags) {
        this.couponFilterTags = couponFilterTags;
    }

    public String getCouponTitle() {
        return couponTitle;
    }

    public void setCouponTitle(String couponTitle) {
        this.couponTitle = couponTitle;
    }

    public String getCouponDisplayDescription() {
        return couponDisplayDescription;
    }

    public void setCouponDisplayDescription(String couponDisplayDescription) {
        this.couponDisplayDescription = couponDisplayDescription;
    }

    public int getCouponRedemptionsAllowed() {
        return couponRedemptionsAllowed;
    }

    public void setCouponRedemptionsAllowed(int couponRedemptionsAllowed) {
        this.couponRedemptionsAllowed = couponRedemptionsAllowed;
    }

    public double getCouponValue() {
        return couponValue;
    }

    public void setCouponValue(double couponValue) {
        this.couponValue = couponValue;
    }
}