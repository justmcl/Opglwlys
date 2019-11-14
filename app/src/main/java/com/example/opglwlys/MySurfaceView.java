package com.example.opglwlys;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.opengl.GLSurfaceView;
import android.opengl.GLUtils;
import android.renderscript.Sampler;
import android.util.Log;
import android.view.MotionEvent;
import android.opengl.GLES30;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.Toast;

import static com.example.opglwlys.MainActivity.dbHelper;

class MySurfaceView extends GLSurfaceView 
{
	private final float TOUCH_SCALE_FACTOR = 180.0f/320;//角度缩放比例
    private SceneRenderer mRenderer;//场景渲染器

    private ArrayList<Float> datas=new ArrayList<Float>();
    private ArrayList<Float> oo=new ArrayList<Float>();
    private ArrayList<Float> strokeC=new ArrayList<Float>();
    private ArrayList<Float> strokeP=new ArrayList<Float>();

//    private mdh dbHelper;



    private int flag1=0;
    private int flag2=0;

    private int xmin=0;
    private int xmax=0;
    private int ymin=0;
    private int ymax=0;




    //    private float triangleCoords[]={   // in counterclockwise order:
//            0.5f,  -0.5f, // top
//            0.5f, 0.5f, // bo.tom left
//            -0.5f, -0.5f,
//            -0.5f, 0.5f
//    };
    final float UNIT_SIZE=0.15f;
    float triangleCoords[]=new float[]
            {
                    -0.5f,0.5f,0, -0.5f,-0.5f,0,0.5f,0.5f,0, 0.5f,-0.5f,0, 0.5f,-0.5f,0,
            };
    float texCoor[]=new float[]//顶点颜色值数组，每个顶点4个色彩值RGBA
            {
                    0,0, 0,1, 1,0 , 1,1, 1,1,
            };
    private float x1;//上次的触控位置Y坐标
    private float y1;//上次的触控位置X坐标
    
    int textureId;//系统分配的纹理id
    int textureId1;//系统分配的纹理id


