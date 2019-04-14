package edu.children.xiaoshizi.logic;

import android.app.Activity;

import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.shareboard.SnsPlatform;

import java.util.ArrayList;

import zuo.biao.library.util.Log;

public class UmengThirdLoginHandle {
    private static final String TAG = "UmengThirdLoginHandle";
    public static final SHARE_MEDIA[] list = {SHARE_MEDIA.QQ, SHARE_MEDIA.SINA, SHARE_MEDIA.WEIXIN,
            SHARE_MEDIA.FACEBOOK, SHARE_MEDIA.TWITTER, SHARE_MEDIA.LINKEDIN, SHARE_MEDIA.DOUBAN, SHARE_MEDIA.RENREN, SHARE_MEDIA.KAKAO,
            SHARE_MEDIA.VKONTAKTE, SHARE_MEDIA.DROPBOX};
    public ArrayList<SnsPlatform> platforms = new ArrayList<SnsPlatform>();
    private Activity mContext;
    UMAuthListener authListener;
    public UmengThirdLoginHandle(Activity mContext, UMAuthListener authListener) {
        this.mContext = mContext;
        this.authListener=authListener;
        platforms.clear();
        for (SHARE_MEDIA e : list) {
            if (!e.toString().equals(SHARE_MEDIA.GENERIC.toString())) {
                platforms.add(e.toSnsPlatform());
            }
        }
    }

    public void auther(SHARE_MEDIA shareMedia){
//        SHARE_MEDIA snsPlatform=share_media.toSnsPlatform().mPlatform;
//        final boolean isauth = false;//UMShareAPI.get(mContext).isAuthorize(mContext,snsPlatform);
//        Log.d(TAG,"isauth="+isauth);
//        if (isauth) {
//            UMShareAPI.get(mContext).deleteOauth(mActivity, list.get(position).mPlatform, authListener);
//        } else {
            UMShareAPI.get(mContext).doOauthVerify(mContext,shareMedia, authListener);
//        }
    }

}
