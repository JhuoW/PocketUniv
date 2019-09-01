package com.pocketuniversity.friend.view;

import com.example.pocketuniversity.R;

import android.app.Dialog;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * 自定义dialog 本dialog内容图片与中间按钮默认不显示
 * 
 * @author jiangyue
 * 
 */
public class MyDialog extends Dialog {
	private TextView title;// 标题提示
	private TextView content;// 内容
	private Button left;// 做边的按�?
	private Button right;// 右边的按�?
	private Button middle;// 中间按钮
	private ImageView icon;// 提示图标

	private LinearLayout dialog;// dialog布局

	private LinearLayout dialogContent;// dialog内容View
	private LinearLayout dialogBtn;// dialog按钮�?

	public MyDialog(Context context) {
		super(context, R.style.list_dialog);
		this.setContentView(R.layout.dialog);
		initViews();// 初始化控�?
		setSizeAndPosition();//固定到默认位�?  如果�?�? 重写此方�?
	}

	private void initViews() {
		title = (TextView) findViewById(R.id.title);
		content = (TextView) findViewById(R.id.content);
		left = (Button) findViewById(R.id.left);
		right = (Button) findViewById(R.id.right);
		middle = (Button) findViewById(R.id.middle);
		icon = (ImageView) findViewById(R.id.icon);
		dialog = (LinearLayout) findViewById(R.id.dialog);
		dialogContent = (LinearLayout) findViewById(R.id.dialog_content);
		dialogBtn = (LinearLayout) findViewById(R.id.dialog_btn);

	}

	/**
	 * �?dialog内容里面添加 新的view
	 * 
	 * @param view
	 */
	public void addContetView(View view) {
		dialog.addView(view);
	}

	/**
	 * 设置左边按钮文字
	 * 
	 * @param str
	 * @return
	 */
	public MyDialog setLeftBtnText(String str) {
		left.setText(str);
		return this;
	}

	/**
	 * 设置中间按钮文字
	 * 
	 * @param str
	 * @return
	 */
	public MyDialog setMiddleBtnText(String str) {
		middle.setText(str);
		return this;
	}

	/**
	 * 设置右边按钮文字
	 * 
	 * @param str
	 * @return
	 */
	public MyDialog setRightBtnText(String str) {
		right.setText(str);
		return this;
	}

	/**
	 * 设置标题文字
	 * 
	 * @param str
	 * @return
	 */
	public MyDialog setTitle(String str) {
		title.setText(str);
		return this;
	}

	/**
	 * 设置内容文字
	 * 
	 * @param str
	 * @return
	 */
	public MyDialog setContent(String str) {
		content.setText(str);
		return this;
	}

	/**
	 * 显示标题
	 * 
	 * @return
	 */
	public MyDialog showTitle() {
		showViews(title);
		return this;
	}

	/**
	 * 显示内容
	 * 
	 * @return
	 */
	public MyDialog showContent() {
		showViews(content);
		return this;
	}

	/**
	 * 显示内容图标
	 * 
	 * @return
	 */
	public MyDialog showIcon() {
		showViews(icon);
		return this;
	}

	/**
	 * 显示左边按钮
	 * 
	 * @return
	 */
	public MyDialog showLeft() {
		showViews(left);
		return this;
	}

	/**
	 * 显示中间按钮
	 * 
	 * @return
	 */
	public MyDialog showMiddle() {
		showViews(middle);
		return this;
	}

	/**
	 * 显示右边按钮
	 * 
	 * @return
	 */
	public MyDialog showRight() {
		showViews(right);
		return this;
	}

	/**
	 * 隐藏标题
	 * 
	 * @return
	 */
	public MyDialog hideTitle() {
		hideViews(title);
		return this;
	}

	/**
	 * 隐藏内容
	 * 
	 * @return
	 */
	public MyDialog hideContent() {
		hideViews(content);
		return this;
	}

	/**
	 * 隐藏内容图标
	 * 
	 * @return
	 */
	public MyDialog hideIcon() {
		hideViews(icon);
		return this;
	}

	/**
	 * 隐藏左边按钮
	 * 
	 * @return
	 */
	public MyDialog hideLeft() {
		hideViews(left);
		return this;
	}

	/**
	 * 隐藏右边按钮
	 * 
	 * @return
	 */
	public MyDialog hideRight() {
		hideViews(right);
		return this;
	}

	/**
	 * 隐藏中间按钮
	 * 
	 * @return
	 */
	public MyDialog hideMiddle() {
		hideViews(middle);
		return this;
	}

	/**
	 * 设置指定控件显示
	 * 
	 * @param view
	 * @return
	 */
	private void showViews(View view) {
		view.setVisibility(View.VISIBLE);
	}

	/**
	 * 设置指定控件显示
	 * 
	 * @param view
	 * @return
	 */
	public void hideViews(View view) {
		view.setVisibility(View.GONE);
	}

	/**
	 * 设置左边按钮监听
	 * 
	 * @param listener
	 * @return
	 */
	public MyDialog setLeftOnclick(android.view.View.OnClickListener listener) {
		left.setOnClickListener(listener);
		return this;
	}

	/**
	 * 设置右边按钮监听
	 * 
	 * @param listener
	 * @return
	 */
	public MyDialog setRightOnclick(android.view.View.OnClickListener listener) {
		right.setOnClickListener(listener);
		return this;
	}

	/**
	 * 设置中间按钮监听
	 * 
	 * @param listener
	 * @return
	 */
	public MyDialog setMiddleOnclick(android.view.View.OnClickListener listener) {
		middle.setOnClickListener(listener);
		return this;
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

	/**
	 * 默认设置dialog大小与位�?
	 */
	public void setSizeAndPosition() {
		setSizeAndPosition(0, 0, 0, 0, 10f, Gravity.CENTER);
	}

	public TextView getTitle() {
		return title;
	}

	public TextView getContent() {
		return content;
	}

	public Button getLeft() {
		return left;
	}

	public Button getRight() {
		return right;
	}

	public Button getMiddle() {
		return middle;
	}

	public ImageView getIcon() {
		return icon;
	}

	public LinearLayout getDialog() {
		return dialog;
	}

	public LinearLayout getDialogContent() {
		return dialogContent;
	}

	public LinearLayout getDialogBtn() {
		return dialogBtn;
	}
	@Override
	public void show() {
		getWindow().setWindowAnimations(R.style.dialog_anim);
		super.show();
	}
}
