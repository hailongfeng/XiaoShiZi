package edu.children.xiaoshizi.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.jph.takephoto.app.TakePhoto;
import com.jph.takephoto.app.TakePhotoImpl;
import com.jph.takephoto.compress.CompressConfig;
import com.jph.takephoto.model.CropOptions;
import com.jph.takephoto.model.InvokeParam;
import com.jph.takephoto.model.TContextWrap;
import com.jph.takephoto.model.TResult;
import com.jph.takephoto.permission.InvokeListener;
import com.jph.takephoto.permission.PermissionManager;
import com.jph.takephoto.permission.TakePhotoInvocationHandler;

import java.io.File;

import edu.children.xiaoshizi.R;
import edu.children.xiaoshizi.utils.Constant;
import edu.children.xiaoshizi.utils.FileUtil;
import zuo.biao.library.ui.ItemDialog;
import zuo.biao.library.util.Log;

public abstract class BaseTakePhotoActivity extends XszBaseActivity implements TakePhoto.TakeResultListener, InvokeListener {
    TakePhoto takePhoto;
    InvokeParam invokeParam;
    File file;
    Uri uri;
    int size;
    CropOptions cropOptions;
    protected File test;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getTakePhoto().onCreate(savedInstanceState);
        super.onCreate(savedInstanceState);
        initEvents();
    }
    public void initEvents(){
        //各控件初始化
        file = new File(FileUtil.getCacheDir(Constant.CACHE_DIR_FILE), System.currentTimeMillis() + ".png");
        uri = Uri.fromFile(file);
        size = Math.min(getResources().getDisplayMetrics().widthPixels, getResources().getDisplayMetrics().heightPixels);
        cropOptions = new CropOptions.Builder().setOutputX(size).setOutputX(size).setWithOwnCrop(false).create();
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
                    if (position==0){
                        takePhoto.onPickFromCaptureWithCrop(uri, cropOptions);
                    }else {
                        //从照片选择并裁剪
                        takePhoto.onPickFromGalleryWithCrop(uri, cropOptions);
                    }
                    break;
            }
        }
    };
    protected  void takePicture(){
        String[] choices = {"拍照","从相机里选择"};
        new ItemDialog(context, choices, "请选择", DIALOG_SET_GRADLE, onDialogItemClick).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        getTakePhoto().onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        getTakePhoto().onSaveInstanceState(outState);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //以下代码为处理Android6.0、7.0动态权限所需
        PermissionManager.TPermissionType type = PermissionManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionManager.handlePermissionsResult(this, type, invokeParam, this);
    }

    @Override
    public PermissionManager.TPermissionType invoke(InvokeParam invokeParam) {
        PermissionManager.TPermissionType type = PermissionManager.checkPermission(TContextWrap.of(this), invokeParam.getMethod());
        if (PermissionManager.TPermissionType.WAIT.equals(type)) {
            this.invokeParam = invokeParam;
        }
        return type;
    }

    private TakePhoto getTakePhoto() {
        //获得TakePhoto实例
        if (takePhoto == null) {
            takePhoto = (TakePhoto) TakePhotoInvocationHandler.of(this).bind(new TakePhotoImpl(this, this));
        }
        //设置压缩规则，最大500kb
        takePhoto.onEnableCompress(new CompressConfig.Builder().setMaxSize(500 * 1024).create(), true);
        return takePhoto;
    }

    @Override
    public void takeSuccess(final TResult result) {
        //成功取得照片
        test = new File(result.getImage().getOriginalPath());
        Log.d(TAG,test.getAbsolutePath());
    }


    @Override
    public void takeFail(TResult result, String msg) {
        //取得失败
//        Toast.makeText(Setting.this,"设置失败",Toast.LENGTH_SHORT).show();
        showShortToast("设置失败");
    }

    @Override
    public void takeCancel() {
        //取消
    }




}
