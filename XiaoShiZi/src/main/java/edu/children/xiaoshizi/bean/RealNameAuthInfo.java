package edu.children.xiaoshizi.bean;


import zuo.biao.library.base.BaseModel;

public class RealNameAuthInfo  extends BaseModel {
    private String userName	;
    private String verifiedType	;
    private String cardType	;
    private String cardNum	;
    private String cardProsImagesUrl	;
    private String cardConsImagesUrl	;
    private String verifiedVideoUrl	;
    private String verifiedVideoPosterUrl	;
    private String verifiedResult	;
    private String verifiedStatus;


    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getVerifiedType() {
        return verifiedType;
    }

    public void setVerifiedType(String verifiedType) {
        this.verifiedType = verifiedType;
    }

    public String getCardType() {
        return cardType;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType;
    }

    public String getCardNum() {
        return cardNum;
    }

    public void setCardNum(String cardNum) {
        this.cardNum = cardNum;
    }

    public String getCardProsImagesUrl() {
        return cardProsImagesUrl;
    }

    public void setCardProsImagesUrl(String cardProsImagesUrl) {
        this.cardProsImagesUrl = cardProsImagesUrl;
    }

    public String getCardConsImagesUrl() {
        return cardConsImagesUrl;
    }

    public void setCardConsImagesUrl(String cardConsImagesUrl) {
        this.cardConsImagesUrl = cardConsImagesUrl;
    }

    public String getVerifiedVideoUrl() {
        return verifiedVideoUrl;
    }

    public void setVerifiedVideoUrl(String verifiedVideoUrl) {
        this.verifiedVideoUrl = verifiedVideoUrl;
    }

    public String getVerifiedVideoPosterUrl() {
        return verifiedVideoPosterUrl;
    }

    public void setVerifiedVideoPosterUrl(String verifiedVideoPosterUrl) {
        this.verifiedVideoPosterUrl = verifiedVideoPosterUrl;
    }

    public String getVerifiedResult() {
        return verifiedResult;
    }

    public void setVerifiedResult(String verifiedResult) {
        this.verifiedResult = verifiedResult;
    }

    public String getVerifiedStatus() {
        return verifiedStatus;
    }

    public void setVerifiedStatus(String verifiedStatus) {
        this.verifiedStatus = verifiedStatus;
    }

    @Override
    protected boolean isCorrect() {
        return true;
    }
}
