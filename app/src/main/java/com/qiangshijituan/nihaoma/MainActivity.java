package com.qiangshijituan.nihaoma;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.qiangshijituan.nihaoma.R.id.rl_xx1;
import static com.qiangshijituan.nihaoma.R.id.rl_xx2;
import static com.qiangshijituan.nihaoma.R.id.rl_xx3;


/**
 * Created by big_cow on 2016/11/30.
 */

public class MainActivity extends Activity implements View.OnClickListener {

    //图片数量
    private static final int IMAGE_SIZE = 5;

    private static final int TAKE_PICTURE = 0;
    private static final int CHOOSE_PICTURE = 1;
    private static final int SCALE = 5;//照片缩小比例
    private ImageView iv_image = null;
    private static final String TAG = "PublishActivity";
    private ImageView publish_imgs, publish_imgs2, publish_imgs3, publish_imgs4, publish_imgs5, im_et_cha, im_et_cha2, im_et_cha3, im_et_cha4, im_et_cha5;
    private List<Fragment> list = new ArrayList<Fragment>();
    private int mCurrentPageIndex;
    private int mScreen1_3;
    private EditText et_jianbao_biaoti, et_jianbao_miaoshu, et_jianbao_number, et_jianbao_price;
    private String str_jianbao_biaoti, str_jianbao_miaoshu, str_jianbao_number, str_jianbao_price, str_jianbao_email;
    private TextView tv_jianbao_fabu;
    private HashMap<String, String> map;
    private EditText et_jianbao_email;
    private ArrayList<String> imgs;
    private ArrayList<Bitmap> bitmaps;
   /* private RelativeLayout rl_xx1;
    private RelativeLayout rl_xx2;
    private RelativeLayout rl_xx3;
    private RelativeLayout rl_xx4;
    private RelativeLayout rl_xx5;*/

    //news 相同的部分提取出来
    // private List<RelativeLayout> relativeLayoutList = new ArrayList<RelativeLayout>(); //RelativeLayout集合
    private List<ImageView> pictureList = new ArrayList<ImageView>(); //ImageView存放照片的集合

    //照相机的图片视图（负责选择添加照片）
    private ImageView iv_add = null;
    //清除照片IV
    private List<ImageView> xPictureList = new ArrayList<ImageView>(); // x 删除图片图片的集合

    //布局ID
    private static final int[] layoutIds = {rl_xx1, rl_xx2, rl_xx3, R.id.rl_xx4, R.id.rl_xx5};
    private static final int[] imageIds = {R.id.takephoto_picture, R.id.takephoto_picture2,
            R.id.takephoto_picture3, R.id.takephoto_picture4, R.id.takephoto_picture5};
    private static final int[] yiChuIds = {R.id.im_x1, R.id.im_x2, R.id.im_x3, R.id.im_x4, R.id.im_x5};


   /* private ImageView takePhoto_picture, takePhotot_photo, takePhoto_picture2,
            takePhoto_picture3, takePhoto_picture4, takePhoto_picture5, yichu, yichu2, yichu3, yichu4, yichu5;*/


    // 存放图片路径
    private ArrayList<String> mList = new ArrayList<String>();
    private ArrayList<Bitmap> mBitmapList = new ArrayList<Bitmap>();
    //    private HashMap<ImageView, PictureType> map = new HashMap<ImageView, PictureType>();
  /*  private ImageView[] imageViews = new ImageView[5]; // 图片数组
    private ImageView[] xImageViews = new ImageView[5]; // x图片数组*/
    //拍照的路径
    private String takePhotoPath;
    private Bitmap newBitmap;
    private Bitmap smallBitmap;
    private Uri originalUri;
    private int takePhotoName = 0;
    private int idx;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;


