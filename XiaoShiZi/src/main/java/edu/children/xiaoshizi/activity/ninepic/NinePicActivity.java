package edu.children.xiaoshizi.activity.ninepic;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dou361.dialogui.DialogUIUtils;
import com.dou361.dialogui.listener.DialogUIListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import edu.children.xiaoshizi.R;
import edu.children.xiaoshizi.activity.XszBaseActivity;
import edu.children.xiaoshizi.adapter.view.NinePicAddView;
import me.nereo.multi_image_selector.MultiImageSelector;
import me.nereo.multi_image_selector.MultiImageSelectorActivity;
import zuo.biao.library.base.BaseAdapter;
import zuo.biao.library.base.BaseView;

public class NinePicActivity extends XszBaseActivity {

    private static final int REQUEST_IMAGE = 1001;

    /**
     * 默认最多选择6张
     */
    int maxImg = 3;
    @BindView(R.id.tvUrl)
    TextView tvUrl;
    @BindView(R.id.recycleView)
    RecyclerView recycleView;
    ArrayList<String> listPhotoPath = new ArrayList<>();

    private BaseAdapter adapter1;
    String add;
    List<String> mImgs = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nine_pic);
    }


    public void initView() {
        add = getResources().getString(R.string.cp_open_review_add);
        mImgs.add(add);
        recycleView.setLayoutManager(new GridLayoutManager(this, 3));
        recycleView.addItemDecoration(new SpaceItemDecoration(context,3, 11, false));

        adapter1 = new BaseAdapter<String, NinePicAddView>(context) {

            @Override
            public NinePicAddView createView(int viewType, ViewGroup parent) {
                return new NinePicAddView(context, parent);
            }
        };
        adapter1.setOnViewClickListener(onViewClickListener);
        recycleView.setAdapter(adapter1);
        adapter1.refresh(mImgs);
    }

   private BaseView.OnViewClickListener onViewClickListener= new BaseView.OnViewClickListener() {
        @Override
        public void onViewClick(@NonNull BaseView bv, @NonNull View v) {
            if (v.getId() == R.id.ivAdd) {
                startCamera();
            } else if (v.getId() == R.id.ivDel) {
                DialogUIUtils.showMdAlert(NinePicActivity.this, "提示", "确认要删除?",new DialogUIListener() {
                    @Override
                    public void onPositive() {
                        //删除照片
                        mImgs.remove(bv.position);
                        isNeedShowAdd(mImgs);
                        adapter1.refresh(mImgs);
                    }
                    @Override
                    public void onNegative() {

                    }
                }).show();
            }
        }
    };

    @Override
    public void initData() {

    }

    @Override
    public void initEvent() {

    }

    private void startCamera() {
        MultiImageSelector.create()
                .showCamera(true) // 是否显示相机. 默认为显示
                .count(maxImg) // 最大选择图片数量, 默认为9. 只有在选择模式为多选时有效
                .origin(listPhotoPath)
                .multi()
                .start(this, REQUEST_IMAGE); // 开始拍照
    }

    public void addImgs(String img) {
        /**添加的图片需要在+之前**/
        int index = mImgs.size() - 1;
        mImgs.add(index, img);
        isNeedShowAdd(mImgs);
    }

    /**
     * 是否显示添加图片的按钮
     **/
    void isNeedShowAdd(List<String> mImgs) {
        /**满足6张图片则隐藏+**/
        if (mImgs.size() > maxImg)
            mImgs.remove(mImgs.size() - 1);
        else if (!mImgs.contains(add))
            mImgs.add(add);
        showUrl();
    }

    public List<String> getImgs() {
        /**移除添加文字**/
        if (mImgs != null)
            mImgs.remove(add);
        return mImgs;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE) {
            if (resultCode == RESULT_OK) { //照片的路径集合返回
                listPhotoPath = data.getStringArrayListExtra(MultiImageSelectorActivity.EXTRA_RESULT);
                for (int i = 0; i < listPhotoPath.size(); i++) {
                    addImgs(listPhotoPath.get(i));
                }
                adapter1.refresh(mImgs);
            }
        }
    }


    /**
     * 打印路劲
     */
    void showUrl() {
        int count = mImgs.size();
        tvUrl.setText("");
        for (int i = 0; i < count; i++) {
            if (mImgs.get(i).equals(add)) continue;
            tvUrl.append(i + "->" + mImgs.get(i) + "\n");
        }
    }

}
