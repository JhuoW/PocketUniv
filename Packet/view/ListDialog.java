package com.pocketuniversity.friend.view;
import com.example.pocketuniversity.R;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ListDialog extends Dialog {
	private Context context;
	public ListDialog(Context context) {
		super(context, R.style.list_dialog);
		this.context=context;
	}
	public void init(String[] items,
			ListDialogItemOnclick click) {
		LinearLayout layout = new LinearLayout(context);
		layout.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.WRAP_CONTENT));
		layout.setOrientation(LinearLayout.VERTICAL);
		layout.setBackgroundColor(Color.WHITE);
		layout.setGravity(Gravity.CENTER);
		// 加载textView
		TextView textView;
		View view;
		String name;
		for (int i = 0; i < items.length; i++) {
			name = items[i];
			textView = getTextView(context, name, click);
			layout.addView(textView);
			if (i != items.length) {
				view = getView(context);
				layout.addView(view);
			}
		}
		setContentView(layout);
		setSizeAndPosition(0, 0, 0, 0, 10f, Gravity.CENTER);
	}

	/**
	 * 设置dialog的打下和位置 如果 参数�?0则不设置
	 * 
	 * @param width
	 *            宽度
	 * @param height
	 *            高度
	 * @param x
	 *            x坐标偏移�?
	 * @param y
	 *            y坐标偏移�?
	 * @param gravity
	 *            dialog位置 Gravity.CENTER 位于中间 Gravity.TOP位于顶部 等等
	 */
	public void setSizeAndPosition(int width, int height, int x, int y,
			float aplha, int gravity) {
		Window window = getWindow();
		WindowManager.LayoutParams layout = window.getAttributes();
		window.setGravity(gravity);
		if (width == 0) {
			DisplayMetrics dm = new DisplayMetrics();
			window.getWindowManager().getDefaultDisplay().getMetrics(dm);
			width = (int) ((dm.widthPixels) * 0.9);// 宽度height =
		}
		layout.width = width;// 宽度
		if (height > 0) {
			layout.height = height;// 高度
		}
		if (x > 0) {
			layout.x = x;// x坐标
		}
		if (y > 0) {
			layout.y = y;// y坐标
		}
		if (aplha > 0) {
			layout.alpha = 10f; // 透明�?
		}
		window.setAttributes(layout);
	}

	private View getView(Context context) {
		View view = new View(context);
		view.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, 1));
		view.setBackgroundColor(context.getResources().getColor(R.color.base_line));
		return view;
	}

	/**
	 * 获取textView
	 * 
	 * @param context
	 * @param str
	 * @return
	 */
	private TextView getTextView(Context context, CharSequence str,
			final ListDialogItemOnclick click) {
		TextView textView = new TextView(context);
		textView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.WRAP_CONTENT));
		textView.setGravity(Gravity.CENTER);
		textView.setPadding(2, 22, 2, 22);
		textView.setTextSize(16);
		textView.setTextColor(Color.BLACK);
		textView.setText(str);
		textView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				click.onClick(v);
			}
		});
		return textView;
	}

	public interface ListDialogItemOnclick {
		public void onClick(View view);
	}
	@Override
	public void show() {
		getWindow().setWindowAnimations(R.style.dialog_anim);
		super.show();
	}
}
