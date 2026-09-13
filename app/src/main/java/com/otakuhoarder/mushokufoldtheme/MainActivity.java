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

    private static final int FIT_FILL = 0;
    private static final int FIT_FIT = 1;
    private static final int FIT_CENTER = 2;

    private int wallpaperTarget = WallpaperManager.FLAG_SYSTEM | WallpaperManager.FLAG_LOCK;
    private int fitMode = FIT_FILL;
    private ImageView preview;
    private final Button[] targetButtons = new Button[3];
    private final Button[] fitButtons = new Button[3];

    @Override
    protected void onCreate(Bundle state) {
        super.onCreate(state);
        getWindow().setStatusBarColor(BG);
        getWindow().setNavigationBarColor(BG);
        setContentView(buildUi());
    }

    private View buildUi() {
        ScrollView scroll = new ScrollView(this);
        scroll.setFillViewport(true);
        scroll.setBackgroundColor(BG);

        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setPadding(dp(16), dp(18), dp(16), dp(30));
        root.setBackgroundColor(BG);
        scroll.addView(root, new ScrollView.LayoutParams(-1, -2));

        TextView brand = text("MUSHOKUFOLD THEME", 12, CYAN, Gravity.CENTER);
        brand.setLetterSpacing(.18f);
        root.addView(brand, full(dp(28)));

        TextView title = text("Roxy Edition", 30, TEXT, Gravity.CENTER);
        title.setTypeface(Typeface.create(Typeface.SERIF, Typeface.BOLD));
        root.addView(title, full(dp(44)));

        root.addView(text("v0.4.1 • Galaxy Z Fold7 • Water Magic Theme", 12, MUTED, Gravity.CENTER), full(dp(30)));

        boolean wide = getResources().getConfiguration().screenWidthDp >= 600;
        LinearLayout studio = new LinearLayout(this);
        studio.setOrientation(wide ? LinearLayout.HORIZONTAL : LinearLayout.VERTICAL);
        LinearLayout.LayoutParams studioLp = full(-2);
        studioLp.setMargins(0, dp(12), 0, 0);
        root.addView(studio, studioLp);

        LinearLayout info = panel();
        info.setPadding(dp(15), dp(15), dp(15), dp(15));
        LinearLayout.LayoutParams infoLp = wide ? new LinearLayout.LayoutParams(0, -2, .34f) : full(-2);
        if (wide) infoLp.setMargins(0, 0, dp(12), 0);
        else infoLp.setMargins(0, 0, 0, dp(12));
        studio.addView(info, infoLp);

        TextView roxy = text("Roxy Migurdia", 23, TEXT, Gravity.START);
        roxy.setTypeface(Typeface.DEFAULT_BOLD);
        info.addView(roxy, full(dp(34)));
        info.addView(text("THE SILENT WATER MAGICIAN", 12, CYAN, Gravity.START), full(dp(28)));
        info.addView(text("Blue moonlight, water magic and a Fold7-focused wallpaper preview. This build is intentionally Roxy-only so we can get the visual quality right before adding other characters.", 14, MUTED, Gravity.START), full(dp(100)));

        LinearLayout badge = new LinearLayout(this);
        badge.setGravity(Gravity.CENTER);
        badge.setBackground(rounded(PANEL_2, 16, BLUE, 1));
        TextView badgeText = text("HIGH-RES ROXY WALLPAPER", 13, TEXT, Gravity.CENTER);
        badgeText.setTypeface(Typeface.DEFAULT_BOLD);
        badge.addView(badgeText, full(dp(48)));
        info.addView(badge, full(dp(50)));

        LinearLayout iconRow = new LinearLayout(this);
        iconRow.setOrientation(LinearLayout.HORIZONTAL);
        iconRow.setPadding(0, dp(12), 0, 0);
        iconRow.addView(iconTile("✦"), weighted(dp(54)));
        iconRow.addView(iconTile("◉"), weighted(dp(54)));
        iconRow.addView(iconTile("◇"), weighted(dp(54)));
        iconRow.addView(iconTile("☾"), weighted(dp(54)));
        info.addView(iconRow, full(dp(66)));
        info.addView(text("Roxy icon pack preview", 11, MUTED, Gravity.CENTER), full(dp(24)));

        LinearLayout previewPanel = panel();
        previewPanel.setPadding(dp(10), dp(10), dp(10), dp(10));
        studio.addView(previewPanel, wide ? new LinearLayout.LayoutParams(0, -2, .66f) : full(-2));

        TextView previewTitle = text(wide ? "Unfolded Preview" : "Cover Preview", 16, TEXT, Gravity.START);
        previewTitle.setTypeface(Typeface.DEFAULT_BOLD);
        previewPanel.addView(previewTitle, full(dp(30)));

        preview = new ImageView(this);
        preview.setImageResource(R.drawable.roxy_theme);
        preview.setScaleType(ImageView.ScaleType.CENTER_CROP);
        preview.setBackground(rounded(Color.BLACK, 20, BLUE, 2));
        previewPanel.addView(preview, full(wide ? dp(540) : dp(470)));

        TextView caption = text("Moonlit Resolve • Roxy Wallpaper 01", 13, CYAN, Gravity.CENTER);
        caption.setPadding(0, dp(8), 0, 0);
        previewPanel.addView(caption, full(dp(36)));

        LinearLayout controls = panel();
        controls.setPadding(dp(14), dp(14), dp(14), dp(16));
        LinearLayout.LayoutParams controlsLp = full(-2);
        controlsLp.setMargins(0, dp(14), 0, 0);
        root.addView(controls, controlsLp);

        controls.addView(section("Wallpaper Mode"), full(dp(32)));
        LinearLayout targetRow = new LinearLayout(this);
        targetRow.setOrientation(LinearLayout.HORIZONTAL);
        targetButtons[0] = choice("Home", () -> setTarget(0, WallpaperManager.FLAG_SYSTEM));
        targetButtons[1] = choice("Lock", () -> setTarget(1, WallpaperManager.FLAG_LOCK));
        targetButtons[2] = choice("Both", () -> setTarget(2, WallpaperManager.FLAG_SYSTEM | WallpaperManager.FLAG_LOCK));
        targetRow.addView(targetButtons[0], weighted(dp(50)));
        targetRow.addView(targetButtons[1], weighted(dp(50)));
        targetRow.addView(targetButtons[2], weighted(dp(50)));
        controls.addView(targetRow, full(dp(54)));

        controls.addView(section("Fit & Position"), full(dp(38)));
        LinearLayout fitRow = new LinearLayout(this);
        fitRow.setOrientation(LinearLayout.HORIZONTAL);
        fitButtons[0] = choice("Fill", () -> setFit(0, FIT_FILL));
        fitButtons[1] = choice("Fit", () -> setFit(1, FIT_FIT));
        fitButtons[2] = choice("Center", () -> setFit(2, FIT_CENTER));
        fitRow.addView(fitButtons[0], weighted(dp(50)));
        fitRow.addView(fitButtons[1], weighted(dp(50)));
        fitRow.addView(fitButtons[2], weighted(dp(50)));
        controls.addView(fitRow, full(dp(54)));

        Button apply = new Button(this);
        apply.setText("Apply Roxy Wallpaper");
        apply.setAllCaps(false);
        apply.setTextSize(17);
        apply.setTypeface(Typeface.DEFAULT_BOLD);
        apply.setTextColor(Color.WHITE);
        apply.setBackground(rounded(BLUE, 18, CYAN, 2));
        apply.setOnClickListener(v -> applyWallpaper());
        LinearLayout.LayoutParams applyLp = full(dp(62));
        applyLp.setMargins(0, dp(14), 0, 0);
        controls.addView(apply, applyLp);

        TextView footer = text("Roxy Edition first. Once this visual quality and layout are approved, the same system can expand to the other characters.", 12, MUTED, Gravity.CENTER);
        footer.setPadding(dp(8), dp(16), dp(8), 0);
        root.addView(footer, full(dp(62)));

        refreshButtons();
        return scroll;
    }

    private TextView iconTile(String glyph) {
        TextView tile = text(glyph, 24, TEXT, Gravity.CENTER);
        tile.setBackground(rounded(Color.rgb(8, 48, 105), 14, CYAN, 1));
        return tile;
    }

    private TextView section(String value) {
        TextView t = text(value, 15, TEXT, Gravity.START | Gravity.CENTER_VERTICAL);
        t.setTypeface(Typeface.DEFAULT_BOLD);
        return t;
    }

    private Button choice(String value, Runnable action) {
        Button b = new Button(this);
        b.setText(value);
        b.setAllCaps(false);
        b.setTextSize(14);
        b.setTextColor(TEXT);
        b.setOnClickListener(v -> action.run());
        return b;
    }

    private void setTarget(int index, int target) {
        wallpaperTarget = target;
        refreshButtons();
        Toast.makeText(this, targetButtons[index].getText() + " screen selected", Toast.LENGTH_SHORT).show();
    }

    private void setFit(int index, int mode) {
        fitMode = mode;
        if (preview != null) {
            preview.setScaleType(mode == FIT_FILL ? ImageView.ScaleType.CENTER_CROP : mode == FIT_FIT ? ImageView.ScaleType.FIT_CENTER : ImageView.ScaleType.CENTER_INSIDE);
        }
        refreshButtons();
    }

    private void refreshButtons() {
        for (int i = 0; i < targetButtons.length; i++) {
            if (targetButtons[i] == null) continue;
            boolean selected = (i == 0 && wallpaperTarget == WallpaperManager.FLAG_SYSTEM)
                    || (i == 1 && wallpaperTarget == WallpaperManager.FLAG_LOCK)
                    || (i == 2 && wallpaperTarget == (WallpaperManager.FLAG_SYSTEM | WallpaperManager.FLAG_LOCK));
            targetButtons[i].setBackground(rounded(selected ? BLUE : PANEL_2, 14, selected ? CYAN : Color.rgb(38, 66, 98), selected ? 2 : 1));
        }
        for (int i = 0; i < fitButtons.length; i++) {
            if (fitButtons[i] == null) continue;
            boolean selected = i == fitMode;
            fitButtons[i].setBackground(rounded(selected ? BLUE : PANEL_2, 14, selected ? CYAN : Color.rgb(38, 66, 98), selected ? 2 : 1));
        }
    }

    private void applyWallpaper() {
        Bitmap source = BitmapFactory.decodeResource(getResources(), R.drawable.roxy_theme);
        if (source == null) {
            Toast.makeText(this, "Roxy artwork could not be loaded.", Toast.LENGTH_LONG).show();
            return;
        }
        try {
            int outW = 1440;
            int outH = 2560;
            Bitmap out = Bitmap.createBitmap(outW, outH, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(out);
            canvas.drawColor(BG);
            Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG);
            drawBitmapMode(canvas, source, outW, outH, paint);
            WallpaperManager.getInstance(this).setBitmap(out, null, true, wallpaperTarget);
            Toast.makeText(this, "Roxy wallpaper applied", Toast.LENGTH_SHORT).show();
        } catch (IOException | RuntimeException e) {
            Toast.makeText(this, "Wallpaper error: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void drawBitmapMode(Canvas canvas, Bitmap bitmap, int outW, int outH, Paint paint) {
        int sw = bitmap.getWidth();
        int sh = bitmap.getHeight();
        if (sw <= 0 || sh <= 0) return;

        if (fitMode == FIT_FILL) {
            float scale = Math.max((float) outW / sw, (float) outH / sh);
            int cropW = Math.max(1, Math.round(outW / scale));
            int cropH = Math.max(1, Math.round(outH / scale));
            int sx = Math.max(0, (sw - cropW) / 2);
            int sy = Math.max(0, (sh - cropH) / 2);
            canvas.drawBitmap(bitmap, new Rect(sx, sy, Math.min(sw, sx + cropW), Math.min(sh, sy + cropH)), new Rect(0, 0, outW, outH), paint);
            return;
        }

        float scale = Math.min((float) outW / sw, (float) outH / sh);
        if (fitMode == FIT_CENTER) scale = Math.min(1f, scale);
        int dw = Math.max(1, Math.round(sw * scale));
        int dh = Math.max(1, Math.round(sh * scale));
        int left = (outW - dw) / 2;
        int top = (outH - dh) / 2;
        canvas.drawBitmap(bitmap, null, new Rect(left, top, left + dw, top + dh), paint);
    }

    private LinearLayout panel() {
        LinearLayout p = new LinearLayout(this);
        p.setOrientation(LinearLayout.VERTICAL);
        p.setBackground(rounded(PANEL, 22, Color.rgb(29, 70, 116), 1));
        return p;
    }

    private TextView text(String value, int size, int color, int gravity) {
        TextView t = new TextView(this);
        t.setText(value);
        t.setTextSize(size);
        t.setTextColor(color);
        t.setGravity(gravity);
        return t;
    }

    private GradientDrawable rounded(int fill, int radius, int stroke, int strokeWidth) {
        GradientDrawable g = new GradientDrawable();
        g.setColor(fill);
        g.setCornerRadius(dp(radius));
        if (strokeWidth > 0) g.setStroke(dp(strokeWidth), stroke);
        return g;
    }

    private LinearLayout.LayoutParams full(int height) {
        return new LinearLayout.LayoutParams(-1, height);
    }

    private LinearLayout.LayoutParams weighted(int height) {
        LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(0, height, 1f);
        p.setMargins(dp(3), 0, dp(3), 0);
        return p;
    }

    private int dp(int value) {
        return Math.round(value * getResources().getDisplayMetrics().density);
    }
}