    //  private ScrollableLayout mScrollLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        // initPhoto();
        intitButton();
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    /**
     * 初始化视图
     */
    private void initView() {

        for (int i = 0; i < IMAGE_SIZE; i++) {
            //??
            // relativeLayoutList.add((RelativeLayout) findViewById(layoutIds[i]));
            //图片框视图
            pictureList.add((ImageView) findViewById(imageIds[i]));
            //图片框视图绑定点击事件
            pictureList.get(i).setOnClickListener(MainActivity.this);
            //叉号视图
            xPictureList.add((ImageView) findViewById(yiChuIds[i]));
            //叉号视图绑定点击事件
            xPictureList.get(i).setOnClickListener(MainActivity.this);

        }

        iv_add = pictureList.get(0);

        /*
        rl_xx1 = (RelativeLayout) findViewById(R.id.rl_xx1);
        rl_xx2 = (RelativeLayout) findViewById(R.id.rl_xx2);
        rl_xx3 = (RelativeLayout) findViewById(R.id.rl_xx3);
        rl_xx4 = (RelativeLayout) findViewById(R.id.rl_xx4);
        rl_xx5 = (RelativeLayout) findViewById(R.id.rl_xx5);

        takePhoto_picture = (ImageView) findViewById(R.id.takephoto_picture); // 图片
        takePhoto_picture2 = (ImageView) findViewById(R.id.takephoto_picture2);   // 图片
        takePhoto_picture3 = (ImageView) findViewById(R.id.takephoto_picture3); // 图片
        takePhoto_picture4 = (ImageView) findViewById(R.id.takephoto_picture4); // 图片
        takePhoto_picture5 = (ImageView) findViewById(R.id.takephoto_picture5); // 图片

        */

//        imageViews[0] = publish_imgs;
//        imageViews[1] = publish_imgs2;
//        imageViews[2] = publish_imgs3;
//        imageViews[3] = publish_imgs4;
//        imageViews[4] = publish_imgs5;

        et_jianbao_biaoti = (EditText) findViewById(R.id.et_jiianbao_biaoti); //标题
        et_jianbao_miaoshu = (EditText) findViewById(R.id.et_jianbao_miaoshu); //描述
        et_jianbao_number = (EditText) findViewById(R.id.et_jianbao_number);//手机号
        et_jianbao_price = (EditText) findViewById(R.id.et_jianbao_price);// 价格;
        et_jianbao_email = (EditText) findViewById(R.id.et_jianbao_email); //社交软件
        tv_jianbao_fabu = (TextView) findViewById(R.id.tv_jianbao_fabu);
        et_jianbao_biaoti.addTextChangedListener(textWatcher);
        et_jianbao_miaoshu.addTextChangedListener(textWatcher);
        //  点击x 清除EditText
        im_et_cha = (ImageView) findViewById(R.id.im_et_x1); // x
        im_et_cha2 = (ImageView) findViewById(R.id.im_et_x4); // x
        im_et_cha3 = (ImageView) findViewById(R.id.im_et_x3); // x
        im_et_cha4 = (ImageView) findViewById(R.id.im_et_x2); // x
        im_et_cha5 = (ImageView) findViewById(R.id.im_et_x5); // x
        //  清除EditText
        im_et_cha.setOnClickListener(MainActivity.this);
        im_et_cha2.setOnClickListener(MainActivity.this);
        im_et_cha3.setOnClickListener(MainActivity.this);
        im_et_cha4.setOnClickListener(MainActivity.this);
        im_et_cha5.setOnClickListener(MainActivity.this);
/*
        // 移除照片的x
        yichu = (ImageView) findViewById(R.id.im_x1); // x图片
        yichu2 = (ImageView) findViewById(R.id.im_x2); // x图片
        yichu3 = (ImageView) findViewById(R.id.im_x3); // x图片
        yichu4 = (ImageView) findViewById(R.id.im_x4); // x图片
        yichu5 = (ImageView) findViewById(R.id.im_x5); // x图片
        // 移除的数组
        xImageViews[0] = yichu;
        xImageViews[1] = yichu2;
        xImageViews[2] = yichu3;
        xImageViews[3] = yichu4;
        xImageViews[4] = yichu5;
        // 存放照片的
        imageViews[0] = takePhoto_picture;
        imageViews[1] = takePhoto_picture2;
        imageViews[2] = takePhoto_picture3;
        imageViews[3] = takePhoto_picture4;
        imageViews[4] = takePhoto_picture5;
        // 移除照片的点击事件
        yichu.setOnClickListener(MainActivity.this);
        yichu2.setOnClickListener(MainActivity.this);
        yichu3.setOnClickListener(MainActivity.this);
        yichu4.setOnClickListener(MainActivity.this);
        yichu5.setOnClickListener(MainActivity.this);
        // 图片点击事件
//        takePhoto_picture.setOnClickListener(MainActivity.this);
//        takePhoto_picture2.setOnClickListener(MainActivity.this);
//        takePhoto_picture3.setOnClickListener(MainActivity.this);
//        takePhoto_picture4.setOnClickListener(MainActivity.this);
//        takePhoto_picture5.setOnClickListener(MainActivity.this);
*/


    }


