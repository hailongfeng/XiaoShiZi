package edu.children.xiaoshizi.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.dou361.dialogui.DialogUIUtils;
import com.dou361.dialogui.listener.DialogUIListener;
import com.flyco.roundview.RoundLinearLayout;
import com.flyco.roundview.RoundTextView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TreeMap;

import butterknife.BindView;
import edu.children.xiaoshizi.R;
import edu.children.xiaoshizi.activity.ninepic.SpaceItemDecoration;
import edu.children.xiaoshizi.adapter.view.NinePicAddView;
import edu.children.xiaoshizi.logic.LogicService;
import edu.children.xiaoshizi.net.rxjava.ApiSubscriber;
import edu.children.xiaoshizi.net.rxjava.Response;
import edu.children.xiaoshizi.utils.Constant;
import edu.children.xiaoshizi.utils.DateUtil;
import edu.children.xiaoshizi.utils.FileProvider7;
import edu.children.xiaoshizi.utils.StringUtils;
import edu.children.xiaoshizi.utils.XszCache;
import me.nereo.multi_image_selector.MultiImageSelector;
import me.nereo.multi_image_selector.MultiImageSelectorActivity;
import zuo.biao.library.base.BaseAdapter;
import zuo.biao.library.base.BaseView;
import zuo.biao.library.util.StringUtil;

/**
 * 投稿界面
 */
public class ContributeArticleActivity extends XszBaseActivity {
    @BindView(R.id.btn_sure)
    Button btn_sure;
    @BindView(R.id.edt_suggestion_title)
    EditText edt_suggestion_title;
    @BindView(R.id.edt_suggestion_content)
    EditText edt_suggestion_content;
    @BindView(R.id.rg_home_or_school)
    RadioGroup rg_home_or_school;

    @BindView(R.id.rll_user_video_tougao)
    RoundLinearLayout rll_user_tougao;
    @BindView(R.id.rtv_user_tougao)
    RoundTextView rtv_user_tougao;
    @BindView(R.id.iv_user_tougao)
    ImageView iv_user_tougao;

    @BindView(R.id.recycleView)
    RecyclerView recycleView;
    ArrayList<String> listPhotoPath = new ArrayList<>();
    private BaseAdapter takeImageAdapter;
    private String add;
    List<String> mTakeImgs = new ArrayList<>();
    private static final int REQUEST_IMAGE = 1001;
    private static final int REQUEST_VIDEO = 1002;


