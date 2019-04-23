package edu.children.xiaoshizi.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;


import org.devio.takephoto.app.TakePhoto;
import org.devio.takephoto.app.TakePhotoImpl;
import org.devio.takephoto.compress.CompressConfig;
import org.devio.takephoto.model.CropOptions;
import org.devio.takephoto.model.InvokeParam;
import org.devio.takephoto.model.TContextWrap;
import org.devio.takephoto.model.TResult;
import org.devio.takephoto.permission.InvokeListener;
import org.devio.takephoto.permission.PermissionManager;
import org.devio.takephoto.permission.TakePhotoInvocationHandler;

import java.io.File;

import edu.children.xiaoshizi.utils.Constant;
import edu.children.xiaoshizi.utils.XszCache;
import zuo.biao.library.ui.ItemDialog;
import zuo.biao.library.util.Log;

public abstract class BaseTakePhotoActivity extends XszBaseActivity implements TakePhoto.TakeResultListener, InvokeListener {

    private TakePhoto takePhoto;
    private InvokeParam invokeParam;
    protected File imageFile;
    private Uri uri;
    private CropOptions cropOptions;
    protected String originalFilePath;
    protected String compressFilePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        takePhoto=getTakePhoto();
        takePhoto.onCreate(savedInstanceState);
        super.onCreate(savedInstanceState);
        int size = Math.min(getResources().getDisplayMetrics().widthPixels, getResources().getDisplayMetrics().heightPixels);
        cropOptions = new CropOptions.Builder().setOutputX(size).setOutputX(size).setWithOwnCrop(false).create();

//        initEvents();
    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        takePhoto.onSaveInstanceState(outState);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        takePhoto.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //以下代码为处理Android6.0、7.0动态权限所需
        PermissionManager.TPermissionType type = PermissionManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionManager.handlePermissionsResult(this, type, invokeParam, this);
    }

    public void initEvents(){
        //各控件初始化
    }

    private static final int DIALOG_SET_GRADLE=1;
    ItemDialog.OnDialogItemClickListener  onDialogItemClick=new ItemDialog.OnDialogItemClickListener() {
        @Override
        public void onDialogItemClick(int requestCode, int position, String item) {
            if (position < 0) {
                return;
            }
            switch (requestCode) {
                case DIALOG_SET_GRADLE:
                    imageFile = new File(XszCache.getCacheDir(Constant.CACHE_DIR_FILE), System.currentTimeMillis() + ".jpeg");
                    uri = Uri.fromFile(imageFile);
                    if (position==0){
                        takePhoto.onPickFromCapture(uri);
//                        takePhoto.onPickMultiple(3);
                    }else {
                        //从照片选择并裁剪
//                        takePhoto.onPickFromGallery();
                        takePhoto.onPickMultipleWithCrop(2,cropOptions);
                    }
                    break;
            }
        }
    };
    protected  void takePicture(){
        String[] choices = {"拍照","从相机里选择"};
        new ItemDialog(context, choices, "请选择", DIALOG_SET_GRADLE, onDialogItemClick).show();
    }

    private TakePhoto getTakePhoto() {
        //获得TakePhoto实例
        if (takePhoto == null) {
            takePhoto = (TakePhoto) TakePhotoInvocationHandler.of(this).bind(new TakePhotoImpl(this, this));
        }
        //设置压缩规则，最大2000kb
        takePhoto.onEnableCompress(new CompressConfig.Builder().setMaxSize(2000 * 1024).create(), true);
        return takePhoto;
    }

    @Override
    public void takeSuccess(final TResult result) {
        result.getImage().getCompressPath();
        originalFilePath = result.getImage().getOriginalPath();
        compressFilePath = result.getImage().getCompressPath();
        Log.d(TAG,"originalFilePath="+originalFilePath+",compressFilePath="+compressFilePath);
    }
    @Override
    public void takeFail(TResult result, String msg) {
        showShortToast("设置失败:"+msg);
    }
    @Override
    public void takeCancel() {
        //取消
    }

    @Override
    public PermissionManager.TPermissionType invoke(InvokeParam invokeParam) {
        PermissionManager.TPermissionType type = PermissionManager.checkPermission(TContextWrap.of(this), invokeParam.getMethod());
        if (PermissionManager.TPermissionType.WAIT.equals(type)) {
            this.invokeParam = invokeParam;
        }
        return type;
    }

}