    private void initPhoto() {

        pictureList.get(idx).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                showPicturePicker(MainActivity.this);
            }
        });
    }

    /**
     * 获取照片
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case TAKE_PICTURE:
                    Uri uri = data.getData();
                    if (uri == null) {
                        Bitmap bitmap = (Bitmap) data.getExtras().get("data");

                        String name = String.valueOf(System.currentTimeMillis());

                        ImageTools.savePhotoToSDCard(newBitmap, Environment.getExternalStorageDirectory().getAbsolutePath(), name);
                    } else {
                        Cursor cursor = managedQuery(uri, new String[]{MediaStore.Images.Media.DATA}, null, null, null);
                        int index = cursor.getColumnIndex(MediaStore.Images.Media.DATA);
                        cursor.moveToFirst();
                        String path = cursor.getString(index);

                    }
//                    //将保存在本地的图片取出并缩小后显示在界面上
//                    Bitmap bitmap = BitmapFactory.decodeFile(takePhotoPath);
//                    newBitmap = ImageTools.zoomLittleBitmap(bitmap);
//                    //由于Bitmap内存占用较大，这里需要回收内存，否则会报out of memory异常
//                    bitmap.recycle();
//                    //已经保存的图片数量
//                    idx = mBitmapList.size();
//                    //TODO
//                    pictureList.get(idx).setImageBitmap(newBitmap);
//                    pictureList.get(idx).setVisibility(View.VISIBLE); // 图片显示
//                    relativeLayoutList.get(idx).setVisibility(View.VISIBLE); // 整个控件显示
//                    xPictureList.get(idx).setVisibility(View.VISIBLE); // x显示
//                    addMore();
//                    mList.add(takePhotoPath);
//                    mBitmapList.add(newBitmap);


                    /*
                    //将处理过的图片显示在界面上，
                    switch (mBitmapList.size()) {
                        // 如果位置等于0的时候我们往HashMap中添加第一个图片，以后就以此类推
                        case 0:

                            break;
                        case 1:
                            takePhoto_picture2.setImageBitmap(newBitmap);
                            takePhoto_picture2.setVisibility(View.VISIBLE);
                            rl_xx2.setVisibility(View.VISIBLE);
                            yichu2.setVisibility(View.VISIBLE);
                            addMore();
                            mList.add(takePhotoPath);
                            mBitmapList.add(newBitmap);
                            break;
                        case 2:
                            takePhoto_picture3.setImageBitmap(newBitmap);
                            takePhoto_picture3.setVisibility(View.VISIBLE);
                            rl_xx3.setVisibility(View.VISIBLE);
                            yichu3.setVisibility(View.VISIBLE);
                            addMore();
                            mList.add(takePhotoPath);
                            mBitmapList.add(newBitmap);
                            break;
                        case 3:
                            takePhoto_picture4.setImageBitmap(newBitmap);
                            takePhoto_picture4.setVisibility(View.VISIBLE);
                            rl_xx4.setVisibility(View.VISIBLE);
                            yichu4.setVisibility(View.VISIBLE);
                            addMore();
                            mList.add(takePhotoPath);
                            mBitmapList.add(newBitmap);
                            break;
                        case 4:
                            takePhoto_picture5.setImageBitmap(newBitmap);
                            takePhoto_picture5.setVisibility(View.VISIBLE);
                            rl_xx5.setVisibility(View.VISIBLE);
                            yichu5.setVisibility(View.VISIBLE);
                            addMore();
                            mList.add(takePhotoPath);
                            mBitmapList.add(newBitmap);
                            break;
                    }
                    */
                    // 并保存到本地
                    //:TODO 如果手机不支持返回uri，则需要保存到本地，在获取uri

