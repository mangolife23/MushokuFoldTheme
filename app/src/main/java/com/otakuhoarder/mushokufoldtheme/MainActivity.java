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
import android.util.Base64;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

public class MainActivity extends Activity {
    private static final int BG = Color.rgb(8, 12, 18);
    private static final int PANEL = Color.rgb(17, 24, 33);
    private static final int GOLD = Color.rgb(202, 164, 104);
    private static final int TEXT = Color.rgb(241, 236, 225);
    private static final int MUTED = Color.rgb(166, 171, 178);

    private final ThemeSpec[] themes = new ThemeSpec[] {
            new ThemeSpec("Rudeus", "Earth • Wind • Adventure", Color.rgb(133, 101, 72), Color.rgb(74, 132, 98)),
            new ThemeSpec("Roxy", "Water Magic • Night Sky", Color.rgb(60, 88, 145), Color.rgb(92, 67, 135)),
            new ThemeSpec("Sylphie", "Healing • Forest Light", Color.rgb(74, 135, 101), Color.rgb(177, 201, 168)),
            new ThemeSpec("Eris", "Sword • Ember • Resolve", Color.rgb(143, 58, 54), Color.rgb(202, 113, 62))
    };

    private int selectedTheme = 0;
    private int wallpaperTarget = WallpaperManager.FLAG_SYSTEM | WallpaperManager.FLAG_LOCK;
    private ThemePreview preview;
    private TextView selectedLabel;
    private final Button[] themeButtons = new Button[4];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        root.setPadding(dp(18), dp(22), dp(18), dp(28));
        root.setBackgroundColor(BG);
        scroll.addView(root, new ScrollView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        TextView eyebrow = text("MUSHOKU TENSEI × GALAXY Z FOLD7", 12, GOLD, Gravity.CENTER);
        eyebrow.setLetterSpacing(0.12f);
        root.addView(eyebrow, fullWidth(dp(28)));

        TextView title = text("A New World on Your Screen", 28, TEXT, Gravity.CENTER);
        title.setTypeface(null, 1);
        root.addView(title, fullWidth(dp(44)));

        TextView subtitle = text("Choose a character, preview the mood, then apply it to your Home screen, Lock screen, or both.", 14, MUTED, Gravity.CENTER);
        subtitle.setPadding(dp(8), 0, dp(8), dp(14));
        root.addView(subtitle, fullWidth(ViewGroup.LayoutParams.WRAP_CONTENT));

        LinearLayout characterRow = new LinearLayout(this);
        characterRow.setOrientation(LinearLayout.HORIZONTAL);
        characterRow.setGravity(Gravity.CENTER);
        for (int i = 0; i < themes.length; i++) {
            final int index = i;
            Button b = new Button(this);
            themeButtons[i] = b;
            b.setText(themes[i].name);
            b.setAllCaps(false);
            b.setTextSize(14);
            b.setPadding(dp(4), 0, dp(4), 0);
            b.setOnClickListener(v -> selectTheme(index));
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(0, dp(52), 1f);
            lp.setMargins(dp(3), 0, dp(3), 0);
            characterRow.addView(b, lp);
        }
        root.addView(characterRow, fullWidth(dp(58)));
        updateThemeButtons();

        selectedLabel = text("", 16, TEXT, Gravity.CENTER);
        selectedLabel.setTypeface(null, 1);
        selectedLabel.setPadding(0, dp(12), 0, dp(8));
        root.addView(selectedLabel, fullWidth(dp(48)));

        preview = new ThemePreview();
        root.addView(preview, fullWidth(dp(330)));

        TextView modeTitle = text("APPLY TO", 12, GOLD, Gravity.CENTER);
        modeTitle.setLetterSpacing(0.16f);
        modeTitle.setPadding(0, dp(18), 0, dp(6));
        root.addView(modeTitle, fullWidth(dp(48)));

        LinearLayout targets = new LinearLayout(this);
        targets.setOrientation(LinearLayout.HORIZONTAL);
        targets.addView(targetButton("Home", WallpaperManager.FLAG_SYSTEM), weighted());
        targets.addView(targetButton("Lock", WallpaperManager.FLAG_LOCK), weighted());
        targets.addView(targetButton("Both", WallpaperManager.FLAG_SYSTEM | WallpaperManager.FLAG_LOCK), weighted());
        root.addView(targets, fullWidth(dp(54)));

        Button apply = new Button(this);
        apply.setText("Apply Selected Theme");
        apply.setAllCaps(false);
        apply.setTextSize(17);
        apply.setTextColor(Color.rgb(22, 17, 11));
        apply.setTypeface(null, 1);
        apply.setBackground(rounded(GOLD, 16, GOLD, 0));
        LinearLayout.LayoutParams applyLp = fullWidth(dp(58));
        applyLp.setMargins(0, dp(14), 0, 0);
        root.addView(apply, applyLp);
        apply.setOnClickListener(v -> applyWallpaper());

        TextView note = text("v0.3 • Generated character-art integration scaffold • Fold-aware preview", 12, Color.rgb(112, 119, 129), Gravity.CENTER);
        note.setPadding(dp(8), dp(18), dp(8), 0);
        root.addView(note, fullWidth(ViewGroup.LayoutParams.WRAP_CONTENT));

        selectTheme(0);
        return scroll;
    }