    private String headPortrait="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contribute_article);
    }

    @Override
    public void initView() {
        initPictus();
    }

    @Override
    public void initData() {
    }


    public void initPictus() {
        add = getResources().getString(R.string.cp_open_review_add);
        mTakeImgs.add(add);
        recycleView.setLayoutManager(new GridLayoutManager(this, 3));
        recycleView.addItemDecoration(new SpaceItemDecoration(context,3, 11, false));
        takeImageAdapter = new BaseAdapter<String, NinePicAddView>(context) {
            @Override
            public NinePicAddView createView(int viewType, ViewGroup parent) {
                return new NinePicAddView(context, parent);
            }
        };
        takeImageAdapter.setOnViewClickListener(onViewClickListener);
        recycleView.setAdapter(takeImageAdapter);
        takeImageAdapter.refresh(mTakeImgs);
    }

    private BaseView.OnViewClickListener onViewClickListener= new BaseView.OnViewClickListener() {
        @Override
        public void onViewClick(@NonNull BaseView bv, @NonNull View v) {
            if (v.getId() == R.id.ivAdd) {
                startCamera();
//                takePicture();
            } else if (v.getId() == R.id.ivDel) {
                DialogUIUtils.showMdAlert(context, "提示", "确认要删除?",new DialogUIListener() {
                    @Override
                    public void onPositive() {
                        //删除照片
                        mTakeImgs.remove(bv.position);
                        isNeedShowAdd(mTakeImgs);
                        takeImageAdapter.refresh(mTakeImgs);
                    }
                    @Override
                    public void onNegative() {

                    }
                }).show();
            }
        }
    };
    private void startCamera() {
        MultiImageSelector.create()
                .showCamera(true) // 是否显示相机. 默认为显示
                .count(3) // 最大选择图片数量, 默认为9. 只有在选择模式为多选时有效
                .origin(listPhotoPath)
                .multi()
                .start(this, REQUEST_IMAGE); // 开始拍照
    }

    public void addImgs(String img) {
        /**添加的图片需要在+之前**/
        int index = mTakeImgs.size() - 1;
        mTakeImgs.add(index, img);
        isNeedShowAdd(mTakeImgs);
    }

    /**
     * 是否显示添加图片的按钮
     **/
    void isNeedShowAdd(List<String> mImgs) {
        /**满足6张图片则隐藏+**/
        if (mImgs.size() > 3)
            mImgs.remove(mImgs.size() - 1);
        else if (!mImgs.contains(add))
            mImgs.add(add);
    }

    public List<String> getImgs() {
        /**移除添加文字**/
        if (mTakeImgs != null)
            mTakeImgs.remove(add);
        return mTakeImgs;
    }


    @Override
    public void initEvent() {
        btn_sure.setOnClickListener(this);
        rll_user_tougao.setOnClickListener(this);
    }

    private void submit(){
        showLoading(R.string.msg_handing);
        TreeMap sm = new TreeMap<String,String>();
        if (StringUtil.isEmpty(edt_suggestion_title,true)) {
            showShortToast("投稿标题不能为空");
            return;
        }
//        sm.put("width","480");
//        sm.put("height","480");
        sm.put("title",edt_suggestion_title.getText().toString());
//        投稿类型。P家长投稿，S学校投稿
        boolean isJiaZhang=((RadioButton)rg_home_or_school.getChildAt(0)).isChecked();
        sm.put("type", isJiaZhang?"P":"S");
        sm.put("introduce",edt_suggestion_content.getText().toString());
        List<File> imagefiles=new ArrayList<>();
        for (int i = 0; i < mTakeImgs.size(); i++) {
           File file= new File(mTakeImgs.get(i));
           if (file.exists()){
               print("imageFile:"+file.getAbsolutePath());
               imagefiles.add(file);
           }else {
               print("imageFile不存在:");
           }

        }
        List<File> videofiles=new ArrayList<>();
        if (videoFile.exists()){
            print("videoFile:"+videoFile.getAbsolutePath());
            videofiles.add(videoFile);
        }
        if (!StringUtils.isEmpty(videoImagePath)){
            print("videoImagePath:"+ videoImagePath);
            videofiles.add(new File(videoImagePath));
        }


        LogicService.submitDraftContent(context, sm, imagefiles,videofiles,new ApiSubscriber<Response<String>>() {
            @Override
            public void onSuccess(Response<String> respon) {
                hideLoading();
                showShortToast(respon.getMessage());
                finish();
            }

            @Override
            protected void onFail(Throwable  error) {
                hideLoading();
                showShortToast(error.getMessage());
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rll_user_video_tougao:
//                takePicture();
                takeVideo();
                break;
//            case R.id.iv_takephoto:
//                takePicture();
//                break;
            case R.id.btn_sure:
                submit();
                break;
        }
    }
    private File videoFile;
    private String videoImagePath;

    void takeVideo(){
        Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        videoFile = createMediaFile(); // create a imageFile to save the video
        Uri fileUri = FileProvider7.getUriForFile(this, videoFile);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);  // set the image imageFile name
        intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 0); // set the video image quality to high
        startActivityForResult(intent, REQUEST_VIDEO);
    }

    private File createMediaFile(){
        String timeStamp = DateUtil.format(new Date(),DateUtil.P10);
        String imageFileName = "VID_" + timeStamp;
        String suffix = ".mp4";
        File mediaStorageDir=XszCache.getCacheDir(Constant.CACHE_DIR_FILE);
        File mediaFile = new File(mediaStorageDir + File.separator + imageFileName + suffix);
        return mediaFile;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK) {
            if (requestCode==REQUEST_VIDEO){
                if (!videoFile.exists()){
                    showShortToast("视频拍摄失败，请重试");
                    return;
                }
                MediaMetadataRetriever mmr=new MediaMetadataRetriever();//实例化MediaMetadataRetriever对象
                mmr.setDataSource(videoFile.getAbsolutePath());//设置数据源为该文件对象指定的绝对路径
                Bitmap bitmap=mmr.getFrameAtTime();//获得视频第一帧的Bitmap对象
                iv_user_tougao.setImageBitmap(bitmap);
                iv_user_tougao.setVisibility(View.VISIBLE);
                rtv_user_tougao.setVisibility(View.INVISIBLE);
                String path=saveBitmap(bitmap);
                videoImagePath =path;
                print(path);
                //uploadVideo();
            }else if (requestCode == REQUEST_IMAGE) {
                    listPhotoPath = data.getStringArrayListExtra(MultiImageSelectorActivity.EXTRA_RESULT);
                    for (int i = 0; i < listPhotoPath.size(); i++) {
                        addImgs(listPhotoPath.get(i));
                    }
                    takeImageAdapter.refresh(mTakeImgs);
            }
        }
    }


    String  saveBitmap(Bitmap bitmap){
        String timeStamp = DateUtil.format(new Date(),DateUtil.P10);
        String imageFileName = "IMG_" + timeStamp;
        String suffix = ".jpeg";
        File fileBitmap=new File(XszCache.getCacheDir(Constant.CACHE_DIR_FILE),imageFileName+suffix);
        FileOutputStream fos=null;
        try {
            fos = new FileOutputStream(fileBitmap);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            return fileBitmap.getAbsolutePath();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }finally {
            if (fos!=null){
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }


//    @Override
//    public void takeSuccess(TResult result) {
//        super.takeSuccess(result);
//        ArrayList<TImage> images=result.getImages();
//        for (int i = 0; i < images.size(); i++) {
//            addImgs(images.get(i).getOriginalPath());
//        }
//        takeImageAdapter.refresh(mTakeImgs);


//        if(!StringUtils.isEmpty(originalFilePath)){
//            TreeMap sm = new TreeMap<String,String>();
//            sm.put("width","80");
//            sm.put("height","80");
//            List<File> files =new ArrayList<>();
//            files.add(new File(originalFilePath));
//            loadImage(originalFilePath,iv_takephoto);
//            LogicService.uploadPic(context,sm,files, new ApiSubscriber<Response<JSONArray>>() {
//                @Override
//                public void onSuccess(Response<JSONArray> respon) {
//                    JSONArray jsonArray=respon.getResult();
//                    JSONObject jsonObject=jsonArray.getJSONObject(0);
//                    Log.d(TAG,jsonObject.getString("objectUrl"));
//                    Log.d(TAG,jsonObject.getString("objectUrlWithStyle"));
//                    Log.d(TAG,jsonObject.getString("fileName"));
//                    headPortrait=jsonObject.getString("objectUrlWithStyle");
////                    loadImage(headPortrait,iv_credentials_video_pic);
//                }
//
//                @Override
//                protected void onFail(Throwable  error) {
//                    showShortToast(error.getMessage());
//                    error.printStackTrace();
//                }
//            });
//        }
//    }
}
