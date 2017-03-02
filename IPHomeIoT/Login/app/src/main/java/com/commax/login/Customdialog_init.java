package com.commax.login;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import com.commax.login.Common.AboutFile;
import com.commax.login.Common.TypeDef;

/**
 * Created by OWNER on 2016-08-02.
 */
public class Customdialog_init extends Dialog implements View.OnClickListener {
    AboutFile aboutFile = new AboutFile();
    String TAG = Customdialog_init.class.getSimpleName();

    Button mLeftButton;
    Button mRightButton;
    String[] mparams;


    private static final int LENGTH = 20;

    // 클릭버튼이 하나일때 생성자 함수로 클릭이벤트를 받는다.
    public Customdialog_init(Context context, String[] params) {
        super(context);
        //params[0] : type , [1] : ip:port , [2] : ADD , UPDATE , DELETE , [3] : site code , [4] :dong , [5] : ho ,[6] : resourceNO , [7] : domainName
        mparams = params;
        Log.d(TAG, "mparams : " + mparams);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 다이얼로그 외부 화면 흐리게 표현
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.custom_dialog_initialize);

        if(TypeDef.IP_Home_IoT_navigation)
        {
            hideNavigationBar();
        }
        else if(TypeDef.Lotte_navigation)
        {
            SubActivity.getInstance().sendCustomBroadcastMessage(TypeDef.SYSTEM_KEY_HIDE_ACTION);
        }

        mLeftButton = (Button) findViewById(R.id.btn_cancel);
        mRightButton = (Button) findViewById(R.id.btn_ok);
        mLeftButton.setOnClickListener(this);
        mRightButton.setOnClickListener(this);
    }

    //외곽 터치시 다이얼로그 닫히는 거 막기 위해서 onTuouchEvent 를 false 로 리턴한다.
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return false;
    }

    @Override
    public void onClick(View v) {
        if (v == mLeftButton) {
            Log.d(TAG, "cancle");
            cancel();
        } else {
            Log.d(TAG, "사용자 초기화");

            //UC 사용자 초기화 -> UC 그룹 초기화 -> 유라클 초기화
            if((!(TextUtils.isEmpty(aboutFile.readFile("ews")))) && !(TextUtils.isEmpty(aboutFile.readFile("UC_User_Register"))))
            {
                try {
                    Log.e(TAG, "ip:port : " + mparams[1] + " , SiteCode : " + mparams[3] + ", dong/ho : " + mparams[4] + "/" + mparams[5] +
                            " , resourceNo : " + mparams[6] + ", IP:PORT : " + mparams[1] + " , LocalServerIP : " + mparams[8] + ", User Id : " + mparams[9] + ", macaddress : " + mparams[10]);

                    //TODO 통합인증 사용자 삭제가 진행완료되고 로컬 서버 API호출 해야 하나?
                    if(TypeDef.Connect_Local_Server)
                    {
                        //for IP Home IoT
                        if(mparams[8].equals("127.0.0.1"))
                        {
                            Log.d(TAG, "Local server is not exist");
                        }
                        else
                        {
                            //params[0] : type , [1] : local Server , [2] : user id  [3] : Dong , [4] : Ho , [5] : Macaddress ,[6] : resourceNO
                            String[] resourceNo_initial = {"resourceNo_initial", mparams[8] , mparams[9] , mparams[4], mparams[5] , mparams[10] , mparams[9] };
                            SubActivity.getInstance().startTask(resourceNo_initial);
                        }
                    }

                    //params[0] : type , [1] : ip:port , [2] : ADD , UPDATE , DELETE , [3] : site code , [4] :dong , [5] : ho ,[6] : resourceNO , [7] : domainName
                    String[] uc_user_register = {"uc_user", mparams[1], "DELETE", mparams[3], mparams[4], mparams[5], mparams[6], mparams[7]};
                    SubActivity.getInstance().startTask(uc_user_register);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            else
            {
                //유라클 초기화

                if(TypeDef.Connect_Local_Server)
                {
                    //로컬 서버 계정 삭제 알림
                    if(mparams[8].equals("127.0.0.1"))
                    {
                        Log.d(TAG, "Local server is not exist");
                    }
                    else
                    {
                        //params[0] : type , [1] : local Server , [2] : user id  [3] : Dong , [4] : Ho , [5] : Macaddress ,[6] : resourceNO
                        String[] resourceNo_initial = {"resourceNo_initial", mparams[8] , mparams[9] , mparams[4], mparams[5] , mparams[10] , mparams[9] };
                        SubActivity.getInstance().startTask(resourceNo_initial);
                    }
                }

                String initialize[] = {"v1", "user/me" ,"14" , aboutFile.readFile("token")};
                SubActivity.getInstance().startTask(initialize);
            }
            dismiss();
        }
    }

    private void hideNavigationBar(){

        try {
            // 액티비티 아래의 네비게이션 바가 안보이게
            final int flags = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;

            // This work only for android 4.4+
            getWindow().getDecorView().setSystemUiVisibility(flags);

            // Code below is to handle presses of Volume up or Volume down.
            // Without this, after pressing volume buttons, the navigation bar will
            // show up and won't hide
            final View decorView = getWindow().getDecorView();
            decorView.setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {

                @Override
                public void onSystemUiVisibilityChange(int visibility) {
                    if ((visibility & View.SYSTEM_UI_FLAG_FULLSCREEN) == 0) {
                        decorView.setSystemUiVisibility(flags);
                    }
                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
