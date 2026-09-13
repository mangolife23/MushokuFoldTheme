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
    private static final int BG = Color.rgb(7, 10, 15);
    private static final int PANEL = Color.rgb(17, 22, 30);
    private static final int PANEL_2 = Color.rgb(24, 31, 41);
    private static final int GOLD = Color.rgb(210, 172, 103);
    private static final int TEXT = Color.rgb(246, 241, 231);
    private static final int MUTED = Color.rgb(160, 169, 181);

    private final ThemeSpec[] themes = {
            new ThemeSpec("Rudeus", "Earth • Wind • Adventure", "Verdant Journey", Color.rgb(126, 96, 64), Color.rgb(66, 133, 97), R.drawable.rudeus_theme),
            new ThemeSpec("Roxy", "Water Magic • Night Sky", "Azure Spellcraft", Color.rgb(50, 78, 142), Color.rgb(100, 74, 155), R.drawable.roxy_theme),
            new ThemeSpec("Sylphie", "Healing • Forest Light", "Whispering Grove", Color.rgb(67, 130, 93), Color.rgb(170, 207, 169), R.drawable.sylphie_theme),
            new ThemeSpec("Eris", "Sword • Ember • Resolve", "Crimson Blade", Color.rgb(146, 54, 48), Color.rgb(224, 116, 50), R.drawable.eris_theme)
    };

    private int selectedTheme = 0;
    private int wallpaperTarget = WallpaperManager.FLAG_SYSTEM | WallpaperManager.FLAG_LOCK;
    private ThemePreview preview;
    private TextView selectedName;
    private TextView selectedTagline;
    private LinearLayout cardsHost;

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
        root.setPadding(dp(18), dp(20), dp(18), dp(30));
        root.setBackgroundColor(BG);
        scroll.addView(root, new ScrollView.LayoutParams(-1, -2));

        TextView eyebrow = label("MUSHOKU TENSEI • FOLD THEME STUDIO", 12, GOLD, Gravity.CENTER);
        eyebrow.setLetterSpacing(.14f);
        root.addView(eyebrow, match(dp(28)));

        TextView title = label("A New World on Your Fold", 29, TEXT, Gravity.CENTER);
        title.setTypeface(Typeface.DEFAULT_BOLD);
        root.addView(title, match(dp(45)));

        TextView version = label("v0.4.0 • THEME STUDIO • ADAPTIVE FOLD UI", 12, MUTED, Gravity.CENTER);
        root.addView(version, match(dp(28)));

        boolean wide = getResources().getConfiguration().screenWidthDp >= 600;
        LinearLayout studio = new LinearLayout(this);
        studio.setOrientation(wide ? LinearLayout.HORIZONTAL : LinearLayout.VERTICAL);
        studio.setPadding(0, dp(14), 0, 0);
        root.addView(studio, match(-2));

        LinearLayout selectorPanel = panel();
        selectorPanel.setPadding(dp(14), dp(14), dp(14), dp(14));
        LinearLayout.LayoutParams selectorLp = wide ? new LinearLayout.LayoutParams(0, -2, .38f) : match(-2);
        if (wide) selectorLp.setMargins(0, 0, dp(12), 0);
        else selectorLp.setMargins(0, 0, 0, dp(12));
        studio.addView(selectorPanel, selectorLp);

        selectorPanel.addView(sectionTitle("Choose your character"), match(dp(32)));
        selectorPanel.addView(label("Each theme changes artwork, accent colors and wallpaper styling.", 13, MUTED, Gravity.START), match(dp(42)));
        cardsHost = new LinearLayout(this);
        cardsHost.setOrientation(wide ? LinearLayout.VERTICAL : LinearLayout.HORIZONTAL);
        selectorPanel.addView(cardsHost, match(-2));

        LinearLayout previewPanel = panel();
        previewPanel.setPadding(dp(12), dp(12), dp(12), dp(12));
        studio.addView(previewPanel, wide ? new LinearLayout.LayoutParams(0, -2, .62f) : match(-2));

        selectedName = label("", 25, TEXT, Gravity.START);
        selectedName.setTypeface(Typeface.DEFAULT_BOLD);
        previewPanel.addView(selectedName, match(dp(35)));
        selectedTagline = label("", 14, MUTED, Gravity.START);
        previewPanel.addView(selectedTagline, match(dp(28)));

        preview = new ThemePreview(this);
        previewPanel.addView(preview, match(wide ? dp(510) : dp(440)));

        LinearLayout chips = new LinearLayout(this);
        chips.setOrientation(LinearLayout.HORIZONTAL);
        chips.setGravity(Gravity.CENTER);
        chips.setPadding(0, dp(10), 0, 0);
        chips.addView(chip("Wallpaper"), weighted(dp(42)));
        chips.addView(chip("Accent"), weighted(dp(42)));
        chips.addView(chip("Fold UI"), weighted(dp(42)));
        previewPanel.addView(chips, match(dp(52)));

        LinearLayout applyPanel = panel();
        applyPanel.setPadding(dp(16), dp(15), dp(16), dp(16));
        LinearLayout.LayoutParams applyPanelLp = match(-2);
        applyPanelLp.setMargins(0, dp(14), 0, 0);
        root.addView(applyPanel, applyPanelLp);

        applyPanel.addView(sectionTitle("Apply wallpaper"), match(dp(34)));
        LinearLayout targetRow = new LinearLayout(this);
        targetRow.setOrientation(LinearLayout.HORIZONTAL);
        targetRow.addView(targetButton("Home", WallpaperManager.FLAG_SYSTEM), weighted(dp(50)));
        targetRow.addView(targetButton("Lock", WallpaperManager.FLAG_LOCK), weighted(dp(50)));
        targetRow.addView(targetButton("Both", WallpaperManager.FLAG_SYSTEM | WallpaperManager.FLAG_LOCK), weighted(dp(50)));
        applyPanel.addView(targetRow, match(dp(54)));

        Button apply = new Button(this);
        apply.setText("Apply selected character theme");
        apply.setAllCaps(false);
        apply.setTextSize(16);
        apply.setTypeface(Typeface.DEFAULT_BOLD);
        apply.setTextColor(Color.rgb(30, 22, 12));
        apply.setBackground(rounded(GOLD, 16, GOLD, 0));
        apply.setOnClickListener(v -> applyWallpaper());
        LinearLayout.LayoutParams applyLp = match(dp(58));
        applyLp.setMargins(0, dp(12), 0, 0);
        applyPanel.addView(apply, applyLp);

        TextView note = label("Theme Studio v0.4 applies character wallpapers now. Icon packs and widgets are the next layer.", 12, MUTED, Gravity.CENTER);
        note.setPadding(dp(10), dp(16), dp(10), 0);
        root.addView(note, match(dp(58)));

        rebuildCards();
        selectTheme(0);
        return scroll;
    }

    private LinearLayout panel() {
        LinearLayout p = new LinearLayout(this);
        p.setOrientation(LinearLayout.VERTICAL);
        p.setBackground(rounded(PANEL, 22, Color.rgb(45, 54, 66), 1));
        return p;
    }

    private TextView sectionTitle(String s) {
        TextView t = label(s, 17, TEXT, Gravity.START);
        t.setTypeface(Typeface.DEFAULT_BOLD);
        return t;
    }

    private void rebuildCards() {
        if (cardsHost == null) return;
        cardsHost.removeAllViews();
        boolean wide = getResources().getConfiguration().screenWidthDp >= 600;
        for (int i = 0; i < themes.length; i++) {
            final int index = i;
            ThemeSpec spec = themes[i];
            LinearLayout card = new LinearLayout(this);
            card.setOrientation(wide ? LinearLayout.HORIZONTAL : LinearLayout.VERTICAL);
            card.setGravity(Gravity.CENTER_VERTICAL);
            card.setPadding(dp(8), dp(8), dp(8), dp(8));
            boolean selected = i == selectedTheme;
            card.setBackground(rounded(selected ? blend(spec.primary, PANEL, .45f) : PANEL_2, 16, selected ? GOLD : Color.rgb(49, 58, 70), selected ? 2 : 1));
            card.setOnClickListener(v -> selectTheme(index));

            ImageView image = new ImageView(this);
            image.setScaleType(ImageView.ScaleType.CENTER_CROP);
            image.setImageResource(spec.drawable);
            card.addView(image, wide ? new LinearLayout.LayoutParams(dp(70), dp(88)) : new LinearLayout.LayoutParams(dp(64), dp(92)));

            LinearLayout copy = new LinearLayout(this);
            copy.setOrientation(LinearLayout.VERTICAL);
            copy.setPadding(wide ? dp(10) : 0, wide ? 0 : dp(7), 0, 0);
            TextView name = label(spec.name, 15, TEXT, wide ? Gravity.START : Gravity.CENTER);
            name.setTypeface(Typeface.DEFAULT_BOLD);
            copy.addView(name, match(dp(24)));
            copy.addView(label(spec.mode, 11, selected ? GOLD : MUTED, wide ? Gravity.START : Gravity.CENTER), match(dp(22)));
            card.addView(copy, new LinearLayout.LayoutParams(wide ? 0 : -1, -2, wide ? 1f : 0f));

            LinearLayout.LayoutParams lp = wide ? match(dp(106)) : new LinearLayout.LayoutParams(0, dp(150), 1f);
            lp.setMargins(wide ? 0 : dp(3), dp(4), wide ? 0 : dp(3), dp(4));
            cardsHost.addView(card, lp);
        }
    }

    private void selectTheme(int index) {
        selectedTheme = index;
        ThemeSpec spec = themes[index];
        if (selectedName != null) selectedName.setText(spec.name + " — " + spec.mode);
        if (selectedTagline != null) selectedTagline.setText(spec.tagline);
        if (preview != null) preview.invalidate();
        rebuildCards();
    }

    private Button targetButton(String text, int target) {
        Button b = new Button(this);
        b.setText(text);
        b.setAllCaps(false);
        b.setTextColor(TEXT);
        b.setTextSize(14);
        b.setBackground(rounded(PANEL_2, 14, Color.rgb(58, 68, 82), 1));
        b.setOnClickListener(v -> {
            wallpaperTarget = target;
            Toast.makeText(this, text + " screen selected", Toast.LENGTH_SHORT).show();
        });
        return b;
    }

    private TextView chip(String value) {
        TextView t = label(value, 12, TEXT, Gravity.CENTER);
        t.setBackground(rounded(PANEL_2, 13, Color.rgb(57, 67, 80), 1));
        return t;
    }

    private Bitmap loadArt() {
        return BitmapFactory.decodeResource(getResources(), themes[selectedTheme].drawable);
    }

    private void applyWallpaper() {
        try {
            Bitmap art = loadArt();
            if (art == null) {
                Toast.makeText(this, "Artwork failed to load", Toast.LENGTH_LONG).show();
                return;
            }
            ThemeSpec spec = themes[selectedTheme];
            int w = 1440, h = 2560;
            Bitmap output = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(output);
            Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG);
            drawCover(canvas, art, 0, 0, w, h, paint);

            LinearGradient shade = new LinearGradient(0, h * .45f, 0, h,
                    new int[]{Color.TRANSPARENT, Color.argb(145, 7, 10, 15), Color.argb(235, 7, 10, 15)},
                    null, Shader.TileMode.CLAMP);
            paint.setShader(shade);
            canvas.drawRect(0, 0, w, h, paint);
            paint.setShader(null);

            paint.setColor(Color.WHITE);
            paint.setTypeface(Typeface.create(Typeface.SERIF, Typeface.BOLD));
            paint.setTextSize(96);
            canvas.drawText(spec.name.toUpperCase(), 90, h - 235, paint);
            paint.setColor(spec.accent);
            paint.setTextSize(44);
            paint.setTypeface(Typeface.DEFAULT_BOLD);
            canvas.drawText(spec.mode, 94, h - 165, paint);
            paint.setColor(Color.rgb(226, 229, 234));
            paint.setTextSize(34);
            paint.setTypeface(Typeface.DEFAULT);
            canvas.drawText(spec.tagline, 94, h - 105, paint);

            WallpaperManager.getInstance(this).setBitmap(output, null, true, wallpaperTarget);
            Toast.makeText(this, spec.name + " theme applied", Toast.LENGTH_SHORT).show();
        } catch (IOException | RuntimeException e) {
            Toast.makeText(this, "Wallpaper error: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void drawCover(Canvas canvas, Bitmap art, int left, int top, int right, int bottom, Paint paint) {
        if (art == null || art.getWidth() <= 0 || art.getHeight() <= 0) return;
        float scale = Math.max((float) (right - left) / art.getWidth(), (float) (bottom - top) / art.getHeight());
        int sourceW = Math.max(1, Math.round((right - left) / scale));
        int sourceH = Math.max(1, Math.round((bottom - top) / scale));
        int sourceX = Math.max(0, (art.getWidth() - sourceW) / 2);
        int sourceY = Math.max(0, (art.getHeight() - sourceH) / 2);
        Rect src = new Rect(sourceX, sourceY, Math.min(art.getWidth(), sourceX + sourceW), Math.min(art.getHeight(), sourceY + sourceH));
        Rect dst = new Rect(left, top, right, bottom);
        canvas.drawBitmap(art, src, dst, paint);
    }

    private int blend(int a, int b, float amountA) {
        float amountB = 1f - amountA;
        return Color.rgb(
                Math.round(Color.red(a) * amountA + Color.red(b) * amountB),
                Math.round(Color.green(a) * amountA + Color.green(b) * amountB),
                Math.round(Color.blue(a) * amountA + Color.blue(b) * amountB));
    }

    private TextView label(String value, int size, int color, int gravity) {
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

    private LinearLayout.LayoutParams match(int height) {
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

    private class ThemePreview extends View {
        private final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG);

        ThemePreview(Activity activity) {
            super(activity);
            setBackground(rounded(PANEL_2, 22, Color.rgb(62, 72, 86), 1));
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            Bitmap art = loadArt();
            if (art == null) {
                paint.setColor(Color.rgb(230, 90, 90));
                paint.setTextSize(dp(17));
                canvas.drawText("ARTWORK LOAD ERROR", dp(20), dp(38), paint);
                return;
            }

            int inset = dp(6);
            drawCover(canvas, art, inset, inset, getWidth() - inset, getHeight() - inset, paint);
            ThemeSpec spec = themes[selectedTheme];

            LinearGradient shade = new LinearGradient(0, getHeight() * .38f, 0, getHeight(),
                    new int[]{Color.TRANSPARENT, Color.argb(70, 5, 8, 12), Color.argb(232, 6, 9, 14)},
                    null, Shader.TileMode.CLAMP);
            paint.setShader(shade);
            canvas.drawRoundRect(inset, inset, getWidth() - inset, getHeight() - inset, dp(20), dp(20), paint);
            paint.setShader(null);

            paint.setColor(Color.argb(205, 10, 14, 20));
            canvas.drawRoundRect(dp(18), dp(18), dp(150), dp(58), dp(14), dp(14), paint);
            paint.setColor(spec.accent);
            paint.setTypeface(Typeface.DEFAULT_BOLD);
            paint.setTextSize(dp(12));
            canvas.drawText(getWidth() > dp(520) ? "UNFOLDED PREVIEW" : "COVER PREVIEW", dp(30), dp(43), paint);

            paint.setColor(Color.WHITE);
            paint.setTypeface(Typeface.create(Typeface.SERIF, Typeface.BOLD));
            paint.setTextSize(dp(32));
            canvas.drawText(spec.name, dp(24), getHeight() - dp(88), paint);
            paint.setColor(spec.accent);
            paint.setTypeface(Typeface.DEFAULT_BOLD);
            paint.setTextSize(dp(15));
            canvas.drawText(spec.mode, dp(25), getHeight() - dp(58), paint);
            paint.setColor(Color.rgb(225, 229, 235));
            paint.setTypeface(Typeface.DEFAULT);
            paint.setTextSize(dp(12));
            canvas.drawText(spec.tagline, dp(25), getHeight() - dp(32), paint);

            float iconY = getHeight() - dp(142);
            float startX = getWidth() - dp(150);
            for (int i = 0; i < 4; i++) {
                paint.setColor(i % 2 == 0 ? spec.primary : spec.accent);
                float cx = startX + dp((i % 2) * 50);
                float cy = iconY + dp((i / 2) * 48);
                canvas.drawRoundRect(cx, cy, cx + dp(36), cy + dp(36), dp(10), dp(10), paint);
                paint.setColor(Color.argb(160, 255, 255, 255));
                canvas.drawCircle(cx + dp(18), cy + dp(18), dp(7), paint);
            }
        }
    }

    private static class ThemeSpec {
        final String name;
        final String tagline;
        final String mode;
        final int primary;
        final int accent;
        final int drawable;

        ThemeSpec(String name, String tagline, String mode, int primary, int accent, int drawable) {
            this.name = name;
            this.tagline = tagline;
            this.mode = mode;
            this.primary = primary;
            this.accent = accent;
            this.drawable = drawable;
        }
    }
}