//                  break;

                case CHOOSE_PICTURE:
                    ContentResolver resolver = getContentResolver();
                    //照片的原始资源地址
                    originalUri = data.getData();
                    try {
                        //使用ContentProvider通过URI获取原始图片
                        Bitmap photo = MediaStore.Images.Media.getBitmap(resolver, originalUri);
                        if (photo != null) {
                            //为防止原始图片过大导致内存溢出，这里先缩小原图显示，然后释放原始Bitmap占用的内存
                            // smallBitmap = ImageTools.zoomLittleBitmap(photo, photo.getWidth() / SCALE,
                            //  photo.getHeight() / SCALE);

                            smallBitmap = ImageTools.zoomLittleBitmap(photo);
                            //释放原始图片占用的内存，防止out of memory异常发生
                            photo.recycle();

                            //照片列表的长度
                            idx = mBitmapList.size();
                            //设置图片
                            pictureList.get(idx).setImageBitmap(smallBitmap);
                            //删除点击事件
                            pictureList.get(idx).setOnClickListener(null);
                            // 图片显示
                            pictureList.get(idx).setVisibility(View.VISIBLE);
                            // x显示
                            xPictureList.get(idx).setVisibility(View.VISIBLE);
                            // 整个控件显示
                            //relativeLayoutList.get(idx).setVisibility(View.VISIBLE);
                            //添加图片到列表
                            String s = originalUri.toString();
                            mList.add(s);
                            mBitmapList.add(smallBitmap);

                            //检查图片列表是否已满
                            if (mBitmapList.size() < IMAGE_SIZE) {
                                //添加图片控件后移
                                addMore();
                            }

                           /* switch (mBitmapList.size()) {
                                case 0:
                                    takePhoto_picture.setImageBitmap(smallBitmap);
                                    takePhoto_picture.setVisibility(View.VISIBLE);
                                    rl_xx1.setVisibility(View.VISIBLE);
                                    yichu.setVisibility(View.VISIBLE);
                                    addMore();
                                    String s = originalUri.toString();
                                    mList.add(s);
                                    mBitmapList.add(smallBitmap);

                                    break;
                                case 1:
                                    takePhoto_picture2.setImageBitmap(smallBitmap);
                                    takePhoto_picture2.setVisibility(View.VISIBLE);
                                    rl_xx2.setVisibility(View.VISIBLE);
                                    yichu2.setVisibility(View.VISIBLE);
                                    addMore();
                                    Uri picture_uri2 = data.getData();
                                    // 把Uri转化成String类型
                                    String s2 = originalUri.toString();
                                    mList.add(s2);
                                    mBitmapList.add(smallBitmap);
                                    break;
                                case 2:
                                    takePhoto_picture3.setImageBitmap(smallBitmap);
                                    takePhoto_picture3.setVisibility(View.VISIBLE);
                                    rl_xx3.setVisibility(View.VISIBLE);
                                    yichu3.setVisibility(View.VISIBLE);
                                    addMore();
                                    Uri picture_uri3 = data.getData();
                                    String s3 = originalUri.toString();
                                    mList.add(s3);
                                    mBitmapList.add(smallBitmap);
                                    break;
                                case 3:
                                    takePhoto_picture4.setImageBitmap(smallBitmap);
                                    takePhoto_picture4.setVisibility(View.VISIBLE);
                                    rl_xx4.setVisibility(View.VISIBLE);
                                    yichu4.setVisibility(View.VISIBLE);
                                    addMore();
                                    Uri picture_uri4 = data.getData();
                                    String s4 = originalUri.toString();
                                    mList.add(s4);
                                    mBitmapList.add(smallBitmap);
                                    break;
                                case 4:
                                    takePhoto_picture5.setImageBitmap(smallBitmap);
                                    takePhoto_picture5.setVisibility(View.VISIBLE);
                                    rl_xx5.setVisibility(View.VISIBLE);
                                    yichu5.setVisibility(View.VISIBLE);
                                    Uri picture_uri5 = data.getData();
                                    String s5 = originalUri.toString();
                                    mList.add(s5);
                                    mBitmapList.add(smallBitmap);
                                    break;
                            }
                            */
                        }

                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {

                        e.printStackTrace();
                    }

                default:
                    break;
            }
        }
    }


    public void showPicturePicker(Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("图片来源");
        builder.setNegativeButton("取消", null);
        builder.setItems(new String[]{"拍照", "相册"}, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case TAKE_PICTURE:
                        takePhotoName++;
                        takePhotoPath = Environment.getExternalStorageDirectory() + "/image" + takePhotoName + ".jpg";
                        Intent openCameraIntent = new Intent(takePhotoPath);
                        Uri imageUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(), "image.jpg"));
                        //指定照片保存路径（SD卡），image.jpg为一个临时文件，每次拍照后这个图片都会被替换
                        openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                        startActivityForResult(openCameraIntent, TAKE_PICTURE);
                        break;

                    case CHOOSE_PICTURE:
                        Intent openAlbumIntent = new Intent(Intent.ACTION_GET_CONTENT);
                        openAlbumIntent.setType("image/*");
                        startActivityForResult(openAlbumIntent, CHOOSE_PICTURE);
                        break;

                    default:
                        break;
                }
            }
        });
        builder.create().show();
    }

    // 点击×  清除EditTextq
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.im_et_x1:
                et_jianbao_biaoti.setText("");
                break;
            case R.id.im_et_x3:
                et_jianbao_price.setText("");
                break;
            case R.id.im_et_x4:
                et_jianbao_number.setText("");
                break;
            case R.id.im_et_x2:
                et_jianbao_miaoshu.setText("");
                break;
            case R.id.im_et_x5:
                et_jianbao_email.setText("");
                break;
        }

        // TODO
        switch (view.getId()) {
            case R.id.im_x1:
                resetImageView(0);
                break;
            case R.id.im_x2:
                resetImageView(1);
                break;
            case R.id.im_x3:
                resetImageView(2);
                break;
            case R.id.im_x4:
                resetImageView(3);
                break;
            case R.id.im_x5:
                resetImageView(4);
                break;
        }

        ImageView iv = (ImageView) findViewById(view.getId());

        if (iv_add.equals(iv)) {
            showPicturePicker(MainActivity.this);
        }
        /*
        switch (view.getId()) {
            case R.id.takephoto_picture:
                showPicturePicker(MainActivity.this);
                break;

            case R.id.takephoto_picture2:
                showPicturePicker(MainActivity.this);
                break;

            case R.id.takephoto_picture3:
                showPicturePicker(MainActivity.this);
                break;
            case R.id.takephoto_picture4:
                showPicturePicker(MainActivity.this);
                break;

            case R.id.takephoto_picture5:
                showPicturePicker(MainActivity.this);
                break;
        }
        */
    }

    // 添加照片
    private void addMore() {
        //添加照片控件后移
        iv_add = pictureList.get(idx + 1);
        //设置图片
        iv_add.setImageResource(R.mipmap.pppp);
        //设置显示
        iv_add.setVisibility(View.VISIBLE);
        //设置点击事件
        iv_add.setOnClickListener(MainActivity.this);
        //imageViews[mBitmapList.size() + 1].setImageResource(R.mipmap.pppp);
        //imageViews[mBitmapList.size() + 1].setVisibility(View.VISIBLE);
        //让显示照相机的图片做添加图片的操作，让其图片没有点击事件（然后）让其余图片点击看大图
//        for (int i = 0; i < pictureList.size(); i++) {
//            //  imageViews[i].setOnClickListener(null);
//            pictureList.get(idx).setOnClickListener(null);
//        }
        //imageViews[mBitmapList.size() + 1].setOnClickListener(MainActivity.this);

    }

    // 移除图片的方法
    private void resetImageView(int position) {

        //删除指定位置的图片
        mBitmapList.remove(position);

        //被删除图片右侧有图片，则左移
        for (int i = position; i < mBitmapList.size(); i++) {
            Bitmap img = mBitmapList.get(i);
            pictureList.get(i).setImageBitmap(img);
        }
        //位移后空出的位置变照相机
        iv_add = pictureList.get(mBitmapList.size());
        iv_add.setImageResource(R.mipmap.pppp);
        iv_add.setOnClickListener(this);
        //叉号隐藏
        xPictureList.get(mBitmapList.size()).setVisibility(View.INVISIBLE);

        //最后一个图片控件
        if (mBitmapList.size() + 1 < IMAGE_SIZE) {
            pictureList.get(mBitmapList.size() + 1).setImageBitmap(null);
            pictureList.get(mBitmapList.size() + 1).setOnClickListener(null);
        }


//       /*
//
//        for (int i = 0; i < pictureList.size(); i++) {
//           /* imageViews[i].setVisibility(View.INVISIBLE);
//            imageViews[i].setImageBitmap(null);
//            xImageViews[i].setVisibility(View.INVISIBLE);*/
//
//            pictureList.get(idx).setVisibility(View.INVISIBLE);
//            xPictureList.get(idx).setVisibility(View.INVISIBLE);
//            pictureList.get(idx).setImageBitmap(null);
//
//        }
//        // TODO
//        mBitmapList.remove(position);//存储bitmap的集合
//        //把移除完剩下的图片显示出来
//        for (int i = 0; i < mBitmapList.size(); i++) {
//            imageViews[i].setImageBitmap(mBitmapList.get(i));
//            imageViews[i].setVisibility(View.VISIBLE);
//            xImageViews[i].setVisibility(View.VISIBLE);
//            pictureList.get(idx).setImageBitmap(mBitmapList.get(i));
//            pictureList.get(idx).setVisibility(View.VISIBLE);
//            xPictureList.get(idx).setVisibility(View.VISIBLE);
//        }
//        //在最后一个位置显示加号+++
//        int positionPlus = mBitmapList.size();
//        imageViews[positionPlus].setImageResource(R.mipmap.pppp);
//        imageViews[positionPlus].setVisibility(View.VISIBLE);
//        imageViews[positionPlus].setOnClickListener(MainActivity.this);
//        pictureList.get(idx).setImageResource(R.mipmap.pppp);
//        pictureList.get(idx).setVisibility(View.VISIBLE);
//        pictureList.get(idx).setOnClickListener(MainActivity.this);
//        */
    }


    // 上传数据
    private void initUploading() {
        //intiData();
        // OkHttp接口回调
        OkHttpUtil.setGetEntiydata(new OkHttpUtil.EntiyData() {
            @Override
            public void getEntiy(Object obj) {

                Publish_Bean publish_o1 = (Publish_Bean) obj;
                String status = publish_o1.getStatus();
                if (status.equals("200")) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(MainActivity.this, "发布成功", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(MainActivity.this, "发布失败", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });

        OkHttpUtil.postMuti(map, "http://192.168.4.188/Goods/"
                + "app/item/issue.json", MainActivity.this, Publish_Bean.class, imgs);
    }

    private void intiData() {
        str_jianbao_biaoti = et_jianbao_biaoti.getText().toString().trim();
        str_jianbao_miaoshu = et_jianbao_miaoshu.getText().toString().trim();
        str_jianbao_price = et_jianbao_price.getText().toString().trim();
        str_jianbao_number = et_jianbao_number.getText().toString().trim();
        str_jianbao_email = et_jianbao_email.getText().toString().trim();
        map = new HashMap<>();
        map.put("title", str_jianbao_biaoti);
        map.put("description", str_jianbao_miaoshu);
        map.put("price", str_jianbao_price);
        map.put("mobile", str_jianbao_number);
        map.put("email", str_jianbao_email);
        SharedPreferences share = getSharedPreferences("TOKEN", MODE_PRIVATE);
        String token = share.getString("token", "ll");
        map.put("token", token);

    }

    private void intitButton() { //点击发布

        tv_jianbao_fabu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intiData();
                if ("".equals(str_jianbao_biaoti)) {
                    Toast.makeText(MainActivity.this, "亲，怎们可以没有标题呢", Toast.LENGTH_SHORT).show();
                } else if ("".equals(str_jianbao_miaoshu)) {
                    Toast.makeText(MainActivity.this, "亲，描述下你的宝贝", Toast.LENGTH_SHORT).show();
                } else if ("".equals(str_jianbao_price)) {
                    Toast.makeText(MainActivity.this, "无价之宝，别闹了，留个价格呗", Toast.LENGTH_SHORT).show();
                } else if ("".equals(str_jianbao_number)) {
                    Toast.makeText(MainActivity.this, "亲，留下电话方便联系呦", Toast.LENGTH_SHORT).show();
                } else {
                    initUploading();
                }
            }
        });
    }

    // 判断Edittext最多输入多少字符
    TextWatcher textWatcher = new TextWatcher() {

        private Toast toast;

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count,
                                      int after) {
            //TODO
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before,
                                  int count) {
            //TODO
        }

        @Override
        public void afterTextChanged(Editable s) {

            Log.d("TAG", "afterTextChanged    " + "str=" + s.toString());
            int len = et_jianbao_biaoti.getText().toString().length();
            int len2 = et_jianbao_miaoshu.getText().toString().length();
            if (len >= 9) {
                Toast.makeText(MainActivity.this, "亲,最多输入10个字呦", Toast.LENGTH_SHORT).show();
                et_jianbao_biaoti.setFilters(new InputFilter[]{new InputFilter.LengthFilter(10)}); //最多输入10的字符
            }
            if (len >= 29) {
                Toast.makeText(MainActivity.this, "亲,最多输入30个字呦", Toast.LENGTH_SHORT).show();
                et_jianbao_miaoshu.setFilters(new InputFilter[]{new InputFilter.LengthFilter(30)}); //最多输入30的字符
            }
        }
    };


    /**
     * 验证手机格式
     */
    public static boolean isMobileNO(String mobiles) {
    /*
    移动：134、135、136、137、138、139、150、151、157(TD)、158、159、187、188
    联通：130、131、132、152、155、156、185、186
    电信：133、153、180、189、（1349卫通）
    总结起来就是第一位必定为1，第二位必定为3或5或8，其他位置的可以为0-9
    */
        String et_jianbao_number = "[1][358]\\d{9}";//"[1]"代表第1位为数字1，"[358]"代表第二位可以为3、5、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。
        if (TextUtils.isEmpty(mobiles)) {
            return false;
        } else {
            return mobiles.matches(et_jianbao_number);
        }
    }

    /**
     * 点击空白区域隐藏键盘.
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if (MainActivity.this.getCurrentFocus() != null) {
                if (MainActivity.this.getCurrentFocus().getWindowToken() != null) {
                    imm.hideSoftInputFromWindow(MainActivity.this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                }
            }
        }
        return super.onTouchEvent(event);
    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("Main Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }
}


