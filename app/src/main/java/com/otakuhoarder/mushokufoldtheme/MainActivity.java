package com.otakuhoarder.mushokufoldtheme;

import android.app.Activity;
import android.app.WallpaperManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Shader;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

public class MainActivity extends Activity {
    private static final int MANA_BLUE = Color.rgb(55, 146, 188);
    private static final int EMERALD = Color.rgb(40, 118, 87);
    private static final int TWILIGHT = Color.rgb(92, 72, 140);

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setStatusBarColor(Color.rgb(7, 21, 27));
        getWindow().setNavigationBarColor(Color.rgb(7, 21, 27));
        setContentView(buildUi());
    }

    private View buildUi() {
        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setGravity(Gravity.CENTER_HORIZONTAL);
        root.setPadding(dp(24), dp(32), dp(24), dp(24));
        root.setBackgroundColor(Color.rgb(7, 21, 27));

        TextView title = new TextView(this);
        title.setText("MUSHOKU FOLD THEME"); title.setTextColor(Color.rgb(218,244,238));
        title.setTextSize(26); title.setGravity(Gravity.CENTER); title.setTypeface(null, 1);
        root.addView(title, fullWidth(dp(54)));

        TextView subtitle = new TextView(this);
        subtitle.setText("Mana-inspired fantasy theme for Galaxy Z Fold7");
        subtitle.setTextColor(Color.rgb(149,193,187)); subtitle.setTextSize(15); subtitle.setGravity(Gravity.CENTER);
        subtitle.setPadding(0,0,0,dp(18)); root.addView(subtitle, fullWidth(ViewGroup.LayoutParams.WRAP_CONTENT));

        root.addView(new ManaPreview(), fullWidth(dp(260)));
        TextView hint = new TextView(this);
        hint.setText("Choose a spell palette. The wallpaper is generated on-device so it scales cleanly to the cover and inner displays.");
        hint.setTextColor(Color.rgb(196,218,214)); hint.setTextSize(14); hint.setGravity(Gravity.CENTER);
        hint.setPadding(dp(6),dp(18),dp(6),dp(12)); root.addView(hint, fullWidth(ViewGroup.LayoutParams.WRAP_CONTENT));
        root.addView(themeButton("Apply Mana Blue", MANA_BLUE), fullWidth(dp(52)));
        root.addView(themeButton("Apply Emerald Forest", EMERALD), fullWidth(dp(52)));
        root.addView(themeButton("Apply Twilight Magic", TWILIGHT), fullWidth(dp(52)));

        TextView footer = new TextView(this);
        footer.setText("v0.1 • OtakuHoarder Fold7 build"); footer.setTextColor(Color.rgb(101,139,135));
        footer.setTextSize(12); footer.setGravity(Gravity.CENTER); footer.setPadding(0,dp(18),0,0);
        root.addView(footer, fullWidth(ViewGroup.LayoutParams.WRAP_CONTENT)); return root;
    }

    private Button themeButton(String label, int accent) {
        Button button = new Button(this); button.setText(label); button.setTextColor(Color.WHITE);
        button.setTextSize(16); button.setAllCaps(false); button.setBackgroundColor(accent);
        LinearLayout.LayoutParams lp = fullWidth(dp(52)); lp.setMargins(0,dp(6),0,dp(6)); button.setLayoutParams(lp);
        button.setOnClickListener(v -> applyWallpaper(accent)); return button;
    }

    private void applyWallpaper(int accent) {
        try {
            WallpaperManager wm = WallpaperManager.getInstance(this);
            Bitmap bitmap = createWallpaper(accent, 1440, 1920); wm.setBitmap(bitmap);
            Toast.makeText(this, "Theme wallpaper applied", Toast.LENGTH_SHORT).show();
        } catch (IOException e) { Toast.makeText(this, "Could not apply wallpaper: " + e.getMessage(), Toast.LENGTH_LONG).show(); }
    }

    private Bitmap createWallpaper(int accent, int width, int height) {
        Bitmap bitmap = Bitmap.createBitmap(width,height,Bitmap.Config.ARGB_8888); Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG); int dark = Color.rgb(5,16,23);
        LinearGradient gradient = new LinearGradient(0,0,width,height,new int[]{dark,mix(dark,accent,0.45f),accent},new float[]{0f,0.62f,1f},Shader.TileMode.CLAMP);
        paint.setShader(gradient); canvas.drawRect(0,0,width,height,paint); paint.setShader(null);
        paint.setStyle(Paint.Style.STROKE); paint.setStrokeWidth(6f);
        for (int i=0;i<8;i++) { int alpha=80-i*7; paint.setColor(Color.argb(Math.max(alpha,18),200,255,240)); float r=150+i*72; canvas.drawCircle(width*.72f,height*.28f,r,paint); }
        paint.setStyle(Paint.Style.FILL); paint.setColor(Color.argb(65,230,255,250)); Path rune=new Path();
        rune.moveTo(width*.10f,height*.78f); rune.lineTo(width*.34f,height*.60f); rune.lineTo(width*.48f,height*.87f); rune.close(); canvas.drawPath(rune,paint);
        return bitmap;
    }

    private static int mix(int a,int b,float t) { return Color.rgb((int)(Color.red(a)*(1-t)+Color.red(b)*t),(int)(Color.green(a)*(1-t)+Color.green(b)*t),(int)(Color.blue(a)*(1-t)+Color.blue(b)*t)); }
    private LinearLayout.LayoutParams fullWidth(int height) { return new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,height); }
    private int dp(int value) { return Math.round(value*getResources().getDisplayMetrics().density); }

    private class ManaPreview extends View {
        private final Paint paint=new Paint(Paint.ANTI_ALIAS_FLAG); ManaPreview(){super(MainActivity.this);}
        @Override protected void onDraw(Canvas canvas) { super.onDraw(canvas); int w=getWidth(),h=getHeight();
            LinearGradient g=new LinearGradient(0,0,w,h,new int[]{Color.rgb(8,30,36),MANA_BLUE,EMERALD},null,Shader.TileMode.CLAMP);
            paint.setShader(g); canvas.drawRoundRect(0,0,w,h,dp(24),dp(24),paint); paint.setShader(null); paint.setStyle(Paint.Style.STROKE); paint.setStrokeWidth(dp(2)); paint.setColor(Color.argb(120,225,255,246));
            float cx=w*.72f,cy=h*.40f; for(int i=0;i<5;i++) canvas.drawCircle(cx,cy,dp(28+i*18),paint);
            paint.setStyle(Paint.Style.FILL); paint.setColor(Color.argb(170,245,255,250)); paint.setTextSize(dp(22)); canvas.drawText(w>dp(500)?"UNFOLDED • ARCANE DESK":"COVER • MANA MODE",dp(20),h-dp(24),paint);
        }
    }
}
