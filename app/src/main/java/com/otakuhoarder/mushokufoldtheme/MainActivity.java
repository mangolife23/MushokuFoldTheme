package com.otakuhoarder.mushokufoldtheme;

import android.app.Activity;
import android.app.WallpaperManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Shader;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import java.io.IOException;

public class MainActivity extends Activity {
 private static final int BG=Color.rgb(8,12,18), PANEL=Color.rgb(17,24,33), GOLD=Color.rgb(202,164,104), TEXT=Color.rgb(241,236,225), MUTED=Color.rgb(166,171,178);
 private final ThemeSpec[] themes={
  new ThemeSpec("Rudeus","Earth • Wind • Adventure",Color.rgb(133,101,72),R.drawable.rudeus_theme),
  new ThemeSpec("Roxy","Water Magic • Night Sky",Color.rgb(60,88,145),R.drawable.roxy_theme),
  new ThemeSpec("Sylphie","Healing • Forest Light",Color.rgb(74,135,101),R.drawable.sylphie_theme),
  new ThemeSpec("Eris","Sword • Ember • Resolve",Color.rgb(143,58,54),R.drawable.eris_theme)};
 private int selectedTheme=0,wallpaperTarget=WallpaperManager.FLAG_SYSTEM|WallpaperManager.FLAG_LOCK; private ThemePreview preview; private TextView selectedLabel; private final Button[] themeButtons=new Button[4];
 @Override protected void onCreate(Bundle b){super.onCreate(b);getWindow().setStatusBarColor(BG);getWindow().setNavigationBarColor(BG);setContentView(buildUi());}
 private View buildUi(){ScrollView scroll=new ScrollView(this);scroll.setFillViewport(true);scroll.setBackgroundColor(BG);LinearLayout root=new LinearLayout(this);root.setOrientation(LinearLayout.VERTICAL);root.setPadding(dp(18),dp(22),dp(18),dp(28));root.setBackgroundColor(BG);scroll.addView(root,new ScrollView.LayoutParams(-1,-2));TextView title=text("MUSHOKU TENSEI × GALAXY Z FOLD7",22,GOLD,Gravity.CENTER);title.setTypeface(null,1);root.addView(title,full(dp(52)));root.addView(text("v0.3.2 • DRAWABLE ART BUILD",13,Color.WHITE,Gravity.CENTER),full(dp(32)));LinearLayout row=new LinearLayout(this);for(int i=0;i<4;i++){final int x=i;Button bt=new Button(this);themeButtons[i]=bt;bt.setText(themes[i].name);bt.setAllCaps(false);bt.setOnClickListener(v->selectTheme(x));LinearLayout.LayoutParams lp=new LinearLayout.LayoutParams(0,dp(52),1);lp.setMargins(dp(2),0,dp(2),0);row.addView(bt,lp);}root.addView(row,full(dp(58)));selectedLabel=text("",16,TEXT,Gravity.CENTER);root.addView(selectedLabel,full(dp(48)));preview=new ThemePreview();root.addView(preview,full(dp(360)));LinearLayout targets=new LinearLayout(this);targets.addView(targetButton("Home",WallpaperManager.FLAG_SYSTEM),weighted());targets.addView(targetButton("Lock",WallpaperManager.FLAG_LOCK),weighted());targets.addView(targetButton("Both",WallpaperManager.FLAG_SYSTEM|WallpaperManager.FLAG_LOCK),weighted());LinearLayout.LayoutParams tp=full(dp(54));tp.setMargins(0,dp(16),0,0);root.addView(targets,tp);Button apply=new Button(this);apply.setText("Apply Selected Theme");apply.setAllCaps(false);apply.setTextSize(17);apply.setOnClickListener(v->applyWallpaper());LinearLayout.LayoutParams ap=full(dp(60));ap.setMargins(0,dp(12),0,0);root.addView(apply,ap);selectTheme(0);return scroll;}
 private Bitmap art(){return BitmapFactory.decodeResource(getResources(),themes[selectedTheme].drawable);}
 private void selectTheme(int i){selectedTheme=i;if(selectedLabel!=null)selectedLabel.setText(themes[i].name+" • "+themes[i].tagline);if(preview!=null)preview.invalidate();for(int n=0;n<4;n++){if(themeButtons[n]!=null){boolean s=n==i;themeButtons[n].setTextColor(s?Color.WHITE:MUTED);themeButtons[n].setBackground(rounded(s?themes[n].primary:PANEL,14,s?GOLD:Color.rgb(46,54,64),s?2:1));}}}
 private Button targetButton(String s,int t){Button b=new Button(this);b.setText(s);b.setAllCaps(false);b.setTextColor(TEXT);b.setOnClickListener(v->{wallpaperTarget=t;Toast.makeText(this,s+" selected",Toast.LENGTH_SHORT).show();});return b;}
 private void applyWallpaper(){try{Bitmap a=art();if(a==null){Toast.makeText(this,"Artwork failed to load",Toast.LENGTH_LONG).show();return;}Bitmap out=Bitmap.createBitmap(1800,2400,Bitmap.Config.ARGB_8888);Canvas c=new Canvas(out);Paint p=new Paint(Paint.ANTI_ALIAS_FLAG|Paint.FILTER_BITMAP_FLAG);drawCover(c,a,0,0,1800,2400,p);WallpaperManager.getInstance(this).setBitmap(out,null,true,wallpaperTarget);Toast.makeText(this,themes[selectedTheme].name+" applied",Toast.LENGTH_SHORT).show();}catch(IOException|RuntimeException e){Toast.makeText(this,"Wallpaper error: "+e.getMessage(),Toast.LENGTH_LONG).show();}}
 private void drawCover(Canvas c,Bitmap a,int l,int t,int r,int b,Paint p){if(a==null)return;float scale=Math.max((float)(r-l)/a.getWidth(),(float)(b-t)/a.getHeight());int sw=Math.max(1,Math.round((r-l)/scale)),sh=Math.max(1,Math.round((b-t)/scale));int sx=Math.max(0,(a.getWidth()-sw)/2),sy=Math.max(0,(a.getHeight()-sh)/2);c.drawBitmap(a,new Rect(sx,sy,Math.min(a.getWidth(),sx+sw),Math.min(a.getHeight(),sy+sh)),new Rect(l,t,r,b),p);}
 private TextView text(String v,int s,int c,int g){TextView t=new TextView(this);t.setText(v);t.setTextSize(s);t.setTextColor(c);t.setGravity(g);return t;} private GradientDrawable rounded(int f,int r,int s,int sw){GradientDrawable g=new GradientDrawable();g.setColor(f);g.setCornerRadius(dp(r));if(sw>0)g.setStroke(dp(sw),s);return g;} private LinearLayout.LayoutParams full(int h){return new LinearLayout.LayoutParams(-1,h);} private LinearLayout.LayoutParams weighted(){LinearLayout.LayoutParams p=new LinearLayout.LayoutParams(0,dp(50),1);p.setMargins(dp(2),0,dp(2),0);return p;} private int dp(int v){return Math.round(v*getResources().getDisplayMetrics().density);}
 private class ThemePreview extends View{private final Paint p=new Paint(Paint.ANTI_ALIAS_FLAG|Paint.FILTER_BITMAP_FLAG);ThemePreview(){super(MainActivity.this);setBackground(rounded(PANEL,22,Color.rgb(55,63,73),1));}@Override protected void onDraw(Canvas c){super.onDraw(c);Bitmap a=art();if(a==null){p.setColor(Color.RED);p.setTextSize(dp(18));c.drawText("ART LOAD ERROR",dp(20),dp(40),p);return;}drawCover(c,a,dp(6),dp(6),getWidth()-dp(6),getHeight()-dp(6),p);LinearGradient shade=new LinearGradient(0,getHeight()*.55f,0,getHeight(),new int[]{Color.TRANSPARENT,Color.argb(210,8,12,18)},null,Shader.TileMode.CLAMP);p.setShader(shade);c.drawRect(0,0,getWidth(),getHeight(),p);p.setShader(null);p.setColor(Color.WHITE);p.setTextSize(dp(25));p.setTypeface(android.graphics.Typeface.DEFAULT_BOLD);c.drawText(themes[selectedTheme].name,dp(22),getHeight()-dp(48),p);p.setTextSize(dp(13));c.drawText(themes[selectedTheme].tagline,dp(22),getHeight()-dp(22),p);}}
 private static class ThemeSpec{final String name,tagline;final int primary,drawable;ThemeSpec(String n,String t,int p,int d){name=n;tagline=t;primary=p;drawable=d;}}
}