    public MySurfaceView(Context context) {
        super(context);
        this.setEGLContextClientVersion(3);	//设置使用OPENGL ES3.0
        mRenderer = new SceneRenderer();	//创建场景渲染器
        setRenderer(mRenderer);				//设置渲染器
        setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);//设置渲染模式为主动渲染
    }
	
	//触摸事件回调方法
    @SuppressLint("ClickableViewAccessibility")
	@Override 
    public boolean onTouchEvent(MotionEvent e)	
    {
//        float y = e.getY();
//        float x = e.getX();
        float x ;
        float y ;
        float h=getHeight() /2;
        float w =getWidth()/2;
        float pressure=e.getPressure();
        float s=pressure/128;


        switch (e.getAction()) {
            case MotionEvent.ACTION_MOVE:
            {
                if(s==1f/128){
                int xx=(int)e.getX();
                int yy=(int)e.getY();
                    SQLiteDatabase db2 = dbHelper.getWritableDatabase(); // 查询 Book 表中所有的数据
//        Cursor cursor = db2.query("Book", new String[]{"coord,pressure,page"}, "page=?", new String[]{"1"}, null, null, null);
                    Cursor cursor = db2.query("Book", null, null, null, null, null, null);
                    while (cursor.moveToNext()) {
                        int id = cursor.getInt(cursor.getColumnIndex("id"));

                        int page = cursor.getInt(cursor.getColumnIndex("page"));

                        int xmin = cursor.getInt(cursor.getColumnIndex("xmin"));
                        int xmax = cursor.getInt(cursor.getColumnIndex("xmax"));
                        int ymin = cursor.getInt(cursor.getColumnIndex("ymin"));
                        int ymax = cursor.getInt(cursor.getColumnIndex("ymax"));

                        String coord = cursor.getString(cursor.getColumnIndex("coord"));

                        if(page==1&&xmin<=xx&&xx<=xmax&&ymin<=yy&&yy<=ymax){
                            int dot=coord.length()/8;
                            for(int i=1;i<=dot;i++) {
                                String a1=coord.substring(8*i-8,8*i-4);
                                int a2=Integer.parseInt(a1);
                                float a3=0.0001f*a2;
                                String b1=coord.substring(8*i-4,8*i);
                                int b2=Integer.parseInt(b1);
                                float b3=0.0001f*b2;
                                float xxx=(xx-getWidth()/2f)/(getWidth()/2f);
                                float yyy=-1*(yy-getHeight()/2f)/(getWidth()/2f);
                                Log.e("bbbb",String.valueOf(xxx)+"   "+yyy);
                                Log.e("bbbb",String.valueOf(a3)+"   "+b3);




                                if((xxx-a3)*(xxx-a3)+(yyy-b3)*(yyy-b3)<=2){
                                    db2.delete("Book", "id = ?",new String[] { String.valueOf(id) });
                                    Log.e("bbbb","balavalabala");
//                                    SQLiteDatabase db2 = dbHelper.getWritableDatabase(); // 查询 Book 表中所有的数据
//        Cursor cursor = db2.query("Book", new String[]{"coord,pressure,page"}, "page=?", new String[]{"1"}, null, null, null);
                                    Cursor cursor2 = db2.query("Book", null, null, null, null, null, null);

                                    while (cursor.moveToNext()) {
                                        String cooord = cursor.getString(cursor.getColumnIndex("coord"));
                                        String pressure1 = cursor.getString(cursor.getColumnIndex("pressure"));
                                        int page1 = cursor.getInt(cursor.getColumnIndex("page"));
                                        if(page1==1){
                                            datas.clear();
                                            oo.clear();
//                                            Log.e("fff", cooord );
//                                            Log.e("fff", pressure1);
//                                            Log.e("fff", String.valueOf(page));
                                            getStroke(cooord,pressure1);

                                            setlist(datas,oo);
                                            requestRender();

                                        }
                                    }
                                    break;
                                }
                            }
                        }
                    }

                }
            }//手指功能
            {
                if(s!=1f/128) {
                    x = (e.getX() - w) / w;
                    y = -1 * (e.getY() - h) / w;
                    float dx = x - x1;//计算触控笔X位移
                    float dy = y - y1;//计算触控笔Y位移
                    float ll = (float) Math.sqrt(dx * dx + dy * dy);
                    float rrss = s / ll;
                    float dx2 =
                            dy * rrss;
                    float dy2 = -1 *
                            dx * rrss;

                    {
                        datas.add(x - s);
                        datas.add(y + s);
                        datas.add(0f);
                        oo.add(0f);
                        oo.add(0f);
                        datas.add(x - s);
                        datas.add(y + s);
                        datas.add(0f);
                        oo.add(0f);
                        oo.add(0f);

                        datas.add(x - s);
                        datas.add(y - s);
                        datas.add(0f);
                        oo.add(0f);
                        oo.add(1f);

                        datas.add(x + s);
                        datas.add(y + s);
                        datas.add(0f);
                        oo.add(1f);
                        oo.add(0f);
                        //右下
                        datas.add(x + s);
                        datas.add(y - s);
                        datas.add(0f);
                        oo.add(1f);
                        oo.add(1f);
                        datas.add(x + s);
                        datas.add(y - s);
                        datas.add(0f);
                        oo.add(1f);
                        oo.add(1f);
                        //画线
                        //左上
                        datas.add(x1 - dx2);
                        datas.add(y1 - dy2);
                        datas.add(0f);
                        oo.add(0.5f);
                        oo.add(0.5f);
                        datas.add(x1 - dx2);
                        datas.add(y1 - dy2);
                        datas.add(0f);
                        oo.add(0.5f);
                        oo.add(0.5f);
                        datas.add(x1 + dx2);
                        datas.add(y1 + dy2);
                        datas.add(0f);
                        oo.add(0.5f);
                        oo.add(0.6f);
                        Log.e("ss", String.valueOf(x1 - dx2) + "           " + String.valueOf(y1 - dy2));

                        datas.add(x - dx2);
                        datas.add(y - dy2);
                        datas.add(0f);
                        oo.add(0.6f);
                        oo.add(0.5f);
                        Log.e("ss", String.valueOf(x + dx2) + "           " + String.valueOf(y + dy2));

                        //右下
                        datas.add(x + dx2);
                        datas.add(y + dy2);
                        datas.add(0f);
                        oo.add(0.6f);
                        oo.add(0.6f);
                        datas.add(x + dx2);
                        datas.add(y + dy2);
                        datas.add(0f);
                        oo.add(0.6f);
                        oo.add(0.6f);
                        Log.e("ss", String.valueOf(x - dx2) + "           " + String.valueOf(y - dy2));
                    }//加入顶点
                    {
                        xmin = Math.min(xmin, (int) e.getX());
                        xmax = Math.max(xmax, (int) e.getX());
                        ymin = Math.min(ymin, (int) e.getY());
                        ymax = Math.max(ymax, (int) e.getY());
                    }//获取范围

                    strokeC.add(e.getX() / 4096);
                    strokeC.add(e.getY() / 4096);
                    strokeP.add(pressure);

                    x1 = x;
                    y1 = y;

                    setlist(datas, oo);
                    //                float[]one = datas.toAr ray(new float[datas.size()]);
                    requestRender();
                    break;
                }
            }//Spen功能

            case MotionEvent.ACTION_DOWN:
            {
                if(s==1f/128){
                    int xx=(int)e.getX();
                    int yy=(int)e.getY();
                    SQLiteDatabase db2 = dbHelper.getWritableDatabase(); // 查询 Book 表中所有的数据
//        Cursor cursor = db2.query("Book", new String[]{"coord,pressure,page"}, "page=?", new String[]{"1"}, null, null, null);
                    Cursor cursor = db2.query("Book", null, null, null, null, null, null);
                    while (cursor.moveToNext()) {
                        int id = cursor.getInt(cursor.getColumnIndex("id"));

                        int page = cursor.getInt(cursor.getColumnIndex("page"));

                        int xmin = cursor.getInt(cursor.getColumnIndex("xmin"));
                        int xmax = cursor.getInt(cursor.getColumnIndex("xmax"));
                        int ymin = cursor.getInt(cursor.getColumnIndex("ymin"));
                        int ymax = cursor.getInt(cursor.getColumnIndex("ymax"));

                        String coord = cursor.getString(cursor.getColumnIndex("coord"));

                        if(page==1&&xmin<=xx&&xx<=xmax&&ymin<=yy&&yy<=ymax){
                            int dot=coord.length()/8;
                            for(int i=1;i<=dot;i++) {
                                String a1=coord.substring(8*i-8,8*i-4);
                                int a2=Integer.parseInt(a1);
                                float a3=0.0001f*a2;
                                String b1=coord.substring(8*i-4,8*i);
                                int b2=Integer.parseInt(b1);
                                float b3=0.0001f*b2;
                                float xxx=(xx-getWidth()/2f)/(getWidth()/2f);
                                float yyy=-1*(yy-getHeight()/2f)/(getWidth()/2f);
                                Log.e("bbbb",String.valueOf(xxx)+"   "+yyy);
                                Log.e("bbbb",String.valueOf(a3)+"   "+b3);




                                if((xxx-a3)*(xxx-a3)+(yyy-b3)*(yyy-b3)<=2){
                                    db2.delete("Book", "id = ?",new String[] { String.valueOf(id) });
                                    Log.e("bbbb","balavalabala");
//                                    SQLiteDatabase db2 = dbHelper.getWritableDatabase(); // 查询 Book 表中所有的数据
//        Cursor cursor = db2.query("Book", new String[]{"coord,pressure,page"}, "page=?", new String[]{"1"}, null, null, null);
                                    Cursor cursor2 = db2.query("Book", null, null, null, null, null, null);

                                    while (cursor.moveToNext()) {
                                        String cooord = cursor.getString(cursor.getColumnIndex("coord"));
                                        String pressure1 = cursor.getString(cursor.getColumnIndex("pressure"));
                                        int page1 = cursor.getInt(cursor.getColumnIndex("page"));
                                        if(page1==1){
                                            datas.clear();
                                            oo.clear();
//                                            Log.e("fff", cooord );
//                                            Log.e("fff", pressure1);
//                                            Log.e("fff", String.valueOf(page));
                                            getStroke(cooord,pressure1);

                                            setlist(datas,oo);
                                            requestRender();

                                        }
                                    }
                                    break;
                                }
                            }
                        }
                    }

                }
            }//手指功能
            {if(s!=1f/128){
                performClick();
                x = (e.getX()-w)/w;
                y= -1*(e.getY()-h)/w;
                x1=x;
                y1=y;

                strokeC.clear();
                strokeP.clear();

                {
                if (datas.size() != 1) {
                    datas.add(x - s);
                    datas.add(y + s);
                    datas.add(0f);
                    oo.add(0f);
                    oo.add(0f);
                }
                datas.add(x - s);
                datas.add(y + s);
                datas.add(0f);
                oo.add(0f);
                oo.add(0f);

                datas.add(x - s);
                datas.add(y - s);
                datas.add(0f);
                oo.add(0f);
                oo.add(1f);

                datas.add(x + s);
                datas.add(y + s);
                datas.add(0f);
                oo.add(1f);
                oo.add(0f);
                //右下
                datas.add(x + s);
                datas.add(y - s);
                datas.add(0f);
                oo.add(1f);
                oo.add(1f);
                datas.add(x + s);
                datas.add(y - s);
                datas.add(0f);
                oo.add(1f);
                oo.add(1f);
            }//加入顶点
                {
                xmin=(int)e.getX();
                xmax=(int)e.getX();
                ymin=(int)e.getY();
                ymax=(int)
                        e.getY();
            }//获取范围

                strokeC.add(e.getX()/4096);
                strokeC.add(e.getY()/4096);
                strokeP.add(pressure);

                flag1=1;

                setlist(datas,oo);
                requestRender();
                break;
                }
            }//Spen功能
            case MotionEvent.ACTION_UP:
                getString(strokeC,strokeP,xmin,xmax,ymin,ymax);
                break;
        }
        return true;
    }

	private class SceneRenderer implements Renderer
    {   
    	Triangle texRect;//纹理三角形对象引用
        BG bg;
    	
        public void onDrawFrame(GL10 gl)
            {

            //清除深度缓冲与颜色缓冲
            GLES30.glClear( GLES30.GL_DEPTH_BUFFER_BIT | GLES30.GL_COLOR_BUFFER_BIT);
            //绘制纹理三角形
                texRect.setCoords(triangleCoords);
                texRect.setTex(texCoor);


                texRect.drawSelf(textureId1);

                GLES30.glEnable(GLES30.GL_BLEND);
                GLES30.glBlendFunc(GLES30.GL_SRC_ALPHA, GLES30.GL_ONE_MINUS_SRC_ALPHA);
                bg.drawSelf(textureId);



            }

        public void onSurfaceChanged(GL10 gl, int width, int height) {
            //设置视窗大小及位置 
        	GLES30.glViewport(0, 0, width, height); 
        	//计算GLSurfaceView的宽高比
            float ratio = (float) width / height;
            MatrixState.wh=ratio;
            //调用此方法计算产生透视投影矩阵
            MatrixState.setProjectFrustum(-ratio, ratio, -1, 1, 1, 10);
            //调用此方法产生摄像机9参数位置矩阵
            MatrixState.setCamera(0,0,3,0f,0f,0f,0f,1.0f,0.0f);
            GLES30.glEnable(GLES30.GL_CULL_FACE);

            textureId=initTexture(R.drawable.bg);
            textureId1=initTexture(R.drawable.touxiang);


            bg.drawSelf(textureId1);

            texRect.drawSelf(textureId);
            SQLiteDatabase db2 = dbHelper.getWritableDatabase(); // 查询 Book 表中所有的数据
//        Cursor cursor = db2.query("Book", new String[]{"coord,pressure,page"}, "page=?", new String[]{"1"}, null, null, null);
            Cursor cursor = db2.query("Book", null, null, null, null, null, null);

            while (cursor.moveToNext()) {
                String cooord = cursor.getString(cursor.getColumnIndex("coord"));
                String pressure = cursor.getString(cursor.getColumnIndex("pressure"));
                int page = cursor.getInt(cursor.getColumnIndex("page"));
                if(page==1){
                    Log.e("fff", cooord );
                    Log.e("fff", pressure);
                    Log.e("fff", String.valueOf(page));
                    getStroke(cooord,pressure);

                }
            }

        }

        public void onSurfaceCreated(GL10 gl, EGLConfig config) {
            //设置屏幕背景色RGBA
            GLES30.glClearColor(0f,1f,1f, 0f);
            //创建三角形对对象 
            texRect=new Triangle(MySurfaceView.this);
            bg=new BG(MySurfaceView.this);
            //打开深度检测
            GLES30.glEnable(GLES30.GL_DEPTH_TEST);
            //初始化纹理





            //关闭背面剪裁   
            GLES30.glEnable(GLES30.GL_CULL_FACE);


        }

    }
    public int initTexture(int drawableId)//textureId
    {
        //生成纹理ID
        int[] textures = new int[1];
        GLES30.glGenTextures
                (
                        1,          //产生的纹理id的数量
                        textures,   //纹理id的数组
                        0           //偏移量
                );
        int textureId=textures[0];
        GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, textureId);
        GLES30.glTexParameterf(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_MIN_FILTER,GLES30.GL_NEAREST);
        GLES30.glTexParameterf(GLES30.GL_TEXTURE_2D,GLES30.GL_TEXTURE_MAG_FILTER,GLES30.GL_LINEAR);
        GLES30.glTexParameterf(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_WRAP_S,GLES30.GL_CLAMP_TO_EDGE);
        GLES30.glTexParameterf(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_WRAP_T,GLES30.GL_CLAMP_TO_EDGE);

        //通过输入流加载图片===============begin===================
        InputStream is = this.getResources().openRawResource(drawableId);
        Bitmap bitmapTmp;
        try
        {
            bitmapTmp = BitmapFactory.decodeStream(is);
        }
        finally
        {
            try
            {
                is.close();
            }
            catch(IOException e)
            {
                e.printStackTrace();
            }
        }
        //通过输入流加载图片===============end=====================

        //实际加载纹理
        GLUtils.texImage2D
                (
                        GLES30.GL_TEXTURE_2D,   //纹理类型
                        0, 					  //纹理的层次，0表示基本图像层，可以理解为直接贴图
                        bitmapTmp, 			  //纹理图像
                        0					  //纹理边框尺寸
                );
        bitmapTmp.recycle(); 		  //纹理加载成功后释放图片

        return textureId;
    }
    public void setlist(ArrayList<Float> datas,ArrayList<Float> datas1){
        float one[]=new float[datas.size()];
        float two[]=new float[datas.size()];

        for(int i=0;i<datas.size();i++){
            one[i]=datas.get(i);
        }
        for(int i=0;i<datas1.size();i++){
            two[i]=datas1.get(i);
        }
        triangleCoords=one;
        texCoor=two;

//     Log.e("ss","山");//
    }
    public void getString (ArrayList<Float> datas,ArrayList<Float> datas1,int a,int b,int c,int d){
        String stroke=new String();
        String stroke1=new String();

        for(int i=0;i<datas.size();i++){
            String point=new DecimalFormat("0.0000").format(datas.get(i));
            stroke+=point.substring(2,6);
        }
        for(int i=0;i<datas1.size();i++){
            String point=new DecimalFormat("0.0000").format(datas1.get(i));
            stroke1+=point.substring(2,6);
        }
        SQLiteDatabase db1 = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues(); // 开始组装第一条数据
        values.put("coord",stroke);
        values.put("pressure",stroke1);
        values.put("page",1);
        values.put("xmin",a);
        values.put("xmax",b);
        values.put("ymin",c);
        values.put("ymax",d);

        db1.insert("Book", null, values); // 插入第一条数据
        values.clear();
//        Log.e("df",stroke);
    }
    public void  getStroke(String datas2,String datas1){
        int dot=datas2.length()/8;
        int flag1=0;


        for(int i=1;i<=dot;i++) {
            String a1=datas2.substring(8*i-8,8*i-4);
            int a2=Integer.parseInt(a1);
            float a3=0.0001f*a2;
            String b1=datas2.substring(8*i-4,8*i);
            int b2=Integer.parseInt(b1);
            float b3=0.0001f*b2;
            String c1=datas1.substring(4*i-4,4*i);
            int c2=Integer.parseInt(c1);
            float c3=0.0001f*c2;

            float x=(a3*4096-getWidth()/2f)/(getWidth()/2f);
            float y=-1*(b3*4096-getHeight()/2f)/(getWidth()/2f);
            float p=c3;
            float s=p/128;

            float dx = x - x1;//计算触控笔X位移
            float dy = y - y1;//计算触控笔Y位移
            float ll=(float) Math.sqrt(dx*dx+dy*dy);
            float rrss=s/ll;
            float dx2=
                    dy*rrss;
            float dy2=-1*
                    dx*rrss;

            if (flag1==1){
                datas.add(x - s);
                datas.add(y + s);
                datas.add(0f);
                oo.add(0f);
                oo.add(0f);
                datas.add(x - s);
                datas.add(y + s);
                datas.add(0f);
                oo.add(0f);
                oo.add(0f);

                datas.add(x - s);
                datas.add(y - s);
                datas.add(0f);
                oo.add(0f);
                oo.add(1f);

                datas.add(x + s);
                datas.add(y + s);
                datas.add(0f);
                oo.add(1f);
                oo.add(0f);
                //右下
                datas.add(x + s);
                datas.add(y - s);
                datas.add(0f);
                oo.add(1f);
                oo.add(1f);
                datas.add(x + s);
                datas.add(y - s);
                datas.add(0f);
                oo.add(1f);
                oo.add(1f);
                //画线
                //左上
                datas.add(x1 - dx2);
                datas.add(y1 - dy2);
                datas.add(0f);
                oo.add(0.5f);
                oo.add(0.5f);
                datas.add(x1 - dx2);
                datas.add(y1 - dy2);
                datas.add(0f);
                oo.add(0.5f);
                oo.add(0.5f);
                datas.add(x1 + dx2);
                datas.add(y1 + dy2);
                datas.add(0f);
                oo.add(0.5f);
                oo.add(0.6f);

                datas.add(x - dx2);
                datas.add(y - dy2);
                datas.add(0f);
                oo.add(0.6f);
                oo.add(0.5f);

                //右下
                datas.add(x + dx2);
                datas.add(y + dy2);
                datas.add(0f);
                oo.add(0.6f);
                oo.add(0.6f);
                datas.add(x + dx2);
                datas.add(y + dy2);
                datas.add(0f);
                oo.add(0.6f);
                oo.add(0.6f);
            }//加入顶点
            else {
                if(datas.size()!=0){
                    datas.add(x - s);
                    datas.add(y + s);
                    datas.add(0f);
                    oo.add(0f);
                    oo.add(0f);
                }
                datas.add(x - s);
                datas.add(y + s);
                datas.add(0f);
                oo.add(0f);
                oo.add(0f);

                datas.add(x - s);
                datas.add(y - s);
                datas.add(0f);
                oo.add(0f);
                oo.add(1f);

                datas.add(x + s);
                datas.add(y + s);
                datas.add(0f);
                oo.add(1f);
                oo.add(0f);
                //右下
                datas.add(x + s);
                datas.add(y - s);
                datas.add(0f);
                oo.add(1f);
                oo.add(1f);
                datas.add(x + s);
                datas.add(y - s);
                datas.add(0f);
                oo.add(1f);
                oo.add(1f);
                flag1=1;
            }
            setlist(datas,oo);
            requestRender();
            x1=x;
            y1=y;
        }
    }
}
