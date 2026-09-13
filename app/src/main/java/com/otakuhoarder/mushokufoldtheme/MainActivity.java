package com.otakuhoarder.mushokufoldtheme;

import android.app.Activity;
import android.app.WallpaperManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

public class MainActivity extends Activity {
    private static final int BG = Color.rgb(4, 12, 28);
    private static final int PANEL = Color.rgb(8, 25, 48);
    private static final int PANEL_2 = Color.rgb(10, 35, 68);
    private static final int BLUE = Color.rgb(42, 117, 255);
    private static final int CYAN = Color.rgb(77, 190, 255);
    private static final int TEXT = Color.rgb(245, 249, 255);
    private static final int MUTED = Color.rgb(166, 190, 218);
    private static final int FIT_FILL = 0, FIT_FIT = 1, FIT_CENTER = 2;

    private int wallpaperTarget = WallpaperManager.FLAG_SYSTEM | WallpaperManager.FLAG_LOCK;
    private int fitMode = FIT_FILL;
    private ImageView preview;
    private TextView imageStatus;
    private Bitmap roxyBitmap;
    private final Button[] targetButtons = new Button[3];
    private final Button[] fitButtons = new Button[3];

    @Override protected void onCreate(Bundle state) {
        super.onCreate(state);
        getWindow().setStatusBarColor(BG);
        getWindow().setNavigationBarColor(BG);
        roxyBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.roxy_wallpaper);
        setContentView(buildUi());
    }

    private View buildUi() {
        ScrollView scroll = new ScrollView(this); scroll.setFillViewport(true); scroll.setBackgroundColor(BG);
        LinearLayout root = new LinearLayout(this); root.setOrientation(LinearLayout.VERTICAL); root.setPadding(dp(16),dp(18),dp(16),dp(30)); root.setBackgroundColor(BG);
        scroll.addView(root,new ScrollView.LayoutParams(-1,-2));

        TextView brand=text("MUSHOKUFOLD THEME",12,CYAN,Gravity.CENTER); brand.setLetterSpacing(.18f); root.addView(brand,full(dp(28)));
        TextView title=text("Roxy Edition",30,TEXT,Gravity.CENTER); title.setTypeface(Typeface.create(Typeface.SERIF,Typeface.BOLD)); root.addView(title,full(dp(44)));
        root.addView(text("v0.4.2 • Roxy PNG Fix • Galaxy Z Fold7",12,MUTED,Gravity.CENTER),full(dp(30)));

        boolean wide=getResources().getConfiguration().screenWidthDp>=600;
        LinearLayout studio=new LinearLayout(this); studio.setOrientation(wide?LinearLayout.HORIZONTAL:LinearLayout.VERTICAL);
        LinearLayout.LayoutParams studioLp=full(-2); studioLp.setMargins(0,dp(12),0,0); root.addView(studio,studioLp);

        LinearLayout info=panel(); info.setPadding(dp(15),dp(15),dp(15),dp(15));
        LinearLayout.LayoutParams infoLp=wide?new LinearLayout.LayoutParams(0,-2,.34f):full(-2); if(wide)infoLp.setMargins(0,0,dp(12),0);else infoLp.setMargins(0,0,0,dp(12)); studio.addView(info,infoLp);
        TextView roxy=text("Roxy Migurdia",23,TEXT,Gravity.START); roxy.setTypeface(Typeface.DEFAULT_BOLD); info.addView(roxy,full(dp(34)));
        info.addView(text("THE SILENT WATER MAGICIAN",12,CYAN,Gravity.START),full(dp(28)));
        info.addView(text("Roxy-only test build. The artwork is now converted to an Android-safe PNG before the APK is packaged.",14,MUTED,Gravity.START),full(dp(72)));
        LinearLayout badge=new LinearLayout(this); badge.setGravity(Gravity.CENTER); badge.setBackground(rounded(PANEL_2,16,BLUE,1));
        TextView badgeText=text("ROXY WALLPAPER • PNG RESOURCE",13,TEXT,Gravity.CENTER); badgeText.setTypeface(Typeface.DEFAULT_BOLD); badge.addView(badgeText,full(dp(48))); info.addView(badge,full(dp(50)));
        LinearLayout iconRow=new LinearLayout(this); iconRow.setOrientation(LinearLayout.HORIZONTAL); iconRow.setPadding(0,dp(12),0,0);
        iconRow.addView(iconTile("✦"),weighted(dp(54))); iconRow.addView(iconTile("◉"),weighted(dp(54))); iconRow.addView(iconTile("◇"),weighted(dp(54))); iconRow.addView(iconTile("☾"),weighted(dp(54))); info.addView(iconRow,full(dp(66)));
        info.addView(text("Roxy icon pack preview",11,MUTED,Gravity.CENTER),full(dp(24)));

        LinearLayout previewPanel=panel(); previewPanel.setPadding(dp(10),dp(10),dp(10),dp(10)); studio.addView(previewPanel,wide?new LinearLayout.LayoutParams(0,-2,.66f):full(-2));
        TextView previewTitle=text(wide?"Unfolded Preview":"Cover Preview",16,TEXT,Gravity.START); previewTitle.setTypeface(Typeface.DEFAULT_BOLD); previewPanel.addView(previewTitle,full(dp(30)));
        preview=new ImageView(this); preview.setScaleType(ImageView.ScaleType.CENTER_CROP); preview.setBackground(rounded(Color.rgb(3,9,20),20,BLUE,2));
        if(roxyBitmap!=null) preview.setImageDrawable(new BitmapDrawable(getResources(),roxyBitmap));
        previewPanel.addView(preview,full(wide?dp(540):dp(470)));
        imageStatus=text("",12,roxyBitmap!=null?Color.rgb(94,230,164):Color.rgb(255,112,112),Gravity.CENTER); updateImageStatus(); previewPanel.addView(imageStatus,full(dp(28)));
        TextView caption=text("Moonlit Resolve • Roxy Wallpaper 01",13,CYAN,Gravity.CENTER); caption.setPadding(0,dp(5),0,0); previewPanel.addView(caption,full(dp(34)));

        LinearLayout controls=panel(); controls.setPadding(dp(14),dp(14),dp(14),dp(16)); LinearLayout.LayoutParams controlsLp=full(-2); controlsLp.setMargins(0,dp(14),0,0); root.addView(controls,controlsLp);
        controls.addView(section("Wallpaper Mode"),full(dp(32)));
        LinearLayout targetRow=new LinearLayout(this); targetRow.setOrientation(LinearLayout.HORIZONTAL);
        targetButtons[0]=choice("Home",()->setTarget(0,WallpaperManager.FLAG_SYSTEM)); targetButtons[1]=choice("Lock",()->setTarget(1,WallpaperManager.FLAG_LOCK)); targetButtons[2]=choice("Both",()->setTarget(2,WallpaperManager.FLAG_SYSTEM|WallpaperManager.FLAG_LOCK));
        targetRow.addView(targetButtons[0],weighted(dp(50))); targetRow.addView(targetButtons[1],weighted(dp(50))); targetRow.addView(targetButtons[2],weighted(dp(50))); controls.addView(targetRow,full(dp(54)));
        controls.addView(section("Fit & Position"),full(dp(38)));
        LinearLayout fitRow=new LinearLayout(this); fitRow.setOrientation(LinearLayout.HORIZONTAL);
        fitButtons[0]=choice("Fill",()->setFit(0,FIT_FILL)); fitButtons[1]=choice("Fit",()->setFit(1,FIT_FIT)); fitButtons[2]=choice("Center",()->setFit(2,FIT_CENTER));
        fitRow.addView(fitButtons[0],weighted(dp(50))); fitRow.addView(fitButtons[1],weighted(dp(50))); fitRow.addView(fitButtons[2],weighted(dp(50))); controls.addView(fitRow,full(dp(54)));
        Button apply=new Button(this); apply.setText("Apply Roxy Wallpaper"); apply.setAllCaps(false); apply.setTextSize(17); apply.setTypeface(Typeface.DEFAULT_BOLD); apply.setTextColor(Color.WHITE); apply.setBackground(rounded(BLUE,18,CYAN,2)); apply.setOnClickListener(v->applyWallpaper());
        LinearLayout.LayoutParams applyLp=full(dp(62)); applyLp.setMargins(0,dp(14),0,0); controls.addView(apply,applyLp);
        root.addView(text("v0.4.2 is specifically the image-rendering repair build. If the green RESOURCE OK line appears, the packaged artwork decoded correctly on your Fold7.",12,MUTED,Gravity.CENTER),full(dp(64)));
        refreshButtons(); return scroll;
    }

    private void updateImageStatus(){if(imageStatus==null)return;if(roxyBitmap!=null)imageStatus.setText("RESOURCE OK • "+roxyBitmap.getWidth()+" × "+roxyBitmap.getHeight());else imageStatus.setText("RESOURCE ERROR • ROXY PNG DID NOT DECODE");}
    private TextView iconTile(String g){TextView t=text(g,24,TEXT,Gravity.CENTER);t.setBackground(rounded(Color.rgb(8,48,105),14,CYAN,1));return t;}
    private TextView section(String s){TextView t=text(s,15,TEXT,Gravity.START|Gravity.CENTER_VERTICAL);t.setTypeface(Typeface.DEFAULT_BOLD);return t;}
    private Button choice(String s,Runnable a){Button b=new Button(this);b.setText(s);b.setAllCaps(false);b.setTextSize(14);b.setTextColor(TEXT);b.setOnClickListener(v->a.run());return b;}
    private void setTarget(int i,int t){wallpaperTarget=t;refreshButtons();Toast.makeText(this,targetButtons[i].getText()+" screen selected",Toast.LENGTH_SHORT).show();}
    private void setFit(int i,int m){fitMode=m;if(preview!=null)preview.setScaleType(m==FIT_FILL?ImageView.ScaleType.CENTER_CROP:m==FIT_FIT?ImageView.ScaleType.FIT_CENTER:ImageView.ScaleType.CENTER_INSIDE);refreshButtons();}
    private void refreshButtons(){for(int i=0;i<3;i++){if(targetButtons[i]==null)continue;boolean s=(i==0&&wallpaperTarget==WallpaperManager.FLAG_SYSTEM)||(i==1&&wallpaperTarget==WallpaperManager.FLAG_LOCK)||(i==2&&wallpaperTarget==(WallpaperManager.FLAG_SYSTEM|WallpaperManager.FLAG_LOCK));targetButtons[i].setBackground(rounded(s?BLUE:PANEL_2,14,s?CYAN:Color.rgb(38,66,98),s?2:1));}for(int i=0;i<3;i++){if(fitButtons[i]==null)continue;boolean s=i==fitMode;fitButtons[i].setBackground(rounded(s?BLUE:PANEL_2,14,s?CYAN:Color.rgb(38,66,98),s?2:1));}}

    private void applyWallpaper(){if(roxyBitmap==null){Toast.makeText(this,"Roxy PNG did not decode — wallpaper not applied.",Toast.LENGTH_LONG).show();return;}try{int w=1440,h=2560;Bitmap out=Bitmap.createBitmap(w,h,Bitmap.Config.ARGB_8888);Canvas c=new Canvas(out);c.drawColor(BG);Paint p=new Paint(Paint.ANTI_ALIAS_FLAG|Paint.FILTER_BITMAP_FLAG);drawBitmapMode(c,roxyBitmap,w,h,p);WallpaperManager.getInstance(this).setBitmap(out,null,true,wallpaperTarget);Toast.makeText(this,"Roxy wallpaper applied",Toast.LENGTH_SHORT).show();}catch(IOException|RuntimeException e){Toast.makeText(this,"Wallpaper error: "+e.getMessage(),Toast.LENGTH_LONG).show();}}
    private void drawBitmapMode(Canvas c,Bitmap b,int w,int h,Paint p){int sw=b.getWidth(),sh=b.getHeight();if(sw<=0||sh<=0)return;if(fitMode==FIT_FILL){float scale=Math.max((float)w/sw,(float)h/sh);int cw=Math.max(1,Math.round(w/scale)),ch=Math.max(1,Math.round(h/scale));int sx=Math.max(0,(sw-cw)/2),sy=Math.max(0,(sh-ch)/2);c.drawBitmap(b,new Rect(sx,sy,Math.min(sw,sx+cw),Math.min(sh,sy+ch)),new Rect(0,0,w,h),p);return;}float scale=Math.min((float)w/sw,(float)h/sh);if(fitMode==FIT_CENTER)scale=Math.min(1f,scale);int dw=Math.max(1,Math.round(sw*scale)),dh=Math.max(1,Math.round(sh*scale));int l=(w-dw)/2,t=(h-dh)/2;c.drawBitmap(b,null,new Rect(l,t,l+dw,t+dh),p);}
    private LinearLayout panel(){LinearLayout p=new LinearLayout(this);p.setOrientation(LinearLayout.VERTICAL);p.setBackground(rounded(PANEL,22,Color.rgb(29,70,116),1));return p;}
    private TextView text(String s,int z,int c,int g){TextView t=new TextView(this);t.setText(s);t.setTextSize(z);t.setTextColor(c);t.setGravity(g);return t;}
    private GradientDrawable rounded(int f,int r,int s,int w){GradientDrawable g=new GradientDrawable();g.setColor(f);g.setCornerRadius(dp(r));if(w>0)g.setStroke(dp(w),s);return g;}
    private LinearLayout.LayoutParams full(int h){return new LinearLayout.LayoutParams(-1,h);}private LinearLayout.LayoutParams weighted(int h){LinearLayout.LayoutParams p=new LinearLayout.LayoutParams(0,h,1f);p.setMargins(dp(3),0,dp(3),0);return p;}private int dp(int v){return Math.round(v*getResources().getDisplayMetrics().density);}
}