    private Button targetButton(String label, int target) {
        Button b = new Button(this);
        b.setText(label);
        b.setAllCaps(false);
        b.setTextColor(TEXT);
        b.setTextSize(14);
        b.setBackground(rounded(PANEL, 14, Color.rgb(50, 59, 71), 1));
        b.setOnClickListener(v -> {
            wallpaperTarget = target;
            Toast.makeText(this, label + " screen selected", Toast.LENGTH_SHORT).show();
        });
        return b;
    }

    private LinearLayout.LayoutParams weighted() {
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(0, dp(50), 1f);
        lp.setMargins(dp(3), 0, dp(3), 0);
        return lp;
    }

    private void selectTheme(int index) {
        selectedTheme = index;
        ThemeSpec t = themes[index];
        if (selectedLabel != null) selectedLabel.setText(t.name + "  •  " + t.tagline);
        if (preview != null) preview.invalidate();
        updateThemeButtons();
    }

    private void updateThemeButtons() {
        for (int i = 0; i < themeButtons.length; i++) {
            if (themeButtons[i] == null) continue;
            ThemeSpec t = themes[i];
            boolean selected = i == selectedTheme;
            themeButtons[i].setTextColor(selected ? Color.WHITE : MUTED);
            themeButtons[i].setBackground(rounded(selected ? t.primary : PANEL, 14, selected ? GOLD : Color.rgb(46, 54, 64), selected ? 2 : 1));
        }
    }

    private void applyWallpaper() {
        ThemeSpec theme = themes[selectedTheme];
        try {
            Bitmap bitmap = createWallpaper(theme, 1800, 2400);
            WallpaperManager wm = WallpaperManager.getInstance(this);
            wm.setBitmap(bitmap, null, true, wallpaperTarget);
            Toast.makeText(this, theme.name + " theme applied", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            Toast.makeText(this, "Could not apply wallpaper: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private Bitmap createWallpaper(ThemeSpec theme, int width, int height) {
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);

        LinearGradient bg = new LinearGradient(0, 0, width, height,
                new int[]{BG, mix(BG, theme.primary, .52f), theme.secondary},
                new float[]{0f, .58f, 1f}, Shader.TileMode.CLAMP);
        p.setShader(bg);
        canvas.drawRect(0, 0, width, height, p);
        p.setShader(null);

        p.setStyle(Paint.Style.STROKE);
        p.setStrokeWidth(6f);
        for (int i = 0; i < 7; i++) {
            p.setColor(Color.argb(Math.max(20, 90 - i * 10), 240, 220, 177));
            canvas.drawCircle(width * .74f, height * .28f, 160 + i * 82, p);
        }

        p.setStyle(Paint.Style.FILL);
        p.setColor(Color.argb(155, 255, 245, 220));
        p.setTextSize(106f);
        p.setTypeface(android.graphics.Typeface.create(android.graphics.Typeface.SERIF, android.graphics.Typeface.BOLD));
        canvas.drawText(theme.name.toUpperCase(), 120, height - 260, p);
        p.setTextSize(46f);
        p.setTypeface(android.graphics.Typeface.create(android.graphics.Typeface.SERIF, android.graphics.Typeface.ITALIC));
        canvas.drawText(theme.tagline, 126, height - 185, p);
        p.setTextSize(36f);
        p.setColor(Color.argb(150, 255, 255, 255));
        canvas.drawText("The journey continues.", 126, height - 110, p);
        return bitmap;
    }

    private TextView text(String value, int size, int color, int gravity) {
        TextView t = new TextView(this);
        t.setText(value);
        t.setTextSize(size);
        t.setTextColor(color);
        t.setGravity(gravity);
        return t;
    }

    private GradientDrawable rounded(int fill, int radius, int stroke, int strokeWidthDp) {
        GradientDrawable g = new GradientDrawable();
        g.setColor(fill);
        g.setCornerRadius(dp(radius));
        if (strokeWidthDp > 0) g.setStroke(dp(strokeWidthDp), stroke);
        return g;
    }

    private LinearLayout.LayoutParams fullWidth(int height) {
        return new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height);
    }

    private int dp(int value) {
        return Math.round(value * getResources().getDisplayMetrics().density);
    }

    private static int mix(int a, int b, float t) {
        return Color.rgb(
                (int) (Color.red(a) * (1 - t) + Color.red(b) * t),
                (int) (Color.green(a) * (1 - t) + Color.green(b) * t),
                (int) (Color.blue(a) * (1 - t) + Color.blue(b) * t));
    }

    private class ThemePreview extends View {
        private final Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);

        ThemePreview() {
            super(MainActivity.this);
            setBackground(rounded(PANEL, 22, Color.rgb(55, 63, 73), 1));
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            ThemeSpec theme = themes[selectedTheme];
            int w = getWidth();
            int h = getHeight();

            LinearGradient gradient = new LinearGradient(0, 0, w, h,
                    new int[]{Color.rgb(12, 17, 24), theme.primary, theme.secondary},
                    new float[]{0f, .62f, 1f}, Shader.TileMode.CLAMP);
            p.setShader(gradient);
            canvas.drawRoundRect(dp(6), dp(6), w - dp(6), h - dp(6), dp(20), dp(20), p);
            p.setShader(null);

            p.setStyle(Paint.Style.STROKE);
            p.setStrokeWidth(dp(2));
            p.setColor(Color.argb(105, 245, 218, 168));
            float cx = w * .72f;
            float cy = h * .34f;
            for (int i = 0; i < 5; i++) canvas.drawCircle(cx, cy, dp(34 + i * 18), p);

            p.setStyle(Paint.Style.FILL);
            p.setColor(Color.argb(225, 255, 248, 232));
            p.setTextSize(dp(w > dp(500) ? 30 : 25));
            p.setTypeface(android.graphics.Typeface.create(android.graphics.Typeface.SERIF, android.graphics.Typeface.BOLD));
            canvas.drawText(theme.name, dp(24), h - dp(78), p);
            p.setTextSize(dp(15));
            p.setTypeface(android.graphics.Typeface.create(android.graphics.Typeface.SANS_SERIF, android.graphics.Typeface.NORMAL));
            canvas.drawText(theme.tagline, dp(24), h - dp(48), p);
            p.setTextSize(dp(12));
            p.setColor(Color.argb(180, 255, 245, 225));
            canvas.drawText(w > dp(500) ? "UNFOLDED • ARTWORK MODE" : "COVER • ARTWORK MODE", dp(24), h - dp(22), p);
        }
    }

    private static class ThemeSpec {
        final String name;
        final String tagline;
        final int primary;
        final int secondary;

        ThemeSpec(String name, String tagline, int primary, int secondary) {
            this.name = name;
            this.tagline = tagline;
            this.primary = primary;
            this.secondary = secondary;
        }
    }
}
